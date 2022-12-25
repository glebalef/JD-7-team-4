package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDate;


/**
 * класс для отчетов о собаках
 */
@Entity
@Table(name = "cat_report")
public class CatReport  {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_cat_id")
    private PersonCat personCat;

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

    public CatReport() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonCat getPersonCat() {
        return personCat;
    }

    public void setPersonCat(PersonCat personCat) {
        this.personCat = personCat;
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
                        " Кличка кошки:" + personCat.getCat().getName() +
                        " Диета по словам усыновителя:" + diet + '\'' +
                        " Отчет о кошке по словам усыновителя:" + generalCondition + '\'' +
                        " Появились ли новые привычик? " + newHabitsAppear +
                        " Отказалась ли кошка от старых привычек? " + oldHabitsRefuse +
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

    public CatReport(PersonCat personCat, String diet, String generalCondition, Boolean newHabitsAppear, Boolean oldHabitsRefuse, LocalDate reportDate, String fileId) {
        this.personCat = personCat;
        this.diet = diet;
        this.generalCondition = generalCondition;
        this.newHabitsAppear = newHabitsAppear;
        this.oldHabitsRefuse = oldHabitsRefuse;
        this.reportDate = reportDate;
        this.fileId = fileId;


    }
}

