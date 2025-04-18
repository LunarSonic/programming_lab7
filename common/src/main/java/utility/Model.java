package utility;
import java.io.Serial;
import java.io.Serializable;

/**
 * Абстрактный класс модели
 */
public abstract class Model implements Validatable, Comparable<Model>, Serializable {
    @Serial
    private static final long serialVersionUID = 16L;
    /**
     * Абстрактный метод для получения годового оборота компании
     * @return annualTurnover
     */
    public abstract long getAnnualTurnover();
}
