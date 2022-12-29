package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.PersonCat;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.repository.PersonCatRepository;

import java.util.Optional;

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

    /**
     * Метод добавления Усыновителя кошки
     *
     * @param personCat создается объект Усыновитель кошки
     * @return Новый усыновитель
     */
    public PersonCat addPersonCat(PersonCat personCat) {
        return personCatRepository.save(personCat);
    }

    /**
     * Метод поиска усыновителя кошки по его идентификатору в БД.
     * <br>
     * Используется метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#findById(Object)}
     *
     * @param id идентификатор искомого усыновителя кошки
     * @return найденный усыновитель кошки
     */
    public PersonCat getPersonCat(Long id) {
        return personCatRepository.findById(id).orElse(null);
    }

    /**
     * Метод присвоения кошки усыновителю и редактирования необходимых данных
     *
     * @param personCat Усыновитель, которому необходимо присвоить кошку
     * @return присвоенная кошка усыновителю, отредактированные данные
     */

    public PersonCat EditPersonCatAndAssignCat(Long id, PersonCat personCat) {
        Optional<PersonCat> optional = personCatRepository.findById(id);
        if (optional.isPresent()) {
            PersonCat fromDB = optional.get();
            fromDB.setFirstName(personCat.getFirstName());
            fromDB.setLastName(personCat.getLastName());
            fromDB.setPhone(personCat.getPhone());
            return personCatRepository.save(fromDB);
        }
        return null;
    }

    /**
     * Метод удаления усыновителя кошки из БД
     *
     * @param id идентификатор удаляемого усыновителя кошки
     */
    public void deletePersonCat(Long id) {
        personCatRepository.deleteById(id);
    }
}

