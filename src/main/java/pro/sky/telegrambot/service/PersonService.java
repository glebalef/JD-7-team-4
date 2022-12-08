package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.Person;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.repository.PersonRepository;

/**
 * сервис-класс для работы с сущностью Person
 *
 * @author Евгений Фисенко
 */
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final TelegramBot telegramBot;

    public PersonService(PersonRepository personRepository, TelegramBot telegramBot) {
        this.personRepository = personRepository;
        this.telegramBot = telegramBot;
    }

    Keyboards keyboards = new Keyboards();

    /**
     * получает имя и фамилию пользователя и, если это новый пользователь, заносит пользователя в базу
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */
    public Person getPersonByChatId(Update update) {

        Long chatId = update.message().chat().id();

        if (personRepository.findByChatId(chatId) == null) {
            Person newPerson = new Person();

            newPerson.setFirstName(update.message().chat().firstName());
            newPerson.setLastName(update.message().chat().lastName());
            newPerson.setChatId(chatId);
            personRepository.save(newPerson);
            return newPerson;
        }


        return null;
    }

    /**
     * получает от пользователя номер телефона и записывает его в таблицу Person
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */

    public String phoneNumberAdd(Update update) throws WrongPhoneNumberException {
        Long chatId = update.message().chat().id();
        String phoneNumber = update.message().text();
        Person personPhone = personRepository.findByChatId(chatId);

        if (phoneNumber.matches("^(([0-9]{0,3}|\\+[0-9]{1,3})[\\- ]?)?(\\(?\\d{3,4}\\)?[\\- ]?)?[\\d\\- ]{6,12}$")) {

            personPhone.setPhone(phoneNumber);
            personRepository.save(personPhone);

            SendMessage success = new SendMessage(chatId, "Спасибо! Ваш номер записан.")
                    .replyMarkup(keyboards.getInitialKeyboard());
            telegramBot.execute(success);
            return phoneNumber;

        } else {
            throw new WrongPhoneNumberException("номер содержит неверные символы");
        }
    }
}
