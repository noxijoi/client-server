package clientapp.managedb;

public enum TypeOfSelection {
    FIO_AND_GROUP,
    COURSE_AND_PL,
    NUM_OF_TASKS,
    NUM_OF_UNDONE_TASKS;

    public static TypeOfSelection getTypeByNumber(int i){
        switch (i){
            case 0:
                return FIO_AND_GROUP;
            case 1:
                return  COURSE_AND_PL;
            case 2:
                return NUM_OF_TASKS;
            case 3:
                return NUM_OF_UNDONE_TASKS;
                default:
                    return FIO_AND_GROUP;
        }
    }
}
