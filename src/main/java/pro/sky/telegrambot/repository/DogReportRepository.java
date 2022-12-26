package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.DogReport;

import java.util.Collection;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport,Long> {

    DogReport findDogReportByFileIdAndPersonDogId (String fileId, Long personId);
    /**
     * выбор последних отчетов по дате создания
     * @return возвращает коллекцию отчетов
     */
    @Query(value="select distinct  on (person_dog_id) * from dog_report order by person_dog_id, report_date desc ", nativeQuery = true)
    Collection<DogReport>findDogReports();
    /**
     * выбор первых отчетов по дате создания
     * @return возвращает коллекцию отчетов
     */
    @Query(value="select distinct  on (person_dog_id) * from dog_report order by person_dog_id, report_date ", nativeQuery = true)
     Collection<DogReport>findFirstReports();
}
