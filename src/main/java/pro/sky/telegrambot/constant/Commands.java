package pro.sky.telegrambot.constant;

public enum Commands {
    START("/start"),
    SHELTER_MENU("Узнать о приюте"),
    SHELTER_INFO("Расскзать о приюте"),
    SHELTER_SCHEDULE("График работы приюта"),
    SHELTER_ADDRESS("Показать адрес приюта"),
    SHELTER_RULES("Как вести себя в приюте"),
    CALL_STAFF("Позвать волонтера"),
    TEL_REQUEST("Поделитесь контактными данными"),
    REPORT_REQUEST("Прислать отчет о питомце");

    String message;

    Commands(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}



