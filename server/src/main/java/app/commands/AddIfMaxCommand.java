package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import objects.Organization;
import utility.User;
import java.io.Serializable;

/**
 * Класс команды add_if_max
 */
public class AddIfMaxCommand extends ServerCommand{
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса AddIfMaxCommand
     */
    public AddIfMaxCommand() {
        super(CommandName.add_if_max.name(), "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда добавления нового элемента Organization в коллекцию, если его значение annualTurnover
     * больше max значения, которое есть в коллекции
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (databaseUserManager.checkUser(user.getLogin(), user.getPassword())) {
            Organization organization = (Organization) objectArg;
            Long id = databaseUserManager.addIfMaxOrganization(organization, user.getLogin());
            if (id == -1L) {
                return new ExecutionResponse(false, "Не получилось выполнить команду");
            } else if (id == -2L) {
                return new ExecutionResponse(false, "Элемент не оказался максимальным");
            }
            organization.setId(id);
            collectionManager.addOrganization(organization);
            return new ExecutionResponse("Максимальный элемент добавлен в коллекцию :)");
        }
        return null;
    }
}
