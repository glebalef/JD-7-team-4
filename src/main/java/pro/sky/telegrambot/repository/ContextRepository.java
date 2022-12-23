package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Context;

public interface ContextRepository extends JpaRepository<Context, Long> {


    Context findByChatId(Long id);
}
