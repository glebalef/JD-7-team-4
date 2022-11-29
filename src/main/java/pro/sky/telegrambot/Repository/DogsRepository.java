package pro.sky.telegrambot.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.Models.Dog;

@Repository
public interface DogsRepository extends JpaRepository <Dog, Long> {
}
