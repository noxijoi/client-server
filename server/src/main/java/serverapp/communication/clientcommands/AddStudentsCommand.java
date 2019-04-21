package serverapp.communication.clientcommands;

import serverapp.communication.cervercommands.Message;
import serverapp.entity.Student;
import serverapp.managedb.DataBaseController;

import java.util.List;

public class AddStudentsCommand extends BaseCommand {

    private List<Student> students;

    public AddStudentsCommand(List<Student> students){
        this.students = students;
        commandDescription = "add " + students.size() + " students to database";
    }

    @Override
    public Message execute(DataBaseController controller) {
        controller.addAllStudents(students);
        Message message = new Message();
        message.setRecordsNum(controller.getRecordsNum());
        return message;
    }

}
