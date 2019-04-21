package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.managedb.DataBaseController;

public class ActivateTableCommand extends BaseCommand {
    private  String tableName;

    public ActivateTableCommand(String tableName){
        this.tableName = tableName;
    }
    @Override
    public Message execute(DataBaseController controller) {
        controller.changeTable(tableName);
        Message message = new Message();
        return message;
    }
}
