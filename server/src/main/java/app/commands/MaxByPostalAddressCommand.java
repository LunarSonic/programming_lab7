package app.commands;
import commands.CommandName;
import app.managers.CollectionManager;
import network.ExecutionResponse;
import objects.Organization;
import utility.User;

import java.io.Serializable;

/**
 * Класс команды max_by_postal_address
 */
public class MaxByPostalAddressCommand extends ServerCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса MaxByPostalAddressCommand
     */
    public MaxByPostalAddressCommand() {
        super(CommandName.max_by_postal_address.name(), "вывести любой объект из коллекции, значение поля postalAddress которого является максимальным");
        this.collectionManager = CollectionManager.getInstance();
    }

    /**
     * Выполняется команда вывода объекта из коллекции, у которого значение postalAddress является max
     * @return успешность выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args, Serializable objectArg, User user) {
        if (args.length != 1)
            return new ExecutionResponse(false, "Неправильное кол-во аргументов!\n");
        Organization my_max = null;
        for (Organization organization : collectionManager.getOrganizationCollection()) {
            if (organization.getPostalAddress().getStreet() != null) {
                if (my_max == null || organization.getPostalAddress().getStreet().compareTo(my_max.getPostalAddress().getStreet()) > 0) {
                    my_max = organization;
                }
            }
        }
        if (my_max != null) {
            return new ExecutionResponse(my_max.toString());
        } else {
            return new ExecutionResponse(false, "Организации не были найдены");
        }
    }
}

