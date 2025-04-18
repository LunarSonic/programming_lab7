package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import utility.User;
import java.io.Serializable;

/**
 * Класс команды clear
 */
public class ClearCommand extends ServerCommand {
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса ClearCommand
     */
    public ClearCommand() {
        super(CommandName.clear.name(), "очистить коллекцию");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда очистки коллекции
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        databaseUserManager.clear(user.getLogin());
        if (collectionManager.getOrganizationCollection().isEmpty()) {
            return new ExecutionResponse("Коллекция пустая!");
        } else {
            collectionManager.clearCollection();
            collectionManager.setOrganizationCollection(databaseUserManager.loadCollection());
            return new ExecutionResponse("Элементы из коллекции, принадлежащие пользователю, удалены");
        }
    }
}
