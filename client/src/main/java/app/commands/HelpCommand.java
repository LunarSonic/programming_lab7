package app.commands;
import commands.CommandName;
import network.ExecutionResponse;
import utility.User;

import java.util.Map;

/**
 * Класс команды help
 */
public class HelpCommand extends ClientCommand {
    private final Map<CommandName, String[]> commands;

    public HelpCommand(Map<CommandName, String[]> commands) {
        super(CommandName.help.name(), "вывести справку по доступным командам");
        this.commands = commands;
    }

    /**
     * Выполняется команда вывода справки по всем доступным командам
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, User user) {
        StringBuilder helpMessage = new StringBuilder("Справка по доступным командам:\n");
        commands.values().forEach(command ->
                helpMessage.append(String.format(" %-35s%-1s%n", command[0], command[1])));
        return new ExecutionResponse(helpMessage.toString());
    }
}

