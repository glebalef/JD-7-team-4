package pro.sky.telegrambot.constant;

import java.util.Objects;

public enum Commands {
    START("/start"),
    SHELTER_MENU("Узнать о приюте"),
    SHELTER_INFO("Расскзать о приюте"),
    SHELTER_SCHEDULE("График работы приюта"),
    SHELTER_ADDRESS("Показать адрес приюта"),
    SHELTER_RULES("Как вести себя в приюте"),
    CALL_STAFF("Позвать волонтера"),
    TEL_REQUEST("Поделитесь контактными данными"),
    REPORT_REQUEST("Прислать отчет о питомце"),
    OLD_HABITS_NEGATIVE("Питомец освоился, старых привычек нет! :)"),
    OLD_HABITS_POSITIVE("Старые привычки пока остались :("),
    NEW_HABITS_POSITIVE("Поведение поменялось - много новых привычек!"),
    NEW_HABITS_NEGATIVE("Пока никаких новых привычек не приобретено..."),

    PERSISTENT_PHOTO_REQUEST("Пожалуйста, направьте, фото! Нам надо взглянуть на Вашего зверька."),
    REPLY_REPORT_REQUEST ("Направьте, пожалуйста, отчет о Вашем питоце в сообщении ниже:"),
    DESCRIBE_DIET ("Опишите, пожалуйста, чем Вы кормите Вашего питомца:"),
    INITIAL_PHOTO_REQUEST ("Спасибо! Теперь направьте, пожалуйста, фотограию Вашего питомца, чтобы мы убедились, что с ним все хорошо!");





    final String message;

    Commands(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}



