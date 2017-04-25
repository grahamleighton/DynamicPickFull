package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.TotePutawayService;
import com.ottouk.pdcu.main.service.TotePutawayServiceImpl;

public class TotePutawayShell extends GeneralShell {

	private TotePutawayService totePutawayService;
	
	private StackLayout layout;
	
	private Composite contentPanel;
	private Composite pPutawayStart;
	private Composite pPutawayTote;
	private Composite pPutawayLocation;
	private Composite pPILocation;
	private Composite pPITote;
	private Composite pPIItems;

	private Label lPutawayTote;
	private Label lPutawayLocation;
	private Label lPILocation;
	private Label lPITote;
	private Label lPIItemsTote;
	private Label lPIItemsCount;
	
	private Text tPutawayStart;
	private Text tPutawayTote;
	private Text tPutawayLocation;
	private Text tPILocation;
	private Text tPITote;
	private Text tPIItems;
	
	private Button bPutawayStartOK;
	private Button bPutawayStartEscape;
	private Button bPutawayToteOK;
	private Button bPutawayTotePI;
	private Button bPutawayToteEscape;
	private Button bPutawayLocationOK;
	private Button bPutawayLocationEscape;
	private Button bPILocationOK;
	private Button bPILocationEscape;
	private Button bPIToteOK;
	private Button bPIToteEscape;
	private Button bPIItemsOK;
	private Button bPIItemsDone;
	
	private static final String TOTE_PUTAWAY_TITLE = "Tote Putaway";
	private static final String TOTE_PUTAWAY_PI_TITLE = "Tote Putaway - PI";
	
	
	public boolean initialise(String[] args) {
		
		while (true) {
			if (logonService.startTotePutaway()) {
				totePutawayService = new TotePutawayServiceImpl();
				return true;
			} else {
				if (questionBox(TOTE_PUTAWAY_TITLE,
						"Unable to start " + TOTE_PUTAWAY_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}
	
	public String setTitle() {
		return TOTE_PUTAWAY_TITLE;
	}
	
	public void handleListenerEvent(Event event) {
		if (event.widget == bPutawayStartOK) {
			submitPPutawayStart();
		} else if (event.widget == bPutawayStartEscape) {
			escape();
		} else if (event.widget == bPutawayToteOK) {
			submitPPutawayTote();
		} else if (event.widget == bPutawayToteEscape) {
			showPPutawayStart();
		} else if (event.widget == bPutawayTotePI) {
			showPPILocation();
		} else if (event.widget == bPutawayLocationOK) {
			submitPPutawayLocation();
		} else if (event.widget == bPutawayLocationEscape) {
			showPPutawayTote();
		} else if (event.widget == bPILocationOK) {
			submitPPILocation();
		} else if (event.widget == bPILocationEscape) {
			showPPutawayTote();
		} else if (event.widget == bPIToteOK) {
			submitPPITote();
		} else if (event.widget == bPIToteEscape) {
			showPPILocation();
		} else if (event.widget == bPIItemsOK) {
			submitPPIItemsOK();
		} else if (event.widget == bPIItemsDone) {
			submitPPIItemsDone();
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
		
	    // create page pPutawayStart
	    pPutawayStart = new Composite(contentPanel, SWT.NONE);
	    pPutawayStart.setLayout(gridLayout);
	    newLabel(pPutawayStart, "Scan any start location");
	    tPutawayStart = newText(pPutawayStart, TotePutawayService.LOCATION_LENGTH);
		bPutawayStartOK = newButton(pPutawayStart, "OK");
		bPutawayStartEscape = newButton(pPutawayStart, "Escape");
		
	    // create page pPutawayTote
	    pPutawayTote = new Composite(contentPanel, SWT.NONE);
	    pPutawayTote.setLayout(gridLayout);
	    lPutawayTote = newLabel(pPutawayTote);
	    tPutawayTote = newText(pPutawayTote, TotePutawayService.TOTE_ID_LENGTH);
		bPutawayToteOK = newButton(pPutawayTote, "OK");
		bPutawayTotePI = newButton(pPutawayTote, "PI");
		bPutawayToteEscape = newButton(pPutawayTote, "Escape");
		
	    // create page pPutawayLocation
	    pPutawayLocation = new Composite(contentPanel, SWT.NONE);
	    pPutawayLocation.setLayout(gridLayout);
	    lPutawayLocation = newLabel(pPutawayLocation);
	    tPutawayLocation = newText(pPutawayLocation, TotePutawayService.LOCATION_LENGTH);
		bPutawayLocationOK = newButton(pPutawayLocation, "OK");
		bPutawayLocationEscape = newButton(pPutawayLocation, "Escape");
	    
		// create page pPILocation
		pPILocation = new Composite(contentPanel, SWT.NONE);
		pPILocation.setLayout(gridLayout);
		lPILocation = newLabel(pPILocation);
		tPILocation = newText(pPILocation, TotePutawayService.LOCATION_LENGTH);
		bPILocationOK = newButton(pPILocation, "OK");
		bPILocationEscape = newButton(pPILocation, "Escape");
		
		// create page pPITote
		pPITote = new Composite(contentPanel, SWT.NONE);
		pPITote.setLayout(gridLayout);
		lPITote = newLabel(pPITote);
		tPITote = newText(pPITote, TotePutawayService.TOTE_ID_LENGTH);
		bPIToteOK = newButton(pPITote, "OK");
		bPIToteEscape = newButton(pPITote, "Escape");
		
	    // create page pPIItems
	    pPIItems = new Composite(contentPanel, SWT.NONE);
	    pPIItems.setLayout(gridLayout);
	    lPIItemsTote = newLabel(pPIItems);
	    newLabel(pPIItems, "SCAN ALL ITEMS");
	    tPIItems = newText(pPIItems, TotePutawayService.TOTE_ITEM_LENGTH);
		bPIItemsOK = newButton(pPIItems, "OK");
		bPIItemsDone = newButton(pPIItems, "Done");
		lPIItemsCount = newLabel(pPIItems);

		
		// set start page
	    showPPutawayStart();
	}
	
	private void escape() {
		
		while (true) {
			if (logonService.stopTotePutaway()) {
				break;
			} else {
				if (questionBox(TOTE_PUTAWAY_TITLE,
						"Unable to stop " + TOTE_PUTAWAY_TITLE + ".\nRetry?") == SWT.NO) {
					// Quit anyway
					break;
				} else {
					showWaitCursor();
				}
			}
		}
		
		shellClose();
	}
	
	private void submitPPutawayStart() {
		
		if (totePutawayService.locationRequest(tPutawayStart.getText())) {
			showPPutawayTote();
		} else {
			errorBox();
			showPPutawayStart();
		}
	}

	private void submitPPutawayTote() {
		
		if (totePutawayService.validatePutawayTote(tPutawayTote.getText())) {
			showPPutawayLocation();
		} else {
			errorBox();
			showPPutawayTote();
		}
	}
	
	private void submitPPutawayLocation() {

		if (totePutawayService.putaway(tPutawayLocation.getText())) {
			
			if (totePutawayService.locationFollows()) {
				showPPutawayTote();
			} else {
				showPPutawayStart();
			}
			
		} else {
			
			int rc = totePutawayService.getReturnCode();
			errorBox(getErrorMessage(rc));
			if (rc == TotePutawayService.RC_ERROR_WRONG_LOCATION) {
				showPPutawayLocation();
			} else {
				showPPutawayTote();
			}
			
		}
	}
	
	private void submitPPILocation() {

		if (totePutawayService.validatePILocation(tPILocation.getText())) {
			showPPITote();
		} else {
			errorBox();
			showPPILocation();
		}
	}
	
	private void submitPPITote() {

		if (totePutawayService.validatePITote(tPITote.getText())) {
			showPPIItems();
		} else {
			errorBox();
			showPPITote();
		}
	}
	
	private void submitPPIItemsDone() {
		
		if (totePutawayService.piRequest()) {
			
			if (totePutawayService.piLocationFollows()) {
				showPPutawayTote();
			} else {
				errorBox();
				showPPutawayStart();
			}
			
		} else {
			errorBox();
			showPPIItems();
		}
	}
	
	private void submitPPIItemsOK() {
		
		if (!totePutawayService.addItem(tPIItems.getText())) {
			errorBox();
		}
		
		showPPIItems();
	}
	
	private void showPPutawayStart() {

		if (layout.topControl != pPutawayStart) {
			layout.topControl = pPutawayStart;
		    contentPanel.layout();
		    shell.setDefaultButton(bPutawayStartOK);
		    resetTitle(TOTE_PUTAWAY_TITLE);
		}
		
		tPutawayStart.setText("");
		tPutawayStart.setFocus();
	}
	
	private void showPPutawayTote() {

		if (layout.topControl != pPutawayTote) {
			layout.topControl = pPutawayTote;
		    contentPanel.layout();
		    shell.setDefaultButton(bPutawayToteOK);
		    resetTitle(TOTE_PUTAWAY_TITLE);
		    
		    /*lPutawayTote.setText("Scan tote into " 
		    		+ totePutawayService.getAlphaLocation());*/
		    lPutawayTote.setText("Scan into " 
		    		+ totePutawayService.getAlphaLocation());
		}
		
		tPutawayTote.setText("");
		tPutawayTote.setFocus();
	}
	
	private void showPPutawayLocation() {

		if (layout.topControl != pPutawayLocation) {
			layout.topControl = pPutawayLocation;
		    contentPanel.layout();
		    shell.setDefaultButton(bPutawayLocationOK);
		    resetTitle(TOTE_PUTAWAY_TITLE);
		    
		    lPutawayLocation.setText("Scan location " 
		    		+ totePutawayService.getAlphaLocation());
		}
		
		tPutawayLocation.setText("");
		tPutawayLocation.setFocus();
	}
	
	private void showPPILocation() {

		if (layout.topControl != pPILocation) {
			layout.topControl = pPILocation;
		    contentPanel.layout();
		    shell.setDefaultButton(bPILocationOK);
		    resetTitle(TOTE_PUTAWAY_PI_TITLE);
		    
		    lPILocation.setText("Scan location "
					+ totePutawayService.getAlphaLocation());
		}
		
		tPILocation.setText("");
		tPILocation.setFocus();
	}
	
	private void showPPITote() {

		if (layout.topControl != pPITote) {
			layout.topControl = pPITote;
		    contentPanel.layout();
		    shell.setDefaultButton(bPIToteOK);
		    resetTitle(TOTE_PUTAWAY_PI_TITLE);
		    
		    lPITote.setText("Scan tote in "
					+ totePutawayService.getAlphaLocation());
		}
		
		tPITote.setText("");
		tPITote.setFocus();
	}
	
	private void showPPIItems() {

		if (layout.topControl != pPIItems) {
			layout.topControl = pPIItems;
		    contentPanel.layout();
		    shell.setDefaultButton(bPIItemsOK);
		    resetTitle(TOTE_PUTAWAY_PI_TITLE);
		    
		    lPIItemsTote.setText("Tote: " + totePutawayService.getToteId());
		}
		
		lPIItemsCount.setText("Count: " 
				+ totePutawayService.getToteItemCount());
		
		tPIItems.setText("");
		tPIItems.setFocus();
	}
	
	private int errorBox() {
		return errorBox(getErrorMessage(totePutawayService.getReturnCode()));
	}
	
	private String getErrorMessage(int returnCode) {
		
		String errorMessage;

		switch (returnCode) {
		case TotePutawayService.RC_ERROR_LOCATION_NOT_VALID:
			errorMessage = "Location not valid";
			break;
		case TotePutawayService.RC_ERROR_NO_LOCATION_FOUND:
			errorMessage = "No free locations in this zone";
			break;
		case TotePutawayService.RC_ERROR_WRONG_LOCATION:
			errorMessage = "Wrong location";
			break;
		case TotePutawayService.RC_ERROR_INVALID_TOTE:
			errorMessage = "Invalid tote";
			break;
		case TotePutawayService.RC_ERROR_TOTE_NOT_FOUND:
			errorMessage = "Tote not found.\nPlease rebuild.";
			break;
		case TotePutawayService.RC_ERROR_NO_FOLLOWING_LOCATION:
			errorMessage = "No more free locations in this zone";
			break;
		case TotePutawayService.RC_ERROR_INVALID_ITEM:
			errorMessage = "Invalid item";
			break;
		case TotePutawayService.RC_ERROR_MAX_ITEMS_IN_TOTE_PUTAWAY:
			errorMessage = "Tote full, please close";
			break;
		case TotePutawayService.RC_COMMS_NOT_CONNECTED:
		case TotePutawayService.RC_COMMS_TRANSACTION_ERROR:
		case TotePutawayService.RC_COMMS_RESPONSE_ERROR:
		case TotePutawayService.RC_COMMS_OTHER_ERROR:
			errorMessage = "Network communication error.\nPlease re-try.";
			break;
		default:
			errorMessage = "Unexpected tote putaway error: " + returnCode;
			break;
		}

		return errorMessage;
	}

}
