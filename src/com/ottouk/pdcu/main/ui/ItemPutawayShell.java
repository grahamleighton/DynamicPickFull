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
import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * class Item Putaway Shell.
 * @author hstd004
 *
 */
public class ItemPutawayShell extends GeneralShell {

	/**
	 * Instance of ItemPutawayService for server interface.
	 */
	private ItemPutAwayService ipaService;
	private static String LastItem;
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
	 * Label for count.
	 */
	private Label lCount;
	
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
	 * Item count Filename.
	 */
//	private static final String ITEM_COUNT_FILE = "D:/java/dynamic/ic.ini";
	 private static final String ITEM_COUNT_FILE = "/dynamic/ic.ini";
	/**
	 * Title constant.
	 */
	private static final String ITEM_PUTAWAY_TITLE = "Item Putaway";
	/**
	 * length constant.
	 */
	private static final int LENGTH8 = 8;
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
	 * Sets up the class.
	 * @param args Ts
	 * @return boolean true or false
	 */
	public final boolean initialise(final String[] args) {
		LastItem = "";
		while (true) {
			if (logonService.startItemPutaway()) {
				ipaService = new ItemPutAwayServiceImpl();
				return true;
			} else {
				if (questionBox(ITEM_PUTAWAY_TITLE,
						"Unable to start " 
						+ ITEM_PUTAWAY_TITLE + ".\nRetry?") == SWT.NO) {
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
		return ITEM_PUTAWAY_TITLE;
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
	    newLabel(pItem, "Scan Item Barcode");
	    tItem  = newText(pItem, LENGTH10);
		bItemOK = newButton(pItem, "OK");
		bItemESC = newButton(pItem, "Escape");
		lCount = newLabel(pItem, "Count : " + getItemCountAsString());
		
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
				if (questionBox(ITEM_PUTAWAY_TITLE,
						"Unable to stop " + ITEM_PUTAWAY_TITLE 
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
		
		ErrorNumber = 0;
		if (str.length() != 10) {
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
		if (str.length() != MainConstants.LOCATION_LENGTH) {
			System.out.println("Length error : " + str.length() );
			valid = false;
		} else {
			System.out.println("Length matches");
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
	/**
	 * Process location.
	 */
	private void submitLocation() {
		if (validateLocation()) {
			if (locationMatch()) {
				System.out.println("tItem");
				System.out.println(tItem.getText());
				ipaService.buildSendItemPutaway(tItem.getText());
				lCount.setText("Count : " + addItemCount());
				LastItem = tItem.getText();
			} else {
				errorBox(ipaService.LOCATION_MISMATCH_ERROR);
				showLocation();
			}
		} else {
			errorBox(ipaService.LOCATION_NOT_VALID);
		}
		showItemStart();
	}
	/**
	 *Increments the item count.
	 */
	private String addItemCount() {
        FileOutputStream os = null;
		Properties props = new Properties();
		String itemCount = "";
		String fileName = ITEM_COUNT_FILE;
		int x = 0;
		try {
            props.load(new FileInputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemCount = props.getProperty("IC", "0000");
        try {
        	x = Integer.valueOf(itemCount).intValue();
        } catch (NumberFormatException e) {
        	x = 0;
        }
        x = x + 1;
        
        itemCount = StringUtils.padNumber(x, MainConstants.FOUR);
        props.setProperty("IC", itemCount);
		try {
			os = new FileOutputStream(fileName);
			props.store(os, "");
	        os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemCount;
	}
	/**
	 *Increments the item count.
	 * @return String of item count
	 */
	private String getItemCountAsString() {
        
		final Properties props = new Properties();
		String itemCount = "";
		final String fileName = ITEM_COUNT_FILE;
		
		try {
            props.load(new FileInputStream(fileName));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        itemCount = props.getProperty("IC", "0000");
        return itemCount;
	}
	/**
	 *Resets the item count.
	 */
	private void resetItemCount() {
        FileOutputStream os = null;
		Properties props = new Properties();
		String itemCount = "";
		String fileName = ITEM_COUNT_FILE;
		Integer i = Integer.valueOf(0);
		int x = 0;
		
		try {
			os = new FileOutputStream(fileName);
		} catch (IOException e) {
            e.printStackTrace();
        }
		try {
			props.load(new FileInputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        itemCount = props.getProperty("IC");
        i = Integer.valueOf(itemCount);
        x = i.intValue();
        x = 0;
        
        itemCount = StringUtils.padNumber(i, MainConstants.FOUR);
        props.setProperty("IC", itemCount);
		try {
			props.store(os, "");
	        os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/**
	 * Shows the item page.
	 */
	private void showItemStart() {

		if (layout.topControl != pItem) {
			layout.topControl = pItem;
		    contentPanel.layout();
		    shell.setDefaultButton(bItemOK);
		    resetTitle(ITEM_PUTAWAY_TITLE);
		}
		
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
		    resetTitle(ITEM_PUTAWAY_TITLE);
		    
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
