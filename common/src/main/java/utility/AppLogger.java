package utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс, который отвечает за логирование в приложении
 * Выводит информационные и ошибочные сообщения
 */
public class AppLogger {
    private final Logger logger;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[36m";

    /**
     * Конструктор, принимающий класс и создающий логгер для него
     * @param clazz класс, из которого мы вызываем логгер
     */
    public AppLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Метод для вывода дополнительной информации
     * @param message сообщение
     */
    public void info(String message) {
        logger.info(ANSI_BLUE + "{}" + ANSI_RESET, message);
    }

    /**
     * Метод для вывода ошибки
     * @param message сообщение
     */
    public void error(String message) {
        logger.error(ANSI_RED + "{}" + ANSI_RESET, message);
    }
}

