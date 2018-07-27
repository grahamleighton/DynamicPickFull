package com.ottouk.pdcu.main.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.ItemPutAwayService;
import com.ottouk.pdcu.main.service.ItemPutAwayServiceImpl;
import com.ottouk.pdcu.main.service.LocationImpl;
import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

/**
 * class Item Putaway Shell.
 * @author hstd004
 *
 */
public class ToteTopupShellDirected extends GeneralShell {

	/**
	 * Instance of ItemPutawayService for server interface.
	 */
	private ItemPutAwayService ipaService;
	private LocationImpl loc;
	
	private static final int ITEM_NOT_VALID = 1;
	private static final int DUPLICATE_ITEM = 2;
	private static final int LOCATION_MISMATCH_ERROR = 4;
	private static final int LOCATION_NOT_VALID = 5;
	private static final int LOCATION_NOT_FOUND = 6;
	private static final int TOO_MANY_ITEMS = 7; 
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
	 * Screen : Start = pgStart
	 * 
	 *      |-----------------
	 *      |   SCAN START   |
	 *      |     LOCATION   |   
	 *      |    ________    |   txtStartLocation
	 *      |      |OK|      |   btnStartOK
	 *      |     |BACK|     |   btnStartBack
	 *      ------------------    
	 */
	/**
	 * define the page
	 */
	private Composite pgStart;
	/**
	 * define the start location field
	 */
	private Text txtStartLocation;
	/**
	 * define the start OK button
	 */
	private Button btnStartOK;
	/**
	 * define the start back button
	 */
	private Button btnStartBack;
	
	/**
	 * Screen : Start = pgLocation
	 * 
	 *      |-----------------
	 *      |      SCAN      |
	 *      |     XXXXXXX    |   lblLocationLocation   
	 *      |    ________    |   txtLocationInput
	 *      |      |OK|      |   btnLocationOK
	 *      |     |BACK|     |   btnLocationBack
	 *      ------------------    
	 */
	/**
	 * define the page
	 */
	private Composite pgLocation;
	/**
	 * define the go-to location display field
	 */
	private Label lblLocationLocation;
	/**
	 * define the location scan field to confirm / update location
	 */
	private Text txtLocationInput;
	/**
	 * define the location OK button
	 */
	private Button btnLocationOK;
	/**
	 * define the location back button
	 */
	private Button btnLocationBack;
	/*
	 * define a List object for the items
	 */
	private List listItems ;


	/**
	 * Screen : Start = pgItem
	 * 
	 *      |-----------------
	 *      |  SCAN ITEM TO  |
	 *      |     XXXXXXX    |   lblItemLocation   
	 *      |    ________    |   txtItemInput
	 *      |   Items : 99   |   lblItemCount
	 *      |      |OK|      |   btnItemOK
	 *      |     |END|      |   btnItemEnd
	 *      ------------------    
	 */
	/**
	 * define the page
	 */
	private Composite pgItem;
	/**
	 * define the current location display field
	 */
	private Label lblItemLocation;
	/**
	 * define the item scan field 
	 */
	private Text txtItemInput;
	/**
	 * define the current item count
	 */
	private Label lblItemCount;
	/**
	 * define the item OK button
	 */
	private Button btnItemOK;
	/**
	 * define the item end button
	 */
	private Button btnItemEnd;
	
	/**
	 * stores the current location alpha value
	 */
	private String locationAlpha;
	
	/**
	 * stores the current location numeric value
	 */
	private String locationNumeric;
	
	
	/**
	 * Title constant.
	 */
	private static final String TOPUP_TITLE = "Directed Top Up";
	/**
	 * length constant.
	 */
	private static final int LENGTH8 = 8;
	/**
	 * Sets up the class.
	 * @param args Ts
	 * @return boolean true or false
	 */
	public final boolean initialise(final String[] args) {
		listItems  = new ArrayList();
		listItems.clear();
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
		
	
		
		if (event.widget == btnStartOK) {
			submitStart();
		} else if (event.widget == btnStartBack) {
			escape();
		} else if (event.widget == btnLocationOK) {
			submitLocation();
		} else if (event.widget == btnLocationBack) {
			showStart();
		} else if (event.widget == btnItemOK) {
			submitItem();
		} else if (event.widget == btnItemEnd) {
			submitItemEnd();
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
		

		// create the Start page
		
		pgStart = new Composite(contentPanel, SWT.NONE);
		pgStart.setLayout(gridLayout);
		
		newLabel(pgStart, "Scan Start Location");
//		newLabel(pgStart,"LOCATION");
		txtStartLocation = newText(pgStart,LENGTH8);
		btnStartOK = newButton(pgStart, "OK");
		btnStartBack = newButton(pgStart, "BACK");
		
		// create the location page
		
		pgLocation = new Composite(contentPanel, SWT.NONE);
		pgLocation.setLayout(gridLayout);
		
		newLabel(pgLocation, "SCAN");
		lblLocationLocation = newLabel(pgLocation,"XXXXXX");
		txtLocationInput = newText(pgLocation,LENGTH8);
		btnLocationOK = newButton(pgLocation, "OK");
		btnLocationBack = newButton(pgLocation, "BACK");
		
		// create the Item page
		
		pgItem = new Composite(contentPanel, SWT.NONE);
		pgItem.setLayout(gridLayout);
		
		newLabel(pgItem, "SCAN ITEM TO");
		lblItemLocation = newLabel(pgItem,"XXXXXX");
		txtItemInput = newText(pgItem,LENGTH8);
		lblItemCount = newLabel(pgItem,"Items : 99");
		btnItemOK = newButton(pgItem, "OK");
		btnItemEnd = newButton(pgItem, "END");
		
		
		// set start page
	    showStart();
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
		
		String str = txtItemInput.getText();
		
		ErrorNumber = 0;
		if (str.length() != LENGTH8 ) {
			ErrorNumber = ITEM_NOT_VALID;
			return false;
		}
		
		if (! Validate.mod11Check(str, LENGTH8) )
		{
			ErrorNumber = ITEM_NOT_VALID;
			return false;
		}
		if ( listItems.contains(str))
		{
			ErrorNumber = DUPLICATE_ITEM;
			return false;
		}
		
		
		return true;
	}
	/**
	 * validatelocation , validates location passed
	 * @param str
	 * @return
	 */
	private boolean validateLocation(String str) {
		boolean valid = true;
		
		if ( loc.isAlphaLocationValid(str)) {
			return valid;
		}
		
		if (str.length() != MainConstants.LOCATION_LENGTH) {
//			System.out.println("Length error : " + str.length() );
			valid = false;
//		} else {
//			System.out.println("Length matches");
		}
		/*
		 * Graham Leighton Nov 2017 
		 * Prevent mis scans on tote top up
		 */
		if ( ! Validate.mod10Check3131(str, 8, "0")) {
//			System.out.println("Invalid location");
			
			valid = false;
		}
		
		return valid;
	}
	/**
	 * Process item.
	 */
	private void submitItem() {
		if (! validateItem()) {
			errorBox(ErrorNumber);
		}
		else
		{
			if ( listItems.size() < 13 ) {
				listItems.add(txtItemInput.getText());
			}
			else
			{
				errorBox(TOO_MANY_ITEMS);
			}
		}
		showItem();
	}
	
	private void submitItemEnd() {

		
		if ( listItems.size() > 0 ) {
			ipaService.buildSendMultiItemToteTopUp(listItems , locationNumeric);
		}

		listItems.clear();
		
		
		submitStart();
	}

	/*
	private void submitLocationOld() {
		if (validateLocation()) {
		
			System.out.println("tItem");
			System.out.println(tItem.getText());
			ipaService.buildSendToteTopUp(tItem.getText(),tLocation.getText(),LastLength);
			topupCount = topupCount + 1;
			
			
			LastItem = tItem.getText();
		} else {
			errorBox(ipaService.LOCATION_NOT_VALID);
		}
		showItemStart();
	}
	*/
	/**
	 * Shows the get start location page.
	 */
	private void showStart() {

		if (layout.topControl != pgStart) {
			layout.topControl = pgStart;
		    contentPanel.layout();
		    shell.setDefaultButton(btnStartOK);
		    resetTitle(TOPUP_TITLE);
		    
		}
		
		txtStartLocation.setText("");
		txtStartLocation.setFocus();
	}
	/**
	 * Shows the location confirmation / update page.
	 */
	private void showLocation() {

		if (layout.topControl != pgLocation) {
			layout.topControl = pgLocation;
		    contentPanel.layout();
		    shell.setDefaultButton(btnLocationOK);
		    resetTitle(TOPUP_TITLE);
		    
		}
		
		txtLocationInput.setText("");
		txtLocationInput.setFocus();
	}
	/**
	 * Shows the item collection
	 */
	private void showItem() {

		if (layout.topControl != pgItem) {
			layout.topControl = pgItem;
		    contentPanel.layout();
		    shell.setDefaultButton(btnItemOK);
		    resetTitle(TOPUP_TITLE);
		    
		}
		lblItemCount.setText ( "Count : " + StringUtils.padNumber( listItems.size() ,2));

		txtItemInput.setText("");
		txtItemInput.setFocus();
	}
	/**
	 * processes the location start page
	 */
	private void submitStart()
	{
		String location = txtStartLocation.getText();
		if (!validateLocation(location)) {
			errorBox(LOCATION_NOT_VALID);
			showStart();
		}
		else
		{
			boolean locFound = interactGetLocation(location);
			
			if ( locFound ) 
			{
				lblLocationLocation.setText(locationAlpha);
				showLocation();
			}
			else
			{
				errorBox(LOCATION_NOT_FOUND);
				showStart();
			}
		}
	}
	/**
	 * interact with server to get the nearest free location
	 * to the one supplied
	 * @param location
	 * @return location
	 */
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
	
	private boolean interactConfirmLocation(String location) {
//		locationAlpha = "RB001A";
//		locationNumeric = "134001";
		
		ipaService.buildSendToteTopUpRequest(location,"C");
		String reply = ipaService.getServerResponse();
		String ResponseLocationAlpha = StringUtils.getString(reply,3,9);
		String ResponseLocationNum = StringUtils.getString(reply,9,15);
		
		System.out.println("Location Alpha :" + ResponseLocationAlpha);
		System.out.println("Location Num   :" + ResponseLocationNum);
		
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
	 * processes the scanned location page
	 */
	private void submitLocation()
	{
		String location;
		location = txtLocationInput.getText();
		
		if (!validateLocation(location)) {
			errorBox(LOCATION_NOT_VALID);
			
			showLocation();
		}
		else
		{
			if ( loc.isAlphaLocationValid(location)) {
				if ( location == locationAlpha) 
				{
					lblItemLocation.setText(locationAlpha);
					showItem();
				}
				else	
				{
					boolean locFound = interactConfirmLocation(location);
					if ( !locFound )
					{
						errorBox(LOCATION_NOT_VALID);
						showLocation();
					}
					else
					{
						lblItemLocation.setText(locationAlpha);
						showItem();
					}
				}
			}
			else
			{
				String midLocation = StringUtils.getString(location,2,7);
				if ( midLocation == locationNumeric )
				{
					lblItemLocation.setText(locationAlpha);
					showItem();
				}	
				else	
				{
					boolean locFound = interactConfirmLocation(location);
					if ( !locFound )
					{
						errorBox(LOCATION_NOT_VALID);
						showLocation();
					}
					else
					{
						lblItemLocation.setText(locationAlpha);
						showItem();
					}
				}
			}
		}
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

		if (returnCode == LOCATION_MISMATCH_ERROR) {
			errorMessage = "Wrong Location";
		}
		if (returnCode == ITEM_NOT_VALID) {
			errorMessage =  "Item not valid";
		}
		if (returnCode == LOCATION_NOT_VALID) {
			errorMessage =  "Location not valid";
		}
		if (returnCode == DUPLICATE_ITEM) {
			errorMessage =  "Item already scanned";
		}
		if (returnCode == LOCATION_NOT_FOUND) {
			errorMessage =  "No Location Found";
		}
		if (returnCode == TOO_MANY_ITEMS) {
			errorMessage =  "Too Many Items";
		}
		return errorMessage;
	}

}
