package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.managedb.DataBaseController;
import serverapp.managedb.TypeOfSelection;

public class SearchStudentsCommand extends BaseCommand{
    private Object firstParam;
    private Object secondParam;
    private TypeOfSelection type;

    public SearchStudentsCommand(Object firstParam, Object secondParam, TypeOfSelection type){
        this.firstParam = firstParam;
        this.secondParam = secondParam;
        this.type = type;
    }

    @Override
    public Message execute(DataBaseController controller) {
        Message message = new Message();
        message.setResultList(controller.findStudentByParam(firstParam, secondParam, type));
        return message;
    }
}
