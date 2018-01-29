package ServerProvider;

import com.sun.net.httpserver.HttpServer;
import hendlers.MyHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerProvider {

    public void startServer() throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

}
