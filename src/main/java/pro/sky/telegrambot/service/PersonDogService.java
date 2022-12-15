package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.PersonDog;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.repository.PersonDogRepository;

/**
 * сервис-класс для работы с сущностью PersonDog
 *
 * @author Евгений Фисенко
 */
@Service
public class PersonDogService {
    private final PersonDogRepository personDogRepository;
    private final TelegramBot telegramBot;

    public PersonDogService(PersonDogRepository personDogRepository, TelegramBot telegramBot) {
        this.personDogRepository = personDogRepository;
        this.telegramBot = telegramBot;
    }

    Keyboards keyboards = new Keyboards();

    /**
     * получает имя и фамилию пользователя и, если это новый пользователь, заносит пользователя в базу
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */
    public PersonDog getPersonByChatId(Update update) {

        Long chatId = update.message().chat().id();

        if (personDogRepository.findByChatId(chatId) == null) {
            PersonDog newPerson = new PersonDog();

            newPerson.setFirstName(update.message().chat().firstName());
            newPerson.setLastName(update.message().chat().lastName());
            newPerson.setChatId(chatId);
            personDogRepository.save(newPerson);
            return newPerson;
        }


        return null;
    }

    /**
     * получает от пользователя номер телефона и записывает его в таблицу PersonDog
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */

    public String phoneNumberAdd(Update update) throws WrongPhoneNumberException {
        Long chatId = update.message().chat().id();
        String phoneNumber = update.message().text();
        PersonDog personDogPhone = personDogRepository.findByChatId(chatId);

        if (phoneNumber.matches("^(([0-9]{0,3}|\\+[0-9]{1,3})[\\- ]?)?(\\(?\\d{3,4}\\)?[\\- ]?)?[\\d\\- ]{6,12}$")) {

            personDogPhone.setPhone(phoneNumber);
            personDogRepository.save(personDogPhone);

            SendMessage success = new SendMessage(chatId, "Спасибо! Ваш номер записан.")
                    .replyMarkup(keyboards.getInitialKeyboard());
            telegramBot.execute(success);
            return phoneNumber;

        } else {
            throw new WrongPhoneNumberException("номер содержит неверные символы");
        }
    }
}
