package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.DogReport;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.PersonRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.PersonService;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    // Все сервис-классы, которые мы используем
    Keyboards keyboards = new Keyboards();
    ReplyMessages replyMessages = new ReplyMessages();
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final ReportRepository reportRepository;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, PersonService personService, PersonRepository personRepository, ReportRepository reportRepository) {
        this.telegramBot = telegramBot;
        this.personService = personService;
        this.personRepository = personRepository;
        this.reportRepository = reportRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            // Process your updates here

            try {
                switch (update.message().text()) {
                    // для кнопки /start
                    case "/start":
                        //Запись нового пользователя в базу
                        personService.getPersonByChatId(update);
                        // начальное меню
                        telegramBot.execute(replyMessages.initialMessage(update)
                                .replyMarkup(keyboards.getInitialKeyboard()));
                        break;

                    //  пункт 1. Информация о приюте
                    case "Узнать о приюте":
                        telegramBot.execute(replyMessages.infoMessage(update)
                                .replyMarkup(keyboards.getInfoKeyboard()));
                        break;

                    case "Расскзать о приюте":
                        telegramBot.execute(replyMessages.generalInfoMessage(update));
                        break;

                    case "График работы приюта":
                        telegramBot.execute(replyMessages.schedualInfoMessage(update));
                        break;

                    case "Показать адрес приюта":
                        telegramBot.execute(replyMessages.adressInfoMessage(update)
                                .replyMarkup(keyboards.getShowOnMap()));
                        break;

                    case "Как вести себя в приюте":
                        telegramBot.execute(replyMessages.rulesInfoMessage(update));
                        break;


                    case "Позвать волонтера":
                        // показывает кнопку "Вернуться в меню"
                        telegramBot.execute(replyMessages.feedBack(update));
                        // Пересылает запрос "Позвать волонтера" в чат волонтеров
                        telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                        break;

                    case "Поделитесь контактными данными":
                        telegramBot.execute(replyMessages.phone(update)
                                .replyMarkup(keyboards.getAutoReply()));
                        break;

                    case "Прислать отчет о питомце":
                        telegramBot.execute(replyMessages.reportRequest(update)
                                .replyMarkup(keyboards.getAutoReply()));
                }

                // запрос номера телефона у пользователя
                if (update.message().replyToMessage().text()
                        .contains("Введите номер телефона для связи")) {
                    personService.phoneNumberAdd(update);
                }

                //ответ пользователя волонтеру в чат волонтеров
                //если сообщение прислано ботом с id (5713161862L) из чата волонтеров
                if (update.message().replyToMessage().from().id().equals(5713161862L)
                        && !update.message().replyToMessage().text().contains("номер телефона")
                        && !update.message().replyToMessage().text().contains("отчет")) {
                    telegramBot.execute(replyMessages.anotherQuestionMessage(update));
                }
                // Принимает ответ от волонтера для пользователя
                //если волонтер отвечает на пересланное соlобщение из бота от пользователя
                if (update.message().replyToMessage().chat().id().equals(update.message().chat().id())) {
                    telegramBot.execute(replyMessages.replyMessage(update)
                            .replyMarkup(keyboards.getAutoReply()));
                }

            } catch (NullPointerException ignored) {
            }


            // метод для проверки поступающего отчета и его сохранения в базу данных
            try {
                if (update.message().replyToMessage().text().equals("Направьте, пожалуйста, отчет о Вашем питомце в сообщении ниже:")) {
                    if (personRepository.findByChatId(update.message().chat().id()).getDog() == null) {
                        telegramBot.execute(replyMessages.noDogResponse(update));
                    } else {
                        DogReport dogReport = new DogReport(
                                personRepository.findByChatId(update.message().chat().id()).getDog(),
                                "кушает",
                                update.message().text(),
                                Boolean.TRUE,
                                Boolean.TRUE,
                                Instant.ofEpochSecond(update.message().date()).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                "файл пока не прислали");
                        reportRepository.save(dogReport);
                        telegramBot.execute(replyMessages.photoRequest(update).replyMarkup(keyboards.getAutoReply()));
                    }
                }
            } catch (NullPointerException ignored) {}

            try {
                if (update.message().replyToMessage().text().equals("Спасибо! Теперь направьте, пожалуйста, фотограию Вашего питомца, чтобы мы убедились, что с ним все хорошо!")) {
                   DogReport dogReport = reportRepository.findDogReportByFileIdAndDogId("файл пока не прислали",personRepository.findByChatId(update.message().chat().id()).getDog().getId());
                   dogReport.setFileId(update.message().photo()[0].fileId());
                   reportRepository.save(dogReport);
                    telegramBot.execute(replyMessages.reportIsSaved(update));
                }
            } catch (NullPointerException ignored) {}
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}


