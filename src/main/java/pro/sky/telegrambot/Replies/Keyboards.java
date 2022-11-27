package pro.sky.telegrambot.Replies;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;


/**
 * Класс для хранения клавиатур,используемых ботом
 */
public final class Keyboards {

    /**
     * клавиатура для начального экрана после получения комнады /start
     */
   private final Keyboard initialKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Узнать о приюте", "Как взять собаку из приюта?", "Прислать отчет о питомце"},
            new String[]{"Позвать волонтера"}
    );

    /**
     * клавиатура c комнаднами для получения справочной информации о приюте
     */
   private final Keyboard infoKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Расскзать о приюте", "График работы приюта"},
            new String[]{"Показать адрес приюта", "Как вести себя в приюте"},
            new String[]{"Поделитесь вашими данными"},
            new String[]{"Позвать волонтера"}
    );

    /**
     * клавиатура c сылками на карты
     */
   private final InlineKeyboardMarkup showOnMap = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Google-карты").url("https://goo.gl/maps/Eb8FvemYc7RyCAnG6"),
            new InlineKeyboardButton("Яндекс-карты").url("https://yandex.ru/maps/-/CCUfBUVkCC"));


    /**
     * клавиатура c запросом отправки отчета о собаке
     */
    private final InlineKeyboardMarkup startReport = new InlineKeyboardMarkup(
            new InlineKeyboardButton("начать заполнение отчета"));



    // Getters
    public Keyboard getInitialKeyboard() {
        return initialKeyboard;
    }

    public Keyboard getInfoKeyboard() {
        return infoKeyboard;
    }

    public InlineKeyboardMarkup getShowOnMap() {
        return showOnMap;
    }
}



