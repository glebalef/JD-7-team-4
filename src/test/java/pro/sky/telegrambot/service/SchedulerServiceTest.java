package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.CatReportRepository;
import pro.sky.telegrambot.repository.DogReportRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {


    @Mock
    DogReportRepository repositoryMockDog;
    @Mock
    CatReportRepository repositoryMockCat;

    @InjectMocks
    SchedulerService schedulerService;
    PersonDog personDog = new PersonDog(123456789L, "John", "Johnson", null);
    final PersonCat personCat = new PersonCat(123456789L, "John", "Johnson", null);
    final Dog dog = new Dog(1L, "Rex", 1, "unknown");
    final Cat cat = new Cat(1L, "Tom", 1, "unknown");

    LocalDate date = LocalDate.of(2022, 12, 01);


    final DogReport dogReport = new DogReport(personDog, "мясо", "хорошо", false, true, date, null);

    final CatReport catReport = new CatReport(personCat, "мясо", "хорошо", false, true, date, null);

    @Test
    void findOldDogReports() {
        personDog.setDog(dog);
        Collection<DogReport> report = new ArrayList<>();
        report.add(dogReport);
        when(repositoryMockDog.findFirstReports()).thenReturn(report);
        assertThat(schedulerService.findOldDogReports()).isEqualTo(report);
    }

    @Test
    void findNewDogReports() {
        personDog.setDog(dog);
        Collection<DogReport> report = new ArrayList<>();
        report.add(dogReport);
        when(repositoryMockDog.findDogReports()).thenReturn(report);
        assertThat(schedulerService.findNewDogReports()).isEqualTo(report);
    }

    @Test
    void findNewCatReports() {
        personCat.setCat(cat);
        Collection<CatReport> report = new ArrayList<>();
        report.add(catReport);
        when(repositoryMockCat.findCatReports()).thenReturn(report);
        assertThat(schedulerService.findNewCatReports()).isEqualTo(report);
    }

    @Test
    void findOldCatReports() {
        personCat.setCat(cat);
        Collection<CatReport> report = new ArrayList<>();
        report.add(catReport);
        when(repositoryMockCat.findFirstReports()).thenReturn(report);
        assertThat(schedulerService.findOldCatReports()).isEqualTo(report);
    }
}