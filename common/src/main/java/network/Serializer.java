package network;
import utility.AppLogger;
import java.io.*;

/**
 * Класс, предназначенный для сериализации и десериализации объекта
 * Используется для преобразования объектов в байтовый массив и обратно
 */
public class Serializer {
    private final AppLogger logger = new AppLogger(Serializer.class);
    private static Serializer instance;

    public static Serializer getInstance() {
        if (instance == null) {
            instance = new Serializer();
        }
        return instance;
    }

    /**
     * Метод для сериализации объекта в байтовый массив
     * @param object сериализуемый объект
     * @return байты сериализованного объекта
     */
    public byte[] serialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Ошибка при сериализации данных");
            return null;
        }
    }

    /**
     * Метод для десериализации объекта из байтового массива
     * @param data массив из байтов, который содержит сериализованные данные
     * @param clazz объект класса, который нужно десериализовать
     * @return десериализованный объект
     * @param <T> параметр типа объекта
     */
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object obj = objectInputStream.readObject();
            if (!clazz.isInstance(obj)) {
                logger.error("Ошибка: получен объект типа " + obj.getClass() + ", ожидался " + clazz);
                return null;
            }
            return clazz.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка при десериализации");
            return null;
        }
    }
}
