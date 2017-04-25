package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Abstract display class. Superclass for all Shells, contains application
 * display.
 * 
 * @author dis065
 * 
 */
public abstract class GeneralDisplay {

	/**
	 * Display.
	 */
	protected static final Display DISPLAY = createDisplay();

	
	/**
	 * Protected empty constructor.
	 */
	protected GeneralDisplay() {
	};

	
	/**
	 * Instruct device to "beep".
	 */
	protected static void beep() {
		if (DISPLAY != null) {
			DISPLAY.beep();
		}
	}
	
	/**
	 * @param shell shell
	 */
	protected static void logShellOpen(Shell shell) {
		StringUtils.log("Open : " + shell.getText() + "@" + shell.handle);
	}
	
	/**
	 * @param shell shell
	 */
	protected static void logShellClose(Shell shell) {
		StringUtils.log("Close: " + shell.getText() + "@" + shell.handle);
	}

	/**
	 * Show hour glass cursor (for active shell).
	 */
	protected static void showWaitCursor() {
		if (DISPLAY != null && !DISPLAY.isDisposed()) {
			showWaitCursor(DISPLAY.getActiveShell());
		}
	}
	
	/**
	 * Show hour glass cursor.
	 * 
	 * @param shell
	 *            shell
	 */
	protected static void showWaitCursor(Shell shell) {
		if (shell != null && !shell.isDisposed()) {
			shell.setCursor(new Cursor(DISPLAY, SWT.CURSOR_WAIT));
		}
	}
	
	/**
	 * Hide hour glass cursor (for all shells).
	 */
	protected static void hideCursor() {
		
		if (DISPLAY != null && !DISPLAY.isDisposed()) {

			Shell[] shells = DISPLAY.getShells();
			for (int i = 0; i < shells.length; i++) {
				hideCursor(shells[i]);
			}
		}
	}
	
	/**
	 * Hide hour glass cursor.
	 * 
	 * @param shell
	 *            shell
	 */
	protected static void hideCursor(Shell shell) {
		if (shell != null && !shell.isDisposed()) {
			shell.setCursor(null);
		}
	}

	/**
	 * @return new display
	 */
	private static Display createDisplay() {
		Display display = new Display();
		StringUtils.log("Create: " + display, true);

		return display;
	}

	/**
	 * Dispose of DISPLAY and any attached shells/widgets.
	 */
	public static void disposeDisplay() {
		if (DISPLAY != null && !DISPLAY.isDisposed()) {
			DISPLAY.dispose();

			StringUtils.log("Dispose: " + DISPLAY, true);
		}
	}

}
