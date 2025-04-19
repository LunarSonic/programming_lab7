package utility;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс для хэширования паролей
 */
public class PasswordCreator {
    AppLogger logger = new AppLogger(PasswordCreator.class);
    private static PasswordCreator instance = null;

    public static PasswordCreator getInstance() {
        if (instance == null) {
            instance = new PasswordCreator();
        }
        return instance;
    }

    /**
     * Метод, используемый для хэширования пароля алгоритмом MD2
     * @param password пароль
     * @return хэшированный пароль
     */
    public String encryptPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD2");
            byte[] digest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger num = new BigInteger(1, digest);
            StringBuilder encryptedPassword = new StringBuilder(num.toString(16));
            while(encryptedPassword.length() < 32) {
                encryptedPassword.insert(0, "0");
            }
            return encryptedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Такого алгоритма хэширования нет");
        }
        return password;
    }

    /**
     * Метод для генерации соли, которая используется для пароля
     * @return соль
     */
    public String generateSalt() {
        String symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]!?()@#$%^&*_+:;{}";
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            salt.append(symbols.charAt((int) (Math.random() * symbols.length())));
        }
        return salt.toString();
    }
}
