package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity

public class Context {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String type;
    private String addDays;
    private Boolean testOff;

    public PersonDog getPersonDog() {
        return personDog;
    }

    public void setPersonDog(PersonDog personDog) {
        this.personDog = personDog;
    }

    @OneToOne
    @JsonIgnore
    private PersonDog personDog;


    public PersonCat getPersonCat() {
        return personCat;
    }

    public void setPersonCat(PersonCat personCat) {
        this.personCat = personCat;
    }

    @OneToOne
    @JsonIgnore
    private PersonCat personCat;


    public Context() {

    }

    public Context(Long chatId, String type, String addDays, Boolean testOff) {
        this.chatId = chatId;
        this.type = type;
        this.addDays = addDays;
        this.testOff = testOff;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddDays() {
        return addDays;
    }

    public void setAddDays(String addDays) {
        this.addDays = addDays;
    }

    public Boolean getTestOff() {
        return testOff;
    }

    public void setTestOff(Boolean testOff) {
        this.testOff = testOff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(id, context.id) && Objects.equals(chatId, context.chatId) && Objects.equals(type, context.type) && Objects.equals(addDays, context.addDays) && Objects.equals(testOff, context.testOff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, type, addDays, testOff);
    }
}
