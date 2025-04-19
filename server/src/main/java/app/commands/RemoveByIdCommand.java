package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import utility.User;
import java.io.Serializable;

/**
 * Класс команды remove_by_id
 */
public class RemoveByIdCommand extends ServerCommand {
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса RemoveByIdCommand
     */
    public RemoveByIdCommand() {
        super(CommandName.remove_by_id.name(), "удалить элемент из коллекции по его id");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда удаления элемента Organization из коллекции по id
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (databaseUserManager.checkUser(user.getLogin(), user.getPassword())) {
            Long id = Long.parseLong(args[1]);
            if (databaseUserManager.checkOrganizationExistence(id)) {
                if (databaseUserManager.removeById(id, user.getLogin())) {
                    collectionManager.removeByIdFromCollection(id);
                    return new ExecutionResponse("Элемент коллекции c id " + id+ " удалён");
                } else {
                    return new ExecutionResponse(false, "Вы не можете удалить элемент с id " + id + ", т.к он не был создан вами");
                }
            } else {
                return new ExecutionResponse(false, "Элемент с id " + id + " отсутствует");
            }
        } else {
            return new ExecutionResponse(false, "Несоответствие логина и пароля");
        }
    }
}
