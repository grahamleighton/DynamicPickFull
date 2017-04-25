package com.ottouk.pdcu.main.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.PickingService;
import com.ottouk.pdcu.main.service.PickingServiceImpl;
import com.ottouk.pdcu.main.utils.StringUtils;


/**
 * Main GUI for Picking activity. 
 * Operators scan start a walk and then subsequently scan a 
 * corresponding end walk
 * @author hstd004
 *
 */

public class PickingShell extends GeneralShell {

	/**
	 * Holds Picking Class.
	 */
//	private Picking pw;
	/**
	 * GUI Object.
	 */
	private StackLayout layout;
	/**
	 * GUI Object.
	 */
	
	private Composite contentPanel;
	/**
	 * GUI Object.
	 */
	private Composite walkPage;
	
	/**
	 * GUI Object.
	 */
	private Text tWalk;
	/**
	 * GUI Object.
	 */
	private Button bOK;
	/**
	 * GUI Object.
	 */
	private Button bEscape;
	/**
	 * GUI Object.
	 */
	private Label lScanPrompt;
	/**
	 * Title for picking GUI.
	 */
	private static final String PICKING_TITLE = "Picking";
	/**
	 * creates instance of PickingService.
	 */
	private PickingService walkService;
	
	/**
	 * initialises the class.
	 * @return true if initialised correctly
	 * @param args - Arguments passed
	 */
	public final boolean initialise(final String[] args) {
		
		while (true) {
			if (logonService.startPickingWalk()) {
				walkService = new PickingServiceImpl();
				return true;
			} else {
				if (questionBox(PICKING_TITLE,
						"Unable to start " + PICKING_TITLE + ".\nRetry?") 
						== SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}
	/**
	 * Sets the screen title for Picking.
	 * @return - returns a String containing the Title
	 */
	public final String setTitle() {
		return PICKING_TITLE;
	}
	/**
	 * Handles the Picking GUI buttons.
	 * @param event - the GUI event 
	 */
	public final void handleListenerEvent(final Event event) {
		if (event.widget == bEscape) {
			escape();
		} else if (event.widget == bOK) {
			walkPageSubmit();
		} 
	}
	/**
	 * Creates the initial GUI screen for Picking.
	 */
	public final void createContents() {

		final int walkSize = 10;
		
		// create a composite for the pages to share
		contentPanel = new Composite(shell, SWT.NONE);
	    layout = new StackLayout();
	    contentPanel.setLayout(layout);
	    contentPanel.setLayoutData(shell.getLayoutData());

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
	    
	    // create the tote page
	    walkPage = new Composite(contentPanel, SWT.NONE);
	    walkPage.setLayout(gridLayout);
	    lScanPrompt = newLabel(walkPage, "Scan Start Walk");
	    tWalk = newText(walkPage, walkSize);
		bOK = newButton(walkPage, "OK");
		bEscape = newButton(walkPage, "Escape");

		
		// set start page
	    showWalkPage();
	}
	/**
	 * Handles the messages when the ESCAPE key is pressed.
	 */
	private void escape() {
		
		while (true) {
			if (logonService.stopPickingWalk()) {
				break;
			} else {
				if (questionBox(PICKING_TITLE,
						"Unable to stop " + PICKING_TITLE + ".\nRetry?") 
							== SWT.NO) {
					// Quit anyway
					break;
				} else {
					showWaitCursor();
				}
			}
		}
		
		shellClose();
	}
	/**
	 * Handles the walk data after the data has been keyed / scanned.
	 */
	private void walkPageSubmit() {
		
		StringUtils.log(tWalk.getText());
		
		if (!walkService.validWalkScan(tWalk.getText())) {
			errorBox("Picking", "Invalid Walk Scan");
			tWalk.setText("");
			tWalk.setFocus();
			return;
		}
		
		
		int status = walkService.processWalkScan(tWalk.getText());
		StringUtils.log("ProcessWalkScan Status");
		StringUtils.log(status);
		if (status != PickingService.RC_WALK_ADDED_OK) {
			errorBox("Picking", walkService.getErrorMsg(status));
			tWalk.setText("");
			tWalk.setFocus();
		} else {
			String msg = "";
			final int eight  = 8;
			lScanPrompt.setText("Scan Start Walk");
			if (walkService.getWalk().compareTo("") != 0) {
				msg = walkService.buildMessage('1');
				StringUtils.log(msg);
				lScanPrompt.setText("Scan End Walk");
				StringUtils.log("Walk in class is  ");
				StringUtils.log(walkService.getWalk());
			} else {
				String w = tWalk.getText().substring(0, eight);
				msg = walkService.buildMessage(w, '9');
				StringUtils.log(msg);
			}
			if (msg.length() > 0) {
				if (!walkService.sendMessage(msg)) {
					errorBox("Picking", "Send Error");
				}
			}
		}
		tWalk.setText("");
		tWalk.setFocus();
	}
	
	/**
	 * Causes the Walk prompt page to appear.
	 */
	private void showWalkPage() {

		if (layout.topControl != walkPage) {
			layout.topControl = walkPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bOK);
		}
		
		tWalk.setText("");
		tWalk.setFocus();
	}
	
	
	
}
