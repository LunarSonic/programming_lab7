package forms;
import exceptions.FormBreak;
import java.io.Serial;
import java.io.Serializable;

/**
 * Абстрактный класс формы для ввода пользовательских данных
 * @param <T>
 */
public abstract class BasicFormation<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    public abstract T form() throws FormBreak;
}
