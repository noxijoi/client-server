package clientapp.managedb;

import clientapp.Client;
import clientapp.managedb.addrecord.AddDialog;
import clientapp.managedb.delete.DeleteDialog;
import clientapp.managedb.search.SearchDialog;
import clientapp.view.FormManipulator;
import clientapp.view.TableComponent;
import clientapp.view.VerifyWordListener;
import clientapp.xml.DOMWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Arrays;
import java.util.List;

public class DataBaseManageDialog {

    private Shell shell;
    private TableComponent tableComponent;
    private Client client;
    private Combo tablesCombo ;

    public DataBaseManageDialog(Shell parent, Client client) {
        shell = createShell(parent);
        shell.pack();
        shell.open();
    }

    private Shell createShell(Shell parent) {
        Shell shell = new Shell(parent );
        shell.setText("Manage data");
        shell.setRedraw(true);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        gridLayout.makeColumnsEqualWidth = false;

        shell.setLayout(gridLayout);

        GridData tableComponentGridData = new GridData();
        tableComponentGridData.horizontalSpan = 5;
        tableComponentGridData.verticalSpan = 10;
        tableComponentGridData.grabExcessHorizontalSpace = true;
        tableComponentGridData.grabExcessVerticalSpace = true;
        tableComponentGridData.horizontalAlignment = GridData.FILL;
        tableComponentGridData.verticalAlignment = GridData.FILL;
        tableComponent = new TableComponent(shell);
        tableComponent.setLayoutData(tableComponentGridData);

        new Label(shell, SWT.NONE).setText("Select Table");
        tablesCombo = new Combo(shell, SWT.DROP_DOWN |SWT.READ_ONLY );
        //List<String> progLangs = client.askAllProgramLang();
        //progLangs.forEach(tableName -> tablesCombo.add(tableName));
        tablesCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                String value =
                    tablesCombo.getItem(tablesCombo.getSelectionIndex());
                client.askChangeTable(value);
                updateTable();
            }
        });

        Button deleteTableButton = FormManipulator.createButton(shell, "Delete Table");
        deleteTableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                MessageBox confirm = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK |SWT.CANCEL);
                confirm.setText("delete Table");
                confirm.setMessage
                        ("Do you really want delete "+ client.getCurrentTableName() + " table???");
                int code = confirm.open();
                if(code == SWT.YES){
                    client.askDropCurrentTable();
                    updateCombo();
                    updateTable();
                }


            }
        });
        Button addTable = FormManipulator.createButton(shell,"Add Table");
        addTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                Shell askNameDialog =  new Shell(shell);
                RowLayout rowLayout = new RowLayout();
                askNameDialog.setLayout(rowLayout);
                askNameDialog.setText("New table");
                new Label(askNameDialog, SWT.NONE).setText("Enter Table name:");
                Text text = new Text(askNameDialog, SWT.SINGLE);
                text.addVerifyListener(new VerifyWordListener());
                Button addButton = FormManipulator.createButton(askNameDialog,"Add");
                addButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent selectionEvent) {
                        String tableName = text.getText();
                        if(Arrays.asList(client.askAllTablesNames()).contains(tableName)){
                            MessageBox warning = new MessageBox(askNameDialog, SWT.ICON_WARNING);
                            warning.setMessage("Table with that name already exists!");
                            warning.open();
                        } else {
                            client.askCreateStudentsTable(tableName);
                            updateCombo();
                        }
                    }
                });
                askNameDialog.pack();
                askNameDialog.open();
            }
        });

        new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);

        Button addRecordButton = FormManipulator.createButton(shell, " Add record");
        addRecordButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                new AddDialog(shell, client );
                updateTable();
            }
        });
        Button searchRecordsButton = FormManipulator.createButton(shell, "Search records");
        searchRecordsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                SearchDialog searchDialog = new SearchDialog(shell, client);
            }
        });
        Button deleteRecordsButton = FormManipulator.createButton(shell, "Delete records");
        deleteRecordsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                new DeleteDialog(shell, client);
            }
        });
        Button exportToXmlButton = FormManipulator.createButton(shell,"Export current table to Xml");
        exportToXmlButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
                fileDialog.setText("Select file to save ");

                fileDialog.setFilterNames(new String[]{"XML files"});
                fileDialog.setFilterExtensions(new String[]{"*.xml"});

                String fn = fileDialog.open();
                if (fn != null) {
                    DOMWriter domWriter = new DOMWriter();
                    domWriter.writeToFile(fn, client.askStudentsFromCurrentTable());
                }
            }
        });
        return shell;
    }

    private void updateCombo() {
        tablesCombo.removeAll();
        List<String> tablesNames = client.askAllTablesNames();
        tablesNames.forEach(name ->tablesCombo.add(name) );
    }

    private void updateTable(){
        tableComponent.clear();
        tableComponent.addAllStudents(client.getStudentsFromCurrentTable());
    }
}
