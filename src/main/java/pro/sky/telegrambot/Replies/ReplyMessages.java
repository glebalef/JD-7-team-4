package pro.sky.telegrambot.Replies;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.Models.Shelter;

public final class ReplyMessages {

    private final Shelter shelter = new Shelter();

    // изначальное сообщение для пользователя после команды /Start
    public SendMessage initialMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Вас приветствует бот Кожуховского приюта для собак. Пожалуйста, выберете интересующий Вас раздел");
    }

    public SendMessage infoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Какую иформацию Вы хотели бы узнать?");
    }

    public SendMessage generalInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "shelter.getInfo()");
    }

    public SendMessage schedualInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSchedule());
    }

    public SendMessage adressInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getAdress());
    }

    public SendMessage rulesInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getRules());
    }

    // фраза на нажатие кнопки "Позвать оператора"
    public SendMessage feedBack(Update update) {
        return new SendMessage(update.message().chat().id(), "Ожидайте ответа оператора");
    }

    // Пересылает сообщение в чат волонтеров (Id чата: -1001634691308L)
    public ForwardMessage anotherQuestionMessage(Update update) {
        return new ForwardMessage(-1001634691308L, update.message().chat().id(), update.message().messageId());
    }

    // Возвращает ответ из чата волонтеров пользователю в бот
    public SendMessage replyMessage(Update update) {
        return new SendMessage(update.message().replyToMessage().forwardFrom().id(), update.message().text());

    }
}
