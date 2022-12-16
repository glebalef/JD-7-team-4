package pro.sky.telegrambot.model;

import java.util.Objects;

/**
 * Класс выбора состояния меню telegram бота
 *
 * @author Евгений Фисенко
 */

public class ShelterType {
    /**
     * поле - состояние типа меню (для кошек или собак)
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShelterType that = (ShelterType) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
