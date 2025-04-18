package app.network;
import java.io.*;
import network.ExecutionResponse;
import network.Request;
import utility.AppLogger;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс, отвечающий за сетевое взаимодействие с сервером
 */
public class NetworkHandler {
    private final String host;
    private final int port;
    private SocketChannel channel;
    private int attempts = 0;
    private final AppLogger logger = new AppLogger(NetworkHandler.class);

    /**
     * Конструктор класса NetworkClient
     * @param host хост сервера
     * @param port порт сервера
     */
    public NetworkHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Метод для подключения к серверу
     */
    public void connect() {
        try {
            SocketAddress address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);
            logger.info("Соединение с сервером установлено!");
        } catch (ConnectException e) {
            logger.info("Сервер недоступен, происходит переподключение");
            if (attempts > 5) {
                logger.error("Соединение с сервером не установлено после 5 попыток. Попробуйте подключиться позже");
            }
            attempts++;
        } catch (IOException e) {
            logger.error("Ошибка подключения к серверу");
            System.exit(1);
        }
    }

    /**
     * Метод для отправки запроса на сервер и получения ответа от него
     * @param request запрос с командой и аргументами
     * @return response ответ от сервера
     */
    public ExecutionResponse sendAndReceive(Request request) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(bytes)) {
            outputStream.writeObject(request);
            ByteBuffer data = ByteBuffer.wrap(bytes.toByteArray());
            channel.write(data);
            ByteBuffer receivedDataLength = ByteBuffer.allocate(4);
            channel.read(receivedDataLength );
            receivedDataLength.flip();
            int responseLength = receivedDataLength.getInt();
            ByteBuffer dataToReceive = ByteBuffer.allocate(responseLength);
            channel.read(dataToReceive);
            dataToReceive.flip(); //переводим буфер в режим чтения
            try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(dataToReceive.array()))) {
                return (ExecutionResponse) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                return new ExecutionResponse(false, "Ошибка десериализации ответа от сервера");
            }
        } catch (IOException e) {
            return new ExecutionResponse(false, "Ошибка в связи с сервером");
        }
    }
}
