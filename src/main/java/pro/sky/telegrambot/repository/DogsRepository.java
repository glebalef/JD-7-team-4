package pro.sky.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.Dog;

@Repository
public interface DogsRepository extends JpaRepository <Dog, Long> {
}
