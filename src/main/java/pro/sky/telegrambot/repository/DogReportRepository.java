package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.CatReport;
import pro.sky.telegrambot.model.DogReport;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport,Long> {

    public DogReport findDogReportByFileIdAndDogId (String fileId, Long personId);


}
