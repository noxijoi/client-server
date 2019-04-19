package serverapp.db;

import java.sql.Connection;

public class DBService {
    //queries
    private final String CREATE_STUDENTS_TABLE = "CREATE TABLE ? (\n" +
            "                                 s_id          INT           NOT NULL    AUTO_INCREMENT     PRIMARY KEY ,\n" +
            "                                 name          VARCHAR(100)  NOT NULL ,\n" +
            "                                 course        INT           NOT NULL,\n" +
            "                                 group_n       INT           NOT NULL ,\n" +
            "                                 total_task    INT           NOT NULL ,\n" +
            "                                 done_task     INT           NOT NULL ,\n" +
            "                                 prog_lang     VARCHAR(100)  NOT NULL\n" +
            "\n" +
            ");";

}
