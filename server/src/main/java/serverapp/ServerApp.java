package serverapp;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import serverapp.db.DataBaseConnectionPool;
import serverapp.managedb.DataBaseController;
import serverapp.managedb.DataBaseManageDialog;
import serverapp.viewcomponents.FormManipulator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ServerApp {
    private static final Logger LOGGER = LogManager.getLogger(ServerApp.class);

    private DataBaseController controller;
    private final String serverProp = "server";
    private Shell shell;
    private Text logArea;
    private Connection connection;
    private Server mainServer;


    public ServerApp(){
        Display display = new Display();
        shell = createShell(display);

        connection = DataBaseConnectionPool.getInstance().getConnection();
        controller = new DataBaseController(connection);
        shell.pack();
        shell.open();
        while(!shell.isDisposed()){
            if(!display.readAndDispatch())
                display.sleep();
        }
    }

    private Shell createShell(Display display) {
        Shell shell = new Shell(display);
        shell.setText("Third lab");
        shell.setRedraw(true);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;

        shell.setLayout(gridLayout);
        GridData buttonGridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);


        Button runServerButton = FormManipulator.createButton(shell,"Run server");
        runServerButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {

                if(mainServer == null){
                    mainServer = new Server();
                    mainServer.start();
                } else {
                    mainServer.setWorkingFlag(true);
                }

            }
        });
        runServerButton.setLayoutData(buttonGridData);
        Button stopServerButton = FormManipulator.createButton(shell, "Stop Server");
        stopServerButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                mainServer.setWorkingFlag(false);
            }
        });
        stopServerButton.setLayoutData(buttonGridData);

        GridData textAreaGridData = new GridData();
        textAreaGridData.horizontalSpan = 2;
        textAreaGridData.widthHint = 400;
        textAreaGridData.heightHint = 500;
        textAreaGridData.grabExcessHorizontalSpace = true;
        textAreaGridData.grabExcessVerticalSpace = true;
        textAreaGridData.horizontalAlignment = GridData.FILL;
        textAreaGridData.verticalAlignment = GridData.FILL;
        logArea = new Text(shell, SWT.MULTI | SWT.READ_ONLY |SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        logArea.setVisible(true);
        logArea.setBackground(new Color(display, 255, 255,255));
        logArea.setLayoutData(textAreaGridData);

        Button toDataManageButton = FormManipulator.createButton(shell,"Manage Database");
        toDataManageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                new DataBaseManageDialog(shell, connection);
            }
        });
        String serverIP;
        try {
            serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            serverIP = "Can't define server IP";
            LOGGER.warn("Can't define server IP");

        }
        new Text(shell, SWT.READ_ONLY | SWT.SINGLE).setText("Server IP: "+ serverIP);

        ResourceBundle bundle = ResourceBundle.getBundle(serverProp);
        String port = bundle.getString("server.port");
        new Text(shell, SWT.READ_ONLY | SWT.SINGLE).setText("Port №" + port);

        return shell;
    }

    public void displayLog(String logMessage){
        logArea.append(logMessage);
    }

}
