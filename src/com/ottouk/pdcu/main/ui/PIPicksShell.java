package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;


import com.ottouk.pdcu.main.service.PIPicksService;
import com.ottouk.pdcu.main.service.PIPicksServiceImpl;
import com.ottouk.pdcu.main.utils.StringUtils;

public class PIPicksShell extends GeneralShell {

	private PIPicksService pipicksService;
	
	public String status;
	private StackLayout layout;
	
	private Composite contentPanel;
	private Composite pPIPick; //show the start of the PI Request Type...
	private Composite pScanLocationandAlphaLoc;
	private Composite pScanItem;
	private Composite pFinal;
	
	//private Label lScanAlpaLoc;


	private Text tPIPick;
	private Text tScanLocationandalpha;
	private Text tScanItem;


	private Button bPIPickOK;
	private Button bPIPickEscape;
	private Button bscanLocationAndAlphaOK;
	private Button bscanLocationAndAlphaEscape;
	private Button bScanLocationNotFound;
	private Button bScanItemOK;
	private Button bScanItemEscape;
	private Button bScanItemNotFound;
	private Button bFinalContinue;
	private Button bFinalEscape;

	private Label lLocationandAlphaLoc;
	private Label lScanItem;
	private Label lscanvalue;
	
	
	private static final String PI_PICKS_TITLE = "PI Picks";
	
	public boolean initialise(String[] args) {

		while (true) {
			if (logonService.startPIPicks()) {
				pipicksService = new PIPicksServiceImpl();
				return true;
			} else {
				if (questionBox(PI_PICKS_TITLE,
						"Unable to start " + PI_PICKS_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}

	public String setTitle() {
		return PI_PICKS_TITLE;
	}

	
	//uses to add triggers on buttons..
	public void handleListenerEvent(Event event) {
		if (event.widget == bPIPickOK) {
			submitshowPIPickStart();
		} else if (event.widget == bPIPickEscape) {
					escape();
		} else if (event.widget == bscanLocationAndAlphaOK){
			//showItemScan();
			submitshowScanLocatioAndAlpha();
		}	else if (event.widget == bscanLocationAndAlphaEscape){
			showPIPickStart();
		} else if (event.widget == bScanLocationNotFound){
			submitNotFound();
		}else if (event.widget == bScanItemOK){
			
			//finalsent();
			submitshowItemScan();
		}else if (event.widget ==bScanItemEscape){
			showScanLocatioAndAlpha();
		} else if (event.widget == bScanItemNotFound){
			ItemNotfound();
		} else if (event.widget == bFinalContinue){
			submitshowPIPickStart();
		}
	}
	
	//this will create some GUI for the whole application..
	public void createContents() {

		// create a composite for the pages to share
		contentPanel = new Composite(shell, SWT.NONE);
		layout = new StackLayout();
		contentPanel.setLayout(layout);
		contentPanel.setLayoutData(shell.getLayoutData());

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;
		
		
		//GridLayout layout = new GridLayout (2,false);
		
		
		// create page scanLocatoion...
		pPIPick = new Composite(contentPanel, SWT.NONE);
		pPIPick.setLayout(gridLayout);
		newLabel(pPIPick, "Scan Start Location:");
		tPIPick = newText(pPIPick, 8);		
		bPIPickOK = newButton(pPIPick, "OK");
		bPIPickEscape = newButton(pPIPick, "Escape");

		// create page pPScanLocationandalphaLoc for scanning location...
		pScanLocationandAlphaLoc = new Composite(contentPanel, SWT.NONE);
		pScanLocationandAlphaLoc.setLayout(gridLayout);
		lLocationandAlphaLoc = newLabel(pScanLocationandAlphaLoc, "Scan + AlphaLoc");
		tScanLocationandalpha = newText(pScanLocationandAlphaLoc, 8);
		bscanLocationAndAlphaOK = newButton(pScanLocationandAlphaLoc, "OK");
		bscanLocationAndAlphaEscape = newButton (pScanLocationandAlphaLoc, "Escape");
		bScanLocationNotFound = newButton (pScanLocationandAlphaLoc,"Location not found");
		
		
		//create a page for scan item for scanning an item ...
		pScanItem = new Composite (contentPanel, SWT.NONE);
		pScanItem.setLayout(gridLayout);
		lScanItem = newLabel (pScanItem , "Scan Item: ");
		lscanvalue = newLabel (pScanItem, "");
		tScanItem = newText (pScanItem, 8);
		bScanItemOK = newButton (pScanItem, "OK");
		bScanItemEscape = newButton (pScanItem, "Escape");
		bScanItemNotFound = newButton (pScanItem, "Item not found");
		
	
		pFinal = new Composite (contentPanel, SWT.NONE);
			pFinal.setLayout(gridLayout);
			newLabel (pFinal, "All the PI's have been");
	                newLabel (pFinal, "completed!");
			bFinalContinue = newButton (pFinal, "Continue");
			bFinalEscape = newButton (pFinal, "Escape");
                
		showPIPickStart();
	}

	private void escape() {

		while (true) {
			if (logonService.stopPIPicks()) {
				break;
			} else {
				if (questionBox(PI_PICKS_TITLE,
						"Unable to stop " + PI_PICKS_TITLE + ".\nRetry?") == SWT.NO) {
					// Quit anyway
					break;
				} else {
					showWaitCursor();
				}
			}
		}

		shellClose();
	}

	
	//show the GUI of the PI type where the user can input the PI type on the textbox...
	//there is also 2 buttons which are OK and escape button..
	//if the PI type is correct it will open another screen (ShowPScanLocation ())...
	private void showPIPickStart() {

		if (layout.topControl != pPIPick) {
			layout.topControl = pPIPick;
			contentPanel.layout();
			shell.setDefaultButton(bPIPickOK);
			resetTitle(PI_PICKS_TITLE);
			
			 tPIPick.addListener(SWT.Verify, new Listener() {
 			      public void handleEvent(Event e) {
 			        String string = e.text;
 			        char[] chars = new char[string.length()];
 			        string.getChars(0, chars.length, chars, 0);
 			        for (int i = 0; i < chars.length; i++) {
 			          if (!('0' <= chars[i] && chars[i] <= '9')) {
 			            e.doit = false;
 			            return;
 			          }
 			        }
 			      }
 			    });
			
		}

                // counts = 0;
		tPIPick.setText("");
		tPIPick.setFocus();
	}
	
	
	private void showScanLocatioAndAlpha(){
		String a = String.valueOf(pipicksService.getNumLoc()); 	  
		
	//	int limit = Integer.valueOf(a);
		
		// if (counts < limit)      //   {
		//StringUtils.log("Scan + alpha_loc");
		// System.out.println(pipicksService.getAlphaLoc());
	
		 lLocationandAlphaLoc.setText(" Scan " + pipicksService.getAlphaLoc().substring(0, 4) + "-" + pipicksService.getAlphaLoc().substring(4) );
		
	
		if (layout.topControl != pScanLocationandAlphaLoc){
			layout.topControl = pScanLocationandAlphaLoc;
			contentPanel.layout();
			shell.setDefaultButton (bscanLocationAndAlphaOK);
			
			  tScanLocationandalpha.addListener(SWT.Verify, new Listener() {
 			      public void handleEvent(Event e) {
 			        String string = e.text;
 			        char[] chars = new char[string.length()];
 			        string.getChars(0, chars.length, chars, 0);
 			        for (int i = 0; i < chars.length; i++) {
 			          if (!('0' <= chars[i] && chars[i] <= '9')) {
 			            e.doit = false;
 			            return;
 			          }
 			        }
 			      }
 		    });		  
			  
		}
	
		tScanLocationandalpha.setText("");
		tScanLocationandalpha.setFocus();
      
	}

	private void showItemScan() {
	
		
		lScanItem.setText("Scan Item:");
		
		lscanvalue.setText(pipicksService.getItemNumber().substring(0, 4 )+ " - "+ pipicksService.getItemNumber().substring(4,8));
		
		//System.out.print(pipicksService.getItemNumber());
		
		if (layout.topControl != pScanItem){
			layout.topControl = pScanItem;
			contentPanel.layout();
			shell.setDefaultButton (bScanItemOK);
			
			//wont allow the user to enter a string as a input...
			tScanItem.addListener(SWT.Verify, new Listener() {
 			      public void handleEvent(Event e) {
 			        String string = e.text;
 			        char[] chars = new char[string.length()];
 			        string.getChars(0, chars.length, chars, 0);
 			        for (int i = 0; i < chars.length; i++) {
 			          if (!('0' <= chars[i] && chars[i] <= '9')) {
 			            e.doit = false;
 			            return;
 			          }
 			        }
 			      }
 			    });
			
		}
			
		tScanItem.setText("");
		tScanItem.setFocus();
	}
	
	
	private void submitshowPIPickStart(){
	if (tPIPick.getText().length() == 8 && tPIPick.getText() != null){
			submitsend1();
	}
		else {
				errorBox ("Wrong Location");
		}
			
		tPIPick.setFocus();
		tPIPick.setText("");
	}
	
	
	private void submitsend1(){	
		if (pipicksService.send(tPIPick.getText())){
		 //	if (pipicksService.getDataFlag().equalsIgnoreCase("Y")){
					if (pipicksService.getAlphaLoc() == null){
							errorBox ("No items to pick");
								showPIPickStart();
													}else {
														showScanLocatioAndAlpha();
													}
		}
			
		 tPIPick.setText("");
		 tPIPick.setFocus();
}
	
	private void submitshowScanLocatioAndAlpha(){
		
		if (tScanLocationandalpha.getText().length()== 8 && tScanLocationandalpha.getText() != null){
			String middle_loc = String.valueOf(tScanLocationandalpha.getText().substring(1, 7));
			
			//	System.out.println (pipicksService.getNumLoc());
				
				if (middle_loc.compareTo(pipicksService.getNumLoc())==0 ){
					//if both alphaloc is the same
					showItemScan();
				} else {
					errorBox ("Wrong Location");
					showScanLocatioAndAlpha();
				}
		}else {
			errorBox ("Wrong Location");	
}		
		tScanLocationandalpha.setText("");
		tScanLocationandalpha.setFocus();
	}
	
	
	//@try need to amend this one...
	private void submitNotFound(){	
		String b = pipicksService.getAlphaLoc();
		//String num_loc = pipicksService.getNumLoc();
	status = "N";	
	String c = pipicksService.getNumLoc();
	if (pipicksService.send2(status))	{
				if	(pipicksService.sendmessage1()){
					//@try
					if  (pipicksService.getDataFlag().compareToIgnoreCase("N")==0) { // trial 1 if the ACK is equal to N and alphaloc is null

			                errorBox("locations have been completed");

			                showPIPickStart();
			               // showScanLocatioAndAlpha();
			                
			                return;
			          }   				            
				} 

				
				if (pipicksService.getAlphaLoc().compareTo(b)==0 && pipicksService.getNumLoc()!= c){
					showScanLocatioAndAlpha();
	
						//System.out.println("compare" + pipicksService.getAlphaLoc());
						//System.out.println ("compare to: " + pipicksService.getAlphaLoc());	
						
				} else if (pipicksService.getAlphaLoc() != pipicksService.getAlphaLoc()){
					showScanLocatioAndAlpha();
		}	
	}
}
	
private void ItemNotfound(){
			
		 String a = pipicksService.getAlphaLoc();
			
			status = "N";	
			String num_loc = pipicksService.getNumLoc();
			if (pipicksService.send2(status))	{
						if	(pipicksService.sendmessage1()){
							//@try
							if  (pipicksService.getDataFlag().compareToIgnoreCase("N")==0) { // trial 1 if the ACK is equal to N and alphaloc is null

					                errorBox("locations have been completed");

					                showPIPickStart();
					               // showScanLocatioAndAlpha();
					                
					                return;
					          }   				            
						} showScanLocatioAndAlpha();
						
						
						if (pipicksService.getAlphaLoc().compareTo(a)==0){
							showItemScan();
										
								
						} else if (pipicksService.getAlphaLoc() != pipicksService.getAlphaLoc()){
							showScanLocatioAndAlpha();

						}
										
			} 	
}

	private void submitshowItemScan(){
		String alpha_loc = pipicksService.getAlphaLoc();
		
		if (tScanItem.getText().length() == 8 && tScanItem.getText() != null){
			String item_loc = pipicksService.getItemNumber().substring(0,8);
		//System.out.println (pipicksService.getItemNumber());
			
			if (tScanItem.getText().compareTo(item_loc) == 0){
				
				pipicksService.send2(pipicksService.getDataFlag());
					if(pipicksService.sendmessage1()){
						
						//@try
						if  (pipicksService.getDataFlag().compareToIgnoreCase("N")==0) { 

				                errorBox("locations have been completed");

				                showPIPickStart();
				               // showScanLocatioAndAlpha();
				                
				                return;
				          }   				            
					} 
				
					
					showScanLocatioAndAlpha();

			} 			
			else {
				errorBox ("Wrong item");
				}
	
			} else{
				errorBox("Wrong item");
		}
		
		//@compare locations then if they are the same show item scan if not
		//display the next location on the ubmitshowScanLocatioAndAlpha()
	if (pipicksService.getAlphaLoc().compareTo(alpha_loc)==0){
			showItemScan();
		
				//System.out.println("compare" + pipicksService.getAlphaLoc());
				//System.out.println ("compare to: " + alpha_loc);
				
				
		} else {
			showScanLocatioAndAlpha();
		}
		
		tScanItem.setText("");
		tScanItem.setFocus();
	}
}
