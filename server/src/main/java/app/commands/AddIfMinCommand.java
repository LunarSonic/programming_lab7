package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import objects.Organization;
import utility.User;

import java.io.Serializable;

public class AddIfMinCommand extends ServerCommand {
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса AddIfMinCommand
     */
    public AddIfMinCommand() {
        super(CommandName.add_if_min.name(), "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда добавления нового элемента Organization в коллекцию, если его значение annualTurnover
     * меньше min значения, которое есть в коллекции
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (databaseUserManager.checkUser(user.getLogin(), user.getPassword())) {
            Organization organization = (Organization) objectArg;
            Long id = databaseUserManager.addIfMinOrganization(organization, user.getLogin());
            if (id == -1L) {
                return new ExecutionResponse("Не получилось выполнить команду");
            } else if (id == -2L) {
                return new ExecutionResponse("Элемент не оказался минимальным");
            }
            organization.setId(id);
            collectionManager.addOrganization(organization);
            return new ExecutionResponse("Минимальный элемент добавлен в коллекцию :)");
        }
        return null;
    }
}
