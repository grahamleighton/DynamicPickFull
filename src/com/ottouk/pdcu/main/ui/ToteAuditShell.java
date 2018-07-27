package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.LocationDetailService;
import com.ottouk.pdcu.main.service.LocationDetailServiceImpl;
import com.ottouk.pdcu.main.service.ToteAuditServiceImpl;
import com.ottouk.pdcu.main.service.ToteAuditService;
import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;


/**
 * GUI for Tote Audit.
 * @author hstd004
 *
 */
public class ToteAuditShell extends GeneralShell {

	/**
	 * Instance of ToteAuditService.
	 */
	private ToteAuditService toteAuditService;
	/***
	 * Instance of LocationDetailService
	 */
	private LocationDetailService locService;
	/**
	 * layout for GUI.
	 */
	private StackLayout layout;
	/**
	 * contentpanel for GUI.
	 */
	private Composite contentPanel;
	/**
	 * Page for collecting Tote ID.
	 */
	private Composite totePage;
	/**
	 * Page for collecting items in tote.
	 */
	private Composite itemPage;
	/**
	 * Page for requesting initial location.
	 */
	private Composite pPIStart;
	/**
	 * Page for confirming at correct location.
	 */
	private Composite confirmLocPage;

	/**
	 * Label for Tote ID.
	 */
	private Label lToteId;
	/**
	 * Label to provide item count information.
	 */
	private Label lItemCount;
	/**
	 * Label to provide confirm location message. 
	 */
	private Label lConfirm;
	/**
	 * text box to save Tote ID.
	 */
	private Text tTote;
	/**
	 * text box to collect tote items.
	 */
	private Text tItem;
	/**
	 * text box to receive initial location.
	 */
	private Text tPIStart;
	/**
	 * text box to confirm location.
	 */
	private Text tConfirm;
	/**
	 * button on tote collect screen to indicate tote id entered.
	 */
	private Button bToteOK;
	/**
	 * button on tote item screen to indicate tote item entered.
	 */
	private Button bItemOK;
	/**
	 * button on tote screen to go back to previous screen.
	 */
	private Button bEscape;
	/**
	 * button on tote item collection screen to indicate end of item scanning.
	 */
	private Button bDone;
	/**
	 * button on location request screen to indicate location keyed.
	 */
	private Button bPIStartOK;
	/**
	 * button on location request screen to exit back out to menu.
	 */
	private Button bPIStartEscape;
	/**
	 * button on location confirmation screen to indicate location entered.
	 */
	private Button bConfirmOK;
	/**
	 * button on location confirmation screen to go back to previous screen.
	 */
	private Button bConfirmEscape;
	
	/**
	 * Title of Audit function for all screens.
	 */
	private static final String TOTE_AUDIT_TITLE = "Tote Audit";
	
	/**
	 * initialise routine that overrides GeneralShell.
	 * @param args - arguments passed to class
	 * @return true if initialise runs without errors
	 */
	public final boolean initialise(final String[] args) {
		
		while (true) {
			if (logonService.startToteAudit()) {
				
				StringUtils.log(TOTE_AUDIT_TITLE + " started");
				locService = new LocationDetailServiceImpl();
				toteAuditService = new ToteAuditServiceImpl();
				return true;
			} else {
				if (questionBox(TOTE_AUDIT_TITLE,
						"Unable to start " + TOTE_AUDIT_TITLE + ".\nRetry?") 
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
	 * Returns the main screen title.
	 * @return String of title
	 */
	public final String setTitle() {
		return TOTE_AUDIT_TITLE;
	}
	/**
	 * Listener for Tote Audit GUI.
	 * @param event - Event that triggered the call.
	 */
	public final void handleListenerEvent(final Event event) {
		if (event.widget == bEscape) {
			escape();
		} else if (event.widget == bToteOK) {
			submitTotePage();
		} else if (event.widget == bItemOK) {
			submitItemPage();
		} else if (event.widget == bDone) {
						
			if  (!toteAuditService.audit()) {
				errorBox("Unexpected response");
			} else { 
			
				if (toteAuditService.isLocationAvailable()) {
					showLocationConfirmPage();
				} else {	
					infoBox("Tote Audit", "All locations PI'd");
					showStartPage();
				}
			}
			
		} else if (event.widget == bPIStartOK) {
			submitStartPage();
		} else if (event.widget == bPIStartEscape) {
			escape();
		} else if (event.widget == bConfirmEscape) {
			showStartPage();
		} else if (event.widget == bConfirmOK) {
			submitLocationConfirmPage();
		}
		
	}
	/**
	 * Creates the GUI interface and controls.
	 */
	public final void createContents() {
		
		// create a composite for the pages to share
		contentPanel = new Composite(shell, SWT.NONE);
	    layout = new StackLayout();
	    contentPanel.setLayout(layout);
	    contentPanel.setLayoutData(shell.getLayoutData());
	    
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
	    
		// create the location request page
	    pPIStart = new Composite(contentPanel, SWT.NONE);
	    pPIStart.setLayout(gridLayout);
	    newLabel(pPIStart, "Scan any start location");
	    tPIStart = newText(pPIStart, MainConstants.LOCATION_LENGTH);
		bPIStartOK = newButton(pPIStart, "OK");
		bPIStartEscape = newButton(pPIStart, "Escape");

		// create the location confirmation page
		confirmLocPage = new Composite(contentPanel, SWT.NONE);
		confirmLocPage.setLayout(gridLayout);
		lConfirm = newLabel(confirmLocPage , "");
		tConfirm = newText(confirmLocPage , MainConstants.LOCATION_LENGTH);
		bConfirmOK = newButton(confirmLocPage , "OK");
		bConfirmEscape = newButton(confirmLocPage , "Escape");
		
		
		
		// create the tote page
	    totePage = new Composite(contentPanel, SWT.NONE);
	    totePage.setLayout(gridLayout);
	    newLabel(totePage, "Scan Tote");
	    tTote = newText(totePage, MainConstants.TOTE_ID_LENGTH);
		bToteOK = newButton(totePage, "OK");
		bEscape = newButton(totePage, "Escape");

	    // create the item page
	    itemPage = new Composite(contentPanel, SWT.NONE);
	    itemPage.setLayout(gridLayout);
	    lToteId = newLabel(itemPage);
	    newLabel(itemPage, "Scan Item");
		tItem = newText(itemPage, MainConstants.TOTE_ITEM_LENGTH);
		
		bItemOK = newButton(itemPage, "OK");
		bDone   = newButton(itemPage, "Done");
		
		lItemCount = newLabel(itemPage);
		
		showStartPage();
	}
	
	/**
	 * Standard error message routine.
	 * @return return code from errorBox in GeneralShell
	 * @param text Text to be displayed to user
	 */
	protected final int errorBox(final String text) {
		return (errorBox("Tote Audit Error", text));
	}
	/**
	 * Displays information to the user.
	 * @param title Title of screen.
	 * @param text Information to convey.
	 */
	private void infoBox(final String title, final String text) {
		errorBox(title, text);
	}
	/**
	 * Tidy up routine when user is escaping from Audit fucntion.
	 */
	private void escape() {
		
		while (true) {
			if (logonService.stopToteAudit()) {
				StringUtils.log(TOTE_AUDIT_TITLE + " stopped");
				break;
			} else {
				if (questionBox(TOTE_AUDIT_TITLE,
						"Unable to stop " + TOTE_AUDIT_TITLE + ".\nRetry?") 
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
	 * Shows an information box indicating that no locations are available.
	 * This can occur as a result of the initial scan i.e. there were no 
	 * locations to PI in this zone OR the user has completed all the 
	 * outstanding PI's in the zone
	 *  
	 */
	protected final void noLocationsBox() {
		errorBox("PI Request", "No Locations In Zone");
	}
	/**
	 * Shows the tote items collection screen.
	 */
	private void showItemPage() {
	
		if (layout.topControl != itemPage) {
			layout.topControl = itemPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bItemOK);
		    lToteId.setText("Tote : " + tTote.getText());
		}
		
	    tItem.setText("");
		tItem.setFocus();
		
		lItemCount.setText("Count: " + toteAuditService.getToteItemCount());
	}
	/**
	 * Shows the location on screen and prompts user to scan location.
	 */
	private void showLocationConfirmPage() {
		
		
		lConfirm.setText("Scan " + toteAuditService.getAlphaLocation());
	
		if (layout.topControl != confirmLocPage) {
			layout.topControl = confirmLocPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bConfirmOK);
		}
		
		tConfirm.setText("");
		tConfirm.setFocus();
		
	}
	

	/**
	 * Shows the initial screen to collect the location starting point.
	 */
	private void showStartPage() {

		if (layout.topControl != pPIStart) {
			layout.topControl = pPIStart;
		    contentPanel.layout();
		    shell.setDefaultButton(bPIStartOK);
		}
		
		tPIStart.setText("");
		tPIStart.setFocus();
	}
	/**
	 * Shows the Tote ID collection screen.
	 */
	private void showTotePage() {

		if (layout.topControl != totePage) {
			layout.topControl = totePage;
		    contentPanel.layout();
		    shell.setDefaultButton(bToteOK);
		}
		
		tTote.setText("");
		tTote.setFocus();
	}
	
	/**
	 * Transacts with server to get the first location to PI.  
	 * if server responds with a location will prompt user to
	 * visit that location.
	 * If server responds with no location will inform user that 
	 * no locations are available
	 */
	private void submitStartPage() {
		
		String loc = tPIStart.getText();
		
		if ( loc.length() > 0 )
		{
			if ( locService.isAlphaLocationValid(loc) ) {
				// Convert to numeric
				if ( locService.getLocationDetails(loc) ) {
					loc = locService.getNumericLocation8();
				}
			}
		}
		
		
		if (toteAuditService.locationRequest(loc)) {
		
			showLocationConfirmPage();
		} else {
			noLocationsBox();
			showStartPage();
		}
	}
	/**
	 * Adds the tote item scanned to the Tote.
	 */
	private void submitItemPage() {
		
		
		
		if (toteAuditService.addItem(tItem.getText())) {
				showItemPage();
		} else {
			// Error
			
			if ((toteAuditService.getReturnCode() 
					== ToteAuditService.RC_WARN_MIS_SCAN)
			|| (toteAuditService.getReturnCode()  
					== ToteAuditService.RC_WARN_ITEM_PREVIOUSLY_USED)) {
				beep();
			} else {
				errorBox("Scan Item/Tote");
			}
			showItemPage();
		}
	}
	/**
	 * Called to validate the location entered.
	 * if valid will call the Tote ID scan page
	 * if not will error and re-show Location confirmation page
	 */
	private void submitLocationConfirmPage() {
		
		String loc = tConfirm.getText();
		
		if ( loc.length() > 0 )
		{
			if ( locService.isAlphaLocationValid(loc) ) {
				// Convert to numeric
				if ( locService.getLocationDetails(loc) ) {
					loc = locService.getNumericLocation8();
				}
			}
		}
		
		if (!toteAuditService.validatePILocation(loc)) {
			errorBox("Wrong Location");
			showLocationConfirmPage();
		} else {
			// correct location , prompt for tote id
			showTotePage();
		}
	}
	/**
	 * Called to validate the Tote ID scanned.
	 * if valid will call the collect item page
	 * if not will re-show the tote id collection page
	 */
	private void submitTotePage() {
		
		
		toteAuditService.setToteId(tTote.getText());
		if (toteAuditService.validatePITote(tTote.getText())) {
			showItemPage();
		} else {
			errorBox("Tote not valid");
			showTotePage();
		}
	}
	
}
