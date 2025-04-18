package objects;
import utility.Validatable;
import java.io.Serial;
import java.io.Serializable;

/**
 * Класс координат
 */
public class Coordinates implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 11L;
    private final Double x; //Значение поля должно быть больше -947, Поле не может быть null
    private final Long y; //Поле не может быть null

    public Coordinates(Double x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    @Override
    public boolean validate() {
        if(x == null) return false;
        if(y == null) return false;
        return (x >= -947);
    }
}

