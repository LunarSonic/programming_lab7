package app.commands;
import commands.CommandName;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import utility.User;
import java.io.Serializable;
import java.util.List;

/**
 * Класс команды remove_all_by_annual_turnover
 */
public class RemoveAllByAnnualTurnoverCommand extends ServerCommand {
    private final CollectionManager collectionManager;
    private final DatabaseUserManager databaseUserManager;

    /**
     * Конструктор класса RemoveAllByAnnualTurnover
     */
    public RemoveAllByAnnualTurnoverCommand() {
        super(CommandName.remove_all_by_annual_turnover.name(), "удалить из коллекции все элементы, значение поля annualTurnover которого эквивалентно заданному");
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
    }

    /**
     * Выполняется команда удаления всех элементов из коллекции, у которых annualTurnover равен заданному
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        if (databaseUserManager.checkUser(user.getLogin(), user.getPassword())) {
            List<Long> ids = databaseUserManager.getIdsOfUsersElements(user.getLogin());
            long annualTurnover = Long.parseLong(args[1]);
            if (databaseUserManager.removeAllByAnnualTurnover(annualTurnover, user.getLogin())) {
                collectionManager.removeAllByAnnualTurnover(annualTurnover, ids);
                return new ExecutionResponse("Элемент/элементы коллекции c annualTurnover " + annualTurnover + " удалён/удалены");
            } else {
                return new ExecutionResponse(false, "Нет элемента/элементов с annualTurnover" + annualTurnover);
            }
        } else {
            return new ExecutionResponse(false, "Несоответствие логина и пароля");
        }
    }
}

