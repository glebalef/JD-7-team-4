package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.DogReport;
import pro.sky.telegrambot.model.ShelterType;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.PersonCatRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.PersonCatService;
import pro.sky.telegrambot.service.PersonDogService;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    // Все сервис-классы, которые мы используем
    Keyboards keyboards = new Keyboards();
    ReplyMessages replyMessages = new ReplyMessages();
    ShelterType shelterType = new ShelterType();
    private final PersonDogService personDogService;
    private final PersonDogRepository personDogRepository;
    private final ReportRepository reportRepository;
    private final PersonCatService personCatService;
    private final PersonCatRepository personCatRepository;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, PersonDogService personDogService, PersonDogRepository personDogRepository, ReportRepository reportRepository, PersonCatService personCatService, PersonCatRepository personCatRepository) {
        this.telegramBot = telegramBot;
        this.personDogService = personDogService;
        this.personDogRepository = personDogRepository;
        this.reportRepository = reportRepository;
        this.personCatService = personCatService;
        this.personCatRepository = personCatRepository;
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
        return null;
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here


            try {
                switch (Objects.requireNonNull(parse(update.message().text()))) {
                    // для кнопки /start
                    case START:
                        personDogService.getPersonByChatId(update);
                        personCatService.getPersonByChatId(update);

                        // начальное меню
                        telegramBot.execute(replyMessages.chooseShelterMessage(update)
                                .replyMarkup(keyboards.getChooseShelter()));
                        break;

                    case DOG_SHELTER:

                        shelterType.setType("dog");

                        telegramBot.execute(replyMessages.initialMessage(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));


                        break;

                    case CAT_SHELTER:
                        shelterType.setType("cat");

                        telegramBot.execute(replyMessages.initialMessage(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));


                        break;

                    //  пункт 1. Информация о приюте
                    case SHELTER_MENU:
                        telegramBot.execute(replyMessages.infoMessage(update)
                                .replyMarkup(keyboards.getInfoKeyboard()));
                        break;
                    case HOW_TO_ADOPT:

                        telegramBot.execute(replyMessages.howToAdopt(update).replyMarkup(keyboards.getAdoptKeyboard()));

                        break;

                    case SHELTER_INFO:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.generalInfoMessage(update));
                        } else {
                            telegramBot.execute(replyMessages.generalInfoMessageCat(update));
                        }
                        break;

                    case SHELTER_SCHEDULE:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.schedualInfoMessage(update));
                        } else {
                            telegramBot.execute(replyMessages.schedualInfoMessageCat(update));
                        }
                        break;

                    case SHELTER_ADDRESS:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.adressInfoMessage(update)
                                    .replyMarkup(keyboards.getShowOnMap()));
                        } else {
                            telegramBot.execute(replyMessages.addressInfoMessageCat(update)
                                    .replyMarkup(keyboards.getShowOnMapCat()));
                        }
                        break;


                    case SHELTER_RULES:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.rulesInfoMessage(update));
                        } else {
                            telegramBot.execute(replyMessages.rulesInfoMessageCat(update));
                        }
                        break;

                    case CALL_STAFF:
                        // показывает кнопку "Вернуться в меню"
                        telegramBot.execute(replyMessages.feedBack(update));
                        // Пересылает запрос "Позвать волонтера" в чат волонтеров
                        telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                        break;

                    case TEL_REQUEST:
                        telegramBot.execute(replyMessages.phone(update)
                                .replyMarkup(keyboards.getAutoReply()));
                        break;

                    case CAR_PASS:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.securityPass(update));
                        } else {
                            telegramBot.execute(replyMessages.securityPassCat(update));
                        }
                        break;

                    case SAFETY:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.safety(update));
                        } else {
                            telegramBot.execute(replyMessages.safetyCat(update));
                        }
                        break;

                    case DOCS_TO_ADOPT:
                        telegramBot.execute(replyMessages.docsToAdopt(update));
                        break;

                    case TRANSPORTATION:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.transportation(update));
                        } else {
                            telegramBot.execute(replyMessages.transportationCat(update));
                        }
                        break;

                    case RECOMMENDATIONS:
                        if (shelterType.getType().equals("dog")) {
                            telegramBot.execute(replyMessages.advice(update)
                                    .replyMarkup(keyboards.getAdviceKeyboard()));
                        } else {
                            telegramBot.execute(replyMessages.advice(update)
                                    .replyMarkup(keyboards.getAdviceKeyboardCat()));
                        }
                        break;

                    case REJECT_CAUSES:
                        telegramBot.execute(replyMessages.rejectCauses(update));
                        break;

                    case HOME_ADVICE:
                        telegramBot.execute(replyMessages.homeAdvice(update)
                                .replyMarkup(keyboards.getHomeKeyboard()));
                        break;

                    case DOG_HANDLER_ADVICE:
                        telegramBot.execute(replyMessages.dogHandlerAdvice(update));
                        break;

                    case BEST_DOG_HANDLERS:
                        telegramBot.execute(replyMessages.bestDogHandlers(update));
                        break;

                    case HOME_ADULT:
                        if (shelterType.getType().equals("dog")) {
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
                        telegramBot.execute(replyMessages.reportRequest(update)
                                .replyMarkup(keyboards.getAutoReply()));
                        break;

                    case OLD_HABITS_NEGATIVE:
                        DogReport dogReport1 = reportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                        dogReport1.setOldHabitsRefuse(Boolean.FALSE);
                        reportRepository.save(dogReport1);
                        telegramBot.execute(replyMessages.newhabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        break;

                    case OLD_HABITS_POSITIVE:
                        DogReport dogReport2 = reportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                        dogReport2.setOldHabitsRefuse(Boolean.TRUE);
                        reportRepository.save(dogReport2);
                        telegramBot.execute(replyMessages.newhabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        break;

                    case NEW_HABITS_POSITIVE:
                        DogReport dogReport3 = reportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                        dogReport3.setNewHabitsAppear(Boolean.TRUE);
                        reportRepository.save(dogReport3);
                        telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        break;

                    case NEW_HABITS_NEGATIVE:
                        DogReport dogReport4 = reportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                        dogReport4.setNewHabitsAppear(Boolean.FALSE);
                        reportRepository.save(dogReport4);
                        telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        break;

                    case PERSISTENT_PHOTO_REQUEST:
                        telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        break;
                }
            } catch (NullPointerException ignored) {
            }

            // запрос номера телефона у пользователя
            try {
                if (update.message().replyToMessage().text()
                        .contains("Введите номер телефона для связи") && shelterType.getType().equals("dog")) {
                    personDogService.phoneNumberAdd(update);
                }

                if (update.message().replyToMessage().text()
                        .contains("Введите номер телефона для связи") && shelterType.getType().equals("cat")) {
                    personCatService.phoneNumberAdd(update);
                }

                //ответ пользователя волонтеру в чат волонтеров
                //если сообщение прислано ботом с id (5713161862L) из чата волонтеров
                if (update.message().replyToMessage().from().id().equals(5713161862L)
                        && !update.message().replyToMessage().text().contains("номер телефона")
                        && !update.message().replyToMessage().text().contains("отчет")) {
                    telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                }
                // Принимает ответ от волонтера для пользователя
                //если волонтер отвечает на пересланное сообщение из бота от пользователя
                if (update.message().replyToMessage().chat().id().equals(update.message().chat().id())) {
                    telegramBot.execute(replyMessages.replyMessage(update)
                            .replyMarkup(keyboards.getAutoReply()));
                }

            } catch (NullPointerException ignored) {
            } catch (WrongPhoneNumberException e) {
                SendMessage msg = new SendMessage(update.message().chat().id()
                        , "Допускаются только цифры и следующие символы: \" + \" , \" - \" , \"( )\".  Попробуйте снова, выбрав в меню \"Поделитесь вашими данными\".")
                        .replyMarkup(keyboards.getInitialKeyboard());
                telegramBot.execute(msg);
            }

            try {
                switch (Objects.requireNonNull(parse(update.message().replyToMessage().text()))) {
                    case REPLY_REPORT_REQUEST:
                        if (personDogRepository.findByChatId(update.message().chat().id()).getDog() == null) {
                            telegramBot.execute(replyMessages.noDogResponse(update));
                        } else {
                            DogReport dogReport = new DogReport(
                                    personDogRepository.findByChatId(update.message().chat().id()).getDog(),
                                    null,
                                    update.message().text(),
                                    null,
                                    null,
                                    Instant.ofEpochSecond(update.message().date()).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                    null);
                            reportRepository.save(dogReport);
                            telegramBot.execute(replyMessages.dietRequest(update).replyMarkup(keyboards.getAutoReply()));
                        }
                        break;

                    case DESCRIBE_DIET:
                        DogReport dogReport = reportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                        dogReport.setDiet(update.message().text());
                        reportRepository.save(dogReport);
                        telegramBot.execute(replyMessages.oldHabitsRequest(update).replyMarkup(keyboards.getOldHabits()));
                        break;

                    case INITIAL_PHOTO_REQUEST:
                    case PERSISTENT_PHOTO_REQUEST:
                        if (update.message().photo() == null) {
                            telegramBot.execute(replyMessages.persistantPhotoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else {
                            DogReport dogReport3 = reportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport3.setFileId(update.message().photo()[0].fileId());
                            reportRepository.save(dogReport3);
                            telegramBot.execute(replyMessages.reportIsSaved(update).replyMarkup(keyboards.getInfoKeyboard()));
                        }
                        break;
                }
            } catch (NullPointerException ignored) {
            }
            logger.info("поймал NPE");
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}


