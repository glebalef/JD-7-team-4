package pro.sky.telegrambot.constant;

public enum Commands {
    START("/start"),
    DOG_SHELTER("Приют для собак"),
    CAT_SHELTER("Приют для кошек"),
    SHELTER_MENU("Узнать о приюте"),
    SHELTER_SCHEDULE("График работы приюта"),
    SHELTER_ADDRESS("Показать адрес приюта"),
    SHELTER_RULES("Правила знакомства с животными"),
    CALL_STAFF("Позвать волонтера"),
    HOW_TO_ADOPT("Как взять животное из приюта?"),
    CAR_PASS("Оформить пропуск на машину"),
    SAFETY("Техника безопасности"),
    DOCS_TO_ADOPT("Список необходимых документов"),
    HOME_ADVICE("Рекомендации по обустройству дома"),
    TRANSPORTATION("Рекомендации по транспортировке"),
    RECOMMENDATIONS("Рекомендации"),
    REJECT_CAUSES("Возможные причины отказа"),
    DOG_HANDLER_ADVICE("Советы кинолога по первичному общению"),
    BEST_DOG_HANDLERS("Рекомендации по проверенным кинологам"),
    HOME_ADULT("Обустройство дома для взрослого животного"),
    KITTEN("Обустройство дома для котенка"),
    PUPPY("Обустройство дома для щенка"),
    BLIND("Рекомендации для ограниченных по зрению"),
    DISABLED("Рекомендации для ограниченных по передвижению"),
    TEL_REQUEST("Поделитесь контактными данными"),
    REPORT_REQUEST("Прислать отчет о питомце"),
    OLD_HABITS_NEGATIVE("Питомец освоился, старых привычек нет! :)"),
    OLD_HABITS_POSITIVE("Старые привычки пока остались :("),
    NEW_HABITS_POSITIVE("Поведение поменялось - много новых привычек!"),
    NEW_HABITS_NEGATIVE("Пока никаких новых привычек не приобретено..."),
    PERSISTENT_PHOTO_REQUEST("Пожалуйста, направьте, фото! Нам надо взглянуть на Вашего зверька."),
    REPLY_REPORT_REQUEST("Направьте, пожалуйста, отчет о Вашем питоце в сообщении ниже:"),
    DESCRIBE_DIET("Опишите, пожалуйста, чем Вы кормите Вашего питомца:"),
    INITIAL_PHOTO_REQUEST("Спасибо! Теперь направьте, пожалуйста, фотографию Вашего питомца, чтобы мы убедились, что с ним все хорошо!"),
    BACK_TO_CHOOSE_SHELTER("назад к выбору приюта"),
    BACK_TO_INITIAL("назад к меню приюта"),
    BACK_TO_ADOPT("назад к инфо об усыновлении"),
    BACK_TO_ADVICE("назад к рекомендациям");


    final String message;

    Commands(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}



