package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.entity.Student;
import serverapp.managedb.DataBaseController;

import java.util.List;

public class TakeStudentsPageCommand extends BaseCommand {
    private int pageSize;
    private int pageNum;

    public TakeStudentsPageCommand(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    @Override
    public Message execute(DataBaseController controller) {
        List<Student> resultList = controller.getPage(pageSize, pageNum);
        Message message = new Message();
        message.setResultList(resultList);
        message.setRecordsNum(controller.getRecordsNum());
        return message;
    }
}
