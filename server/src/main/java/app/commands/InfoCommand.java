package app.commands;
import commands.CommandName;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import utility.User;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс команды info
 */
public class InfoCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса InfoCommand
     */
    public InfoCommand() {
        super(CommandName.info.name(), "вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = CollectionManager.getInstance();
    }

    /**
     * Выполняется команда вывода информации о коллекции
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "в этой сессии ещё не было инициалиазции" : String.valueOf(lastInitTime);
        return new ExecutionResponse("Информация о коллекции: " + "\n" + "Тип: " + collectionManager.getOrganizationCollection().getClass() + "\n" +
                "Кол-во элементов: " + collectionManager.getOrganizationCollection().size() + "\n" + "Дата последней инициалиазции: " + lastInitTimeString + "\n");
    }
}
