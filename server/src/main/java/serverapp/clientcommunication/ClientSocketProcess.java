package serverapp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.net.Socket;

public class ClientSocketProcess implements Runnable {

    private static  final Logger LOGGER = LogManager.getLogger(ClientSocketProcess.class);
    private Socket socket;
    private String currentTable;
    private DataBaseController controller;
    
    public ClientSocketProcess(Socket socket, DataBaseController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {

    }
}
