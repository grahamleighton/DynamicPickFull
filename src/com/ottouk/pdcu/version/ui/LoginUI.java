package com.ottouk.pdcu.version.ui;

import java.io.File;
import java.io.IOException;

import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.version.domain.Login;
import com.ottouk.pdcu.version.service.LogonController;
import com.ottouk.pdcu.version.service.PDCUConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * SWT Composite object that contains all the GUI elements of the PDCU logon screen.
 * 
 * @author dis114
 * 
 */

public class LoginUI extends Composite {
    
    /*
     * Instance Variables
     */
    
    /**
     * Captures the userd id of the operative attemting to logon. 
     */
    private Text logonField;
    /**
     * Message label used to inform teh user of the status of the version check process. 
     */  
    private Label status;
    /**
     * Button added to the Composite that allows the user to invoke the version check and logon process.
     */
    private Button okButton;
    /**
     * Keeps a reference to the active SWT shell - LoginUiShell.
     * 
     * @see LoginUIShell
     */
    private Shell activeShell;
    /**
     * The controller that handles the SWT listener events that are generated by the LoginUI GUI listeners. 
     * The LogonController is used to decouple the business logic from the user interface.
     */
    private LogonController lController;
    
	/**
	 * Delay interval.
	 */
	private static final int DELAYED_EXIT_INTERVAL = 5000;
	/**
	 * Applock parm path.
	 */
	private static final String APPLOCK_JAR_PATH = "\\applockx\\applock.jar";
	/**
	 * Applock jar parms.
	 */
	private static final String APPLOCK_JAR_PARMS = " -sp:0 -jar ";
	
	
	/**
	 * Exit to new applock.
	 */
	public final void exitToApplock() {
		StringUtils.log("Exiting to applock");
		String exe = "\\Windows\\CrEme\\Bin\\Creme.exe";
		try {
//			File j = new File(APPLOCK_JAR_PATH);
//			if (j.exists()) {
			 	status.setText("Starting Applock");
				Runtime.getRuntime().exec(exe 
					+ APPLOCK_JAR_PARMS 
					+ APPLOCK_JAR_PATH);
				try {
					Thread.sleep(DELAYED_EXIT_INTERVAL);       
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
//			}
			
			
			System.exit(0);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
    

    /*
     * Methods
     */
   
    /**
     * Constructor - The one parameter constructor that defaults style to SWT-NONE and calls the
     * two parameter constructor.
     * 
     * @param parent - the ancestor composite
     * 
     */
    public LoginUI(final Composite parent) {
        this(parent, SWT.NONE); // must always supply parent
    }

    /**
     * Constructor - calls the parent constructor with two parameters and then creates and displays 
     * the GUI elements.
     * 
     * @param parent - the ancestor composite
     * @param style - the required SWT style for the Composite.
     * 
     * @see createGUI()
     */
    public LoginUI(final Composite parent, final int style) {
        super(parent, style); // must always supply parent and style
        lController = new LogonController();

        createGui();
    }

    /**
     * Helper method to add a button to the specified Composite object.
     * 
     * @param parent - the composite object that the button is to be added to.
     * @param label - the required label of the button (String)
     * @param tip - the tooltip text that is to be displayed when user hovers over the button (String)
     * @param l - the listener that captures the button press events. (SelectionListener)
     * @return Button - a Button object with all the decorations (e.g. listener, text etc)
     */
    
    protected final Button createButton(final Composite parent, final String label, final String tip,
            final SelectionListener l) {
        Button b = new Button(parent, SWT.NONE);
        b.setFont(new Font(getDisplay(), "Tahoma", PDCUConstants.LOGON_BUTTON_FONT, SWT.BOLD));

        b.setText(label);
        if (tip != null) {
            b.setToolTipText(tip);
        }
        if (l != null) {
            b.addSelectionListener(l);
        }
        return b;
    }
  
    /**
     *  Method to create and display the GUI components of the PDCU Logion Screen.
     */
    protected final void createGui() {

        setLayout(new GridLayout(1, true));

        // Create the input area

        Group entryGroup = new Group(this, SWT.CENTER);
        entryGroup.setText("PDCU Logon v" + PDCUConstants.LOGON_VERSION);
        entryGroup.setFont(new Font(getDisplay(), "Tahoma", PDCUConstants.LOGON_BUTTON_FONT, SWT.NORMAL));
        entryGroup.setLayout(getLayout());

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;

        entryGroup.setLayoutData(gridData);

        // Logon Id text field

        Label l = new Label(entryGroup, SWT.CENTER);
        l.setFont(new Font(getDisplay(), "Tahoma", PDCUConstants.LOGON_LABEL_FONT, SWT.BOLD));
        l.setText("Personnel Number");

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.verticalAlignment = SWT.CENTER;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        l.setLayoutData(gridData);

        logonField = new Text(entryGroup, SWT.BORDER);
        logonField.setFont(new Font(getDisplay(), "Tahoma", PDCUConstants.LOGON_LABEL_FONT, SWT.BOLD));
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.widthHint = 100;
        gridData.heightHint = 20;
        gridData.verticalAlignment = SWT.TOP;
        gridData.grabExcessVerticalSpace = false;
        gridData.grabExcessHorizontalSpace = false;
        logonField.setLayoutData(gridData);
        logonField.setTextLimit(9);
        logonField.setToolTipText("Please enter your 8 digit personnel number");

        // Trap the enter key on the logon field.
        
        logonField.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(final Event event) {
                System.out.println(event.widget + " -Logon - "
                        + logonField.getText());
                // Disable button and logon field
                showWaitCursor();
                // Perform a logon validation 
                Login logon = lController.onLogin(logonField.getText());
                // If 'secret' exit code entered then exit to OS
                if (logon.isExitStatus()) {
                	exitToApplock();
                    activeShell.close();
                }
                // If valid user id entered then do the logon and invoke the next app. 
                if (logon.isValidCredential()) {
                    status.setText(logon.getErrorMsg());
                    logon = lController.doSuccess();
                    if (logon.isExitStatus()) {
                        DelayedExit.exitToMain(3);
                        //new DelayedExit(3, PDCUConstants.GUN_RESOURCEDIR + "PDCUMain.lock");
                       
                    } else {
                        // On logon error , display the error message
                        status.setText(logon.getErrorMsg());
                        hideCursor();
                        logonField.setFocus();
                    }
                } else {
                    // Invalid user id, display error message and allow user to try again.
                    activeShell.getDisplay().beep();
                    hideCursor();
                    status.setText(logon.getErrorMsg());
                    logonField.setText("");
                    logonField.setFocus();
                }

            }
        });

        // Add a Status message label that is used to diaplay any error messages.

        status = new Label(entryGroup, SWT.WRAP);
        gridData = new GridData();
        gridData.widthHint = 200;
        gridData.heightHint = 100;
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.verticalAlignment = SWT.CENTER;

        status.setLayoutData(gridData);

        // create the button area

        Composite buttons = new Composite(this, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.verticalAlignment = SWT.TOP;
        gridData.grabExcessHorizontalSpace = true;

        buttons.setLayoutData(gridData);
        // make all buttons the same size
        FillLayout buttonLayout = new FillLayout(SWT.CENTER);
        buttonLayout.marginHeight = 2;
        buttonLayout.marginWidth = 2;
        buttonLayout.spacing = 5;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        buttons.setLayout(buttonLayout);

        // Add the OK button to the Button area.
        
        okButton = createButton(buttons, "&Ok", "Process input",
                new SelectionAdapter() {
                    // On button pressed event
                    public void widgetSelected(final SelectionEvent e) {

                        System.out.println(e.widget + " -Logon - "
                                + logonField.getText());
                        showWaitCursor();
                        // Valdate the user id
                        Login logon = lController.onLogin(logonField.getText());
                        if (logon.isExitStatus()) {
                        	exitToApplock();
                        	activeShell.close();
                        }
                        if (logon.isValidCredential()) {
                            hideCursor();
                            status.setText(logon.getErrorMsg());
                            logonField.setFocus();
                            // Perform the logon process
                            logon = lController.doSuccess();
                            if (logon.isExitStatus()) { 
               
                                DelayedExit.exitToMain(3);
                                //new DelayedExit(3, PDCUConstants.GUN_RESOURCEDIR + "PDCUMain.lock");
                            } else {
                                // If logon process failed then display error message.
                                hideCursor();
                                status.setText(logon.getErrorMsg());
                                logonField.setFocus();
                            }
                        } else { // Invalid user id 
                            hideCursor();
                            activeShell.getDisplay().beep();
                            status.setText(logon.getErrorMsg());
                            logonField.setText("");
                            logonField.setFocus();
                        }

                    }
                });
    }

    /**
     * Getter for activeShell variable.
     * 
     * @return activeShell - the current SWT shell 
     */
    public final Shell getActiveShell() {
        return activeShell;
    }
    /**
     * Setter for activeShell variable.
     * @param aShell - the current SWT shell
     */
    public final void setActiveShell(final Shell aShell) {
        this.activeShell = aShell;
    }
    /**
     * Getter for logonField variable.
     * 
     * @return logonField - reference to the logon field
     */
    public final Text getLogonField() {
        return logonField;
    }
    /**
     * Setter for logonField variable.
     * 
     * @param myLogonField - the current logonField
     */
    public final void setLogonField(final Text myLogonField) {
        this.logonField = myLogonField;
    }
    /**
     * Getter for OK Button variable.
     * @return okButton - reference to the OK Button.
     */
    public final Button getOkButton() {
        return okButton;
    }
    /**
     * Setter for the OKButton.
     * 
     * @param myokButton - 
     */
    public final void setOkButton(final Button myokButton) {
        this.okButton = myokButton;
    }
    /**
     * Method to show the egg timer cursor, disable the logonfield and disable the okButton.
     * Prevents duplicate logon submissions. Used in conjunction with hideCursor.
     *  
     *  @see hideCursor()
     */
    protected final void showWaitCursor() {
        getDisplay().getActiveShell().setCursor(
                new Cursor(getDisplay(), SWT.CURSOR_WAIT));
        this.logonField.setEnabled(false);
        this.okButton.setEnabled(false);
    }
    /**
     * Method to remove the egg timer cursor and display the normal cursor.
     * Enables the logonfield and the OK button to allow logon requests to be processed. Used in conjunction with 
     * showWaitCursor().
     * 
     * @see showWaitCursor.
     */
    protected final void hideCursor() {
        getDisplay().getActiveShell().setCursor(null);
        this.logonField.setEnabled(true);
        this.okButton.setEnabled(true);

    }
}
