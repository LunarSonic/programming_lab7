package app.cli;
import managers.FileManager;
import utility.AppLogger;
import utility.AppConsole;
import network.ExecutionResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс, который управляет скриптом
 */
public class ScriptManager {
    private final AppConsole console;
    private final AppLogger logger = new AppLogger(ScriptManager.class);
    private int lengthRecursion = -1;
    private final Deque<String> scriptStack = new ArrayDeque<>(); //стек выполняемых скриптов
    private final FileManager fileManager;


    /**
     * Конструктор класса ScriptManager
     */
    public ScriptManager() {
        this.console = new AppConsole();
        this.fileManager = new FileManager();
    }

    /**
     * Проверяет рекурсивность выполнения скрипта
     * @param argument название скрипта, который запускается
     * @param scriptScanner сканер для чтения из скрипта
     * @return true, если может быть рекурсия, иначе false
     */
    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
                if (lengthRecursion < 0) {
                    console.useConsoleScanner();
                    console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..300)");
                    while (lengthRecursion < 0 || lengthRecursion > 300) {
                        try {
                            console.print("> ");
                            lengthRecursion = Integer.parseInt(console.readInput().trim());
                        } catch (NumberFormatException e) {
                            logger.error("Длина не распознана");
                        }
                    }
                    console.useFileScanner(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 300)
                    return false;
            }
        }
        return true;
    }

    /** Метод для выполнения скрипта
     * @param fileName название файла со скриптом
     * @return результат выполнения скрипта
     */
    public ExecutionResponse executeScript(String fileName, Runner runner) {
        Set<String> filePaths = fileManager.loadFilesFromEnvironmentVariables();
        if (filePaths == null || filePaths.isEmpty()) {
            return new ExecutionResponse(false, "Переменная окружения LAB7_PATH не задана!");
        }
        logger.info("Проверяем путь: " + fileName);
        String path = fileManager.findScriptFilePath(fileName);
        Path scriptPath = Paths.get(path);
        File scriptFile = scriptPath.toFile();
        if (!scriptFile.exists()) {
            return new ExecutionResponse(false,"Файла нет: " + scriptPath.toAbsolutePath());
        }
        if (!scriptFile.canRead()) {
            return new ExecutionResponse(false, "Нет прав на чтение файла: " + scriptPath.toAbsolutePath());
        }
        scriptStack.addLast(fileName);
        logger.info("Файл найден, начинается выполнение скрипта ...");
        try (Scanner scannerForScript = new Scanner(scriptFile)) {
            ExecutionResponse statusOfCommand;
            if (!scannerForScript.hasNext()) throw new NoSuchElementException("Файл пустой!");
            console.useFileScanner(scannerForScript);
            String[] userCommand;
            do {
                userCommand = (console.readInput().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                console.println(console.getPrompt() + String.join(" ", userCommand));
                boolean isLaunchNeeded = true;
                if (userCommand[0].equals("execute_script")) {
                    isLaunchNeeded = checkRecursion(userCommand[1], scannerForScript);
                }
                statusOfCommand = isLaunchNeeded ? runner.launchCommand(userCommand) :
                        new ExecutionResponse(false, "Превышена max глубина рекурсии");
                if (userCommand[0].equals("execute_script")) console.useFileScanner(scannerForScript);
                console.println(statusOfCommand.getMessage());
                if (!statusOfCommand.getResponse()) {
                    return new ExecutionResponse(false, "Скрипт завершён из-за ошибки");
                }
            } while (console.hasNextInput() && !userCommand[0].equals("exit") && statusOfCommand.getResponse());
            console.useConsoleScanner();
            return new ExecutionResponse("");
        } catch (FileNotFoundException e) {
            return new ExecutionResponse(false, "Файл не найден: " + scriptPath.toAbsolutePath());
        } catch (NoSuchElementException e) {
            return new ExecutionResponse(false, "Файл пустой: " + scriptPath.toAbsolutePath());
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка выполнения скрипта " + e.getMessage());
        } finally {
            console.useConsoleScanner();
            scriptStack.pollLast();
        }
    }
}
