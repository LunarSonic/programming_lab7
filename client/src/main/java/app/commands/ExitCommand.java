package app.commands;
import commands.CommandName;
import network.ExecutionResponse;
import utility.User;

/**
 * Класс команды exit
 */
public class ExitCommand extends ClientCommand{

    /**
     * Конструктор класса ExitCommand
     */
    public ExitCommand() {
        super(CommandName.exit.name(), "завершить программу (без сохранения в файл)");
    }

    /**
     * Выполняется команда завершения программы
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, User user) {
        return new ExecutionResponse("Выход");
    }
}