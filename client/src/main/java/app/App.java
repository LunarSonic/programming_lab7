package app;
import app.cli.Runner;
import app.network.NetworkHandler;
import app.utility.CommandHandler;
import app.utility.UserModule;
import utility.AppLogger;
import utility.User;

/**
 * Класс для запуска клиентского приложения
 */
public class App {
    public static void main(String[] args) {
        AppLogger logger = new AppLogger(App.class);
        UserModule userModule = new UserModule();
        var networkHandler = new NetworkHandler("helios.cs.ifmo.ru", 8898);
        //var networkHandler = new NetworkHandler("localhost", 8898);
        networkHandler.connect();
        userModule.setNetworkHandler(networkHandler);
        User user = userModule.userAction();
        if (user == null) {
            logger.info("Вход не выполнен после 3 попыток");
            System.exit(1);
        }
        var commandHandler = new CommandHandler(networkHandler, user);
        Runner runner = new Runner(commandHandler);
        runner.setUser(user);
        runner.interactiveMode();
    }
}

