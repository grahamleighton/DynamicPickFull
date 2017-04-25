package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.widgets.Shell;

/**
 * Display error message and prompt user for a yes/no response.
 * 
 * @author dis065
 *
 */
public class QuestionShell extends DialogueShell {

	/**
	 * Default constructor.
	 */
	public QuestionShell() {
		super();
	}

	/**
	 * Alternate constructor.
	 * 
	 * @param args (to be passed to super.initialise() method)
	 */
	public QuestionShell(String[] args) {
		super(args);
	}

	protected Shell addButtonsToShell(final Shell shell) {
		bYes = newButton(shell, "Yes");
		bNo = newButton(shell, "No");
		return shell;
	}

}
