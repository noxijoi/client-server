package serverapp.communication;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import serverapp.communication.cervercommands.Message;
import serverapp.communication.clientcommands.BaseCommand;
import serverapp.communication.clientcommands.Command;
import serverapp.managedb.DataBaseController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketProcess implements Runnable {
    private final static Logger LOGGER = LogManager.getLogger(ClientSocketProcess.class);

    private Socket socket;
    private DataBaseController controller;

    public ClientSocketProcess(Socket socket, DataBaseController controller) {
        this.socket = socket;
        this.controller = controller;
        controller.getCurrentTableName();
        run();
    }

    @Override
    public void run() {
        try {
                final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (socket.isConnected()) {
                Command command = (BaseCommand) inputStream.readObject();
                Message message = command.execute(controller);
                outputStream.writeObject(message);
                //как то надо инфу че произошло выдавать
            }
        } catch (IOException e) {
            LOGGER.error("connection error in clientSocket process because " + e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.error("Can't close socket " + e.getMessage());
        }
    }
}
