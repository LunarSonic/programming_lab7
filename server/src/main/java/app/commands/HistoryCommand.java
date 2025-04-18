package app.commands;
import commands.CommandName;
import app.managers.CommandManager;
import network.ExecutionResponse;
import utility.User;
import java.io.Serializable;

/**
 * Класс команды history для вывода последних 15 команд
 */
public class HistoryCommand extends ServerCommand {
    private final CommandManager commandManager;

    /**
     * Конструктор класса HistoryCommand
     */
    public HistoryCommand(CommandManager commandManager) {
        super(CommandName.history.name(), "вывести последние 15 команд (без их аргументов)");
        this.commandManager = commandManager;
    }

    /**
     * Выполняется команда вывода истории команд
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (commandManager.getCommandHistory() == null) {
            return new ExecutionResponse(false, "История пустая!");
        }
        StringBuilder historyOutput = new StringBuilder("Введённые команды: \n");
        for (String command : commandManager.getCommandHistory()) {
            historyOutput.append(command).append("\n");
        }
        return new ExecutionResponse(historyOutput.toString());
    }
}