package managers;
import utility.AppLogger;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за работу с файлами
 */
public class FileManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    private final Set<String> allFilePaths;
    private final AppLogger logger;

    /**
     * Конструктор класса FileManager
     */
    public FileManager() {
        this.logger = new AppLogger(FileManager.class);
        this.allFilePaths = loadFilesFromEnvironmentVariables();
        if (allFilePaths == null) {
            logger.error("Не удалось загрузить пути к файлам, проверьте LAB7_PATH!");
        }
    }

    /**
     * Метод, который возвращает список файлов, полученных из переменной окружения LAB7_PATH
     * @return filePaths список из всех путей
     */
    public Set<String> loadFilesFromEnvironmentVariables() {
        String lab7Path = System.getenv("LAB7_PATH");
        if (lab7Path == null) {
            logger.error("Переменная окружения LAB7_PATH не установлена :(");
            return null;
        }
        Set<String> filePaths = Arrays.stream(lab7Path.split(File.pathSeparator))
                .map(String::trim)
                .filter(path -> new File(path).isFile())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return filePaths;
    }

    /**
     * Метод, который ищет скрипт по названию файла
     * @param fileName название файла со скриптом
     * @return путь к файлу со скриптом
     */
    public String findScriptFilePath(String fileName) {
        return allFilePaths.stream()
                .filter(path -> path.endsWith(fileName))
                .findFirst()
                .orElse(null);
    }
}