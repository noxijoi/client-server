package serverapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private List<StudentTable> tables;

    public StudentTable getTable(int index){
        return tables.get(index);
    }

    public Content(){

    }

    public void clear() {
    }


    private class StudentTable {
        List<Student> students;
        public StudentTable(){
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
}
