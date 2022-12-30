package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.CatReport;
import pro.sky.telegrambot.model.DogReport;

import java.util.Collection;

public interface CatReportRepository extends JpaRepository<CatReport,Long> {
    public CatReport findCatReportByFileIdAndPersonCatId (String fileId, Long personId);

    /**
     * выбор последних отчетов по дате создания
     * @return возвращает коллекцию отчетов
     * */

    @Query(value="select distinct  on (person_cat_id) * from cat_report order by person_cat_id, report_date desc ", nativeQuery = true)
    Collection<CatReport> findCatReports();
    /**
     * выбор первых отчетов по дате создания
     * @return возвращает коллекцию отчетов
     */
    @Query(value="select distinct  on (person_cat_id) * from cat_report order by person_cat_id, report_date ", nativeQuery = true)
    Collection<CatReport>findFirstReports();
}


