package serverapp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import serverapp.communication.ClientSocketProcess;
import serverapp.db.DataBaseConnectionPool;
import serverapp.managedb.DataBaseController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Server extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);


    private DataBaseConnectionPool connectionPool;
    private static ServerSocket serverSocket;
    private List<ClientSocketProcess> allProcess;
    private boolean workingFlag  = false;
    private final String serverProp = "server";

    public Server() {
        connectionPool = DataBaseConnectionPool.getInstance();
        allProcess = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(serverProp);
            int port = Integer.parseInt(bundle.getString("server.port"));
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server started");
            Thread socketListenerThread = new Thread(new SocketListener(this));
            socketListenerThread.start();
            while (true) {
                if (workingFlag) {
                    TimeUnit.MILLISECONDS.sleep(20);
                } else {
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error("Server can't sleep" +e.getMessage());
        } finally {
           shutDown();
        }
    }

    private void shutDown() {
        try {
            allProcess.forEach(x -> x.close());
            connectionPool.shutDown();
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("Cant shut down server "+ e.getMessage());
        }
    }

    public void setWorkingFlag(boolean b) {
        workingFlag = b;
        if (b){
            LOGGER.info("Server resume work");
        } else{
            LOGGER.info("Server stop work");
            //TODO как то нужно всем потокам не давать работат
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public boolean getWorkingFlag() {
        return workingFlag;
    }

    public void addClient(Socket client) {
        DataBaseController dataBaseController  = new DataBaseController(connectionPool.getConnection());
        allProcess.add(new ClientSocketProcess(client, dataBaseController));
        LOGGER.info("Connected new client " + client.getInetAddress());

    }
}
