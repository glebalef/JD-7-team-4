package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.*;
import pro.sky.telegrambot.service.ContextService;
import pro.sky.telegrambot.service.PersonCatService;
import pro.sky.telegrambot.service.PersonDogService;
import pro.sky.telegrambot.service.SchedulerService;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    // Все сервис-классы, которые мы используем
    private final Keyboards keyboards = new Keyboards();
    private final ReplyMessages replyMessages = new ReplyMessages();
    Context context;
    private final long dogVolunteerChatId = -1001634691308L;
    private final long catVolunteerChatId = -1001865175202L;
    private final long shelterBotChatId = 5713161862L;
    private final Collection<DogReport> reports = new ArrayList<>();
    private final Collection<CatReport> reportsCat = new ArrayList<>();
    private final PersonDogService personDogService;
    private final PersonDogRepository personDogRepository;
    private final DogReportRepository dogReportRepository;
    private final CatReportRepository catReportRepository;
    private final PersonCatService personCatService;
    private final PersonCatRepository personCatRepository;
    private final SchedulerService schedulerService;
    private final ContextRepository contextRepository;
    private final ContextService contextService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, PersonDogService personDogService, PersonDogRepository personDogRepository, DogReportRepository dogReportRepository, CatReportRepository catReportRepository, PersonCatService personCatService, PersonCatRepository personCatRepository, SchedulerService schedulerService, ContextRepository contextRepository, ContextService contextService) {
        this.telegramBot = telegramBot;
        this.personDogService = personDogService;
        this.personDogRepository = personDogRepository;
        this.dogReportRepository = dogReportRepository;
        this.catReportRepository = catReportRepository;
        this.personCatService = personCatService;
        this.personCatRepository = personCatRepository;
        this.schedulerService = schedulerService;
        this.contextRepository = contextRepository;
        this.contextService = contextService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Nullable
    public static Commands parse(String command) {
        Commands[] values = Commands.values();
        for (Commands c : values) {
            if (c.getMessage().equals(command)) {
                return c;
            }
        }
        return Commands.NOTHING;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            if (update.message() != null) {
                context = contextRepository.findByChatId(update.message().chat().id());

                switch (Objects.requireNonNull(parse(update.message().text()))) {
                    // для кнопкок меню бота
                    case START:
                    case BACK_TO_CHOOSE_SHELTER:
                        contextService.getContextByChatId(update);
                        // начальное меню
                        // выбор приюта
                        telegramBot.execute(replyMessages.chooseShelterMessage(update)
                                .replyMarkup(keyboards.getChooseShelter()));
                        break;
                    // запись нового пользователя в базу person_dog
                    // выбор меню приюта для собак
                    case DOG_SHELTER:
                        personDogService.getPersonByChatId(update);
                        context.setType("dog");
                        context.setPersonDog(personDogRepository.findByChatId(update.message().chat().id()));
                        contextRepository.save(context);

                        telegramBot.execute(replyMessages.generalInfoMessage(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));
                        break;
                    // запись нового пользователя в базу person_cat
                    // выбор меню приюта для кошек
                    case CAT_SHELTER:
                        personCatService.getPersonByChatId(update);
                        context.setType("cat");
                        context.setPersonCat(personCatRepository.findByChatId(update.message().chat().id()));
                        contextRepository.save(context);

                        telegramBot.execute(replyMessages.generalInfoMessageCat(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));
                        break;

                    case BACK_TO_INITIAL:
                        telegramBot.execute(replyMessages.initialMessage(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));
                        break;

                    //  пункт 1. Информация о приюте
                    case SHELTER_MENU:
                        telegramBot.execute(replyMessages.infoMessage(update)
                                .replyMarkup(keyboards.getInfoKeyboard()));
                        break;

                    case HOW_TO_ADOPT:
                    case BACK_TO_ADOPT:
                        telegramBot.execute(replyMessages.initialMessage(update)
                                .replyMarkup(keyboards.getAdoptKeyboard()));
                        break;


                    case SHELTER_SCHEDULE:
                        //условие в блоке if для проверки вида меню (для собак или кошек)
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.scheduleInfoMessage(update));
                        } else {
                            telegramBot.execute(replyMessages.scheduleInfoMessageCat(update));
                        }
                        break;

                    case SHELTER_ADDRESS:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.addressInfoMessage(update)
                                    .replyMarkup(keyboards.getShowOnMap()));
                        } else {
                            telegramBot.execute(replyMessages.addressInfoMessageCat(update)
                                    .replyMarkup(keyboards.getShowOnMapCat()));
                        }
                        break;

                    case SHELTER_RULES:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.rulesInfoMessage(update));
                        } else {
                            telegramBot.execute(replyMessages.rulesInfoMessageCat(update));
                        }
                        break;

                    case CALL_STAFF:
                        telegramBot.execute(replyMessages.feedBack(update));
                        // Пересылает запрос "Позвать волонтера" в чат волонтеров
                        if ("dog".equals(context.getType())) {
                            telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                        } else {
                            telegramBot.execute(replyMessages.anotherQuestionMessageCat(update));
                        }
                        break;

                    case TEL_REQUEST:
                        telegramBot.execute(replyMessages.phone(update)
                                .replyMarkup(keyboards.getAutoReply()));
                        break;

                    case CAR_PASS:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.securityPass(update));
                        } else {
                            telegramBot.execute(replyMessages.securityPassCat(update));
                        }
                        break;

                    case SAFETY:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.safety(update));
                        } else {
                            telegramBot.execute(replyMessages.safetyCat(update));
                        }
                        break;

                    case DOCS_TO_ADOPT:
                        telegramBot.execute(replyMessages.docsToAdopt(update));
                        break;

                    case TRANSPORTATION:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.transportation(update));
                        } else {
                            telegramBot.execute(replyMessages.transportationCat(update));
                        }
                        break;

                    case RECOMMENDATIONS:
                    case BACK_TO_ADVICE:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.initialMessage(update)
                                    .replyMarkup(keyboards.getAdviceKeyboard()));
                        } else {
                            telegramBot.execute(replyMessages.initialMessage(update)
                                    .replyMarkup(keyboards.getAdviceKeyboardCat()));
                        }
                        break;

                    case REJECT_CAUSES:
                        telegramBot.execute(replyMessages.rejectCauses(update));
                        break;

                    case HOME_ADVICE:
                        telegramBot.execute(replyMessages.initialMessage(update)
                                .replyMarkup(keyboards.getHomeKeyboard()));
                        break;

                    case DOG_HANDLER_ADVICE:
                        telegramBot.execute(replyMessages.dogHandlerAdvice(update));
                        break;

                    case BEST_DOG_HANDLERS:
                        telegramBot.execute(replyMessages.bestDogHandlers(update));
                        break;

                    case HOME_ADULT:
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.homeAdult(update));
                        } else {
                            telegramBot.execute(replyMessages.homeAdultCat(update));
                        }
                        break;

                    case KITTEN:
                        telegramBot.execute(replyMessages.kitten(update));
                        break;

                    case PUPPY:
                        telegramBot.execute(replyMessages.puppy(update));
                        break;

                    case BLIND:
                        telegramBot.execute(replyMessages.blind(update));
                        break;

                    case DISABLED:
                        telegramBot.execute(replyMessages.disabled(update));
                        break;

                    case REPORT_REQUEST:
                        if (context.getType().equals("dog")
                                && personDogRepository.findByChatId(update.message().chat().id()).getDog() == null) {
                            telegramBot.execute(replyMessages.noDogResponse(update)
                                    .replyMarkup(keyboards.getInitialKeyboard()));
                        } else if (context.getType().equals("cat")
                                && personCatRepository.findByChatId(update.message().chat().id()).getCat() == null) {
                            telegramBot.execute(replyMessages.noDogResponse(update)
                                    .replyMarkup(keyboards.getInitialKeyboard()));
                        } else {
                            telegramBot.execute(replyMessages.reportRequest(update)
                                    .replyMarkup(keyboards.getAutoReply()));
                        }
                        break;

                    case OLD_HABITS_NEGATIVE:
                        if (context.getType().equals("dog")) {
                            DogReport dogReport1 = dogReportRepository.findDogReportByFileIdAndPersonDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getId());
                            dogReport1.setOldHabitsRefuse(Boolean.FALSE);
                            dogReportRepository.save(dogReport1);
                            telegramBot.execute(replyMessages.newHabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        } else if (context.getType().equals("cat")) {
                            CatReport catReport1 = catReportRepository.findCatReportByFileIdAndPersonCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getId());
                            catReport1.setOldHabitsRefuse(Boolean.FALSE);
                            catReportRepository.save(catReport1);
                            telegramBot.execute(replyMessages.newHabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        }
                        break;

                    case OLD_HABITS_POSITIVE:
                        if (context.getType().equals("dog")) {
                            DogReport dogReport2 = dogReportRepository.findDogReportByFileIdAndPersonDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getId());
                            dogReport2.setOldHabitsRefuse(Boolean.TRUE);
                            dogReportRepository.save(dogReport2);
                            telegramBot.execute(replyMessages.newHabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        } else if (context.getType().equals("cat")) {
                            CatReport catReport2 = catReportRepository.findCatReportByFileIdAndPersonCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getId());
                            catReport2.setOldHabitsRefuse(Boolean.TRUE);
                            catReportRepository.save(catReport2);
                            telegramBot.execute(replyMessages.newHabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        }
                        break;

                    case NEW_HABITS_POSITIVE:
                        if (context.getType().equals("dog")) {
                            DogReport dogReport3 = dogReportRepository.findDogReportByFileIdAndPersonDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getId());
                            dogReport3.setNewHabitsAppear(Boolean.TRUE);
                            dogReportRepository.save(dogReport3);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else if (context.getType().equals("cat")) {
                            CatReport catReport3 = catReportRepository.findCatReportByFileIdAndPersonCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getId());
                            catReport3.setNewHabitsAppear(Boolean.TRUE);
                            catReportRepository.save(catReport3);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        }
                        break;

                    case NEW_HABITS_NEGATIVE:
                        if (context.getType().equals("dog")) {
                            DogReport dogReport4 = dogReportRepository.findDogReportByFileIdAndPersonDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getId());
                            dogReport4.setNewHabitsAppear(Boolean.FALSE);
                            dogReportRepository.save(dogReport4);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else if (context.getType().equals("cat")) {
                            CatReport catReport4 = catReportRepository.findCatReportByFileIdAndPersonCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getId());
                            catReport4.setNewHabitsAppear(Boolean.FALSE);
                            catReportRepository.save(catReport4);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        }
                        break;

                    case PERSISTENT_PHOTO_REQUEST:
                        telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        break;
                }
            }

            CallbackQuery callbackQuery = update.callbackQuery();
            if (callbackQuery != null) {
                String data = callbackQuery.data();

                for (DogReport report1 : reports) {
                    if (String.valueOf("продлить" + report1.getPersonDog().getChatId()).equals(data)) {
                        InlineKeyboardButton half = new InlineKeyboardButton("14 дней").callbackData("14 дней" + report1.getPersonDog().getChatId().toString());
                        InlineKeyboardButton full = new InlineKeyboardButton("30 дней").callbackData("30 дней" + report1.getPersonDog().getChatId().toString());
                        InlineKeyboardMarkup prolong = new InlineKeyboardMarkup(half, full);
                        SendMessage prolongTrial = new SendMessage(dogVolunteerChatId, "На какой период продлить испытательный срок для " + report1.getPersonDog().getFirstName() + " (id: " + report1.getPersonDog().getId() + ") ?");
                        telegramBot.execute(prolongTrial.replyMarkup(prolong));
                    }
                    if (String.valueOf("завершить" + report1.getPersonDog().getChatId()).equals(data)) {
                        InlineKeyboardButton success = new InlineKeyboardButton("пройден").callbackData("пройден" + report1.getPersonDog().getChatId().toString());
                        InlineKeyboardButton fail = new InlineKeyboardButton("провален").callbackData("провален" + report1.getPersonDog().getChatId().toString());
                        InlineKeyboardMarkup finish = new InlineKeyboardMarkup(success, fail);
                        SendMessage endTrial = new SendMessage(dogVolunteerChatId, "Какой результат испытательного срока для" + report1.getPersonDog().getFirstName() + " (id: " + report1.getPersonDog().getId() + ") ?");
                        telegramBot.execute(endTrial.replyMarkup(finish));
                    }

                    if (String.valueOf("14 дней" + report1.getPersonDog().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonDog().getContext().getChatId());
                        context1.setAddDays("14");
                        contextRepository.save(context1);
                        SendMessage newHalfTrial = new SendMessage(report1.getPersonDog().getChatId(), "Мы вынуждены продлить ваш испытательный срок на 14 дней");
                        telegramBot.execute(newHalfTrial);
                        SendMessage infoTrial = new SendMessage(dogVolunteerChatId, "Испытательный период для " + report1.getPersonDog().getFirstName() + " (id: " + report1.getPersonDog().getId() + ") продлен на 14 дней");
                        telegramBot.execute(infoTrial);
                    }

                    if (String.valueOf("30 дней" + report1.getPersonDog().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonDog().getContext().getChatId());
                        context1.setAddDays("30");
                        contextRepository.save(context1);
                        SendMessage newFullTrial = new SendMessage(report1.getPersonDog().getChatId(), "Мы вынуждены продлить ваш испытательный срок на 30 дней");
                        telegramBot.execute(newFullTrial);
                        SendMessage infoTrial = new SendMessage(dogVolunteerChatId, "Испытательный период для " + report1.getPersonDog().getFirstName() + " (id: " + report1.getPersonDog().getId() + ") продлен на 30 дней");
                        telegramBot.execute(infoTrial);
                    }

                    if (String.valueOf("пройден" + report1.getPersonDog().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonDog().getContext().getChatId());
                        context1.setTestOff(true);
                        contextRepository.save(context1);
                        SendMessage successTrial = new SendMessage(report1.getPersonDog().getChatId(), "Поздравляем с успешным прохождением испытательного срока!");
                        telegramBot.execute(successTrial);
                        SendMessage infoTrial = new SendMessage(dogVolunteerChatId, "Усыновитель" + report1.getPersonDog().getFirstName() + " (id: " + report1.getPersonDog().getId() + ") уведомлен об успешном прохождении испытательного срока");
                        telegramBot.execute(infoTrial);
                    }
                    if (String.valueOf("провален" + report1.getPersonDog().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonDog().getContext().getChatId());
                        context1.setTestOff(true);
                        contextRepository.save(context1);
                        SendMessage failedTrial = new SendMessage(report1.getPersonDog().getChatId(), "Сожалеем, но вы не прошли испытательный срок");
                        telegramBot.execute(failedTrial);
                        SendMessage infoTrial = new SendMessage(dogVolunteerChatId, "Усыновитель" + report1.getPersonDog().getFirstName() + " (id: " + report1.getPersonDog().getId() + ") уведомлен о неудачном прохождении испытательного срока");
                        telegramBot.execute(infoTrial);
                    }
                }
            }

            if (callbackQuery != null) {
                String data = callbackQuery.data();

                for (CatReport report1 : reportsCat) {
                    if (String.valueOf("продлитьКот" + report1.getPersonCat().getChatId()).equals(data)) {
                        InlineKeyboardButton half = new InlineKeyboardButton("14 дней").callbackData("14 днейКот" + report1.getPersonCat().getChatId().toString());
                        InlineKeyboardButton full = new InlineKeyboardButton("30 дней").callbackData("30 днейКот" + report1.getPersonCat().getChatId().toString());
                        InlineKeyboardMarkup prolong = new InlineKeyboardMarkup(half, full);
                        SendMessage prolongTrial = new SendMessage(catVolunteerChatId, "На какой период продлить испытательный срокдля" + report1.getPersonCat().getFirstName() + " (id: " + report1.getPersonCat().getId() + ") ?");
                        telegramBot.execute(prolongTrial.replyMarkup(prolong));
                    }

                    if (String.valueOf("завершитьКот" + report1.getPersonCat().getChatId()).equals(data)) {
                        InlineKeyboardButton success = new InlineKeyboardButton("пройден").callbackData("пройденКот" + report1.getPersonCat().getChatId().toString());
                        InlineKeyboardButton fail = new InlineKeyboardButton("провален").callbackData("проваленКот" + report1.getPersonCat().getChatId().toString());
                        InlineKeyboardMarkup finish = new InlineKeyboardMarkup(success, fail);
                        SendMessage endTrial = new SendMessage(catVolunteerChatId, "Какой результат испытательного срока для" + report1.getPersonCat().getFirstName() + " (id: " + report1.getPersonCat().getId() + ") ?");
                        telegramBot.execute(endTrial.replyMarkup(finish));
                    }

                    if (String.valueOf("14 днейКот" + report1.getPersonCat().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonCat().getContext().getChatId());
                        context1.setAddDays("14");
                        contextRepository.save(context1);
                        SendMessage newHalfTrial = new SendMessage(report1.getPersonCat().getChatId(), "Мы вынуждены продлить ваш испытательный срок на 14 дней");
                        telegramBot.execute(newHalfTrial);
                        SendMessage infoTrial = new SendMessage(catVolunteerChatId, "Испытательный период для " + report1.getPersonCat().getFirstName() + " (id: " + report1.getPersonCat().getId() + ") продлен на 14 дней");
                        telegramBot.execute(infoTrial);
                    }

                    if (String.valueOf("30 днейКот" + report1.getPersonCat().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonCat().getContext().getChatId());
                        context1.setAddDays("30");
                        contextRepository.save(context1);
                        SendMessage newFullTrial = new SendMessage(report1.getPersonCat().getChatId(), "Мы вынуждены продлить ваш испытательный срок на 30 дней");
                        telegramBot.execute(newFullTrial);
                        SendMessage infoTrial = new SendMessage(catVolunteerChatId, "Испытательный период для " + report1.getPersonCat().getFirstName() + " (id: " + report1.getPersonCat().getId() + ") продлен на 30 дней");
                        telegramBot.execute(infoTrial);
                    }

                    if (String.valueOf("пройденКот" + report1.getPersonCat().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonCat().getContext().getChatId());
                        context1.setTestOff(true);
                        contextRepository.save(context1);
                        SendMessage successTrial = new SendMessage(report1.getPersonCat().getChatId(), "Поздравляем с успешным прохождением испытательного срока!");
                        telegramBot.execute(successTrial);
                        SendMessage infoTrial = new SendMessage(catVolunteerChatId, "Усыновитель" + report1.getPersonCat().getFirstName() + " (id: " + report1.getPersonCat().getId() + ") уведомлен об успешном прохождении испытательного срока");
                        telegramBot.execute(infoTrial);
                    }

                    if (String.valueOf("проваленКот" + report1.getPersonCat().getChatId()).equals(data)) {
                        Context context1 = contextRepository.findByChatId(report1.getPersonCat().getContext().getChatId());
                        context1.setTestOff(true);
                        contextRepository.save(context1);
                        SendMessage failedTrial = new SendMessage(report1.getPersonCat().getChatId(), "Сожалеем, но вы не прошли испытательный срок");
                        telegramBot.execute(failedTrial);
                        SendMessage infoTrial = new SendMessage(catVolunteerChatId, "Усыновитель" + report1.getPersonCat().getFirstName() + " (id: " + report1.getPersonCat().getId() + ") уведомлен о неудачном прохождении испытательного срока");
                        telegramBot.execute(infoTrial);
                    }
                }
            }

            if (callbackQuery != null) {

                //Уведомление усыновителя кошки о плохо заполненном отчете
                if ("плохоCat".equals(update.callbackQuery().data())) {
                    PersonCat personCat = personCatRepository.findByChatId(update.callbackQuery().from().id());
                    telegramBot.execute(replyMessages.badReportReply(update));
                    SendMessage badReportReply = new SendMessage(catVolunteerChatId, "Усыновитель" + personCat.getFirstName()
                            + " (id: " + personCat.getId() + ") уведомлен о плохо заполненном отчете");
                    telegramBot.execute(badReportReply);
                }

                //Уведомление усыновителя собаки о плохо заполненном отчете
                if ("плохо".equals(update.callbackQuery().data())) {
                    PersonDog personDog = personDogRepository.findByChatId(update.callbackQuery().from().id());
                    telegramBot.execute(replyMessages.badReportReply(update));
                    SendMessage badReportNotification = new SendMessage(dogVolunteerChatId, "Усыновитель" + personDog.getFirstName()
                            + " (id: " + personDog.getId() + ") уведомлен о плохо заполненном отчете");
                    telegramBot.execute(badReportNotification);
                }
            }
            // запрос номера телефона у пользователя
            try {
                if (update.message() != null && update.message().replyToMessage() != null) {
                    if (update.message().replyToMessage().text()
                            .contains("Введите номер телефона для связи") && context.getType().equals("dog")) {
                        personDogService.phoneNumberAdd(update);
                    }
                    if (update.message().replyToMessage().text()
                            .contains("Введите номер телефона для связи") && context.getType().equals("cat")) {
                        personCatService.phoneNumberAdd(update);
                    }
                    //ответ пользователя волонтеру в чат волонтеров
                    if (Objects.equals(update.message().replyToMessage().from().id(), shelterBotChatId)
                            && !update.message().replyToMessage().text().contains("номер телефона")
                            && !update.message().replyToMessage().text().contains("отчет")
                            && !update.message().replyToMessage().text().contains("Вы кормите")
                            && !update.message().replyToMessage().text().contains("фотографию")
                            && !update.message().replyToMessage().text().contains("фото")) {
                        //если сообщение прислано ботом с id (5713161862L) из чата волонтеров приюта для собак
                        if (context.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                        }
                        //если сообщение прислано ботом с id (5713161862L) из чата волонтеров приюта для кошек
                        if (context.getType().equals("cat")) {
                            telegramBot.execute(replyMessages.anotherQuestionMessageCat(update));
                        }
                    }
                    // Принимает ответ от волонтера для пользователя
                    //если волонтер отвечает на присланное сообщение из бота от пользователя
                    if (update.message().replyToMessage().forwardFrom() != null
                            && Objects.equals(update.message().replyToMessage().chat().id(), update.message().chat().id())) {
                        telegramBot.execute(replyMessages.replyMessage(update)
                                .replyMarkup(keyboards.getAutoReply()));
                    }
                }
            } catch (WrongPhoneNumberException e) {
                SendMessage msg = new SendMessage(update.message().chat().id()
                        , "Допускаются только цифры и следующие символы: \" + \" , \" - \" , \"( )\".  Попробуйте снова, выбрав в меню \"Поделитесь вашими данными\".")
                        .replyMarkup(keyboards.getInitialKeyboard());
                telegramBot.execute(msg);
            }
            try {
                switch (Objects.requireNonNull(parse(update.message().replyToMessage().text()))) {
                    case REPLY_REPORT_REQUEST:
                        if (context.getType().equals("dog")) {
                            DogReport dogReport = new DogReport(
                                    personDogRepository.findByChatId(update.message().chat().id()),
                                    null,
                                    update.message().text(),
                                    null,
                                    null,
                                    Instant.ofEpochSecond(update.message().date()).atZone(ZoneId.systemDefault()).toLocalDate(),
                                    null);
                            dogReportRepository.save(dogReport);
                            telegramBot.execute(replyMessages.dietRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else if (context.getType().equals("cat")) {
                            CatReport catReport = new CatReport(
                                    personCatRepository.findByChatId(update.message().chat().id()),
                                    null,
                                    update.message().text(),
                                    null,
                                    null,
                                    Instant.ofEpochSecond(update.message().date()).atZone(ZoneId.systemDefault()).toLocalDate(),
                                    null);
                            catReportRepository.save(catReport);
                            telegramBot.execute(replyMessages.dietRequest(update).replyMarkup(keyboards.getAutoReply()));
                        }
                        break;

                    case DESCRIBE_DIET:
                        if (context.getType().equals("dog")) {
                            DogReport dogReport2 = dogReportRepository.findDogReportByFileIdAndPersonDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getId());
                            dogReport2.setDiet(update.message().text());
                            dogReportRepository.save(dogReport2);
                            telegramBot.execute(replyMessages.oldHabitsRequest(update).replyMarkup(keyboards.getOldHabits()));
                        } else if (context.getType().equals("cat")) {
                            CatReport catReport2 = catReportRepository.findCatReportByFileIdAndPersonCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getId());
                            catReport2.setDiet(update.message().text());
                            catReportRepository.save(catReport2);
                            telegramBot.execute(replyMessages.oldHabitsRequest(update).replyMarkup(keyboards.getOldHabits()));

                        }
                        break;

                    case INITIAL_PHOTO_REQUEST:
                    case PERSISTENT_PHOTO_REQUEST:
                        /*if (update.message().photo() == null) {
                            telegramBot.execute(replyMessages.persistentPhotoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        }*/
                        if (context.getType().equals("dog")) {
                            DogReport dogReport3 = dogReportRepository.findDogReportByFileIdAndPersonDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getId());
                            dogReport3.setFileId(update.message().photo()[0].fileId());
                            dogReportRepository.save(dogReport3);
                            telegramBot.execute(replyMessages.reportIsSaved(update).replyMarkup(keyboards.getInitialKeyboard()));
                            telegramBot.execute(new SendMessage(dogVolunteerChatId, "Получен новый отчет:" + dogReport3.toString()));
                            telegramBot.execute(new SendPhoto(dogVolunteerChatId, dogReport3.getFileId()).replyMarkup(keyboards.getBadReport()));

                        } else if (context.getType().equals("cat")) {
                            CatReport catReport3 = catReportRepository.findCatReportByFileIdAndPersonCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getId());
                            catReport3.setFileId(update.message().photo()[0].fileId());
                            catReportRepository.save(catReport3);
                            telegramBot.execute(replyMessages.reportIsSaved(update).replyMarkup(keyboards.getInitialKeyboard()));
                            telegramBot.execute(new SendMessage(catVolunteerChatId, "Получен новый отчет:" + catReport3.toString()));
                            telegramBot.execute(new SendPhoto(catVolunteerChatId, catReport3.getFileId()).replyMarkup(keyboards.getBadReportCat()));
                        }
                        break;
                }
            } catch (NullPointerException ignored) {
                logger.info("поймал NPE");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/3 * * * *")
    public void sendNotificationDog() {
        logger.info("Поиск отчетов из приюта для собак");

        for (DogReport dogReport : schedulerService.findNewDogReports()) {
            //для проверки состояния испытательного срока (ИС): true = в процессе ИС, false = ИС не проводится.
            boolean testOff = dogReport.getPersonDog().getContext().getTestOff();
            //chatId отправителя отчета
            Long personDogId = dogReport.getPersonDog().getId();
            //количество дней с последнего полученного отчета
            long daysBetween = DAYS.between(LocalDate.now(), dogReport.getReportDate());

            //уведомление в чат волонтеров приюта для собак о том, что отчет не поступал 2 или более дней
            if (dogReport.getReportDate().isBefore(LocalDate.now().minusDays(1))
                    && !testOff) {
                SendMessage sendMessage = new SendMessage(dogVolunteerChatId, "Отчет о собаке "
                        + dogReport.getPersonDog().getDog().getName() + " (id: " + dogReport.getPersonDog().getDog().getId() + ") от усыновителя "
                        + dogReport.getPersonDog().getFirstName() + " (id: " + personDogId + ") не поступал уже " + daysBetween + " дней. "
                        + "Дата последнего отчета: " + dogReport.getReportDate());
                telegramBot.execute(sendMessage);
            }
            //напоминание пользователю о необходимости отправить отчет за текущий день
            if (dogReport.getReportDate().equals(LocalDate.now().minusDays(1))
                    && !testOff) {
                SendMessage sendToPerson = new SendMessage(dogReport.getPersonDog().getChatId(), "Дорогой усыновитель, " +
                        "не забудь сегодня отправить отчет");
                telegramBot.execute(sendToPerson);
            }
        }

        for (DogReport dogReport : schedulerService.findOldDogReports()) {
            //для проверки состояния испытательного срока (ИС): true = в процессе ИС, false = ИС не проводится
            boolean testOff = dogReport.getPersonDog().getContext().getTestOff();
            //chatId отправителя отчета
            Long personDogId = dogReport.getPersonDog().getId();
            //количество дней в случае назначения дополнительного испытательного срока
            String addDays = dogReport.getPersonDog().getContext().getAddDays();
            //кнопки выбора результата испытательного срока в случае прохождения необходимого количества дней
            InlineKeyboardButton success = new InlineKeyboardButton("пройден").callbackData("пройден" + dogReport.getPersonDog().getChatId().toString());
            InlineKeyboardButton fail = new InlineKeyboardButton("провален").callbackData("провален" + dogReport.getPersonDog().getChatId().toString());
            InlineKeyboardMarkup decisionTestPeriod = new InlineKeyboardMarkup(success, fail);

            //выбор результата прохождения основного испытательного срока в 30 дней
            if (dogReport.getReportDate().equals(LocalDate.now().minusDays(30))) {
                reports.add(dogReport);
                SendMessage sendMessage = new SendMessage(dogVolunteerChatId, "Испытательный срок в 30 дней у "
                        + dogReport.getPersonDog().getFirstName() + " (id: " + personDogId + ") для собаки "
                        + dogReport.getPersonDog().getDog().getName() + " (id: " + dogReport.getPersonDog().getDog().getId() + ") закончен");
                InlineKeyboardButton more = new InlineKeyboardButton("продлить").callbackData("продлить" + dogReport.getPersonDog().getChatId().toString());
                InlineKeyboardButton enough = new InlineKeyboardButton("завершить").callbackData("завершить" + dogReport.getPersonDog().getChatId().toString());
                InlineKeyboardMarkup decision = new InlineKeyboardMarkup(more, enough);
                telegramBot.execute(sendMessage.replyMarkup(decision));
            }

            //выбор результата прохождения дополнительного испытательного срока в 14 дней
            if (dogReport.getReportDate().equals(LocalDate.now().minusDays(44))
                    && !testOff && addDays.equals("14")) {
                reports.add(dogReport);
                SendMessage halfBigTrial = new SendMessage(dogVolunteerChatId, "Дополнительный испытательный срок в 14 дней у " + dogReport.getPersonDog().getFirstName() + " (id: " + dogReport.getPersonDog().getId()
                        + ") для собаки " + dogReport.getPersonDog().getDog().getName() + " (id: " + dogReport.getPersonDog().getDog().getId() + ") закончен. Какой результат?");
                telegramBot.execute(halfBigTrial.replyMarkup(decisionTestPeriod));
            }
            //выбор результата прохождения дополнительного испытательного срока в 30 дней
            if (dogReport.getReportDate().equals(LocalDate.now().minusDays(60)) && !testOff) {
                reports.add(dogReport);
                SendMessage bigTrial = new SendMessage(dogVolunteerChatId, "Дополнительный испытательный срок в 30 дней у "
                        + dogReport.getPersonDog().getFirstName() + " (id: " + personDogId
                        + ") для собаки " + dogReport.getPersonDog().getDog().getName() + " (id: " + dogReport.getPersonDog().getDog().getId() + ") закончен. Какой результат?");
                telegramBot.execute(bigTrial.replyMarkup(decisionTestPeriod));
            }
        }
    }

    @Scheduled(cron = "0 0/3 * * * *")
    public void sendNotificationCat() {
        logger.info("Поиск отчетов из приюта для кошек");

        for (CatReport catReport : schedulerService.findNewCatReports()) {
            //для проверки состояния испытательного срока (ИС): true = в процессе ИС, false = ИС не проводится
            boolean testOff = catReport.getPersonCat().getContext().getTestOff();
            //chatId отправителя отчета
            Long personCatId = catReport.getPersonCat().getId();
            //количество дней с последнего полученного отчета
            long daysBetween = DAYS.between(LocalDate.now(), catReport.getReportDate());

            //уведомление в чат волонтеров приюта для кошек о том, что отчет не поступал 2 или более дней
            if (catReport.getReportDate().isBefore(LocalDate.now().minusDays(1)) && !testOff) {
                SendMessage sendMessage = new SendMessage(catVolunteerChatId, "Отчет о кошке " + catReport.getPersonCat().getCat().getName()
                        + " (id: " + catReport.getPersonCat().getCat().getId() + ") от усыновителя "
                        + catReport.getPersonCat().getFirstName() + " (id: " + personCatId + ") не поступал уже " + daysBetween + " дней. " + "Дата последнего отчета: " + catReport.getReportDate());
                telegramBot.execute(sendMessage);
            }

            //напоминание пользователю о необходимости отправить отчет за текущий день
            if (catReport.getReportDate().equals(LocalDate.now().minusDays(1)) && !testOff) {
                SendMessage sendToPerson = new SendMessage(catReport.getPersonCat().getChatId(), "Дорогой усыновитель, " + "не забудь сегодня отправить отчет");
                telegramBot.execute(sendToPerson);
            }
        }

        for (CatReport catReport : schedulerService.findOldCatReports()) {
            //для проверки состояния испытательного срока (ИС): true = в процессе ИС, false = ИС не проводится
            boolean testOff = catReport.getPersonCat().getContext().getTestOff();
            //chatId отправителя отчета
            Long personCatId = catReport.getPersonCat().getId();
            //количество дней в случае назначения дополнительного испытательного срока
            String addDays = catReport.getPersonCat().getContext().getAddDays();
            //кнопки выбора результата испытательного срока в случае прохождения необходимого количества дней
            InlineKeyboardButton success = new InlineKeyboardButton("пройден").callbackData(String.valueOf("пройденКот" + catReport.getPersonCat().getChatId()));
            InlineKeyboardButton fail = new InlineKeyboardButton("провален").callbackData(String.valueOf("проваленКот" + catReport.getPersonCat().getChatId()));
            InlineKeyboardMarkup decisionTestPeriod = new InlineKeyboardMarkup(success, fail);

            //выбор результата прохождения основного испытательного срока в 30 дней
            if (catReport.getReportDate().equals(LocalDate.now().minusDays(30))) {
                reportsCat.add(catReport);
                SendMessage sendMessage = new SendMessage(catVolunteerChatId, "Испытательный срок в 30 дней у " + catReport.getPersonCat().getFirstName() + " (id: " + personCatId + ") для кошки " + catReport.getPersonCat().getCat().getName() + " (id: " + catReport.getPersonCat().getCat().getId() + ") закончен");
                InlineKeyboardButton more = new InlineKeyboardButton("продлить").callbackData("продлитьКот" + catReport.getPersonCat().getChatId().toString());
                InlineKeyboardButton enough = new InlineKeyboardButton("завершить").callbackData("завершитьКот" + catReport.getPersonCat().getChatId().toString());
                InlineKeyboardMarkup decision = new InlineKeyboardMarkup(more, enough);
                telegramBot.execute(sendMessage.replyMarkup(decision));
            }

            //выбор результата прохождения дополнительного испытательного срока в 14 дней
            if (catReport.getReportDate().equals(LocalDate.now().minusDays(44)) && !testOff && addDays.equals("14")) {
                reportsCat.add(catReport);
                SendMessage halfBigTrial = new SendMessage(catVolunteerChatId, "Дополнительный испытательный срок в 14 дней у " + catReport.getPersonCat().getFirstName() + " (id: " + catReport.getPersonCat().getId() + ") для кошки " + catReport.getPersonCat().getCat().getName() + " (id: " + catReport.getPersonCat().getCat().getId() + ") закончен. Какой результат?");
                telegramBot.execute(halfBigTrial.replyMarkup(decisionTestPeriod));
            }

            //выбор результата прохождения дополнительного испытательного срока в 30 дней
            if (catReport.getReportDate().equals(LocalDate.now().minusDays(60)) && !testOff) {
                reportsCat.add(catReport);
                SendMessage bigTrial = new SendMessage(catVolunteerChatId, "Дополнительный испытательный срок в 30 дней у " + catReport.getPersonCat().getFirstName() + " (id: " + personCatId + ") для кошки " + catReport.getPersonCat().getCat().getName() + " (id: " + catReport.getPersonCat().getCat().getId() + ") закончен. Какой результат?");
                telegramBot.execute(bigTrial.replyMarkup(decisionTestPeriod));
            }
        }
    }
}



