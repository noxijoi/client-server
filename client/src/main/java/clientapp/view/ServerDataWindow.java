package clientapp.view;

import clientapp.ClientApp;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class ServerDataWindow {
    private Shell shell;
    public static final String IP_PATTERN = "(\\d{1,3}\\.){3}\\d{1,3}" ;

    public ServerDataWindow(ClientApp app){
        Display display =Display.getCurrent();
        shell = new Shell(display);
        RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        shell.setLayout(rowLayout);
        shell.setText("Server IP");

        new Label(shell, SWT.NONE).setText("Enter server IP:");
        Text ipText  = new Text(shell, SWT.SINGLE);
        ipText.addVerifyListener(verifyEvent -> {
            verifyEvent.doit = false;
            char typedChar = verifyEvent.character;
            if(Character.isDigit(typedChar)||typedChar == '.'){
                verifyEvent.doit = true;
            }
        });

        new Label(shell, SWT.NONE).setText("Enter server port #");
        Text portText = new Text(shell, SWT.SINGLE);
        portText.addVerifyListener(new VerifyNumberListener());

        Button button = new Button(shell, SWT.PUSH);
        button.setText("Ok");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                String ip = ipText.getText();
                String port = portText.getText();
                if(ip.matches(IP_PATTERN) && !portText.getText().isEmpty()){
                    app.setIP(ip);
                    app.setPort(Integer.parseInt(port));
                    app.init();
                    shell.close();
                }
            }
        });
        shell.pack();
        shell.open();
        while(!shell.isDisposed()){
            if(!display.readAndDispatch())
                display.sleep();
        }
    }

}
