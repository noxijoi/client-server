DROP TABLE stud;
CREATE TABLE IF NOT EXISTS stud(
                                 s_id          INT           NOT NULL    AUTO_INCREMENT   PRIMARY KEY ,
                                 f_name        VARCHAR(100)  NOT NULL ,
                                 l_name        VARCHAR(100)  NOT NULL ,
                                 p_name        VARCHAR(100)  NOT NULL ,
                                 course        INT           NOT NULL,
                                 group_n       INT           NOT NULL ,
                                 total_task    INT           NOT NULL ,
                                 done_task     INT           NOT NULL ,
                                 prog_lang     VARCHAR(100)  NOT NULL
);