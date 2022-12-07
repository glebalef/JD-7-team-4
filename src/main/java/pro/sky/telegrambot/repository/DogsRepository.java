package pro.sky.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.DogReport;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogsRepository extends JpaRepository <Dog, Long> {
}
