package app.cli;
import commands.CommandName;
import app.commands.ExecuteScriptCommand;
import app.commands.ExitCommand;
import app.commands.HelpCommand;
import network.ExecutionResponse;
import utility.AppConsole;
import utility.AppLogger;
import app.utility.CommandHandler;
import utility.User;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Класс для исполнения команд
 */
public class Runner {
    private User user;
    private final Map<CommandName, String[]> commands;
    private final AppConsole console = new AppConsole();
    private final AppLogger logger = new AppLogger(Runner.class);
    private final ScriptManager scriptManager;
    private final CommandHandler commandHandler;

    /**
     * Конструктор класса Runner
     */
    public Runner(CommandHandler commandHandler) {
        this.scriptManager = new ScriptManager();
        this.commandHandler = commandHandler;
        this.commands = initCommands();
    }

    /**
     * Метод для инициализации команд, используем его для команды help
     * @return Map с командами
     */
    private Map<CommandName, String[]> initCommands() {
        return Map.ofEntries(
                Map.entry(CommandName.help, new String[]{CommandName.help.getName(), "вывести справку по доступным командам"}),
                Map.entry(CommandName.exit, new String[]{CommandName.exit.getName(), "завершить программу (без сохранения в файл)"}),
                Map.entry(CommandName.execute_script, new String[]{CommandName.execute_script.getName(), "считать и исполнить скрипт из указанного файла"}),
                Map.entry(CommandName.add, new String[]{CommandName.add.getName(), "добавить новый элемент в коллекцию"}),
                Map.entry(CommandName.add_if_max, new String[]{CommandName.add_if_max.getName(), "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции"}),
                Map.entry(CommandName.add_if_min, new String[]{CommandName.add_if_min.getName(), "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции"}),
                Map.entry(CommandName.clear, new String[]{CommandName.clear.getName(), "очистить коллекцию"}),
                Map.entry(CommandName.history, new String[]{CommandName.history.getName(), "вывести последние 15 команд (без их аргументов"}),
                Map.entry(CommandName.info, new String[]{CommandName.info.getName(), "вывести в стандартный поток вывода информацию о коллекции"}),
                Map.entry(CommandName.max_by_postal_address, new String[]{CommandName.max_by_postal_address.getName(), "вывести любой объект из коллекции, значение поля postalAddress которого является максимальным"}),
                Map.entry(CommandName.remove_all_by_annual_turnover, new String[]{CommandName.remove_all_by_annual_turnover.getName(), "удалить из коллекции все элементы, значение поля annualTurnover которого эквивалентно заданному"}),
                Map.entry(CommandName.remove_by_id, new String[]{CommandName.remove_by_id.getName(), "удалить элемент из коллекции по его id"}),
                Map.entry(CommandName.show, new String[]{CommandName.show.getName(), "вывести в стандартный поток вывода все элементы коллекции в строковом представлении"}),
                Map.entry(CommandName.sum_of_annual_turnover, new String[]{CommandName.sum_of_annual_turnover.getName(), "вывести сумму значений поля annualTurnover для всех элементов коллекции"}),
                Map.entry(CommandName.update, new String[]{CommandName.update.getName(), "обновить значение элемента коллекции, id которого равен заданному"})
        );
    }

    /**
     * Метод, который отвечает за запуск команд
     * @param userCommand массив строк, представляющих из себя команду и её аргументы
     * @return результат выполнения команды
     */
    public ExecutionResponse launchCommand(String[] userCommand) {
        if (userCommand[0].isEmpty()) {
            return new ExecutionResponse(false, "Команда не может быть пустой!");
        }
        CommandName command;
        try {
            command = CommandName.valueOf(userCommand[0]);
        } catch (IllegalArgumentException e) {
            return new ExecutionResponse(false, "Неизвестная команда: " + userCommand[0]);
        }
        try {
            switch (userCommand[0]) {
                case "execute_script":
                    ExecutionResponse response1 = new ExecuteScriptCommand().execute(userCommand, user);
                    if (!response1.getResponse()) {
                        return response1;
                    }
                    ExecutionResponse response2 = scriptManager.executeScript(userCommand[1], this);
                    if (response2 == null) {
                        return new ExecutionResponse(false, "Ошибка при выполнении скрипта: пустой ответ");
                    }
                    return new ExecutionResponse(response2.getResponse() + response1.getMessage() + "\n" + response2.getMessage().trim());
                case "exit":
                    return new ExitCommand().execute(userCommand, user);
                case "help":
                    return new HelpCommand(commands).execute(userCommand, user);
                default:
                    ExecutionResponse handlerResponse = commandHandler.handleCommand(command, userCommand);
                    if (handlerResponse == null) {
                        return new ExecutionResponse(false, "Команда не выполнена!");
                    }
                    return handlerResponse;
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Произошла ошибка во время выполнения команды");
        }
    }

    /**
     * Метод для интерактивного режима работы программы,
     * в котором программа ожидает ввода команд от пользователя и выполняет их
     */
    public void interactiveMode() {
        try {
            ExecutionResponse statusOfCommand;
            while (true) {
                String input = console.readInput().trim();
                String[] userCommand = input.split(" ", 2);
                statusOfCommand = launchCommand(userCommand);
                if (userCommand[0].equals("exit"))
                    break;
                console.println(statusOfCommand.getMessage());
            }
        } catch (NoSuchElementException e) {
            logger.error("Пользовательский ввод не найден");
        } catch (IllegalStateException e) {
            logger.error("Непредвиденная ошибка");
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}


