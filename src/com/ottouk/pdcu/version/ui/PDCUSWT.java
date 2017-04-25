package com.ottouk.pdcu.version.ui;

import java.io.IOException;

import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.version.service.AppLock;
import com.ottouk.pdcu.version.service.PDCUConstants;

/**
 * The main entry point for the PDCU Boot strap application. Responsible for checking that 
 * only one instance of the application is running and displaying the PDCU logon screen. 
 * 
 * The PDCU Boot strap application initiates the version checking logic to ensure that the 
 * application it invokes (PDCUMain) is the latest available version.
 *  
 * @author dis114
 * @see LoginUiShell - The main screen that is invoked from this class.
 */
public final class PDCUSWT {
    
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
	private final static void exitToApplock() {
		System.out.println("Exiting to applock");
		String exe = "\\Windows\\CrEme\\Bin\\Creme.exe";
		try {
//			File j = new File(APPLOCK_JAR_PATH);
//			if (j.exists()) {
			 
				System.out.println("Running " + exe + " " + APPLOCK_JAR_PARMS + APPLOCK_JAR_PATH);
				Runtime.getRuntime().exec(exe 
					+ APPLOCK_JAR_PARMS 
					+ APPLOCK_JAR_PATH);
				System.out.println("Ran " + exe);
				try {
					Thread.sleep(DELAYED_EXIT_INTERVAL);       
				} catch (InterruptedException ex) {
					System.out.println("Interrupt exception");
					Thread.currentThread().interrupt();
				}
//			}
			
			
			System.exit(0);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
        * Default Constructor.
        */
       private PDCUSWT() {
           
       }
       /**
        * Application entry point.
        * 
        * @param args - list of arguments passed in via command line. None expected.
        */
	public static void main(final String[] args) {
		
		//AppLock appLock = new AppLock();
		try {
			if (AppLock.appAlreadyRunning(PDCUConstants.GUN_RESOURCEDIR 
					+ "PDCUBoot.lock")) {
				// App already running so exit!
				System.exit(0);
			} else {
				// App is not running, display the user interface
				new LoginUIShell();
				System.out.println("Back from GUI");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    
		        System.out.println("In Finally");
		        System.out.println("releaseLock");
			// Tidy up the lock file upon exit.
			AppLock.releaseLock();
	        System.out.println("exitToApplock");
			exitToApplock();
		}
	}

}
