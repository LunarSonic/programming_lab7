package network;
import java.io.Serial;
import java.io.Serializable;

/**
 * Класс для хранения и вывода информации о результате выполнения метода на сервере
 */
public class ExecutionResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 15L;
    private final boolean response; //переменная, которая указывает на успешность метода
    private final String message; //сообщение с описанием

    /**
     * Конструктор класса ExecutionResponse
     * @param response ответ (true, если успешно, иначе false)
     * @param message сообщение о результате выполнения
     */
    public ExecutionResponse(boolean response, String message) {
        this.response = response;
        this.message = message;
    }

    /**
     * Конструктор класса ExecutionResponse, по умолчанию команда была выполнена успешно
     * @param message сообщение о результате выполнения
     */
    public ExecutionResponse(String message) {
        this(true, message);
    }

    /**
     * Геттер для получения сообщения о выполнении
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Геттер для получения ответа о выполнении
     * @return response
     */
    public boolean getResponse() {
        return response;
    }

    /**
     * Переопределённый метод toString
     * @return строка в определённом формате
     */
    @Override
    public String toString() {
        return getResponse() + "; " + getMessage();
    }
}

