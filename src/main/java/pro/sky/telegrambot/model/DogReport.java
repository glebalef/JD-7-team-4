package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


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
    @JoinColumn(name = "dog")
    private Dog dog;

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

    public DogReport() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
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

    public DogReport(Dog dog, String diet, String generalCondition, Boolean newHabitsAppear, Boolean oldHabitsRefuse, LocalDateTime reportDateTime, String fileId) {
        this.dog = dog;
        this.diet = diet;
        this.generalCondition = generalCondition;
        this.newHabitsAppear = newHabitsAppear;
        this.oldHabitsRefuse = oldHabitsRefuse;
        this.reportDateTime = reportDateTime;
        this.fileId = fileId;


    }
}

