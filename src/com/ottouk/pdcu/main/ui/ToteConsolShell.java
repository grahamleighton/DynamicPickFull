package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.ToteConsolServiceImpl;
import com.ottouk.pdcu.main.service.ToteConsolService;
import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;


/**
 * GUI for Tote Consol.
 * @author hstd004
 *
 */
public class ToteConsolShell extends GeneralShell {

	/**
	 * Instance of ToteConsolService.
	 */
	private ToteConsolService toteConsolService;
	/**
	 * layout for GUI.
	 */
	private StackLayout layout;
	/**
	 * contentpanel for GUI.
	 */
	private Composite contentPanel;

	
	/**
	 * Page for requesting initial location.
	 */
	private Composite pConsolStart;    
	/** Label to show "Find Location". */
	/** Button to represent OK. */
	private Button bStartOK;
	/** Button to represent Escape. */
	private Button bStartEscape;
	/** Button to allow escape from tote swap */
	private Button bToteSwapEscape;
	
	
	
	/**
	 * Page for collecting Empty Tote ID.
	 */
	private Composite pEmptyTote;
	/**
	 * Page for confirming at correct location.
	 */
	private Composite pConfirmLoc;
	/**
	 * Page for collecting items in tote.
	 */
	private Composite pItemPage;
	/**
	 * Page for collecting Tote ID in location.
	 */
	private Composite pLocatedTote;
	/**
	 * Page for swapping totes.
	 */
	private Composite pSwapPage;
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
	 * Label to provide swap location. 
	 */
	private Label lSwap;
	/**
	 * text box to save Tote ID.
	 */
	private Text tTote;
	/**
	 * text box to save empty Tote ID.
	 */
	private Text tEmptyTote;
	/**
	 * text box to collect tote items.
	 */
	private Text tItem;
	/**
	 * text box to receive initial location.
	 */
	private Text tConsolStart;
	/**
	 * text box to confirm location.
	 */
	private Text tConfirm;
	/**
	 * Text to check swap location confirmation.
	 */
	private Text tSwap;;
	/**
	 * button on tote item collect screen to signify tote swap.
	 */
	private Button bToteSwap;
	/**
	 * button on tote collect screen to indicate tote id entered.
	 */
	private Button bToteOK;
	/**
	 * button on tote collect screen to indicate escape.
	 */
	private Button bToteEscape;
	/**
	 * button on empty tote collect screen to indicate tote id entered.
	 */
	private Button bEmptyToteOK;
	/**
	 * button on empty tote collect screen to indicate escape.
	 */
	private Button bEmptyToteEscape;
	/**
	 * button on tote item screen to indicate tote item entered.
	 */
	private Button bItemOK;
	/**
	 * button on tote swap screen.
	 */
	private Button bSwapOK;
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
	private static final String TOTE_CONSOL_TITLE = "Tote Consol";
	
	/**
	 * initialise routine that overrides GeneralShell.
	 * @param args - arguments passed to class
	 * @return true if initialise runs without errors
	 */
	public final boolean initialise(final String[] args) {
		
		while (true) {
			if (logonService.startToteConsol()) {
				
				StringUtils.log(TOTE_CONSOL_TITLE + " started");
				toteConsolService = new ToteConsolServiceImpl();
				return true;
			} else {
				if (questionBox(TOTE_CONSOL_TITLE,
						"Unable to start " + TOTE_CONSOL_TITLE + ".\nRetry?") 
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
		return TOTE_CONSOL_TITLE;
	}
	/**
	 * Listener for Tote Consol GUI.
	 * @param event - Event that triggered the call.
	 */
	public final void handleListenerEvent(final Event event) {
		if (event.widget == bStartEscape) {
			escape();
		} else if (event.widget == bStartOK) {
			startPageSubmit();
		} else if (event.widget == bConfirmOK) {
			confirmLocationSubmit();
		} else if (event.widget == bToteSwap) {
			showToteSwapPage();
		} else if (event.widget == bToteSwapEscape) {
			if (toteConsolService.getTrolleyTote().length() 
					== MainConstants.TOTE_ID_LENGTH) {
				/*
				 * There's an empty tote sitting in 
				 * totebuildservice object so we need
				 * to end it even though no items.
				 */
				System.out.println("Tidying up");
				toteConsolService.setTrolleyTote(
						toteConsolService.getTrolleyTote());
			}
			showConsolStartPage();
		} else if (event.widget == bConfirmEscape) {
			if (toteConsolService.getTrolleyToteItemCount() > 0) {
				showToteSwapPage();
			} else {
				if (toteConsolService.getTrolleyTote().length() 
						== MainConstants.TOTE_ID_LENGTH) {
					/*
					 * There's an empty tote sitting in 
					 * totebuildservice object so we need
					 * to end it even though no items.
					 */
					System.out.println("Tidying up");
					toteConsolService.setTrolleyTote(
							toteConsolService.getTrolleyTote());
				}
				showConsolStartPage();
			}
		} else if (event.widget == bEmptyToteOK) {
			emptyToteSubmit();
		} else if (event.widget == bEmptyToteEscape) {
			showConsolStartPage();
		} else if (event.widget == bToteOK) {
			totePageSubmit();
		} else if (event.widget == bToteEscape) {
			showLocationConfirmPage();
		} else if (event.widget == bItemOK) {
			itemPageSubmit();
		} else if (event.widget == bSwapOK) {
			toteSwapSubmit();
		}
/*
		else if (event.widget == bItemOK) {
			itemPageSubmit();
		} else if (event.widget == bDone) {
			StringUtils.log("Done button pressed");
		}
			if  (!toteAuditService.audit()) {
				errorBox("Unexpected response");
			} else { 
				if (toteConsolService.getLocationObj().isLocationAvailable()) {
					showLocationConfirmPage();
				} else {	
					infoBox("Tote Audit", "All locations PI'd");
					showPIStartPage();
				}
			}
			
		} else if (event.widget == bConsolStartOK) {
			startPageSubmit();
		} else if (event.widget == bConsolStartEscape) {
			escape();
		} else if (event.widget == bConfirmEscape) {
			showPIStartPage();
		} else if (event.widget == bConfirmOK) {
			confirmPageSubmit();
		}
		*/
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
	    pConsolStart = new Composite(contentPanel, SWT.NONE);
	    pConsolStart.setLayout(gridLayout);
	    newLabel(pConsolStart, "Scan any start location");
	    tConsolStart = newText(pConsolStart, MainConstants.LOCATION_LENGTH);
		bStartOK = newButton(pConsolStart, "OK");
		bStartEscape = newButton(pConsolStart, "Escape");

		// create the location confirm page
	    pConfirmLoc = new Composite(contentPanel, SWT.NONE);
	    pConfirmLoc.setLayout(gridLayout);
	    lConfirm = newLabel(pConfirmLoc, "");
	    tConfirm = newText(pConfirmLoc, MainConstants.LOCATION_LENGTH);
		bConfirmOK = newButton(pConfirmLoc, "OK");
		bConfirmEscape = newButton(pConfirmLoc, "Escape");


		// create the tote id collection page
	    pLocatedTote = new Composite(contentPanel, SWT.NONE);
	    pLocatedTote.setLayout(gridLayout);
	    newLabel(pLocatedTote, "Scan Tote");
	    tTote = newText(pLocatedTote, MainConstants.TOTE_ID_LENGTH);
		bToteOK = newButton(pLocatedTote, "OK");
		bToteEscape = newButton(pLocatedTote, "Escape");
		
		// create the empty tote id collection page
	    pEmptyTote = new Composite(contentPanel, SWT.NONE);
	    pEmptyTote.setLayout(gridLayout);
	    newLabel(pEmptyTote, "Scan Empty Tote");
	    tEmptyTote = newText(pEmptyTote, MainConstants.TOTE_ID_LENGTH);
		bEmptyToteOK = newButton(pEmptyTote, "OK");
		bEmptyToteEscape = newButton(pEmptyTote, "Escape");

		// create the item collection page
	    pItemPage = new Composite(contentPanel, SWT.NONE);
	    pItemPage.setLayout(gridLayout);
	    lToteId = newLabel(pItemPage, "");
	    newLabel(pItemPage, "Scan Items / Tote");
	    tItem = newText(pItemPage, MainConstants.TOTE_ID_LENGTH);
	    lItemCount = newLabel(pItemPage, "Count : 0");
		bItemOK = newButton(pItemPage, "OK");
		bToteSwap = newButton(pItemPage,"SWAP");
		
		// create the swap page
	    pSwapPage = new Composite(contentPanel, SWT.NONE);
	    pSwapPage.setLayout(gridLayout);
	    newLabel(pSwapPage, "Swap Totes");
	    lSwap = newLabel(pSwapPage, "");
	    tSwap = newText(pSwapPage, MainConstants.LOCATION_LENGTH);
		bSwapOK = newButton(pSwapPage, "OK");
		bToteSwapEscape = newButton(pSwapPage, "Escape");

		showConsolStartPage();
	}
	/**
	 * Standard error message routine.
	 * @return return code from errorBox in GeneralShell
	 * @param text Text to be displayed to user
	 */
	protected final int errorBox(final String text) {
		return (errorBox("Tote Consol Error", text));
	}
	/**
	 * Tidy up routine when user is escaping from Audit fucntion.
	 */
	private void escape() {
		
		while (true) {
			if (logonService.stopToteConsol()) {
				StringUtils.log(TOTE_CONSOL_TITLE + " stopped");
				break;
			} else {
				if (questionBox(TOTE_CONSOL_TITLE,
						"Unable to stop " + TOTE_CONSOL_TITLE + ".\nRetry?") 
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
	 * Shows a message saying that all Consol locations have been 
	 * completed for this zone. Can also be used to indicate that
	 * there were no locations to be consoled to start with.
	 *  
	 */
	protected final void allConsolComplete() {
		
		if (toteConsolService.getTrolleyToteItemCount() > 0) {
			/*
			 * We have trolley items but no next location so 
			 * force to perform swap
			 */
			showToteSwapPage();
		} else {
			errorBox("Tote Consol", "Consol Complete");
			showConsolStartPage();
		}
	}
	/**
	 * Shows the location on screen and prompts user to scan location.
	 */
	private void showLocationConfirmPage() {
		StringUtils.log("Location Confirm Page");
		StringUtils.log(toteConsolService.getLocationObj().getAlphaLocation());
		lConfirm.setText("Scan " 
				+ toteConsolService.getLocationObj().getDisplayLocation());
	
		if (layout.topControl != pConfirmLoc) {
			layout.topControl = pConfirmLoc;
		    contentPanel.layout();
		    shell.setDefaultButton(bConfirmOK);
		}
		
		tConfirm.setText("");
		tConfirm.setFocus();
		
	}
	/**
	 * Validates the confirmed location.
	 */
	private void confirmLocationSubmit() {
		StringUtils.log("confirmLocationSubmit");
		StringUtils.log(tConfirm.getText());
		
		if (toteConsolService.confirmLocation(tConfirm.getText())) {
			showCollectTote();
		} else {
			errorBox("Wrong Location");
			showLocationConfirmPage();
		}
		
	}
	/**
	 * Validates the empty tote scanned.
	 */
	private void emptyToteSubmit() {
		StringUtils.log("emptyToteSubmit");
		StringUtils.log(tEmptyTote.getText());
		
		if (toteConsolService.setTrolleyTote(tEmptyTote.getText())) {
			showLocationConfirmPage();
		} else {
			errorBox("Invalid Tote");
			showEmptyTotePage();
		}
		
	}
	/**
	 * Collect the tote id in the location.
	 */
	private void showCollectTote() {

		if (layout.topControl != pLocatedTote) {
			layout.topControl = pLocatedTote;
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
	private void startPageSubmit() {
		
		StringUtils.log(tConsolStart.getText());
		if (toteConsolService.getLocationObj()
				.validCheckDigit(tConsolStart.getText())) {
			if (toteConsolService.getStartLocation(tConsolStart.getText())) {
				StringUtils.log("got location");
				showEmptyTotePage();
//				showLocationConfirmPage();
			} else {
				allConsolComplete();
			}
		} else {
			errorBox("Invalid Location");
			showConsolStartPage();
		}
	}
	/**
	 * Collects the tote in the location.
	 */
	private void totePageSubmit() {
		StringUtils.log("totePageSubmit()");
		StringUtils.log(tTote.getText());
		
		if (tTote.getText().equalsIgnoreCase(
				toteConsolService.getTrolleyTote())) {
			errorBox("Scan tote in location");
			showCollectTote();
		} else {
			if (toteConsolService.setLastTote(tTote.getText())) {
				showCollectItemPage();
			} else {
				errorBox("Invalid tote");
				showCollectTote();
			}
		}
	}
	/**
	 * Confirms the swap tote page actions.
	 */
	private void toteSwapSubmit() {
		StringUtils.log("toteSwapSubmit()");
		StringUtils.log(tSwap.getText());

		if (toteConsolService.getLocationObj().matchLocation(tSwap.getText())) {
			if (toteConsolService.totePutaway(
					toteConsolService.getLocationObj().getAlphaLocation())) {
				showLocationConfirmPage();
			} else {
				allConsolComplete();
			}
		} else {
			if (toteConsolService.getLocationObj()
					.isMatchToLastLocation(tSwap.getText())) {
				if (toteConsolService.totePutaway(
						toteConsolService.getLocationObj().
							getLastLocationAlpha(false))) {
					showLocationConfirmPage();
				} else {
					allConsolComplete();
				}
			} else {
				errorBox("Invalid location");
				showToteSwapPage();
			}
		}
	}
	/**
	 * Validates the item scan in the trolley tote.
	 */
	private void itemPageSubmit() {
		StringUtils.log("itemPageSubmit()");
		StringUtils.log(tItem.getText());
		
		String itemScanned = tItem.getText();
		
		if (toteConsolService.isValidItemOrTote(itemScanned) 
				== MainConstants.VALID_NEITHER) {
			beep();
			showCollectItemPage();
		} else {
			if (itemScanned.length() == MainConstants.TOTE_ID_LENGTH) {
				if (toteConsolService.whichToteScanned(itemScanned) 
						== MainConstants.TOTE_ID_TROLLEY) {
					/*
					 * tote trolley scanned so send empty 
					 * location and get next location.
					 */
					toteConsolService.locationEmptied(
						toteConsolService.
							getLocationObj().getAlphaLocation());
					if (toteConsolService.
							getLocationObj().isLocationAvailable()) {
						showLocationConfirmPage();
					} else {
						allConsolComplete();
					}
				} else {
					/*
					 *  To be here user must have indicated 
					 *  that a swap is needed.
					 */
					if (toteConsolService.whichToteScanned(itemScanned)
						== MainConstants.TOTE_ID_LOCATION) {
							showToteSwapPage();
					} else {
						beep();
						showCollectItemPage();
					}
					
						
				}
			} else {
				/*
				 * If we're here then we must have have scanned a valid item.
				 */
				
				if (! toteConsolService.isToteFull() && toteConsolService.addItemToTrolleyTote(itemScanned)) {
					showCollectItemPage();
				} else {
					if ( toteConsolService.isToteFull() ) {
						showToteSwapPage();
					}
					else
					{
						beep();
						beep();
						errorBox("Not valid object");
					}
				}
			}
		}
	}
	/**
	 * Collects the items into the tote.
	 */
	private void showCollectItemPage() {
		StringUtils.log("showCollectItemPage");
		if (layout.topControl != pItemPage) {
			layout.topControl = pItemPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bItemOK);
		}
		
		lItemCount.setText(
				"Count : " + toteConsolService.getTrolleyToteItemCount() + "/" + toteConsolService.getToteCapacity());
		lToteId.setText(toteConsolService.getTrolleyTote());

		tItem.setText("");
		tItem.setFocus();
		
	}
	/**
	 * Shows the initial screen to collect the location starting point.
	 */
	private void showConsolStartPage() {

		if (layout.topControl != pConsolStart) {
			layout.topControl = pConsolStart;
		    contentPanel.layout();
		    shell.setDefaultButton(bStartOK);
		}
		
		tConsolStart.setText("");
		tConsolStart.setFocus();
	}
	/**
	 * Shows the screen to collect the empty tote.
	 */
	private void showEmptyTotePage() {

		if (layout.topControl != pEmptyTote) {
			layout.topControl = pEmptyTote;
		    contentPanel.layout();
		    shell.setDefaultButton(bEmptyToteOK);
		}

		tEmptyTote.setText("");
		tEmptyTote.setFocus();
	}
	/**
	 * Shows the screen to swap the totes.
	 */
	private void showToteSwapPage() {

		if (layout.topControl != pSwapPage) {
			layout.topControl = pSwapPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bSwapOK);
		}
		beep();

		if (toteConsolService.getLocationObj()
				.getAlphaLocation().length() 
				== MainConstants.EMBEDDED_LOCATION_LENGTH) {
			lSwap.setText("Scan : "
				+ toteConsolService.getLocationObj().getDisplayLocation());
		} else {
			lSwap.setText("Scan : "
				+ toteConsolService.
				getLocationObj().getLastLocationAlpha(true));
		}
		tSwap.setText(""); 
		tSwap.setFocus();
	}
	
}
