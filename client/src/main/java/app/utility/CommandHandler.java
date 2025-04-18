package app.utility;
import commands.CommandName;
import network.ExecutionResponse;
import app.network.NetworkHandler;
import network.Request;
import app.network.RequestCreator;
import utility.User;

/**
 * Класс, предназначенный для отправки запроса от клиента на сервер
 */
public class CommandHandler {
    private final NetworkHandler networkHandler;
    private final RequestCreator requestCreator;

    public CommandHandler(NetworkHandler networkHandler, User user) {
        this.networkHandler = networkHandler;
        this.requestCreator = new RequestCreator(user);
    }

    /**
     * Обработка команды клиента, создание запроса и его отправка на сервер
     * @param command команда для выполнения
     * @param userCommand аргументы команды
     * @return результат выполнения команды от сервера
     */
    public ExecutionResponse handleCommand(CommandName command, String[] userCommand) {
        if (userCommand.length == 0 || userCommand[0].isEmpty()) {
            return new ExecutionResponse(false, "Команда не может быть пустой");
        }
        Request request = requestCreator.createRequest(command, userCommand);
        if (request == null) {
            return new ExecutionResponse(false, "Ошибка при создании организации");
        }
        return networkHandler.sendAndReceive(request);
    }
}