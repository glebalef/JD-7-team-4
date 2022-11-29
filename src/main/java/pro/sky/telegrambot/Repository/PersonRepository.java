package pro.sky.telegrambot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.Models.Person;

import java.util.Collection;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Collection<Person> findByChatId(Long id);
}
