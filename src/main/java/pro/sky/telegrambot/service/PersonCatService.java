package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.PersonCat;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.repository.PersonCatRepository;

/**
 * сервис-класс для работы с сущностью PersonCat
 *
 * @author Евгений Фисенко
 */
@Service
public class PersonCatService {
    private final PersonCatRepository personCatRepository;
    private final TelegramBot telegramBot;

    public PersonCatService(PersonCatRepository personCatRepository, TelegramBot telegramBot) {
        this.personCatRepository = personCatRepository;
        this.telegramBot = telegramBot;
    }

    Keyboards keyboards = new Keyboards();

    /**
     * получает имя и фамилию пользователя и, если это новый пользователь, заносит пользователя в базу
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */
    public PersonCat getPersonByChatId(Update update) {

        Long chatId = update.message().chat().id();

        if (personCatRepository.findByChatId(chatId) == null) {
            PersonCat newPerson = new PersonCat();

            newPerson.setFirstName(update.message().chat().firstName());
            newPerson.setLastName(update.message().chat().lastName());
            newPerson.setChatId(chatId);
            personCatRepository.save(newPerson);
            return newPerson;
        }


        return null;
    }

    /**
     * получает от пользователя номер телефона и записывает его в таблицу PersonCat
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */

    public String phoneNumberAdd(Update update) throws WrongPhoneNumberException {
        Long chatId = update.message().chat().id();
        String phoneNumber = update.message().text();
        PersonCat personCatPhone = personCatRepository.findByChatId(chatId);

        if (phoneNumber.matches("^(([0-9]{0,3}|\\+[0-9]{1,3})[\\- ]?)?(\\(?\\d{3,4}\\)?[\\- ]?)?[\\d\\- ]{6,12}$")) {

            personCatPhone.setPhone(phoneNumber);
            personCatRepository.save(personCatPhone);

            SendMessage success = new SendMessage(chatId, "Спасибо! Ваш номер записан.")
                    .replyMarkup(keyboards.getInitialKeyboard());
            telegramBot.execute(success);
            return phoneNumber;

        } else {
            throw new WrongPhoneNumberException("номер содержит неверные символы");
        }
    }
}

