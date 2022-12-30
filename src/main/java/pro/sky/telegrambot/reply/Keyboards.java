package pro.sky.telegrambot.reply;

import com.pengrad.telegrambot.model.request.*;


/**
 * класс для хранения раздичных видов клавиатур, используемых при ответах бота
 */
public final class Keyboards {


    /**
     * Начальная клавитаура после /start
     */

    private final Keyboard chooseShelter = new ReplyKeyboardMarkup(
            "Приют для собак", "Приют для кошек");
    private final Keyboard initialKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Узнать о приюте", "Как взять животное из приюта?"},
            new String[]{"Прислать отчет о питомце", "Позвать волонтера"},
            new String[]{"назад к выбору приюта"});

    /**
     * клавиатура с запросами информации о приюте (константы ид класса Shelter)
     */
    private final Keyboard infoKeyboard = new ReplyKeyboardMarkup(
            new String[]{"График работы приюта", "Показать адрес приюта"},
            new String[]{"Оформить пропуск на машину", "Техника безопасности"},
            new String[]{"Поделитесь контактными данными", "Позвать волонтера"},
            new String[]{"назад к меню приюта"});
    private final Keyboard adoptKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Правила знакомства с животными", "Список необходимых документов"},
            new String[]{"Возможные причины отказа", "Рекомендации"},
            new String[]{"Поделитесь контактными данными", "Позвать волонтера"},
            new String[]{"назад к меню приюта"});
    private final Keyboard adviceKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Рекомендации по транспортировке", "Рекомендации по обустройству дома"},
            new String[]{"Советы кинолога по первичному общению", "Рекомендации по проверенным кинологам"},
            new String[]{"Позвать волонтера", "назад к инфо об усыновлении"});
    private final Keyboard adviceKeyboardCat = new ReplyKeyboardMarkup(
            new String[]{"Обустройство дома для взрослого животного", "Обустройство дома для котенка"},
            new String[]{"Рекомендации для ограниченных по зрению", "Рекомендации для ограниченных по передвижению"},
            new String[]{"Позвать волонтера", "назад к инфо об усыновлении"});
    private final Keyboard homeKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Обустройство дома для взрослого животного", "Обустройство дома для щенка"},
            new String[]{"Рекомендации для ограниченных по зрению", "Рекомендации для ограниченных по передвижению"},
            new String[]{"Позвать волонтера", "назад к рекомендациям"});
    private final Keyboard oldHabits = new ReplyKeyboardMarkup(
            new String[]{"Старые привычки пока остались :("},
            new String[]{"Питомец освоился, старых привычек нет! :)"});
    private final Keyboard newHabits = new ReplyKeyboardMarkup(
            new String[]{"Пока никаких новых привычек не приобретено..."},
            new String[]{"Поведение поменялось - много новых привычек!"});

    /**
     * клавиатура с ссылками на карты Гугл и Яндекс
     */
    private final InlineKeyboardMarkup showOnMap = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Google-карты").url("https://goo.gl/maps/Eb8FvemYc7RyCAnG6"),
            new InlineKeyboardButton("Яндекс-карты").url("https://yandex.ru/maps/-/CCUfBUVkCC"));
    private final InlineKeyboardMarkup showOnMapCat = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Google-карты").url("https://goo.gl/maps/gfSCartq8hrTyYUc8"),
            new InlineKeyboardButton("Яндекс-карты").url("https://yandex.ru/maps/-/CCUnyRUycD"));

    private final InlineKeyboardMarkup badReportCat = new InlineKeyboardMarkup(
            new InlineKeyboardButton("отчет заполнен плохо").callbackData("плохоCat"));

    private final InlineKeyboardMarkup badReport = new InlineKeyboardMarkup(
            new InlineKeyboardButton("отчет заполнен плохо").callbackData("плохо"));

    // Геттеры для клавиатур

    public InlineKeyboardMarkup getBadReportCat() {
        return badReportCat;
    }

    public InlineKeyboardMarkup getBadReport() {
        return badReport;
    }

    public Keyboard getChooseShelter() {
        return chooseShelter;
    }

    public Keyboard getAutoReply() {
        return new ForceReply();
    }

    public Keyboard getInitialKeyboard() {
        return initialKeyboard;
    }

    public Keyboard getInfoKeyboard() {
        return infoKeyboard;
    }

    public InlineKeyboardMarkup getShowOnMap() {
        return showOnMap;
    }

    public Keyboard getOldHabits() {
        return oldHabits;
    }

    public Keyboard getNewHabits() {
        return newHabits;
    }

    public Keyboard getAdoptKeyboard() {
        return adoptKeyboard;
    }

    public Keyboard getAdviceKeyboard() {
        return adviceKeyboard;
    }

    public Keyboard getAdviceKeyboardCat() {
        return adviceKeyboardCat;
    }

    public Keyboard getHomeKeyboard() {
        return homeKeyboard;
    }

    public InlineKeyboardMarkup getShowOnMapCat() {
        return showOnMapCat;
    }
}



