package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Person;
import pro.sky.telegrambot.constant.Shelter;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.PersonRepository;
import pro.sky.telegrambot.service.PersonService;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private TelegramBot telegramBot;

    // Все сервис-классы, которые мы используем

    Shelter shelter = new Shelter();
    Keyboards keyboards = new Keyboards();
    ReplyMessages replyMessages = new ReplyMessages();
    private final PersonRepository personRepository;
    private final PersonService personService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      PersonRepository personRepository, PersonService personService) {
        this.telegramBot = telegramBot;
        this.personRepository = personRepository;
        this.personService = personService;
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
                    case "Вернуться в меню":

                        //Запись нового пользователя в базу
                        Person newPerson = new Person();

                        if (personService.getPersonByChatId(update) == null) {
                            newPerson.setFirstName(update.message().chat().firstName());
                            newPerson.setLastName(update.message().chat().lastName());
                            newPerson.setChatId(update.message().chat().id());
                            personRepository.save(newPerson);
                        }

                        SendResponse responseInitial = telegramBot
                                .execute(replyMessages.initialMessage(update)
                                        .replyMarkup(keyboards.getInitialKeyboard()));
                        break;

                    //  пункт 1. Информация о приюте
                    case "Узнать о приюте":
                        SendResponse responseInfo = telegramBot
                                .execute(replyMessages.infoMessage(update)
                                        .replyMarkup(keyboards.getInfoKeyboard()));
                        break;

                    case "Расскзать о приюте":
                        SendResponse responseGeneralInfo = telegramBot
                                .execute(replyMessages.generalInfoMessage(update));
                        break;

                    case "График работы приюта":
                        SendResponse schedualInfo = telegramBot
                                .execute(replyMessages.schedualInfoMessage(update));
                        break;

                    case "Показать адрес приюта":
                        SendResponse adressInfo = telegramBot
                                .execute(replyMessages.adressInfoMessage(update)
                                        .replyMarkup(keyboards.getShowOnMap()));
                        break;

                    case "Как вести себя в приюте":
                        SendResponse rulesInfo = telegramBot
                                .execute(replyMessages.rulesInfoMessage(update));
                        break;


                    case "Позвать волонтера":
                        // показывает кнопку "Вернуться в меню"
                        SendResponse feedBack = telegramBot
                                .execute(replyMessages.feedBack(update)
                                        .replyMarkup(keyboards.getFeedBack()));

                        // Пересылает запрос "Позвать волонтера" в чат волонтеров
                        SendResponse callForFeedBack = telegramBot
                                .execute(replyMessages.anotherQuestionMessage(update));
                        break;

                    default:

                        // ТОТ САМЫЙ КУСОК КОДА ДЛЯ ПЕРЕСЫЛКИ СООБЩЕНИЙ ВОЛОНТЕРАМ КОТОРЫЙ
                        // НУЖНО АКТИВИРОВАТЬ ТОЛЬКО ПОСЛЕ НАЖАТИЯ "Позвать волонтера"

                        SendResponse sendToFeedBack = telegramBot
                                .execute(replyMessages.anotherQuestionMessage(update));

                        // Принимает ответ от волонтера для пользователя
                        SendResponse reply = telegramBot
                                .execute(replyMessages.replyMessage(update));
                }
            } catch (NullPointerException ignored) {
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}


