package pro.sky.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.DogReport;

@Repository
public interface DogsRepository extends JpaRepository <Dog, Long> {

}
