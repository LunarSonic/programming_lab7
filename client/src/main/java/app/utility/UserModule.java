package app.utility;
import app.network.NetworkHandler;
import app.network.RequestCreator;
import network.ExecutionResponse;
import utility.*;
import java.io.Console;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Класс, отвечающий за регистрацию и авторизацию пользователя в приложении
 */
public class UserModule {
    Console console = System.console();
    private AppConsole appConsole = new AppConsole();
    private AppLogger logger = new AppLogger(UserModule.class);
    private NetworkHandler networkHandler;

    /**
     * Метод для взаимодействия с пользователем
     * @return объект класса User с данными пользователя
     */
    public User userAction() {
        appConsole.println("Добро пожаловать в приложение!");
        appConsole.println("Есть ли у вас учётная запись? [yes/no]");
        while (true) {
            String input = appConsole.readInput().trim().toLowerCase(Locale.ROOT);
            if (input.equals("yes")) {
                return loginUser();
            } else if (input.equals("no")) {
                return registerUser();
            }
        }
    }

    /**
     * Метод, который регистрирует нового пользователя в приложении
     * @return объект класса User, если всё успешно, иначе false
     */
    private User registerUser() {
        String login;
        String password;
        appConsole.println("Начинается регистрация пользователя ...");
        while (true) {
            appConsole.println("Введите логин: ");
            try {
                login = appConsole.readInput().trim();
                if (login.isEmpty()) {
                    logger.error("Логин не может быть пустым");
                } else if (!login.matches("^[a-zA-Z0-9._-]+$")) {
                    logger.error("Логин может состоять только из английских букв, цифр и символов . , _ , -");
                } else if (login.length() < 5) {
                    logger.error("Логин не может иметь меньше 5 символов");
                } else {
                    break;
                }
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
            } catch (NoSuchElementException e) {
                logger.error("Данное значение поля не может быть использовано");
            }
        }
        while (true) {
            appConsole.println("Введите пароль: ");
            try {
                char[] symbols = console.readPassword();
                password = String.valueOf(symbols);
                if (password.isEmpty()) {
                    logger.error("Пароль не может быть пустым");
                } else if (!password.matches("^[a-zA-Z0-9#$%&@~?!_]+$")) {
                    logger.error("Пароль может состоять только из английских букв, цифр и спец.символов # ,$ , %, &, @, ~, ?, !, _");
                } else {
                    break;
                }
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
            } catch (NoSuchElementException e) {
                logger.error("Данное значение поля не может быть использовано");
            }
        }
        User user = new User(login, password);
        RequestCreator requestCreator = new RequestCreator(user);
        ExecutionResponse response = networkHandler.sendAndReceive(requestCreator.createRegisterRequest(user));
        appConsole.println(response.getMessage());
        return user;
    }

    /**
     * Метод, который авторизует пользователя в приложении
     * @return объект класса User, если всё успешно, иначе false
     */
    private User loginUser() {
        String login;
        String password;
        int attempts = 3; //3 попытки для входа в приложение
        appConsole.println("Начинается авторизация пользователя ...");
        while (attempts > 0) {
            while (true) {
                appConsole.println("Введите логин: ");
                try {
                    login = appConsole.readInput().trim();
                    if (login.isEmpty()) {
                        logger.error("Логин не может быть пустым");
                    } else if (!login.matches("^[a-zA-Z0-9._-]+$")) {
                        logger.error("Логин может состоять только из английских букв, цифр и символов . , _ , -");
                    } else if (login.length() < 5) {
                        logger.error("Логин не может иметь меньше 5 символов");
                    } else {
                        break;
                    }
                } catch (IllegalStateException e) {
                    logger.error("Непредвиденная ошибка");
                } catch (NoSuchElementException e) {
                    logger.error("Данное значение поля не может быть использовано");
                }
            }
            while (true) {
                appConsole.println("Введите пароль: ");
                try {
                    char[] symbols = console.readPassword();
                    password = String.valueOf(symbols);
                    if (password.isEmpty()) {
                        logger.error("Пароль не может быть пустым");
                    } else if (!password.matches("^[a-zA-Z0-9#$%&@~?!_]+$")) {
                        logger.error("Пароль может состоять только из английских букв, цифр и спец.символов #, $, %, &, @, ~, ?, !, _");
                    } else {
                        break;
                    }
                } catch (IllegalStateException e) {
                    logger.error("Непредвиденная ошибка");
                } catch (Exception e) {
                    logger.error("Ошибка при вводе пароля");
                }
            }
            User user = new User(login, password);
            RequestCreator requestCreator = new RequestCreator(user);
            ExecutionResponse response = networkHandler.sendAndReceive(requestCreator.createLoginRequest(user));
            appConsole.println(response.getMessage());
            if (response.getResponse()) {
                return user;
            } else {
                attempts--;
                if (attempts > 0) {
                    logger.error("Осталось попыток для входа в приложение: " + attempts);
                } else {
                    logger.error("Превышено количество попыток для входа. Повторите действие позже");
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Сеттер для объекта класса NetworkHandler
     * @param networkHandler объекта класса NetworkHandler
     */
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
}
