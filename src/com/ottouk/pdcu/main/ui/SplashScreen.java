package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple shell to display blank splash screen.
 * 
 * @author dis065
 *
 */
public final class SplashScreen extends GeneralDisplay {

	/**
	 * Splash screen name.
	 */
	private static final String SPLASH_SCREEN_SHELL_NAME = "Please wait...";
	
	
	/**
	 * Show splash screen and hour glass.
	 */
	public static void show() {
		
		// Create blank splash screen 
		/*Shell shell = new Shell(DISPLAY, SWT.NO_TRIM);*/
		
		// Create blank titled splash screen
		Shell shell = new Shell(DISPLAY, SWT.TITLE);
		
		shell.setMaximized(true);
		shell.setText(SPLASH_SCREEN_SHELL_NAME);
		
		// Open splash screen
		shell.open();
		/*logShellOpen(shell);*/
		
		// Show hour glass
		showWaitCursor(shell);
	}
	
	/**
	 * Hide splash screen(s) and hour glass(es).
	 */
	public static void hide() {
		Shell[] shells = DISPLAY.getShells();
		for (int i = 0; i < shells.length; i++) {
			
			// Hide any hour glass
			hideCursor(shells[i]);
			
			if (shells[i].getText().equals(SPLASH_SCREEN_SHELL_NAME)) {
				// Close splash screen
				/*logShellClose(shells[i]);*/
				shells[i].close();
			}
		}
	}

};
