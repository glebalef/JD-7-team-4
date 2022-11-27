package pro.sky.telegrambot.Replies;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import pro.sky.telegrambot.Models.DogReport;
import pro.sky.telegrambot.Models.Shelter;
import pro.sky.telegrambot.Repositories.ReportRepository;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * Класс для хранение методов, по отпрвке сообщений
 */
public final class ReplyMessages {

    private final Shelter shelter = new Shelter();

    /**
     * Начальное сообшение с приветсвием и выбором начальных сообщений
     */
    public SendMessage initialMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Вас приветствует бот Кожуховского приюта для собак. Пожалуйста, выберете интересующий Вас раздел");
    }

    /**
     * Сообщение с запросом выбора более подробной информации оприюте
     */
    public SendMessage infoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Какую иформацию Вы хотели бы узнать?");
    }

    /**
     * Сообщение с общей информацией о приюте (через геттер)
     */
    public SendMessage generalInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getInfo());
    }

    /**
     * Сообщение с расписание работы приюта (через геттер)
     */
    public SendMessage schedualInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSchedule());
    }
    /**
     * Сообщение с адресом приюта и ссылками на карты в Yandex и Google (через геттер)
     */
    public SendMessage adressInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getAdress());
    }

    /**
     * Сообщение с правилами поведения в приюте (через геттер)
     */
    public SendMessage rulesInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getRules());

    }

    /**
     * Сообщение с запросом отчета о собаке (в разработке)
     */
    public SendMessage requestDogReport (Update update) {
        return new SendMessage(update.message().chat().id(), "Пожалуйста направьте ответом на данное сообщение отчет о Вашем питомце в следующем формате: " +
                " **чем питается ваш питомец**каково общее состояние Вашего питомца**появились ли новые привычки у питомца (да/нет)?**исчезли ли старые привычки у питомца(да/нет)?**");
    }
}
