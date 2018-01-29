import ServerProvider.ServerProvider;

import java.io.IOException;

public class App {

    public static void main(String[] args) {

        ServerProvider sp = new ServerProvider();

        try {
            sp.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
