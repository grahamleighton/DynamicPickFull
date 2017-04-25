package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.domain.CartonPutaway;
import com.ottouk.pdcu.main.service.CartonPutawayService;
import com.ottouk.pdcu.main.service.CartonPutawayServiceImpl;




public class CartonPutawayShell extends GeneralShell  {

	private CartonPutawayService cartonPutawayService;

	private StackLayout layout;

	private Composite contentPanel;
	private Composite pCartonPutawayStart;
	private Composite pCartonLocationPutawayStart;
	private Composite pLocationNumberStart;
	private Composite pPIComplete;
	
	private Text tCartonNumber;
	private Text tCartonLocation;
	private Text tLocationNumber;

	private Button bCartonPutawayStartOK;
	private Button bPutawayStartEscape;
	private Button bCartonLocationOK;
	private Button bCartonLocationEscape;
	private Button bLocationNumberOK;
	private Button bLocationNumberEscape;
	private Button bPIComplete;



	private static final String CARTON_PUTAWAY_TITLE = "Carton Putaway";

	public boolean initialise(String[] args) {

		while (true) {
			if (logonService.startCartonPutaway()) {
				System.out.println(CARTON_PUTAWAY_TITLE + " started");
				cartonPutawayService = new CartonPutawayServiceImpl();
				return true;
			} else {
				if (questionBox(CARTON_PUTAWAY_TITLE,
						"Unable to start " + CARTON_PUTAWAY_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}

	public String setTitle() {
		return CARTON_PUTAWAY_TITLE;
	}

	public void handleListenerEvent(Event event) {
		if (event.widget == bCartonPutawayStartOK) {
			submitPPutawayStart();
		} else if (event.widget == bPutawayStartEscape) {
			escape();
		} else if (event.widget == bCartonLocationOK){
			submitCartonLocation(); //need to add carton Location show page
		}else if (event.widget == bCartonLocationEscape) {
			escape();
		}else if (event.widget == bLocationNumberOK){
			submitLocationNumber();//Location Number submitpage...
		}else if (event.widget == bLocationNumberEscape){
			escape();
		}
	}

	public void createContents() {
		System.out.println("createContents CPA Start");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
		shell.setLayout(gridLayout);

		// create a composite for the pages to share
		contentPanel = new Composite(shell, SWT.NONE);
		layout = new StackLayout();
		contentPanel.setLayout(layout);

		contentPanel.setLayoutData(shell.getLayoutData());

		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		labelData.horizontalSpan = 1;

		// create page pPutawayStart/ CartonNumber
		pCartonPutawayStart = new Composite(contentPanel, SWT.NONE);
		pCartonPutawayStart.setLayout(gridLayout);
		newLabel(pCartonPutawayStart, "Scan Carton Number", labelData);
		tCartonNumber = newText(pCartonPutawayStart, "", 10);
		//   System.out.println(tCartonNumber.getText());
		bCartonPutawayStartOK = newButton(pCartonPutawayStart, "OK");
		bPutawayStartEscape = newButton(pCartonPutawayStart, "Escape");

		//create page for CartonLocation..
		pCartonLocationPutawayStart = new Composite(contentPanel, SWT.NONE);
		pCartonLocationPutawayStart.setLayout(gridLayout);
		newLabel(pCartonLocationPutawayStart, "Scan Carton TOP", labelData);
		tCartonLocation = newText(pCartonLocationPutawayStart, "", 10);
		bCartonLocationOK = newButton(pCartonLocationPutawayStart, "OK");
		bCartonLocationEscape = newButton(pCartonLocationPutawayStart, "Escape");


		//create page for LocationNumber...
		pLocationNumberStart = new Composite(contentPanel, SWT.NONE);
		pLocationNumberStart.setLayout(gridLayout);
		newLabel(pLocationNumberStart, "Scan Location Number", labelData);
		tLocationNumber = newText(pLocationNumberStart, "", 8);
		bLocationNumberOK = newButton(pLocationNumberStart, "OK");
		bLocationNumberEscape = newButton(pLocationNumberStart, "Escape");


		// set start page
		System.out.println("createContents CPA End");
		showPPutawayStart();
	}

	private void escape() {

		while (true) {
			if (logonService.stopCartonPutaway()) {
				System.out.println(CARTON_PUTAWAY_TITLE + " stopped");
				break;
			} else {
				if (questionBox(CARTON_PUTAWAY_TITLE,
						"Unable to stop " + CARTON_PUTAWAY_TITLE + ".\nRetry?") == SWT.NO) {
					// Quit anyway
					break;
				} else {
					showWaitCursor();
				}
			}
		}
		shellClose();
	}


	//2.) Accept 1o digit barcode , this is the carton number , then perform the following checks...
	private void submitPPutawayStart() {

		String t = "86";
		// System.out.println(tCartonNumber.getText());

		//Make sure the number is either 10 or 2 digits long and numeric only...
		if (tCartonNumber.getText().length() != 2 && tCartonNumber.getText().length()!= 10){

			errorBox("must be 2 or 10");
			//causes the carton putaway page to appear in front.. 
			showPPutawayStart();	
			return;
		}

		//if it is 2 digis and equal to 86 go back to main menu...
		if (tCartonNumber.getText().equals(t)) {
			errorBox("Go back to Menu");
			//function for the first screen here...
			System.out.println("System exited");
			escape();
			return;
		}

		//if it is 10 digits long make sure it is valid..
		//making sure that the 10 digits is valid..
		if (cartonPutawayService.cartonvalidation(tCartonNumber.getText())) {
			//errorBox("valid");
			//show the next screen here/..();
			showCartonLocationPage();


		} else {
			errorBox("invalid barcode");
			showPPutawayStart();
		}
	}	

	//3.) Accept 2 or 10 digit carton location then perform the following checks, 
	private void submitCartonLocation(){
		String t = "86";
		// System.out.println(tCartonNumber.getText());


		//i.) 
		if (tCartonLocation.getText().length()!= 2 && tCartonLocation.getText().length()!= 10){
			errorBox("must be 2 or 10");
			//causes the carton putaway page to appear in front.. 
			showCartonLocationPage();	
			return;
		}

		//ii.)
		if (tCartonLocation.getText().equals(t)) {
			errorBox("Go back to Menu");
			//function for the first screen here...
			System.out.println("System exited");
			escape();
			return;
		}

		//3.)
		if (cartonPutawayService.LocationValidation(tCartonLocation.getText())) {
			//errorBox("valid");
			//show the next screen here/..();
			showLocationNumberPage();



		} else {
			errorBox("invalid barcode");
			showCartonLocationPage();
		}
	}	

	//4.) 8 digit barcode..
	private void submitLocationNumber(){
		String t = "86";
		// System.out.println(tCartonNumber.getText());

		//i.)
		if (tLocationNumber.getText().length()!= 2 && tLocationNumber.getText().length()!= 8){
			errorBox("must be 2 or 8");
			//causes the carton putaway page to appear in front.. 
			showLocationNumberPage();	
			return;
		}

		//ii.)
		if (tLocationNumber.getText().equals(t)) {
			errorBox("Go back to Menu");
			//function for the first screen here...
			//System.out.println("System exited");
			escape();
			return;
		}

		//iii.)
		if (cartonPutawayService.LocationNumberValidation(tLocationNumber.getText())) {
			//errorBox("valid");
			ValidateandMatchbarcode();
			
			

		} else {
			errorBox("invalid location");
			showLocationNumberPage();
			return;
		}

	}

	//5.) function that get both the 6 digits of carton location and location number and validate it against each other.
	private void ValidateandMatchbarcode(){
		if (cartonPutawayService.validate(tCartonLocation.getText(), tLocationNumber.getText(), tCartonNumber.getText())){		
			//errorBox ("Match!"); // go back to step 1.)
			showPPutawayStart();
			
		}
		
		else {
			errorBox("Dont match! input Location number again"); // go back to step 4.)
			showLocationNumberPage();	
	}		
}
	/**
	 * Use to show the GUI page of each barcode...
	 */
	private void showLocationNumberPage() {
		if (layout.topControl !=pLocationNumberStart)
			if (layout.topControl != pLocationNumberStart){
				layout.topControl = pLocationNumberStart;
				contentPanel.layout();
				shell.setDefaultButton (bLocationNumberOK);
			}
		tLocationNumber.setText("");
		tLocationNumber.setFocus();

	}

	private void showPPutawayStart() {
		if (layout.topControl != pCartonPutawayStart) {
			layout.topControl = pCartonPutawayStart;
			contentPanel.layout();
			shell.setDefaultButton(bCartonPutawayStartOK);
			resetTitle(CARTON_PUTAWAY_TITLE);
		}
		
		tCartonNumber.setText("");
		tCartonNumber.setFocus();
	}

	private void showCartonLocationPage()
	{
		if (layout.topControl != pCartonLocationPutawayStart){
			layout.topControl = pCartonLocationPutawayStart;
			contentPanel.layout();
			shell.setDefaultButton (bCartonLocationOK);

		}
		tCartonLocation.setText("");
		tCartonLocation.setFocus();
	}
	
	private void showPiComplete (){
		if (layout.topControl != pPIComplete){
			layout.topControl = pPIComplete;
			contentPanel.layout();
			shell.setDefaultButton(bPIComplete);
		}
	}
}
	
	