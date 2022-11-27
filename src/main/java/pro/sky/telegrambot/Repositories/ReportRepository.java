package pro.sky.telegrambot.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.Models.DogReport;

@Repository
public interface ReportRepository extends JpaRepository<DogReport,Long> {
}
