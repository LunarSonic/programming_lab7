package app.commands;
import commands.CommandName;
import network.ExecutionResponse;
import utility.User;

/**
 * Класс команды execute_script
 */
public class ExecuteScriptCommand extends ClientCommand {

    /**
     * Конструктор класса ExecuteScriptCommand
     */
    public ExecuteScriptCommand() {
        super(CommandName.execute_script.getName(), "считать и исполнить скрипт из указанного файла");
    }

    /**
     * Выполняется команда выполнения скрипта
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, User user) {
        return new ExecutionResponse("");
    }
}
