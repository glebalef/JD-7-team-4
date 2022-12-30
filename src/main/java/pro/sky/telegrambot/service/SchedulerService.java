package pro.sky.telegrambot.service;


import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.CatReport;
import pro.sky.telegrambot.model.DogReport;
import pro.sky.telegrambot.repository.CatReportRepository;
import pro.sky.telegrambot.repository.DogReportRepository;

import java.util.Collection;

@Service
public class SchedulerService {
    private final DogReportRepository dogReportRepository;
    private final CatReportRepository catReportRepository;


    public SchedulerService(DogReportRepository dogReportRepository, CatReportRepository catReportRepository) {
        this.dogReportRepository = dogReportRepository;
        this.catReportRepository = catReportRepository;
    }

    /**
     * возвращает коллекцию последних отчетов по дате
     */
    public Collection<DogReport> findNewDogReports() {
        return dogReportRepository.findDogReports();
    }

    /**
     * возвращает коллекцию последних отчетов по дате
     */
    public Collection<DogReport> findOldDogReports() {
        return dogReportRepository.findFirstReports();
    }

    /**
     * возвращает коллекцию последних отчетов по дате
     */
    public Collection<CatReport> findNewCatReports() {
        return catReportRepository.findCatReports();
    }
    /**
     * возвращает коллекцию последних отчетов по дате
     */
    public Collection<CatReport> findOldCatReports() {
        return catReportRepository.findFirstReports();
    }

}





