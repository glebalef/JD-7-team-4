package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.PersonDog;

public interface PersonDogRepository extends JpaRepository<PersonDog, Long> {
    /**
     * ищет пользователя из таблицы person в базе данных aaktdjun
     *
     * @param id - уникальный идентификатор пользователя в приложении telegram
     * @return возвращает пользователя
     */
    PersonDog findByChatId(Long id);


}
