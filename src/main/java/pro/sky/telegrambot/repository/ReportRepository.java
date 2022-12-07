package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.DogReport;

@Repository
public interface ReportRepository extends JpaRepository<DogReport,Long> {

    DogReport findDogReportByFileIdAndDogId (String fileId, Long dogId);
}


