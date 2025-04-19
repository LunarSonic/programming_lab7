package app.database;
import utility.AppLogger;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Класс, который управляет подключением к базе данных и инициализацией таблиц и последовательностей
 */
public class DatabaseManager {
    private final AppLogger logger = new AppLogger(DatabaseManager.class);

    /**
     * Метод, который устанавливает соединение с базой данных
     * @return объект подключения
     */
    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            String propertiesPath = "/home/studs/s464900/programming_lab7/server/src/main/resources/database.properties";
            Properties properties = new Properties();
            properties.load(new FileInputStream(propertiesPath));
            String url = properties.getProperty("link");
            String username = properties.getProperty("login");
            String password = properties.getProperty("password");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            logger.error("Драйвер JDBC не найден");
            System.exit(-1);
        } catch (SQLException e) {
            logger.error("Ошибка при подключении к базе данных");
            System.exit(-1);
        } catch (IOException e) {
            logger.error("Не получилось загрузить данные из файла конфигурации");
            System.exit(-1);
        }
        return null;
    }

    /**
     * Метод, который создаёт таблицы и последовательности в базе данных, если они ещё не существуют
     */
    public void createBase() {
        String creationOfTablesAndSequencesQuery = """
                CREATE SEQUENCE IF NOT EXISTS usersSequence INCREMENT BY 1 START WITH 1 MINVALUE 1 NO MAXVALUE CACHE 1;
                CREATE SEQUENCE IF NOT EXISTS organizationsSequence INCREMENT BY 1 START WITH 1 MINVALUE 1 NO MAXVALUE CACHE 1;
                
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY DEFAULT nextval('usersSequence'),
                    login VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    salt VARCHAR(255) NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS organizations (
                    id BIGINT PRIMARY KEY DEFAULT nextval('organizationsSequence'),
                    org_name TEXT NOT NULL,
                    coordinate_x DOUBLE PRECISION NOT NULL CHECK(coordinate_x > -947),
                    coordinate_y BIGINT NOT NULL,
                    creation_date DATE NOT NULL,
                    annual_turnover BIGINT NOT NULL CHECK(annual_turnover > 0),
                    type VARCHAR(20) CHECK(type in ('PUBLIC', 'TRUST', 'COMMERCIAL', 'OPEN_JOINT_STOCK_COMPANY') OR type IS NULL),
                    postal_address TEXT NOT NULL,
                    owner_id BIGINT NOT NULL REFERENCES users(id)
                );
                """;
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(creationOfTablesAndSequencesQuery);
        } catch (SQLException e) {
            logger.error("Ошибка при подключении к базе данных");
        }
    }

    /**
     * Геттер для получения объекта подключения
     * @return объект подключения
     */
    public Connection getConnection() {
        return connect();
    }
}
