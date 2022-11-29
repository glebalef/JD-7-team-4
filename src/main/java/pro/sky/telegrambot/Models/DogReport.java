package pro.sky.telegrambot.Models;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * класс для отчетов о собаках
 */
@Entity
@Table(name = "dog_reports")
public class DogReport {

    @GeneratedValue
    @Id
    @Column (name = "id")
    private Long id;

    @Column (name = "ownerid")
    private Long ownerId;

    public DogReport(Long ownerId, Long chatId, String diet, String generalCondition, Boolean newHabitsAppear, Boolean oldHabitsRefuse, LocalDateTime reportDateTime) {
        this.ownerId = ownerId;
        this.chatId = chatId;
        this.diet = diet;
        this.generalCondition = generalCondition;
        this.newHabitsAppear = newHabitsAppear;
        this.oldHabitsRefuse = oldHabitsRefuse;
        this.reportDateTime = reportDateTime;
    }

    @Column (name = "chatid")
    private Long chatId;

    @Column (name = "diet")
    private String diet;

    @Column (name = "condition")
    private String generalCondition;

    @Column (name = "newhabits")
    private Boolean newHabitsAppear;

    @Column (name = "oldhabits")
    private Boolean oldHabitsRefuse;

    @Column (name ="datetime")
    private LocalDateTime reportDateTime;


    public DogReport(String diet, String generalCondition) {
        this.diet = diet;
        this.generalCondition = generalCondition;
    }


    public DogReport() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
    public long getChatId() {
        return chatId;
    }
    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogReport dogReport = (DogReport) o;
        return id.equals(dogReport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
