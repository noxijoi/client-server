package clientapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private List<Student> students;

    public Content(){
        students = new ArrayList<>();
    }

    public void add(Student student) {
        students.add(student);
    }

    public void addAll(List<Student> studentList){
        students.addAll(studentList);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void clear() {
        students.clear();
    }
}
