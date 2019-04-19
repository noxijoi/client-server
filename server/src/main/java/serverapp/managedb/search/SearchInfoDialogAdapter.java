package serverapp.managedb.search;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import serverapp.DataBaseController;

public class SearchInfoDialogAdapter extends SelectionAdapter {
    private DataBaseController dataBaseController;
    public SearchInfoDialogAdapter(DataBaseController dataBaseController) {
        this.dataBaseController = dataBaseController;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        ToolItem source = (ToolItem) e.getSource();
        Shell shell = source.getParent().getShell();
        new SearchDialog(shell, dataBaseController);
    }
}
