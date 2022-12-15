package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс пользователя telegram ботом
 *
 * @author Евгений Фисенко
 */
@Entity
public class PersonCat {

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
     * поле - идентификатор собаки для таблицы PersonCat(присваивается волонтером в случае усыновления животного)
     * связь с таблицей dog
     */


    @OneToOne
    private Cat cat;

    public PersonCat() {

    }

    public PersonCat(Long chatId, String firstName, String lastName, String phone) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;

    }


    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
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
        PersonCat personCat = (PersonCat) o;
        return Objects.equals(id, personCat.id) && Objects.equals(chatId, personCat.chatId) && Objects.equals(firstName, personCat.firstName) && Objects.equals(lastName, personCat.lastName) && Objects.equals(phone, personCat.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, firstName, lastName, phone);
    }
}
