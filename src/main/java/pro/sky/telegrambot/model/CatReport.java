package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDateTime;


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
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Column(name = "diet")
    private String diet;

    @Column(name = "condition")
    private String generalCondition;

    @Column(name = "newhabits")
    private Boolean newHabitsAppear;

    @Column(name = "oldhabits")
    private Boolean oldHabitsRefuse;

    @Column(name = "report_date")
    private LocalDateTime reportDateTime;

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

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
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
                        " Кличка кошки:" + cat.getName() +
                        " Диета по словам усыновителя:" + diet + '\'' +
                        " Отчет о кошке по словам усыновителя:" + generalCondition + '\'' +
                        " Появились ли новые привычик? " + newHabitsAppear +
                        " Отказалась ли кошка от старых привычек? " + oldHabitsRefuse +
                        " Дата и время отчета: " + reportDateTime;
    }

    public Boolean getOldHabitsRefuse() {
        return oldHabitsRefuse;
    }

    public void setOldHabitsRefuse(Boolean oldHabitsRefuse) {
        this.oldHabitsRefuse = oldHabitsRefuse;
    }

    public LocalDateTime getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(LocalDateTime reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public CatReport(Cat cat, String diet, String generalCondition, Boolean newHabitsAppear, Boolean oldHabitsRefuse, LocalDateTime reportDateTime, String fileId) {
        this.cat = cat;
        this.diet = diet;
        this.generalCondition = generalCondition;
        this.newHabitsAppear = newHabitsAppear;
        this.oldHabitsRefuse = oldHabitsRefuse;
        this.reportDateTime = reportDateTime;
        this.fileId = fileId;


    }
}

