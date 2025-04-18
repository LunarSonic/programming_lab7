package app.network;
import network.Request;
import commands.CommandName;

/**
 * Класс для парсинга запроса от сервера
 */
public class RequestParser {

    /**
     * Метод, который парсит команду и аргументы из полученного запроса от клиента
     * @param receivedRequest полученный запрос от клиента
     * @return запрос с командой и возможно с аргументами команды
     */
    public String[] parseCommand(Request receivedRequest) {
        String[] commandAndArgs;
        if (isCommandWithArgs(receivedRequest.getCommandName())) {
            commandAndArgs = new String[]{receivedRequest.getCommandName().toString(), receivedRequest.getCommandArgs()};
        } else if (isCommandWithArgsAndObjectArg(receivedRequest.getCommandName())) {
            String command = receivedRequest.getCommandName().toString() + " " + receivedRequest.getCommandArgs() + " " + receivedRequest.getCommandObjectArg().toString();
            commandAndArgs = command.trim().split(" ", 3);
        } else {
            commandAndArgs = new String[]{receivedRequest.getCommandName().toString()};
        }
        return commandAndArgs;
    }

    /**
     * Метод для проверки на команды с обычным аргументом
     * @param command команда
     * @return true, если это команда remove_by_id или remove_all_by_annual_turnover, иначе false
     */
    private boolean isCommandWithArgs(CommandName command) {
        return command == CommandName.remove_by_id || command == CommandName.remove_all_by_annual_turnover;
    }

    /**
     * Метод для проверки на команду с обычным аргументом и аргументом, прредставляющим собой объект Organization
     * @param command команда
     * @return true, если это команда update, иначе false
     */
    private boolean isCommandWithArgsAndObjectArg(CommandName command) {
        return command == CommandName.update;
    }
}

