package app.network;
import app.database.DatabaseManager;
import app.database.DatabaseUserManager;
import app.managers.CollectionManager;
import app.managers.CommandManager;
import network.ExecutionResponse;
import network.Request;
import network.Serializer;
import utility.AppLogger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс, который принимает подключения от клиентов
 */
public class NetworkHandler {
    private final InetSocketAddress address;
    private final CommandManager commandManager;
    private final DatabaseUserManager databaseUserManager;
    private Selector selector;
    private final RequestParser requestParser;
    private final AppLogger logger = new AppLogger(NetworkHandler.class);
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    private final ExecutorService readRequestPool = Executors.newCachedThreadPool();
    private final ExecutorService writeRequestPool = Executors.newCachedThreadPool();

    /**
     * Конструктор класса NetworkHandler
     * @param address порт, на котором будет запущен сервер
     */
    public NetworkHandler(InetSocketAddress address) {
        this.address = address;
        this.collectionManager = CollectionManager.getInstance();
        this.databaseUserManager = DatabaseUserManager.getInstance();
        this.databaseManager = new DatabaseManager();
        this.commandManager = new CommandManager();
        this.requestParser = new RequestParser();
    }

    /**
     * Метод, который инициализирует сервер, создаёт канал, регистрируя его в селекторе
     * @return true, если сервер инициализирован, иначе false
     */
    public boolean initialize() {
        try {
            databaseManager.createBase();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Сервер инициализирован, ждём подключения клиента ...");
            collectionManager.loadCollection();
            logger.info("Коллекция успешно загружена из базы данных");
            return true;
        } catch (IOException e) {
            logger.error("Ошибка при инициализации сервера: " + e.getMessage());
            return false;
        }
    }

    /**
     * Метод, который запускает сервер и ожидает подключения клиента
     */
    public void start() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input == null || input.isEmpty()) {
                    continue;
                }
                input = input.trim();
                if (input.equals("exit")) {
                    logger.info("Завершение работы сервера");
                    System.exit(0);
                }
            }
        }).start();
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        acceptClient(key);
                    } else if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.configureBlocking(false);
                        Request receivedRequest = readRequestPool.submit(() -> {
                            try {
                                return readRequest(key, socketChannel);
                            } catch (IOException e) {
                                logger.error("Ошибка в обработке подключений");
                                return null;
                            }
                        }).get();
                        if (receivedRequest == null) { //если клиент отключился, пропускаем обработку и отправку ответа
                            continue;
                        }
                        Request request = receivedRequest;
                        Thread processRequest = new Thread(() -> {
                            ExecutionResponse response = executeRequest(request);
                            writeRequestPool.submit(() -> {
                                try {
                                    sendResponse(socketChannel, response);
                                } catch (IOException e) {
                                    logger.error("Ошибка в обработке подключений");
                                }
                            });
                        });
                        processRequest.start();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка в обработке подключений");
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Метод, который принимает подключение клиента
     * @param key объект, полученный из SelectionKey и содержащий данные для регистрации канала
     */
    private void acceptClient(SelectionKey key) throws IOException {
        var serverSocketChannel = (ServerSocketChannel) key.channel();
        var client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        logger.info("Клиент подключился");
    }

    /**
     * Метод для чтения запроса от клиента
     * Он считывает байты из канала и десериализует в объект запроса
     * @param key объект, полученный из SelectionKey и содержащий данные для регистрации канала
     * @param client канал, в который отправляется ответ
     * @return объект класса Request
     */
    private Request readRequest(SelectionKey key, SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            logger.info("Клиент отключился");
            client.close();
            key.cancel();
            return null;
        }
        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        if (data.length > 0) {
            Request request = Serializer.getInstance().deserialize(data, Request.class);
            return request;
        } else {
            return null;
        }
    }

    /**
     * Метод для обработки запроса от клиента
     */
    private ExecutionResponse executeRequest(Request request) {
        ExecutionResponse response = null;
        switch (request.getRequestType()) {
            case COMMAND -> {
                var command = commandManager.getCommands().get(request.getCommandName().toString());
                if (command == null) {
                    response = new ExecutionResponse(false, "Команда " + request.getCommandName() + " не найдена");
                } else {
                    String[] args = requestParser.parseCommand(request);
                    response = command.execute(args, request.getCommandObjectArg(), request.getUser());
                }
            }
            case LOGIN -> response = databaseUserManager.logInUser(request.getUser());
            case REGISTER -> response = databaseUserManager.registerUser(request.getUser());
        }
        if (request.getCommandName() != null) {
            commandManager.addCommandToHistory(request.getCommandName().getName());
        }
        logger.info("Запрос от клиента обработан");
        return response;
    }

    /**
     * Метод, который отправляет ответ клиенту
     * @param client канал, через который отправляется ответ
     */
    private void sendResponse(SocketChannel client, ExecutionResponse response) throws IOException {
        byte[] responseData = Serializer.getInstance().serialize(response);
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(responseData.length);
        lengthBuffer.flip();
        client.write(lengthBuffer);
        ByteBuffer buffer = ByteBuffer.wrap(responseData);
        client.write(buffer);
        logger.info("Ответ отправлен клиенту :)");
    }
}