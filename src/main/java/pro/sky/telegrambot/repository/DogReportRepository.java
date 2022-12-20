package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.DogReport;

import java.util.Collection;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport,Long> {

    public DogReport findDogReportByFileIdAndDogId (String fileId, Long personId);

    @Query(value="select  dog_id, max(report_date)from dog_report group by dog_id", nativeQuery = true)
    public Collection<DogReport>findDogReports();


}
