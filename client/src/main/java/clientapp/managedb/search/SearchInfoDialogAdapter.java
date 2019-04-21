package clientapp.managedb.search;

import clientapp.Client;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;


public class SearchInfoDialogAdapter extends SelectionAdapter {
    private Client client;
    public SearchInfoDialogAdapter(Client client) {
        this.client = client;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        ToolItem source = (ToolItem) e.getSource();
        Shell shell = source.getParent().getShell();
        new SearchDialog(shell, client);
    }
}
