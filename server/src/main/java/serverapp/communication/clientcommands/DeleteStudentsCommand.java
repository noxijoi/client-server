package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.managedb.DataBaseController;
import serverapp.managedb.TypeOfSelection;

public class DeleteStudentsCommand extends BaseCommand{
    private Object firstParam;
    private Object secondParam;
    private TypeOfSelection type;

    @Override
    public String getCommandDescription() {
        return super.getCommandDescription();
    }

    @Override
    public Message execute(DataBaseController controller) {
        Message message = new Message();
        message.setResultList(controller.delStudentsByParam(firstParam,secondParam,type));
        message.setRecordsNum(controller.getRecordsNum());
        return message;
    }
}
