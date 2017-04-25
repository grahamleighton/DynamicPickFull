package com.ottouk.pdcu.version.ui;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The main GUI frame window of the PDCU logon and version bootstrap application.
 * Utilises the SWT shell object and contains the LoginUI composite.
 * 
 * @author dis114
 * @see org.eclipse.swt.widgets.Shell;
 */

public class LoginUIShell {
    
    /** display is the top level SWT controller - used to catch the windows event loops. */
    private  Display display;
    
    /** shell is the top level SWT frame of the PDCU boot application. */ 
    private Shell shell;

    /**
     * Constructor method.
     * Sets the application title, adds the SWT composite GUI objects and listens
     * for the windows events, despatching them to the relevant GUI SWT object.
     * 
     * @see - LoginUI - The SWT composite GUI object that is embedded in this Shell.
     */
    public LoginUIShell() {
        
        if (display == null || display.isDisposed()) {
            display = new Display();

            System.out.println("Create: " + display);
    }

        // the shell is the top level window (AKA frame)
        shell = new Shell(display);

        shell.setText("PDCU Boot"); // Give me a title
        shell.setMaximized(true);
       

        // all children split space equally
        shell.setLayout(new FillLayout());

        LoginUI tbUI = new LoginUI(shell);
        tbUI.setActiveShell(shell);

        // shell.pack(); // make shell take minimum size
        shell.open(); // open shell for user access

        // process all user input events until the shell is disposed (i.e.,
        // closed)
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) { // process next message
                display.sleep(); // wait for next message
            }
        }
        //display.dispose(); // must always clean up

    }
    /**
     * shell Getter.
     * 
     * @return shell - the current SWT shell object
     */
    public final Shell getShell() {
        return shell;
    }
    
    /**
     * shell Setter.
     * @param sh - the SWT shell 
     */
    public final void setShell(final Shell sh) {
        this.shell = sh;
    }
}
