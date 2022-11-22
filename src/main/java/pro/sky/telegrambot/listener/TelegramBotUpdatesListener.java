package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
                    logger.info("Processing update: {}", update);
                    // Process your updates here
                    if (Objects.equals(update.message().text(), "/start")) {
                        SendResponse response = telegramBot.execute(initialMessage(update)
                                .replyMarkup(initialKeyboard));
                    };
        }
        );
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    // изначальное сообщение для пользователя после команды /Start
    public SendMessage initialMessage (Update update) {
        return new SendMessage(update.message().chat().id(), "Вас приветствует бот Кожуховского приюта для собак. Пожалуйста, выберете интересующий Вас раздел");
    }


    // Клавиатура для начального экрана - после сообщения /start
    Keyboard initialKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Узнать информацию о приюте", "Как взять собаку из приюта "},
            new String[]{"Прислать отчет о питомце", "Позвать волонтера"});
}
