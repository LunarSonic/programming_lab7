package objects;
import utility.Validatable;
import java.io.Serial;
import java.io.Serializable;

/**
 * Класс адреса
 */
public class Address implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    private final String street; //Длина строки не должна быть больше 122, Поле не может быть null

    public Address(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    /**
     * Метод для валидации поля street
     * @return true, если проходит валидацию, иначе false
     */
    @Override
    public boolean validate() {
        if (street == null) return false;
        return street.length() <= 122;
    }

    @Override
    public String toString() {
        return "Улица: " + street;
    }
}
