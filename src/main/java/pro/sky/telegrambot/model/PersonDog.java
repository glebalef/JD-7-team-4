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
public class PersonDog {

    /**
     * поле генерируемого идентификатора при записи пользователя в базу
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * поле - идентификатор собаки для таблицы PersonDog(присваивается волонтером в случае усыновления животного)
     * связь с таблицей dog
     */


    @OneToOne
    private Dog dog;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @OneToOne(mappedBy = "personDog")
    @JsonIgnore
    private Context context;


    public PersonDog() {

    }

    public PersonDog(Long chatId, String firstName, String lastName, String phone) {
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
        PersonDog personDog = (PersonDog) o;
        return Objects.equals(id, personDog.id) && Objects.equals(chatId, personDog.chatId) && Objects.equals(firstName, personDog.firstName) && Objects.equals(lastName, personDog.lastName) && Objects.equals(phone, personDog.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, firstName, lastName, phone);
    }
}
