package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import objects.Organization;
import utility.User;
import java.io.Serializable;

/**
 * Класс команды update
 */
public class UpdateCommand extends ServerCommand {
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса UpdateCommand
     */
    public UpdateCommand() {
        super(CommandName.update.name(), "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда обновления значения элемента коллекции, у которого id равен заданному
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (databaseUserManager.checkUser(user.getLogin(), user.getPassword())) {
            Long id = Long.parseLong(args[1]);
            Organization organization = (Organization) objectArg;
            if (databaseUserManager.checkOrganizationExistence(id)) {
                if (databaseUserManager.updateOrganizationById(id, organization, user.getLogin())) {
                    organization.setId(id);
                    collectionManager.removeByIdFromCollection(id);
                    collectionManager.addOrganization(organization);
                    return new ExecutionResponse("Элемент коллекции c id " + id + " обновлён");
                } else {
                    return new ExecutionResponse(false, "Вы не можете обновить элемент с id " + id + ", т.к он не был создан вами");
                }
            } else {
                return new ExecutionResponse(false, "Элемент с id " + id + " отстутствует");
            }
        } else {
            return new ExecutionResponse(false, "Несоответствие логина и пароля");
        }
    }
}
