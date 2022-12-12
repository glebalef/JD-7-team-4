package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс пользователя telegram ботом
 *
 * @author Евгений Фисенко
 */
@Entity
public class Person {

    /**
     * поле генерируемого идентификатора при записи пользователя в базу
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * поле уникального идентификатора пользователя в приложении telegram (присваивается автоматически)
     */
    private Long chatId;
    /**
     * поле - имя пользователя (передается приложением telegram)
     */
    private String firstName;
    /**
     * поле - фамилия пользователя (передается приложением telegram)
     */
    private String lastName;

    /**
     * поле - номер телефона пользователя (передается пользователем)
     */
    private String phone;
    /**
     * поле - идентификатор собаки для таблицы person(присваивается волонтером в случае усыновления животного)
     * связь с таблицей dog
     */
    @OneToOne
    private Dog dog;
    public Person() {

    }

    public Person(Long chatId, String firstName, String lastName, String phone) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }


    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(chatId, person.chatId) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(phone, person.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, firstName, lastName, phone);
    }
}
