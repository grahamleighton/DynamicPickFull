package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.widgets.Shell;

/**
 * Display error message and await user acknowledgement.
 * 
 * @author dis065
 * 
 */
public class ErrorShell extends DialogueShell {

	/**
	 * Default constructor.
	 */
	public ErrorShell() {
		super();
	}

	/**
	 * Alternate constructor.
	 * 
	 * @param args
	 *            (to be passed to super.initialise() method)
	 */
	public ErrorShell(String[] args) {
		super(args);
	}

	protected Shell addButtonsToShell(Shell shell) {
		bOK = newButton(shell, "OK");
		return shell;
	}

}
