package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Person;
import pro.sky.telegrambot.repository.PersonRepository;

/**
 * сервис-класс для работы с сущностью Person
 *
 * @author Евгений Фисенко
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * получает имя и фамилию пользователя и, если это новый пользователь, заносит пользователя в базу
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     * @return возвращает пользователя
     */
    public Person getPersonByChatId(Update update) {

        Person newPerson = new Person();

        if (personRepository.findByChatId(update.message().chat().id()) == null) {

            newPerson.setFirstName(update.message().chat().firstName());
            newPerson.setLastName(update.message().chat().lastName());
            newPerson.setChatId(update.message().chat().id());
            personRepository.save(newPerson);
        }

        return newPerson;
    }
}
