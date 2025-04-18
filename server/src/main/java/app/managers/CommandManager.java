package app.managers;
import app.commands.*;
import commands.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

/**
 * Класс для управления командами
 */
public class CommandManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 19L;
    private final CollectionManager collectionManager;
    private static final int COMMAND_HISTORY_SIZE = 15;
    protected Queue<String> commandHistory = new ArrayDeque<>();
    private final Map<String, ServerCommand> commands; //словарь, который необходим для хранения команд и их названий

    /**
     * Конструктор менеджера команд
     */
    public CommandManager() {
        this.collectionManager = CollectionManager.getInstance();
        this.commands = initCommands();
    }

    /**
     * Метод для получения истории команд
     * @return история команд
     */
    public Queue<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Метод для инициализации команд
     * @return Map с командами
     */
    public Map<String, ServerCommand> initCommands() {
        return Map.ofEntries(
                Map.entry(CommandName.add.name(), new AddCommand()),
                Map.entry(CommandName.add_if_max.name(), new AddIfMaxCommand()),
                Map.entry(CommandName.add_if_min.name(), new AddIfMinCommand()),
                Map.entry(CommandName.clear.name(), new ClearCommand()),
                Map.entry(CommandName.history.name(), new HistoryCommand(this)),
                Map.entry(CommandName.info.name(), new InfoCommand()),
                Map.entry(CommandName.max_by_postal_address.name(), new MaxByPostalAddressCommand()),
                Map.entry(CommandName.remove_all_by_annual_turnover.name(), new RemoveAllByAnnualTurnoverCommand()),
                Map.entry(CommandName.remove_by_id.name(), new RemoveByIdCommand()),
                Map.entry(CommandName.show.name(), new ShowCommand()),
                Map.entry(CommandName.sum_of_annual_turnover.name(), new SumOfAnnualTurnoverCommand()),
                Map.entry(CommandName.update.name(), new UpdateCommand())
        );
    }

    /**
     * Метод для добавления команды в историю команд (история всегда обновляется, всего может быть максимум 15 команд)
     * @param command команда, которая добавляется в историю команд
     */
    public void addCommandToHistory(String command) {
        if (commandHistory.size() >= COMMAND_HISTORY_SIZE) {
            commandHistory.poll();
        }
        commandHistory.offer(command);
    }

    /**
     * Геттер для получения всех команд, добавленных в Command Manager
     * @return команды, которые хранятся в Command Manager
     */
    public Map<String, ServerCommand> getCommands() {
        return commands;
    }

    /**
     * Геттер для получения объекта класса CollectionManager
     * @return collectionManager
     */
    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}

