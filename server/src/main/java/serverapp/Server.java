package serverapp;

import org.apache.log4j.Logger;
import serverapp.db.DataBaseConnectionPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class Server implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Server.class);


    private DataBaseConnectionPool connectionPool;
    private static ServerSocket serverSocket;
    private final int PORT = 1111;


    public Server() {
        connectionPool = DataBaseConnectionPool.getInstance();
    }


    public void stop() {
    }

    public void start() {
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);

            LOGGER.info("Server is created");
            while (true) {
                Socket client = null;
                while (client == null) {
                    client = serverSocket.accept();
                }
                Connection connection = connectionPool.getConnection();
                new ClientSocketProcess(client, connection);
                serverSocket.accept();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        finally {
            //ну и как и тогда тебя закрыть

            //serverSocket.close();
        }
    }

}
