package serverapp.clientcommunication.clientcommands;

import serverapp.entity.Student;
import serverapp.managedb.DataBaseController;

import java.util.List;

public class addStudentsCommand extends BaseCommand {

    private List<Student> students;

    public addStudentsCommand(List<Student> students){
        this.students = students;
        commandDescription = "add " + students.size() + " students to database";
    }

    @Override
    public void execute(DataBaseController controller) {
        controller.addAllStudents(students);
    }

}
