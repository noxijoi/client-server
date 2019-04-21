package clientapp.managedb;

import clientapp.Client;
import clientapp.view.FormManipulator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import static clientapp.managedb.TypeOfSelection.getTypeByNumber;

public class ComboTypeAdapter extends SelectionAdapter {

    private Group paramsGroup;
    private Client client;
    private Button actionButton;

    public ComboTypeAdapter(Group paramsGroup, Client client, Button actionButton) {
        this.paramsGroup = paramsGroup;
        this.client = client;
        this.actionButton = actionButton;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        Combo combo = (Combo)e.getSource();
        Composite parent = combo.getParent();
        int index = combo.getSelectionIndex();
        TypeOfSelection type = getTypeByNumber(index);
        RowLayout rowLayout = new RowLayout();
        paramsGroup.setLayout(rowLayout);
        switch (type){
            case FIO_AND_GROUP:
                FormManipulator.createFIOInput(paramsGroup);
                FormManipulator.createNumericInput(paramsGroup,"Group");
                break;
            case COURSE_AND_PL:
                FormManipulator.createNumericInput(paramsGroup, "Course");
                FormManipulator.createComboInputProgLang(paramsGroup, client);
                break;
            case NUM_OF_TASKS:
                FormManipulator.createNumericInput(paramsGroup, "Number of tasks");
                FormManipulator.createNumericInput(paramsGroup,"Number of done tasks");
                break;
            case NUM_OF_UNDONE_TASKS:
                FormManipulator.createNumericInput(paramsGroup,"Number of undone tasks");
                break;
        }
        combo.setEnabled(false);
        paramsGroup.setVisible(true);
        actionButton.setEnabled(true);
        parent.pack();
    }
}
