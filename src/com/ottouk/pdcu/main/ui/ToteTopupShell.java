package com.ottouk.pdcu.main.ui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.ItemPutAwayServiceImpl;
import com.ottouk.pdcu.main.service.ItemPutAwayService;
import com.ottouk.pdcu.main.service.LocationDetailService;
import com.ottouk.pdcu.main.service.LocationImpl;
import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

/**
 * class Item Putaway Shell.
 * @author hstd004
 *
 */
public class ToteTopupShell extends GeneralShell {

	/**
	 * Instance of ItemPutawayService for server interface.
	 */
	private ItemPutAwayService ipaService;
	private LocationDetailService locService;
	private LocationImpl loc;
	private static String LastItem;
	private static int LastLength;
	
	/**
	 * stores the current location alpha value
	 */
	private String locationAlpha;
	
	/**
	 * stores the current location numeric value
	 */
	private String locationNumeric;
	int ErrorNumber = 0;
	/**
	 * Layout.
	 */
	private StackLayout layout;
	/**
	 * Screen.
	 */
	private Composite contentPanel;
	/**
	 * Screen.
	 */
	private Composite pItem;
	/**
	 * Screen.
	 */
	private Composite pLocation;

	/**
	 * Label.
	 */
	private Label lItem;
	/**
	 * Label.
	 */
	private Label lLocation;
	/**
	 * label to hold top up count.
	 */
	private Label lTopUpCount;
	
	/**
	 * Text.
	 */
	private Text tItem;
	/**
	 * Text.
	 */
	private Text tLocation;
	
	/**
	 * Button.
	 */
	private Button bItemOK;
	/**
	 * Button.
	 */
	private Button bItemESC;
	/**
	 * Button.
	 */
	private Button bLocationOK;
	/**
	 * Button.
	 */
	private Button bLocationESC;
	
	/**
	 * Title constant.
	 */
	private static final String TOPUP_TITLE = "TOP UP";
	/**
	 * length constant.
	 */
	private static final int LENGTH8 = 8;
	/**
	 * length constant.
	 */
	private static final int LENGTH13 = 13;
	/**
	 * length constant.
	 */
	private static final int LENGTH15 = 15;
	/**
	 * length constant.
	 */
	private static final int LENGTH10 = 10;
	/**
	 * length constant.
	 */
	private static final int LENGTH3 = 3;
	/**
	 * length constant.
	 */
	private static final int LENGTH1 = 1;
	/**
	 * length constant.
	 */
	private static final int LENGTH7 = 7;
	/**
	 * length constant.
	 */
	private static final int LENGTH9 = 9;
	/**
	 * top up count.
	 */
	private int topupCount;
	/**
	 * Sets up the class.
	 * @param args Ts
	 * @return boolean true or false
	 */
	public final boolean initialise(final String[] args) {
		LastItem = "";
		topupCount = 0;
		LastLength = 0;
		while (true) {
			if (logonService.startItemPutaway()) {
				ipaService = new ItemPutAwayServiceImpl();
				loc = new LocationImpl();
				return true;
			} else {
				if (questionBox(TOPUP_TITLE,
						"Unable to start " 
						+ TOPUP_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}
	/**
	 * Sets the screen title.
	 * @return String of title.
	 */
	public final String setTitle() {
		return TOPUP_TITLE;
	}
	/**
	 * Listener events.
	 * @param event the event.
	 */
	public final void handleListenerEvent(final Event event) {
		if (event.widget == bItemOK) {
			submitItem();
		} else if (event.widget == bItemESC) {
			escape();
		} else if (event.widget == bLocationOK) {
			submitLocation();
		} else if (event.widget == bLocationESC) {
			showItemStart();
		}
	}
	/**
	 * Creates the screens for item putaway.
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
		
	    // create page pItem
	    pItem  = new Composite(contentPanel, SWT.NONE);
	    pItem.setLayout(gridLayout);
	    lTopUpCount =  newLabel(pItem , "Count : 0");
	    newLabel(pItem, "Scan Item Barcode");
	    tItem  = newText(pItem, LENGTH15);
		bItemOK = newButton(pItem, "OK");
		bItemESC = newButton(pItem, "Escape");
		
	    // create page pLocation
	    pLocation   = new Composite(contentPanel, SWT.NONE);
	    pLocation.setLayout(gridLayout);
	    newLabel(pLocation, "Scan Location");
	    tLocation = newText(pLocation , LENGTH8);
		bLocationOK  = newButton(pLocation, "OK");
		bLocationESC = newButton(pLocation, "Escape");

		
		// set start page
	    showItemStart();
	}
	/**
	 * Escapes back to menu after logging off activity.
	 */
	private void escape() {
		
		while (true) {
			if (logonService.stopItemPutaway()) {
				break;
			} else {
				if (questionBox(TOPUP_TITLE,
						"Unable to stop " + TOPUP_TITLE 
							+ ".\nRetry?") == SWT.NO) {
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
	 * validateItem.
	 * 
	 * @return valid
	 */
	private boolean validateItem() {
		String str = tItem.getText();
		final int eight = 8;
		
		ErrorNumber = 0;
		LastLength = str.length();
		if (str.length() != LENGTH8 ) {
			ErrorNumber = ipaService.ITEM_NOT_VALID;
			return false;
		}
		if (str.equalsIgnoreCase(LastItem)) {
			ErrorNumber = ipaService.DUPLICATE_ITEM;
			return false;
		}
		
		return true;
	}
	/**
	 * validate location.
	 * @return valid
	 */
	private boolean validateLocation() {
		boolean valid = true;
		String str = tLocation.getText();
		
		if ( loc.isAlphaLocationValid(str)) {
			return valid;
		}
		
		if (str.length() != MainConstants.LOCATION_LENGTH) {
			System.out.println("Length error : " + str.length() );
			valid = false;
		} else {
			System.out.println("Length matches");
		}
		
		if ( ! Validate.mod10Check3131(str, 8, "0")) {
			System.out.println("Invalid location");
			
			valid = false;
		}
		
		return valid;
	}
	/**
	 * tests if location and item match.
	 * @return true if matches
	 */
	private boolean locationMatch() {
		String loc = tItem.getText().substring(LENGTH3, LENGTH9);
//		System.out.println("loc");
//		System.out.println(loc);
//		System.out.println("location");
//		System.out.println(tLocation.getText());
		if (loc.equals(tLocation.getText().substring(LENGTH1, LENGTH7))) {
//			System.out.println("Location matches");
			return true;
		}
//		System.out.println("Location does not match");
		
		return false;
	}

	/**
	 * Process item.
	 */
	private void submitItem() {
		if (validateItem()) {
			showLocation();
		} else {
			errorBox(ErrorNumber);
			showItemStart();
		}
	}
	
	
	private boolean interactGetLocation(String location) {
//		locationAlpha = "RB001A";
//		locationNumeric = "134001";
		
		ipaService.buildSendToteTopUpRequest(location,"L");
		String reply = ipaService.getServerResponse();
		String ResponseLocationAlpha = StringUtils.getString(reply,3,9);
		String ResponseLocationNum = StringUtils.getString(reply,9,15);
		
//		System.out.println("Location Alpha :" + ResponseLocationAlpha);
//		System.out.println("Location Num   :" + ResponseLocationNum);
		
		if ( ResponseLocationNum == null || ResponseLocationNum == "" )
		{
			locationAlpha = "";
			locationNumeric = "";
			return false;
		}
		else
		{
			locationAlpha = ResponseLocationAlpha;
			locationNumeric = ResponseLocationNum;
		}
		
		return true;
		
	}
	
	/**
	 * Process location.
	 */
	private void submitLocation() {
		
		
		
		String locationScan = tLocation.getText();
		
		
		
		if (validateLocation()) {
		
//			System.out.println("tItem");
//			System.out.println(tItem.getText());
			
			if ( loc.isAlphaLocationValid(locationScan))
			{
				locService.getLocationDetails(locationScan);
				System.out.println("after");
				System.out.println("getNumericLocation()");

				System.out.println(locService.getNumericLocation());

				System.out.println("getAlphaLocation()");
				System.out.println(locService.getAlphaLocation());
				String newLoc = "0" + locService.getNumericLocation() + "0";
				ipaService.buildSendToteTopUp(tItem.getText(),newLoc, LastLength);
			}
			else
			{
				ipaService.buildSendToteTopUp(tItem.getText(),tLocation.getText(),LastLength);
			}
			topupCount = topupCount + 1;
			
			
			LastItem = tItem.getText();
		} else {
			errorBox(ipaService.LOCATION_NOT_VALID);
		}
		showItemStart();
	}
	/**
	 * Shows the item page.
	 */
	private void showItemStart() {

		if (layout.topControl != pItem) {
			layout.topControl = pItem;
		    contentPanel.layout();
		    shell.setDefaultButton(bItemOK);
		    resetTitle(TOPUP_TITLE);
		}
		lTopUpCount.setText ( "Count : " + StringUtils.padNumber( topupCount,4));
		
		
		tItem.setText("");
		tItem.setFocus();
	}
	/**
	 * Shows the location page.
	 */
	private void showLocation() {

		if (layout.topControl != pLocation) {
			layout.topControl = pLocation;
		    contentPanel.layout();
		    shell.setDefaultButton(bLocationOK);
		    resetTitle(TOPUP_TITLE);
		    
		}
		
		tLocation.setText("");
		tLocation.setFocus();
	}
	/**
	 * Shows any error messages.
	 * @param errNo Error number defined from ItemPutaway
	 * @return String Error message.
	 */
	private int errorBox(final int errNo) {
		return errorBox(getErrorMessage(errNo));
	}
	/**
	 * Converts error from number to string.
	 * @param returnCode Error number
	 * @return String error message.
	 */
	private String getErrorMessage(final int returnCode) {
		
		String errorMessage = "Unknown error";

		if (returnCode == ipaService.LOCATION_MISMATCH_ERROR) {
			errorMessage = "Wrong Location";
		}
		if (returnCode == ipaService.ITEM_NOT_VALID) {
			errorMessage =  "Item not valid";
		}
		if (returnCode == ipaService.LOCATION_NOT_VALID) {
			errorMessage =  "Location not valid";
		}
		if (returnCode == ipaService.DUPLICATE_ITEM) {
			errorMessage =  "Item already scanned";
		}
		return errorMessage;
	}

}
