package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.CatReport;
import pro.sky.telegrambot.model.DogReport;
import pro.sky.telegrambot.model.ShelterType;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.CatReportRepository;
import pro.sky.telegrambot.repository.PersonCatRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;
import pro.sky.telegrambot.repository.DogReportRepository;
import pro.sky.telegrambot.service.PersonCatService;
import pro.sky.telegrambot.service.PersonDogService;
import pro.sky.telegrambot.service.SchedulerService;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final DogReportRepository dogReportRepository;
    private final CatReportRepository catReportRepository;
    private final PersonCatService personCatService;
    private final PersonCatRepository personCatRepository;
    private final SchedulerService schedulerService;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, PersonDogService personDogService, PersonDogRepository personDogRepository, DogReportRepository dogReportRepository, CatReportRepository catReportRepository, PersonCatService personCatService, PersonCatRepository personCatRepository, SchedulerService schedulerService) {
        this.telegramBot = telegramBot;
        this.personDogService = personDogService;
        this.personDogRepository = personDogRepository;
        this.dogReportRepository = dogReportRepository;
        this.catReportRepository = catReportRepository;
        this.personCatService = personCatService;
        this.personCatRepository = personCatRepository;
        this.schedulerService = schedulerService;
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
                    // для кнопкок меню бота
                    case START:
                    case BACK_TO_CHOOSE_SHELTER:
                        // начальное меню
                        // выбор приюта
                        telegramBot.execute(replyMessages.chooseShelterMessage(update)
                                .replyMarkup(keyboards.getChooseShelter()));
                        break;
                    // запись нового пользователя в базу person_dog
                    // выбор меню приюта для собак
                    case DOG_SHELTER:
                        personDogService.getPersonByChatId(update);
                        shelterType.setType("dog");

                        telegramBot.execute(replyMessages.generalInfoMessage(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));
                        break;
                    // запись нового пользователя в базу person_cat
                    // выбор меню приюта для кошек
                    case CAT_SHELTER:
                        personCatService.getPersonByChatId(update);
                        shelterType.setType("cat");

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
                        if (shelterType.getType().equals("dog")) {
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
                    case BACK_TO_ADVICE:
                        if (shelterType.getType().equals("dog")) {
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
                        if (shelterType.getType().equals("dog")
                                && personDogRepository.findByChatId(update.message().chat().id()).getDog() == null) {
                            telegramBot.execute(replyMessages.noDogResponse(update)
                                    .replyMarkup(keyboards.getInitialKeyboard()));
                        }

                        if (shelterType.getType().equals("cat")
                                && personCatRepository.findByChatId(update.message().chat().id()).getCat() == null) {
                            telegramBot.execute(replyMessages.noDogResponse(update)
                                    .replyMarkup(keyboards.getInitialKeyboard()));
                        } else {
                            telegramBot.execute(replyMessages.reportRequest(update)
                                    .replyMarkup(keyboards.getAutoReply()));
                        }


                        break;

                    case OLD_HABITS_NEGATIVE:
                        if (shelterType.getType().equals("dog")) {
                            DogReport dogReport1 = dogReportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport1.setOldHabitsRefuse(Boolean.FALSE);
                            dogReportRepository.save(dogReport1);
                            telegramBot.execute(replyMessages.newhabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport1 = catReportRepository.findCatReportByFileIdAndCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getCat().getId());
                            catReport1.setOldHabitsRefuse(Boolean.FALSE);
                            catReportRepository.save(catReport1);
                            telegramBot.execute(replyMessages.newhabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        }
                        break;

                    case OLD_HABITS_POSITIVE:
                        if (shelterType.getType().equals("dog")) {
                            DogReport dogReport2 = dogReportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport2.setOldHabitsRefuse(Boolean.TRUE);
                            dogReportRepository.save(dogReport2);
                            telegramBot.execute(replyMessages.newhabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport2 = catReportRepository.findCatReportByFileIdAndCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getCat().getId());
                            catReport2.setOldHabitsRefuse(Boolean.TRUE);
                            catReportRepository.save(catReport2);
                            telegramBot.execute(replyMessages.newhabitsRequest(update).replyMarkup(keyboards.getNewHabits()));
                        }
                        break;

                    case NEW_HABITS_POSITIVE:
                        if (shelterType.getType().equals("dog")) {
                            DogReport dogReport3 = dogReportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport3.setNewHabitsAppear(Boolean.TRUE);
                            dogReportRepository.save(dogReport3);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport3 = catReportRepository.findCatReportByFileIdAndCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getCat().getId());
                            catReport3.setNewHabitsAppear(Boolean.TRUE);
                            catReportRepository.save(catReport3);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));

                        }
                        break;

                    case NEW_HABITS_NEGATIVE:
                        if (shelterType.getType().equals("dog")) {
                            DogReport dogReport4 = dogReportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport4.setNewHabitsAppear(Boolean.FALSE);
                            dogReportRepository.save(dogReport4);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport4 = catReportRepository.findCatReportByFileIdAndCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getCat().getId());
                            catReport4.setNewHabitsAppear(Boolean.FALSE);
                            catReportRepository.save(catReport4);
                            telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));

                        }
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
                if (update.message().replyToMessage().from().id().equals(5713161862L)
                        && !update.message().replyToMessage().text().contains("номер телефона")
                        && !update.message().replyToMessage().text().contains("отчет")
                        && !update.message().replyToMessage().text().contains("Вы кормите")
                        && !update.message().replyToMessage().text().contains("фотографию")
                        && !update.message().replyToMessage().text().contains("фото")) {
                    //если сообщение прислано ботом с id (5713161862L) из чата волонтеров приюта для собак
                    if (shelterType.getType().equals("dog")) {
                        telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                    }
                    //если сообщение прислано ботом с id (5713161862L) из чата волонтеров приюта для кошек
                    else {
                        telegramBot.execute(replyMessages.anotherQuestionMessageCat(update));
                    }
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
                        if (shelterType.getType().equals("dog")) {
                            DogReport dogReport = new DogReport(
                                    personDogRepository.findByChatId(update.message().chat().id()).getDog(),
                                    null,
                                    update.message().text(),
                                    null,
                                    null,
                                    Instant.ofEpochSecond(update.message().date()).atZone(ZoneId.systemDefault()).toLocalDate(),
                                    null);
                            dogReportRepository.save(dogReport);
                            telegramBot.execute(replyMessages.dietRequest(update).replyMarkup(keyboards.getAutoReply()));
                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport = new CatReport(
                                    personCatRepository.findByChatId(update.message().chat().id()).getCat(),
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
                        if (shelterType.getType().equals("dog")) {
                            DogReport dogReport2 = dogReportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport2.setDiet(update.message().text());
                            dogReportRepository.save(dogReport2);
                            telegramBot.execute(replyMessages.oldHabitsRequest(update).replyMarkup(keyboards.getOldHabits()));
                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport2 = catReportRepository.findCatReportByFileIdAndCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getCat().getId());
                            catReport2.setDiet(update.message().text());
                            catReportRepository.save(catReport2);
                            telegramBot.execute(replyMessages.oldHabitsRequest(update).replyMarkup(keyboards.getOldHabits()));

                        }
                        break;

                    case INITIAL_PHOTO_REQUEST:
                    case PERSISTENT_PHOTO_REQUEST:
                        if (update.message().photo() == null) {
                            telegramBot.execute(replyMessages.persistantPhotoRequest(update).replyMarkup(keyboards.getAutoReply()));
                        }

                        if (shelterType.getType().equals("dog")) {

                            DogReport dogReport3 = dogReportRepository.findDogReportByFileIdAndDogId(null, personDogRepository.findByChatId(update.message().chat().id()).getDog().getId());
                            dogReport3.setFileId(update.message().photo()[0].fileId());
                            dogReportRepository.save(dogReport3);
                            telegramBot.execute(replyMessages.reportIsSaved(update).replyMarkup(keyboards.getInitialKeyboard()));
                            telegramBot.execute(new SendMessage(-1001634691308L, "Получен новый отчет:" + dogReport3.toString()));
                            telegramBot.execute(new SendPhoto(-1001634691308L, dogReport3.getFileId()));

                        } else if (shelterType.getType().equals("cat")) {
                            CatReport catReport3 = catReportRepository.findCatReportByFileIdAndCatId(null, personCatRepository.findByChatId(update.message().chat().id()).getCat().getId());
                            catReport3.setFileId(update.message().photo()[0].fileId());
                            catReportRepository.save(catReport3);
                            telegramBot.execute(replyMessages.reportIsSaved(update).replyMarkup(keyboards.getInitialKeyboard()));
                            telegramBot.execute(new SendMessage(-1001865175202L, "Получен новый отчет:" + catReport3.toString()));
                            telegramBot.execute(new SendPhoto(-1001865175202L, catReport3.getFileId()));
                        }
                        break;
                }
            } catch (NullPointerException ignored) {
                logger.info("поймал NPE");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

   /* @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification() {
        logger.info("Поиск отчетов");

        for (DogReport dogReport : schedulerService.checkReports()) {
            if(dogReport.getreportDate().isAfter(LocalDate.now().minusDays(2))){


                SendMessage sendMessage = new SendMessage(-1001634691308L, "Отчет о собаке " + dogReport.getDog().getName().toString() + " от усыновителя " + dogReport.getDog().getPersonDog().getFirstName().toString() +  " не поступал уже 2 дня");

                telegramBot.execute(sendMessage);

            /*SendMessage sendToPerson = new SendMessage(dogReport.getDog().getPersonDog().getChatId(), "Где отчет???");
            telegramBot.execute(sendToPerson);*/
            }
        }
    }*/
}



