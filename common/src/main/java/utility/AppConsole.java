package utility;
import java.io.PrintStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс для ввода команд и вывода результата
 */
public class AppConsole implements Serializable {
    @Serial
    private static final long serialVersionUID = 14L;
    private PrintStream console;
    private static AppConsole instance = null;

    public static AppConsole getConsoleInstance() {
        if (instance == null) {
            instance = new AppConsole();
        }
        return instance;
    }

    /**
     * Строка приглашения
     */
    private static final String prompt = "$ ";

    /**
     * Сканер для чтения из файла
     */
    private static Scanner fileScanner = null;

    /**
     * Сканер для чтения из стандартного ввода
     */
    private static final Scanner defScanner = new Scanner(System.in);

    /**
     * Конструктор класса Console
     */
    public AppConsole() {
        this.console = System.out;
    }

    /**
     * Выводит obj.toString() в консоль
     * @param obj объект, который будет выведен
     */
    public void print(Object obj) {
        console.print(obj);
    }

    /**
     * Выводит obj.toString() + \n (перенос строки) в консоль
     * @param obj объект, который будет выведен
     */
    public void println(Object obj) {
        console.println(obj);
    }


    /**
     * Чтение строки из fileScanner или defScanner,
     * с проверкой на наличие данных, чтобы избежать исключений.
     */
    public String readInput() throws IllegalStateException, NoSuchElementException {
        if (fileScanner != null) {
            //чтение строки из файла
            if (fileScanner.hasNext()) {
                return fileScanner.nextLine();
            } else {
                return "";
            }
        } else {
            //чтение строки из консоли
            return defScanner.nextLine();
        }
    }

    /**
     * Проверка, есть ли ещё строки для чтения
     * @return возвращает true, если ещё остались строки для чтения, иначе false
     */
    public boolean hasNextInput() {
        return (fileScanner != null) ? fileScanner.hasNextLine() : defScanner.hasNextLine();
    }

    /**
     * Геттер для получения текущего prompt (приглашения)
     * @return prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Переключает ввод на файл, ввод данных будет из переданного сканера
     * @param scanner сканер для чтения из файла
     */
    public void useFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    /**
     * Переключает ввод на стандартный поток вывода (консоль)
     */
    public void useConsoleScanner() {
        fileScanner = null;
    }
}

