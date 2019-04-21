package serverapp.managedb.delete;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import serverapp.managedb.DataBaseController;
import serverapp.entity.Name;
import serverapp.entity.Student;
import serverapp.managedb.TypeOfSelection;
import serverapp.viewcomponents.TableComponent;

import java.util.List;

public class DelInfoAdapter extends SelectionAdapter {
    private Group paramsGroup;
    private Combo combo;
    private DataBaseController dataBaseController;
    private TableComponent resultTable;


    public DelInfoAdapter(Group deletionParams, Combo typeOfDel, DataBaseController dataBaseController, TableComponent resultTable) {
        this.paramsGroup = deletionParams;
        this.combo =typeOfDel;
        this.dataBaseController = dataBaseController;
        this.resultTable = resultTable;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {

        Group group1;
        Control[] children1 ;
        Group group2;
        Control[] children2 = null;

        group1 = (Group) paramsGroup.getChildren()[0];
        children1 = group1.getChildren();

        if(paramsGroup.getChildren().length > 1) {
            group2 = (Group) paramsGroup.getChildren()[1];
            children2 = group2.getChildren();
        }


        Object firstParam = "";
        Object secondParam = "";
        TypeOfSelection type = TypeOfSelection.getTypeByNumber(combo.getSelectionIndex());

        switch (type){
            case FIO_AND_GROUP:
                //как то некрасиво
                Text fName = (Text) children1[1];
                Text lName = (Text) children1[3];
                Text pName = (Text) children1[5];
                firstParam = new Name(fName.getText(), lName.getText(), pName.getText());

                Text groupText = (Text) children2[1];
                if(!groupText.getText().isEmpty()) {
                    secondParam = Integer.parseInt(groupText.getText());
                }
                break;
            case COURSE_AND_PL:
                Text courseText = (Text) children1[1];
                if(!courseText.getText().isEmpty()) {
                    firstParam = Integer.parseInt(courseText.getText());
                }
                Combo progLangCombo = (Combo) children2[1];
                secondParam  = progLangCombo.getText();
                break;
            case NUM_OF_TASKS:
                Text numTaskText = (Text) children1[1];
                if(!numTaskText.getText().isEmpty()) {
                    firstParam = Integer.parseInt(numTaskText.getText());
                }
                Text numDoneTaskText = (Text) children2[1];
                if(!numDoneTaskText.getText().isEmpty()) {
                    secondParam = Integer.parseInt(numDoneTaskText.getText());
                }
                break;
            case NUM_OF_UNDONE_TASKS:
                Text numUndoneTaskText = (Text) children1[1];
                if(!numUndoneTaskText.getText().isEmpty()) {
                    firstParam = Integer.parseInt(numUndoneTaskText.getText());
                }
                break;
        }
        List<Student> removedStudents = dataBaseController.delStudentsByParam(firstParam, secondParam, type);

        Composite parent = paramsGroup.getParent();
        resultTable.clear();
        if(removedStudents.isEmpty()){
            new Label(parent, SWT.NONE).setText("No such students found");
        } else {
            Group tableGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
            tableGroup.setLayout(new GridLayout());
            GridData tableComponentGridData = new GridData();
            tableComponentGridData.grabExcessHorizontalSpace = true;
            tableComponentGridData.grabExcessVerticalSpace = true;
            tableComponentGridData.horizontalAlignment = GridData.FILL;
            tableComponentGridData.verticalAlignment = GridData.FILL;

            resultTable.addAllStudents(removedStudents);
            resultTable.setLayoutData(tableComponentGridData);
            resultTable.setVisible(true);
        }
    }
}
