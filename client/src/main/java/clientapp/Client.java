package clientapp;

import clientapp.managedb.TypeOfSelection;
import clientapp.entity.Student;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {
    private final static Logger LOGGER = LogManager.getLogger(Client.class);

    public Client(Socket socket) {
    }

    public void askAddStudent(Student student) {

    }

    public List<String> askAllProgramLang() {
        return new ArrayList<String>();
    }

    public void askChangeTable(String value) {
    }

    public String getCurrentTableName() {
        return "";
    }

    public void askDropCurrentTable() {
    }

    public List<String> askAllTablesNames() {
        return new ArrayList<>();
    }

    public void askCreateStudentsTable(String tableName) {
    }

    public List<Student> askFindStudentByParam(Object firstParam, Object secondParam, TypeOfSelection type) {
        return new ArrayList<>();
    }

    public List<Student> askDelStudentsByParam(Object firstParam, Object secondParam, TypeOfSelection type) {
        return new ArrayList<>();
    }

    public List<Student> askStudentsFromCurrentTable() {
        return new ArrayList<>();
    }

    public List<Student> getStudentsFromCurrentTable() {
        return new ArrayList<>();
    }
}
