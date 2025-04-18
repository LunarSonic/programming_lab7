package app;
import app.network.NetworkHandler;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        NetworkHandler networkHandler = new NetworkHandler(new InetSocketAddress(8898));
        if (networkHandler.initialize()) {
            networkHandler.start();
        }
    }
}
