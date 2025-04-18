package utility;
import java.io.Serial;
import java.io.Serializable;

/**
 * Класс пользователя, экземпляр этого класс передаём в запросе
 */
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 32L;
    private final String login;
    private final String password;

    /**
     * Конструктор класса User
     * @param login логин
     * @param password пароль
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Геттер для логина
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Геттер для пароля
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
