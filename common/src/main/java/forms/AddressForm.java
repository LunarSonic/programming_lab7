package forms;
import exceptions.FormBreak;
import objects.Address;
import utility.AppLogger;
import utility.AppConsole;
import java.io.Serial;

/**
 * Класс для формирования адреса
 */
public class AddressForm extends BasicFormation<Address> {
    @Serial
    private static final long serialVersionUID = 3L;
    private final AppConsole console;
    private final AppLogger logger;

    public AddressForm() {
        this.console = AppConsole.getConsoleInstance();
        this.logger = new AppLogger(AddressForm.class);
    }

    @Override
    public Address form() throws FormBreak {
        String street = askStreet();
        return new Address(street);
    }

    /**
     * Метод, который запрашивает улицу у пользователя
     */
    private String askStreet() throws FormBreak{
        String street;
        while (true) {
            try {
                console.println("Введите название улицы: ");
                String line = console.readInput().trim();
                if (!line.isEmpty()){
                    street = line;
                    break;
                } else {
                    logger.error("Поле не может быть null");
                }
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return street;
    }
}

