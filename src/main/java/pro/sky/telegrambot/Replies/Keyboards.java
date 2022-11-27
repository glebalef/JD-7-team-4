package pro.sky.telegrambot.Replies;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public final class Keyboards {

    // Клавиатура для начального экрана - после сообщения /start
    private final Keyboard initialKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Узнать о приюте", "Как взять собаку из приюта?", "Прислать отчет о питомце"},
            new String[]{"Позвать волонтера"}
    );

    // клавитура экрана инфорамации с разными вопросами о приюте
    private final Keyboard infoKeyboard = new ReplyKeyboardMarkup(
            new String[]{"Расскзать о приюте", "График работы приюта"},
            new String[]{"Показать адрес приюта", "Как вести себя в приюте"},
            new String[]{"Поделитесь вашими данными"},
            new String[]{"Позвать волонтера"}
    );

    // клавитура со ссылками на карты ('схема маршрута')
    private final InlineKeyboardMarkup showOnMap = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Google-карты").url("https://goo.gl/maps/Eb8FvemYc7RyCAnG6"),
            new InlineKeyboardButton("Яндекс-карты").url("https://yandex.ru/maps/-/CCUfBUVkCC"));


    // клавитура с кнопкой возврата из меню "Позвать волонтера"
    private final Keyboard feedBack = new ReplyKeyboardMarkup(
            "Вернуться в меню");

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

    public Keyboard getFeedBack() {
        return feedBack;
    }
}



