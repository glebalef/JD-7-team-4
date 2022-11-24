package pro.sky.telegrambot.listener;

import pro.sky.telegrambot.Replies.Keyboards;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Models.Shelter;
import pro.sky.telegrambot.Replies.ReplyMessages;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    @Autowired
    private TelegramBot telegramBot;

    // Все сервис-классы, которые мы используем

    Shelter shelter = new Shelter();
    Keyboards keyboards = new Keyboards();
    ReplyMessages replyMessages = new ReplyMessages();

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
                    logger.info("Processing update: {}", update);
                    // Process your updates here
                    switch (update.message().text()) {

                        // для кнопки /start
                        case "/start":
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

//

                    }
                    ;
                }
        );
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}


