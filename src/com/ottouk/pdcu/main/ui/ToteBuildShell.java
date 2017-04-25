package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.ToteBuildService;
import com.ottouk.pdcu.main.service.ToteBuildServiceImpl;
import com.ottouk.pdcu.main.utils.StringUtils;

public class ToteBuildShell extends GeneralShell {

	private ToteBuildService toteBuildService;
	
	private StackLayout layout;
	
	private Composite contentPanel;
	private Composite totePage;
	private Composite itemPage;

	private Label lToteId;
	private Label lItemCount;
	
	private Text tTote;
	private Text tItem;
	private Button bToteOK;
	private Button bItemOK;
	private Button bEscape;
	
	private static final String TOTE_BUILD_TITLE = "Tote Build";
	
	
	public boolean initialise(String[] args) {
		
		while (true) {
			if (logonService.startToteBuild()) {
				toteBuildService = new ToteBuildServiceImpl();
				return true;
			} else {
				if (questionBox(TOTE_BUILD_TITLE,
						"Unable to start " + TOTE_BUILD_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}
	
	public String setTitle() {
		return TOTE_BUILD_TITLE;
	}
	
	public void handleListenerEvent(Event event) {
		if (event.widget == bEscape) {
			escape();
		} else if (event.widget == bToteOK) {
			totePageSubmit();
		} else if (event.widget == bItemOK) {
			itemPageSubmit();
		}
	}
	
	public void createContents() {
		
		// create a composite for the pages to share
		contentPanel = new Composite(shell, SWT.NONE);
	    layout = new StackLayout();
	    contentPanel.setLayout(layout);
	    contentPanel.setLayoutData(shell.getLayoutData());
	    
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
	    
	    // create the tote page
	    totePage = new Composite(contentPanel, SWT.NONE);
	    totePage.setLayout(gridLayout);
	    newLabel(totePage, "Scan Tote");
	    tTote = newText(totePage, ToteBuildService.TOTE_ID_LENGTH);
		bToteOK = newButton(totePage, "OK");
		bEscape = newButton(totePage, "Escape");

	    // create the item page
	    itemPage = new Composite(contentPanel, SWT.NONE);
	    itemPage.setLayout(gridLayout);
	    lToteId = newLabel(itemPage);
	    newLabel(itemPage, "Scan Item/Tote");
		if (ToteBuildService.TOTE_ID_LENGTH > ToteBuildService.TOTE_ITEM_LENGTH) {
			tItem = newText(itemPage, ToteBuildService.TOTE_ID_LENGTH);
		} else {
			tItem = newText(itemPage, ToteBuildService.TOTE_ITEM_LENGTH);
		}
		
		bItemOK = newButton(itemPage, "OK");
		lItemCount = newLabel(itemPage);
		
		
		// set start page
	    showTotePage();
	}
	
	private void escape() {
		
		while (true) {
			if (logonService.stopToteBuild()) {
				break;
			} else {
				if (questionBox(TOTE_BUILD_TITLE,
						"Unable to stop " + TOTE_BUILD_TITLE + ".\nRetry?") == SWT.NO) {
					// Quit anyway
					break;
				} else {
					showWaitCursor();
				}
			}
		}
		
		shellClose();
	}
	
	private void totePageSubmit() {
		
		StringUtils.log(tTote.getText());
		
		if (toteBuildService.addTote(tTote.getText())) {
			// Tote added successfully
			showItemPage();
		} else {
			// Error
			
			// Check for tote having been used before
			if (toteBuildService.getReturnCode() == ToteBuildService.RC_WARN_TOTE_PREVIOUSLY_USED) {
				// Confirm user action
				if (questionBox("Scan Tote", "Warning: tote has been used before.\nAre you sure?") == SWT.YES) {
					// Add tote again
					if (toteBuildService.addToteAgain(tTote.getText())) {
						// Tote added again successfully
						showItemPage();
					} else {
						// Error adding tote again
						errorBox("Scan Tote");
					    showTotePage();
					}
					
				} else {
					// Do not add tote again
					showTotePage();
				}
				
			} else {
				if (toteBuildService.getReturnCode() == ToteBuildService.RC_WARN_MIS_SCAN) {
					beep();
				} else {
					// Show other error
				    errorBox("Scan Tote");
				}
			    showTotePage();
			}
		}
	}
	
	private void itemPageSubmit() {
		
		StringUtils.log(tItem.getText());
		
		if (toteBuildService.addScan(tItem.getText())) {
			
			if (toteBuildService.getToteItemCount() == 0) {
				showTotePage();
			} else {
				showItemPage();
			}
			
		} else {
			// Error
			
			if ((toteBuildService.getReturnCode() == ToteBuildService.RC_WARN_MIS_SCAN)
					|| (toteBuildService.getReturnCode() == ToteBuildService.RC_WARN_ITEM_PREVIOUSLY_USED)
					|| (toteBuildService.getReturnCode() == ToteBuildService.RC_WARN_TOTE_FULL)) {
				beep();
			} else {
				errorBox("Scan Item/Tote");
			}
			showItemPage();
		}
	}
	
	private void showTotePage() {

		if (layout.topControl != totePage) {
			layout.topControl = totePage;
		    contentPanel.layout();
		    shell.setDefaultButton(bToteOK);
		}
		
		tTote.setText("");
		tTote.setFocus();
	}
	
	private void showItemPage() {
		
		if (layout.topControl != itemPage) {
			layout.topControl = itemPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bItemOK);
		    lToteId.setText("Tote: " + tTote.getText());
		}
		
	    tItem.setText("");
		tItem.setFocus();
		
		lItemCount.setText("Count: " + toteBuildService.getToteItemCount());
	}
	
	protected int errorBox(String text) {
		return errorBox(text, getErrorMessage(toteBuildService.getReturnCode()));
	}
	
	private String getErrorMessage(int returnCode) {
		
		String errorMessage;

		switch (returnCode) {
		case ToteBuildService.RC_ERROR_INVALID_SCAN:
			errorMessage = "Invalid item or tote";
			break;
		case ToteBuildService.RC_ERROR_INVALID_TOTE:
			errorMessage = "Invalid tote";
			break;
		case ToteBuildService.RC_ERROR_MAX_ITEMS_IN_TOTE:
			errorMessage = "Tote full, please close";
			break;
		case ToteBuildService.RC_ERROR_TOTE_STILL_OPEN:
			errorMessage = "Please close existing tote before opening another";
			break;
		case ToteBuildService.RC_COMMS_NOT_CONNECTED:
		case ToteBuildService.RC_COMMS_TRANSACTION_ERROR:
		case ToteBuildService.RC_COMMS_RESPONSE_ERROR:
		case ToteBuildService.RC_COMMS_OTHER_ERROR:
			errorMessage = "Network communication error.\nPlease re-try.";
			break;
		default:
			errorMessage = "Unexpected tote build error: " + returnCode;
			break;
		}
		
		return errorMessage;
	}
	
}
