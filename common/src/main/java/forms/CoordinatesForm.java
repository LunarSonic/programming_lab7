package forms;
import exceptions.FormBreak;
import objects.Coordinates;
import utility.AppConsole;
import utility.AppLogger;
import java.io.Serial;
import java.util.NoSuchElementException;
import exceptions.NotInLimitsException;

/**
 * Класс для формирования координат
 */
public class CoordinatesForm extends BasicFormation<Coordinates> {
    @Serial
    private static final long serialVersionUID = 5L;
    private final AppConsole console;
    private final AppLogger logger;

    public CoordinatesForm() {
        this.console = new AppConsole();
        this.logger = new AppLogger(CoordinatesForm.class);
    }

    @Override
    public Coordinates form() throws FormBreak {
        Double x = askX();
        Long y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Метод, который запрашивает координату X
     * @return значение координаты X
     */
    private Double askX() throws FormBreak {
        double x;
        while (true) {
            try {
                console.println("Введите координату X (тип Double): ");
                String line = console.readInput().trim();
                if (line.equals("exit")) throw new FormBreak();
                if (!line.isEmpty()) {
                    x = Double.parseDouble(line);
                    if (x <= -947) throw new NotInLimitsException();
                    break;
                } else {
                    logger.error("Поле не может быть null");
                    throw new FormBreak();
                }
            } catch (NotInLimitsException e) {
                logger.error("Значение должно быть больше -947");
                throw new FormBreak();
            } catch (NumberFormatException e) {
                logger.error("Значение должно быть типа Double");
                throw new FormBreak();
            }  catch (NoSuchElementException e) {
                logger.error("Данное значение поля не может быть использовано");
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Метод, который запрашивает координату Y
     * @return значение координаты Y
     */
    private Long askY() throws FormBreak {
        long y;
        while (true) {
            try {
                console.println("Введите координату Y (тип Long): ");
                String line = console.readInput().trim();
                if (line.equals("exit")) throw new FormBreak();
                if (!line.isEmpty()) {
                    y = Long.parseLong(line);
                    break;
                } else {
                    logger.error("Поле не может быть null");
                    throw new FormBreak();
                }
            } catch (NumberFormatException e) {
                logger.error("Значение должно быть типа Long");
                throw new FormBreak();
            } catch (NoSuchElementException e) {
                logger.error("Данное значение поля не может быть использовано");
            } catch (IllegalStateException e) {
                logger.error("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return y;
    }
}

