package pro.sky.telegrambot.reply;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.constant.Shelter;

public final class ReplyMessages {

    private final Shelter shelter = new Shelter();

    // изначальное сообщение для пользователя после команды /Start

    public SendMessage chooseShelterMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Пожалуйста, выберете интересующий Вас приют");
    }

    public SendMessage initialMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Пожалуйста, выберете интересующий Вас раздел");
    }

    public SendMessage infoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Какую информацию Вы хотели бы узнать?");
    }

    public SendMessage reportRequest(Update update) {
        return new SendMessage(update.message().chat().id(), "Направьте, пожалуйста, отчет о Вашем питомце в сообщении ниже:");
    }

    public SendMessage noDogResponse(Update update) {
        return new SendMessage(update.message().chat().id(), "Чтобы отправить отчет, у Вас должен быть питомец!");
    }

    public SendMessage reportIsSaved(Update update) {
        return new SendMessage(update.message().chat().id(), "Спасибо! Ваш отчет успешно сохранен.");
    }

    public SendMessage generalInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getInfo());
    }

    public SendMessage generalInfoMessageCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getInfoCat());
    }

    public SendMessage scheduleInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSchedule());
    }

    public SendMessage scheduleInfoMessageCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getScheduleCat());
    }

    public SendMessage addressInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getAddress());
    }

    public SendMessage addressInfoMessageCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getAddressCat());
    }

    public SendMessage rulesInfoMessage(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getRules());
    }

    public SendMessage rulesInfoMessageCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getRulesCat());
    }

    public SendMessage securityPass(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSecurityPass());
    }

    public SendMessage securityPassCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSecurityPassCat());
    }

    public SendMessage safety(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSafety());
    }

    public SendMessage safetyCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getSafetyCat());
    }

    public SendMessage docsToAdopt(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getDocsToAdopt());
    }

    public SendMessage transportation(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getTransportation());
    }

    public SendMessage transportationCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getTransportationCat());
    }

    public SendMessage rejectCauses(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getRejectCauses());
    }

    public SendMessage homeAdultCat(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getHomeAdultCat());
    }

    public SendMessage homeAdult(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getHomeAdult());
    }

    public SendMessage puppy(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getPuppy());
    }

    public SendMessage kitten(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getKitten());
    }

    public SendMessage badReportReply(Update update) {
        return new SendMessage(update.callbackQuery().from().id(), shelter.getBadReport());
    }

    public SendMessage blind(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getBlind());
    }

    public SendMessage disabled(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getDisabled());
    }

    public SendMessage dogHandlerAdvice(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getDogHandlerAdvice());
    }

    public SendMessage bestDogHandlers(Update update) {
        return new SendMessage(update.message().chat().id(), shelter.getBestDogHandlers());
    }

    public SendMessage phone(Update update) {
        return new SendMessage(update.message().chat().id(), "Введите номер телефона для связи");
    }

    /**
     * присылает фразу "Ожидайте ответа оператора" при выборе пункта "Позвать оператора"
     *
     * @param update- данные о сообщении пользователя из TelegramBotUpdateListener
     * @return возвращает строку "Ожидайте ответа оператора"
     */
    public SendMessage feedBack(Update update) {
        return new SendMessage(update.message().chat().id(), "Ожидайте ответа оператора");
    }

    /**
     * Пересылает сообщение в чат волонтеров приюта для собак (Id чата: -1001634691308L)
     *
     * @param update - данные о сообщении пользователя из TelegramBotUpdateListener
     * @return возвращает идентификатор сообщения, которое необходимо переслать в чат поддержки
     */
    public ForwardMessage anotherQuestionMessage(Update update) {
        return new ForwardMessage(-1001634691308L, update.message().chat().id(), update.message().messageId());
    }

    /**
     * Пересылает сообщение в чат волонтеров приютя для кошек (Id чата: -1001865175202L)
     *
     * @param update - данные о сообщении пользователя из TelegramBotUpdateListener
     * @return возвращает идентификатор сообщения, которое необходимо переслать в чат поддержки
     */
    public ForwardMessage anotherQuestionMessageCat(Update update) {
        return new ForwardMessage(-1001865175202L, update.message().chat().id(), update.message().messageId());
    }

    /**
     * Возвращает ответ из чата волонтеров приюта для собак пользователю в бот
     *
     * @param update- данные о сообщении пользователя из TelegramBotUpdateListener
     * @return возвращает строку сообщения из чата поддержки
     */

    public SendMessage replyMessage(Update update) {
        return new SendMessage(update.message().replyToMessage().forwardFrom().id(), update.message().text());
    }

    public SendMessage photoRequest(Update update) {
        return new SendMessage(update.message().chat().id(), "Спасибо! Теперь направьте, пожалуйста, фотографию Вашего питомца, чтобы мы убедились, что с ним все хорошо!");
    }

    public SendMessage dietRequest(Update update) {
        return new SendMessage(update.message().chat().id(), "Опишите, пожалуйста, чем Вы кормите Вашего питомца:");
    }

    public SendMessage oldHabitsRequest(Update update) {
        return new SendMessage(update.message().chat().id(), "Расскажите, пропали ли у Вашего питомца приютские привычки?");
    }

    public SendMessage newHabitsRequest(Update update) {
        return new SendMessage(update.message().chat().id(), "А теперь расскажите, ведет ли себя питомец как-то по-новому?");
    }

    public SendMessage persistentPhotoRequest(Update update) {
        return new SendMessage(update.message().chat().id(), "Пожалуйста, направьте, фото! Нам надо взглянуть на Вашего зверька.");
    }
}
