package serverapp.managedb.delete;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import serverapp.DataBaseController;
import serverapp.managedb.ComboTypeAdapter;
import serverapp.viewcomponents.TableComponent;


public class DeleteDialog {
    private Group deletionParams;
    public DeleteDialog(Shell shell, DataBaseController dataBaseController) {
        Shell dialog = new Shell(shell);
        dialog.setText("Delete");
        dialog.setModified(true);
        GridLayout gridLayout = new GridLayout();
        dialog.setLayout(gridLayout);
        Label chooseLbl = new Label(dialog, SWT.NONE);
        chooseLbl.setText("Choose type of deletion");
        Combo comboType = new Combo(dialog, SWT.READ_ONLY);
        String[] types = {"by NAME or GROUP", "by COURSE or PROGRAMMING LANGUAGE",
        "by NUMofTASK or NUMofDONE TASKS","by NUM of UNDONE TASKS"};
        comboType.setItems(types);

        Button deleteButton = new Button(dialog, SWT.PUSH);
        deleteButton.setEnabled(false);

        deletionParams = new Group(dialog, SWT.SHADOW_ETCHED_IN);
        deletionParams.setVisible(false);
        TableComponent resultTable = new TableComponent(dialog);
        comboType.addSelectionListener(new ComboTypeAdapter(deletionParams, dataBaseController, deleteButton));
        deleteButton. addSelectionListener(new DelInfoAdapter(deletionParams, comboType, dataBaseController, resultTable));
        dialog.pack();
        dialog.open();
    }
}
