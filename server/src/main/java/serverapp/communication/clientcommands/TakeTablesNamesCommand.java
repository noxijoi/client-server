package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.managedb.DataBaseController;

import java.util.Arrays;

public class TakeTablesNamesCommand extends BaseCommand {
    @Override
    public Message execute(DataBaseController controller) {
        Message message = new Message();
        message.setResultList(Arrays.asList(controller.getAllTablesNames()));
        return message;
    }
}
