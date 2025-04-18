package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import objects.Organization;
import utility.User;
import java.io.Serializable;

/**
 * Класс команды add
 */
public class AddCommand extends ServerCommand {
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса AddCommand
     */
    public AddCommand() {
        super(CommandName.add.name(), "добавить новый элемент в коллекцию");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда добавления нового элемента Organization в коллекцию
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1) {
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        }
        if (databaseUserManager.checkUser(user.getLogin(), user.getPassword())) {
            Organization organization = (Organization) objectArg;
            Long id = databaseUserManager.addOrganization(organization, user.getLogin());
            if (id == -1L) {
                return new ExecutionResponse(false, "Ошибка при добавлении организации из-за неккоторектных данных");
            }
            organization.setId(id);
            collectionManager.addOrganization(organization);
            return new ExecutionResponse("Организация с id " + id + " успешно добавлена");
        } else {
            return new ExecutionResponse(false, "Несоответствие логина и пароля");
        }
    }
}
