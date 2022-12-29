package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.PersonDog;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.repository.DogsRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;

import java.util.Optional;

/**
 * сервис-класс для работы с сущностью PersonDog
 *
 * @author Евгений Фисенко
 */
@Service
public class PersonDogService {
    private final PersonDogRepository personDogRepository;
    private final TelegramBot telegramBot;
    private final DogsRepository dogsRepository;

    public PersonDogService(PersonDogRepository personDogRepository, TelegramBot telegramBot, DogsRepository dogsRepository) {
        this.personDogRepository = personDogRepository;
        this.telegramBot = telegramBot;
        this.dogsRepository = dogsRepository;
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

    /**
     * Метод добавления Усыновителя собаки
     *
     * @param personDog создается объект Усыновитель собаки
     * @return Новый усыновитель
     */
    public PersonDog addPersonDog(PersonDog personDog) {
        return personDogRepository.save(personDog);
    }

    /**
     * Метод поиска усыновителя собаки по его идентификатору в БД.
     * <br>
     * Используется метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#findById(Object)}
     *
     * @param id идентификатор искомого усыновителя собаки
     * @return найденный усыновитель собаки
     */
    public PersonDog getPersonDog(Long id) {
        return personDogRepository.findById(id).orElse(null);
    }

    /**
     * Метод присвоения собаки усыновителю и редактирования необходимых данных
     *
     * @param personDog Усыновитель, которому необходимо присвоить собаку
     * @return присвоенная собака усыновителю, отредактированные данные
     */

    public PersonDog EditPersonDogAndAssignDog(Long id, PersonDog personDog) {
        Optional<PersonDog> optional = personDogRepository.findById(id);
        if (optional.isPresent()) {
            PersonDog fromDB = optional.get();
            fromDB.setFirstName(personDog.getFirstName());
            fromDB.setLastName(personDog.getLastName());
            fromDB.setPhone(personDog.getPhone());
            return personDogRepository.save(fromDB);
        }
        return null;
    }

    /**
     * Метод удаления усыновителя собаки из БД
     *
     * @param id идентификатор удаляемого усыновителя собаки
     */
    public void deleteDog(Long id) {
        personDogRepository.deleteById(id);
    }

}
