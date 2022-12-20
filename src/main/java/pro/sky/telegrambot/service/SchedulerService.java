package pro.sky.telegrambot.service;


import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.DogReport;
import pro.sky.telegrambot.repository.DogReportRepository;

import java.util.Collection;

@Service
public class SchedulerService {
    private final DogReportRepository dogReportRepository;

    public SchedulerService(DogReportRepository dogReportRepository)
    {this.dogReportRepository = dogReportRepository;
}

    public Collection<DogReport> checkReports() {

        return dogReportRepository.findDogReports();
    }}





