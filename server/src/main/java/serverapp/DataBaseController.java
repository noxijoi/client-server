package serverapp;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import serverapp.entity.Name;
import serverapp.entity.Student;
import serverapp.managedb.TypeOfSelection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseController {
    private final static Logger LOGGER = LogManager.getLogger(DataBaseController.class);

    private Connection connection;
    private String currentTableName = "";

    private final String DATABASE_NAME = "students";
    private final String L_NAME = "l_name";
    private final String F_NAME = "f_name";
    private final String P_NAME = "p_name";
    private final String COURSE = "course";
    private final String GROUP_N = "group_n";
    private final String TOTAL_TASK = "total_task";
    private final String DONE_TASK = "done_task";
    private final String PROG_LANG = "prog_lang";

    private final String DROP_TABlE = "DROP TABLE $tableName";
    private final String CREATE_STUDENTS_TABLE = "CREATE TABLE $tableName (\n" +
            "                                 s_id          INT           NOT NULL    AUTO_INCREMENT    PRIMARY KEY ,\n" +
            "                                 f_name        VARCHAR(100)  NOT NULL ,\n" +
            "                                 l_name        VARCHAR(100)  NOT NULL ,\n " +
            "                                 p_name        VARCHAR(100)  NOT NULL ,\n " +
            "                                 course        INT           NOT NULL ,\n" +
            "                                 group_n       INT           NOT NULL ,\n" +
            "                                 total_task    INT           NOT NULL ,\n" +
            "                                 done_task     INT           NOT NULL ,\n" +
            "                                 prog_lang     VARCHAR(100)  NOT NULL  \n" +
            ")";
    private final String ADD_STUDENT = "INSERT INTO stud(F_NAME, L_NAME, P_NAME, COURSE, GROUP_N, TOTAL_TASK, DONE_TASK, PROG_LANG)\n" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_ALL_STUDENTS_FROM_TABLE = "SELECT * FROM $tableName";
    private final String SELECT_PROG_LANGS = "SELECT DISTINCT PROG_LANG FROM $tableName";
    private final String SELECT_BY_FIO_OR_GROUP = "SELECT * FROM $tableName WHERE (F_NAME = ? AND L_NAME = ? AND P_NAME = ?) OR GROUP_N = ?";
    private final String SELECT_BY_NUM_OF_TASK = "SELECT * FROM $tableName WHERE TOTAL_TASK = ?";
    private final String SELECT_BY_COURSE_OR_PROG_LANG ="SELECT * FROM $tableName WHERE COURSE = ? OR PROG_LANG = ?";
    private final String SELECT_BY_NUM_OF_UNDONE_TASK = "SELECT * FROM $tableName WHERE TOTAL_TASK - DONE_TASK = ?";


    public DataBaseController(Connection connection) {
        this.connection = connection;
    }

    public void addStudent(Student student) {
        try{
            String query = replaceTableName(ADD_STUDENT, currentTableName );
            PreparedStatement statement = connection.prepareStatement(query);

            Name studentName = student.getName();
            statement.setString(1, studentName.firstName);
            statement.setString(2, studentName.lastName);
            statement.setString(3, studentName.patronymic);

            statement.setInt(4, student.getCourse());
            statement.setInt(5, student.getGroupNumber());
            statement.setInt(6, student.getTotalNumOfTask());
            statement.setInt(7, student.getNumOfDoneTasks());
            statement.setString(8, student.getProgrammingLanguage());

            statement.execute();
            LOGGER.info("add student to table " + currentTableName);
        } catch (SQLException e){
            e.printStackTrace();
            LOGGER.warn("can't add student to table " + e.getMessage());
        }

    }

    public void addAllStudents(List<Student> studentList) {
    }

    public List<Student> delStudentsByParam(Object firstParam, Object secondParam, TypeOfSelection type) {
        return null;
    }

    public List<Student> findStudentByParam(Object firstParam, Object secondParam, TypeOfSelection type) {
        List<Student> foundStudents = new ArrayList<>();
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            switch (type) {
                case FIO_AND_GROUP:
                    query = replaceTableName(SELECT_BY_FIO_OR_GROUP, currentTableName);
                    preparedStatement = connection.prepareStatement(query);
                    Name name = (Name)firstParam;
                    preparedStatement.setString(1, name.firstName);
                    preparedStatement.setString(2, name.lastName);
                    preparedStatement.setString(3, name.patronymic);

                    preparedStatement.setInt(4, (Integer)secondParam);
                    resultSet = preparedStatement.executeQuery();
                    foundStudents = studentListFromResultSet(resultSet);
                    break;
                case NUM_OF_TASKS:
                    query = replaceTableName(SELECT_BY_NUM_OF_TASK, currentTableName);
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, (Integer)firstParam);
                    resultSet = preparedStatement.executeQuery();
                    foundStudents = studentListFromResultSet(resultSet);
                    break;
                case COURSE_AND_PL:
                    query = replaceTableName(SELECT_BY_COURSE_OR_PROG_LANG, currentTableName);
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, (Integer)firstParam);
                    preparedStatement.setString(2, (String)secondParam);
                    resultSet = preparedStatement.executeQuery();
                    foundStudents = studentListFromResultSet(resultSet);
                    break;
                case NUM_OF_UNDONE_TASKS:
                    query = replaceTableName(SELECT_BY_NUM_OF_UNDONE_TASK, currentTableName);
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, (Integer)firstParam);
                    resultSet = preparedStatement.executeQuery();
                    foundStudents = studentListFromResultSet(resultSet);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundStudents;
    }

    public List<String> getAllProgrammingLanguages() {
        List<String> progLangs = new ArrayList<>();
        try {
            String query = replaceTableName(SELECT_PROG_LANGS, currentTableName);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String lang = resultSet.getString("prog_lang");
                progLangs.add(lang);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get programming languages  because" + e.getMessage());
        }
        return progLangs;
    }

    public String[] getAllTablesNames() {

        ResultSet tablesSet = null;
        DatabaseMetaData data = null;
        List<String> result = null;
        try {
            data = connection.getMetaData();
            String[] types = {"TABLE"};
            tablesSet = data.getTables(DATABASE_NAME, null, "%", types);
            result = new ArrayList<>();
            while (tablesSet.next()) {
                result.add(tablesSet.getString(3));
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }

        String resArr[] = new String[result.size()];
        resArr = result.toArray(resArr);
        return resArr;
    }

    public void changeTable(String tableName) {
        currentTableName = tableName;
    }

    public List<Student> getStudentsFromCurrentTable() {
        List<Student> result = new ArrayList<>();
        try {
            String query = replaceTableName(SELECT_ALL_STUDENTS_FROM_TABLE, currentTableName);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            result = studentListFromResultSet(resultSet);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
        return result;
    }

    private String replaceTableName(String query, String tableName) {
        return  query.replace("$tableName", tableName);
    }

    private List<Student> studentListFromResultSet(ResultSet resultSet) {
        List<Student> students = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Student student = new Student();

                String fName = resultSet.getString(F_NAME);
                String lName = resultSet.getString(L_NAME);
                String pName = resultSet.getString(P_NAME);
                Name name = new Name(fName, lName, pName);
                student.setName(name);

                student.setCourse(resultSet.getInt(COURSE));
                student.setGroupNumber(resultSet.getInt(GROUP_N));
                student.setTotalNumOfTask(resultSet.getInt(TOTAL_TASK));
                student.setNumOfDoneTasks(resultSet.getInt(DONE_TASK));
                student.setProgrammingLanguage(resultSet.getString(PROG_LANG));

                students.add(student);
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
        return students;
    }

    public String getCurrentTableName() {
        return currentTableName;
    }

    public void dropCurrentTable() {
        try{
            String query = replaceTableName(DROP_TABlE, currentTableName);
            Statement statement = connection.createStatement();
            statement.execute(query);
            LOGGER.info("Table "+currentTableName+" is deleted");
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
        currentTableName = "";
    }

    public void createStudentsTable(String tableName) {
        try{
            String query = replaceTableName(CREATE_STUDENTS_TABLE, tableName );
            Statement statement = connection.createStatement();
            statement.execute(query);
            LOGGER.info("New table is created");
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}

