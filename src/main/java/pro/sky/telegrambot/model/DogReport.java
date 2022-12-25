package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDate;


/**
 * класс для отчетов о собаках
 */
@Entity
@Table(name = "dog_report")
public class DogReport {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_dog_id")
    private PersonDog personDog;

    @Column(name = "diet")
    private String diet;

    @Column(name = "condition")
    private String generalCondition;

    @Column(name = "new_habits")
    private Boolean newHabitsAppear;

    @Column(name = "old_habits")
    private Boolean oldHabitsRefuse;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column (name = "file_id")
    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public DogReport() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonDog getPersonDog() {
        return personDog;
    }

    public void setPersonDog(PersonDog personDog) {
        this.personDog = personDog;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getGeneralCondition() {
        return generalCondition;
    }

    public void setGeneralCondition(String generalCondition) {
        this.generalCondition = generalCondition;
    }

    public Boolean getNewHabitsAppear() {
        return newHabitsAppear;
    }

    public void setNewHabitsAppear(Boolean newHabitsAppear) {
        this.newHabitsAppear = newHabitsAppear;
    }

    @Override
    public String toString() {
        return
                " Номер отчета:" + id +
                " Кличка собаки:" + personDog.getDog().getName() +
                " Диета по словам усыновителя:" + diet + '\'' +
                " Отчет о собаке по словам усыновителя:" + generalCondition + '\'' +
                " Появились ли новые привычик? " + newHabitsAppear +
                " Отказалась ли собака от старых привычек? " + oldHabitsRefuse +
                " Дата и время отчета: " + reportDate;
    }

    public Boolean getOldHabitsRefuse() {
        return oldHabitsRefuse;
    }

    public void setOldHabitsRefuse(Boolean oldHabitsRefuse) {
        this.oldHabitsRefuse = oldHabitsRefuse;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public DogReport(PersonDog personDog, String diet, String generalCondition, Boolean newHabitsAppear, Boolean oldHabitsRefuse, LocalDate reportDate, String fileId) {
        this.personDog = personDog;
        this.diet = diet;
        this.generalCondition = generalCondition;
        this.newHabitsAppear = newHabitsAppear;
        this.oldHabitsRefuse = oldHabitsRefuse;
        this.reportDate = reportDate;
        this.fileId = fileId;


    }
}

