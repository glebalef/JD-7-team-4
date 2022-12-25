package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.CatReport;
import pro.sky.telegrambot.model.DogReport;

public interface CatReportRepository extends JpaRepository<CatReport,Long> {
    public CatReport findCatReportByFileIdAndPersonCatId (String fileId, Long personId);

}
