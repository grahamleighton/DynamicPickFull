package com.ottouk.pdcu.main.ui;

//import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
//import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



//import com.ottouk.pdcu.main.domain.StockAudit;
//import com.ottouk.pdcu.main.service.LogonService;
import com.ottouk.pdcu.main.service.StockAuditService;
import com.ottouk.pdcu.main.service.StockAuditServiceImpl;
import com.ottouk.pdcu.main.utils.StringUtils;


public class StockAuditShell extends GeneralShell {

	private StockAuditService stockauditService;
//	private StockAudit stock;
	//private StockAuditServiceImpl st;
	private String numeric;

	
	private StackLayout layout;

	private Composite contentPanel;
	private Composite pStockPIStart; //show the start of the PI Request Type...
	private Composite   pNotB;

	private Composite pScanLocation;
	//private Composite pShowReasonCode;

	//private Composite pPutawayLocation;
	//private Composite pPILocation;
	private Composite pReasonCode;
        private Composite pFinal;
        private Composite pFinalNotB;
        
        //private Composite pPItype;
        
	private Label lStockPI;
	//private Label lPutawayLocation;
	///private Label lPILocation;


	private Text tStockPIStart;
	private Text tScanLocation;
	private Text tNotBqty;



	//private Text tPutawayLocation;
	//private Text tPILocation;
	private Text tReasonCodeCat;
	private Text tReasonCodeOpt;
	private Text tReasonCodeQuantity;
	private Text tNotBcat;
	//private Text tNotopt;


	private Button bStockPIStartOK;
	private Button bStockPIStartEscape;
	private Button bScanLocationOK;
	//private Button bPutawayTotePI;
	private Button bScanLocationEscape;
	private Button bReasonCodeOK;
	private Button bReasonCodeEscape;
	private Button bNotB;
	private Button bNotBEscape;
        private Button bFinalContinue;
        private Button bFinalEscape;
        private Button bFinalContinueNotB;
        private Button bFinalEscapeNotB;
        
       /*
        //PI type button...
        private Button bA;
        private Button bB;
        private Button bC;
        private Button bD;
        */
        
        
	private Text tNotBopt;

        private String reasonCode;
        private String cat[], opt[], quan[]; //alphaloc[];
        private int counts;

	public static int n1;

	private static final String STOCK_AUDIT_TITLE = "Stock Audit";
	
	public boolean initialise(String[] args) {

		while (true) {
			if (logonService.startStockAudit()) {
				stockauditService = new StockAuditServiceImpl();
				return true;
			} else {
				if (questionBox(STOCK_AUDIT_TITLE,
						"Unable to start " + STOCK_AUDIT_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}
	}

	public String setTitle() {
		return STOCK_AUDIT_TITLE;
	}

	
	//uses to add triggers on buttons..
	public void handleListenerEvent(Event event) {
		if (event.widget == bStockPIStartOK) {
			submitPStockPIstart();
		} else if (event.widget == bStockPIStartEscape) {
			escape();

		} else if (event.widget == bScanLocationEscape) {
			showPStockPIStart();

		} else if (event.widget == bScanLocationOK) {
			submitAndValidate();
		}else if (event.widget == bReasonCodeOK){
			 submitReasonCode();
		}else if (event.widget == bNotB){
                    submitReasonCode2();
                }else if (event.widget ==bReasonCodeEscape){
			escape();
		} else if (event.widget == bNotBEscape){
			escape();
		} else if (event.widget == bFinalEscape){
			showPStockPIStart();
                } else if (event.widget == bFinalContinue){
                    submitFinal();
                } 
                
                else if (event.widget == bFinalEscapeNotB){
                	showPStockPIStart();
                } else if (event.widget == bFinalContinueNotB){
                	submitNonFinal();
                } /**else if (event.widget == bA){
                	tStockPIStart.setText("A");
                	submitsend ();
        			showPScanLocation();
                	
                	} else if (event.widget ==bB){
                		tStockPIStart.setText("B");
                		submitsend ();
            			showPScanLocation();
                	} else if (event.widget ==bC){
                		tStockPIStart.setText("C");
                		submitsend ();
            			showPScanLocation();
                	} else if (event.widget == bD){
                		tStockPIStart.setText("D");
                		submitsend ();
            			showPScanLocation();
                	}*/
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

		GridLayout layout = new GridLayout (2,false);
		
		// create page pStockPIStart for inputing type of PI...
		pStockPIStart = new Composite(contentPanel, SWT.NONE);
		pStockPIStart.setLayout(gridLayout);
		newLabel(pStockPIStart, "Type of P.I.");
		tStockPIStart = newText(pStockPIStart, 1);
		//pPItype = new Composite (contentPanel,SWT.NONE);
		//pPItype.setLayout(layout);
		
		
		//Group PIgroup = new Group(pStockPIStart, SWT.NONE);
		//PIgroup.setText("PI type");
		//PIgroup.setFont(new Font(DISPLAY, "Arial", 10, SWT.BOLD));
		
	//	bA = newButton (pStockPIStart, "A"); bB = newButton(pStockPIStart, "B");
		//bC = newButton (pStockPIStart, "C") ; bD = newButton (pStockPIStart,"D");
		
		//GridLayout groupGridLayout = new GridLayout();
		//groupGridLayout.numColumns = 2;
		//PIgroup.setLayout(groupGridLayout);
		//PIgroup.setLayoutData(shell.getLayoutData());
	    //groupGridLayout.makeColumnsEqualWidth = true;
		
		bStockPIStartOK = newButton(pStockPIStart, "OK");
		bStockPIStartEscape = newButton(pStockPIStart, "Escape");



		// create page pPScanLocation for scanning location..
		pScanLocation = new Composite(contentPanel, SWT.NONE);
		pScanLocation.setLayout(gridLayout);
		lStockPI = newLabel(pScanLocation, "Scan Location: ");
		tScanLocation = newText(pScanLocation, 8);
		bScanLocationOK = newButton(pScanLocation, "OK");
		//bPutawayTotePI = newButton(pScanLocation, "PI");
		bScanLocationEscape = newButton(pScanLocation, "Escape");



		//create a page for pReasonCode...
		pReasonCode = new Composite (contentPanel, SWT.NONE);
		pReasonCode.setLayout(layout);
		//newLabel (pReasonCode , "ReasonCode");
		
		newLabel (pReasonCode, "Catalogue: ");
		tReasonCodeCat = newText (pReasonCode, 6);
		newLabel (pReasonCode, "Size: ");
		tReasonCodeOpt = newText (pReasonCode, 3); //option number will now be 3...
		newLabel (pReasonCode, "Quantity:");
		tReasonCodeQuantity = newText (pReasonCode, 5 ); 
		
		//pReasonCode = new Composite (contentPanel, SWT.NONE);
		//pReasonCode.setLayout(gridLayout);
		bReasonCodeOK = newButton (pReasonCode, "OK");
		bReasonCodeEscape = newButton (pReasonCode, "Escape");

		//create a page for the PI type not equal to B
		pNotB = new Composite (contentPanel, SWT.NONE);
		pNotB.setLayout(layout);
		//Label label = new Label (pNotB, SWT.NONE);
		//label.setText("Catologue #: ");
		//Text  text = new  Text (pNotB, SWT.NONE);
		newLabel (pNotB, "Catalogue: ");
		tNotBcat = newText (pNotB,6);
		newLabel (pNotB, "Size: ");
		tNotBopt = newText (pNotB, 3);
		newLabel (pNotB, "Quantity: ");
		tNotBqty = newText (pNotB, 5);
		bNotB = newButton (pNotB, "OK");
		bNotBEscape = newButton (pNotB, "Escape");
		
                pFinal = new Composite (contentPanel, SWT.NONE);
		pFinal.setLayout(gridLayout);
		newLabel (pFinal, "All the PI's have been");
                newLabel (pFinal, "completed!");
		bFinalContinue = newButton (pFinal, "Continue");
		bFinalEscape = newButton (pFinal, "Escape");

                cat  = new String[11];
                opt  = new String[11];
                quan = new String[11];
                //alphaloc = new String [11];
		// set start page

                
                pFinalNotB = new  Composite (contentPanel, SWT.NONE);
                pFinalNotB.setLayout (gridLayout);
                newLabel (pFinalNotB,"All the PI's have been ");
                newLabel (pFinalNotB, "completed");
                bFinalContinueNotB = newButton (pFinalNotB, "Continue");
                bFinalEscapeNotB = newButton (pFinalNotB, "Escape");
                
                	cat = new String[11];
                	opt = new String [11];
                	quan = new String [11];   
                
		showPStockPIStart();
	}

	private void escape() {

		while (true) {
			if (logonService.stopStockAudit()) {
				break;
			} else {
				if (questionBox(STOCK_AUDIT_TITLE,
						"Unable to stop " + STOCK_AUDIT_TITLE + ".\nRetry?") == SWT.NO) {
					// Quit anyway
					break;
				} else {
					showWaitCursor();
				}
			}
		}

		shellClose();
	}

	
	
	

//when the user click the OK button on the PI type GUI ( bStockPIStartOK ) this function will tigger...
	private void submitPStockPIstart() {
		
		tStockPIStart.setText(tStockPIStart.getText().toUpperCase());
		
		
		if (tStockPIStart!=null && tStockPIStart.getText().length()>0 && tStockPIStart.getText().charAt(0)>='A' && tStockPIStart.getText().charAt(0)<='Z'){
			//submitPLocationRequest();
				
			tStockPIStart.setText(tStockPIStart.getText().toUpperCase());
			
			submitsend ();
			showPScanLocation();
				
			
		//}else if (tStockPIStart.getText().length() == 0 ){
		//	errorBox ("Enter a PI type");
			
		}else{
		
			errorBox ("Enter a PI type");
		}
		
		tStockPIStart.setText("");
		tStockPIStart.setFocus();
	
	}

	private void submitsend (){
		if (stockauditService.send(tStockPIStart.getText())){
                    reasonCode = tStockPIStart.getText();
                    
		}
	}

	//show the GUI of the PI type where the user can input the PI type on the textbox...
	//there is also 2 buttons which are OK and escape button..
	//if the PI type is correct it will open another screen (ShowPScanLocation ())...
	private void showPStockPIStart() {

		if (layout.topControl != pStockPIStart) {
			layout.topControl = pStockPIStart;
			contentPanel.layout();
			shell.setDefaultButton(bStockPIStartOK);
			resetTitle(STOCK_AUDIT_TITLE);
			
			
		}

                // counts = 0;
		tStockPIStart.setText("");
		tStockPIStart.setFocus();
	}
	
	
	/**public boolean RandomAlphaAndNum(){
		if (tScanLocation.getText ())
	}*/
	
	//this GUI will open to ask the user to scan a location using a textbox..
	// it also have two buttons which are bScanLocationOK and Escape..
	//when the user click the OK button (bScanLocationOK) and the location is OK another screen will open if the PI type
	// is B then showReasonCode() GUI will open , if the PI type is B, C and D then showPItypeNotB () will show..
	public void showPScanLocation() {
	
		int limit = stockauditService.getLocationCount();
                if (counts < limit)
                {
                    Integer n = stockauditService.getNumericLocation(counts);
                     numeric = String.valueOf(n);
                     	if (numeric.length() < 6 )
                                    for (int i = 0; i < 6 - numeric.length(); i++)
                                            numeric = "0" + numeric;

                    //StringUtils.log ("show Alpha Location on Scan Location Page ");
                    StringUtils.log ("Scan + alphaLocation");
                    
                    lStockPI.setText(" Scan " + stockauditService.getAlphaLocation(counts).substring(0, 4) + "-" +stockauditService.getAlphaLocation(counts) .substring(4));
                    
                    
                    System.out.println (stockauditService.getAlphaLocation (counts));
                    System.out.println(numeric);

                    if (layout.topControl != pScanLocation) {
                            layout.topControl = pScanLocation;
                            contentPanel.layout();
                            shell.setDefaultButton(bScanLocationOK);
                            resetTitle(STOCK_AUDIT_TITLE);
                            
                            tScanLocation.addListener(SWT.Verify, new Listener() {
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

                    tScanLocation.setText("");
                    tScanLocation.setFocus();
                }else if (reasonCode.equals(stockauditService.PI_STOCKAUDIT_LOCATION_REQUEST_MESSAGE_ID)){
                    showPFinal();
                    
                } else if (reasonCode != stockauditService.PI_STOCKAUDIT_LOCATION_REQUEST_MESSAGE_ID){
                	showPFinalNotB();
                }
                   
        }

//}
	//this function will trigger when the user click on the OK button(bScanLocationOK) 
	//on the showPScanLocation..
	//if the the PI type is equal to B then the showReasonCode() will open or 
	// if it is not equal to B then another screen will open..
	private void submitScanLocation(){	
		if (tScanLocation.getText().length() == 8){
			String middle_num = String.valueOf(tScanLocation.getText().substring(1,7));		
                                        
                       if (middle_num.compareTo(numeric) == 0){
                    	   
               
                                if (reasonCode.equals(stockauditService.PI_STOCKAUDIT_LOCATION_REQUEST_MESSAGE_ID) ){
                                        showReasonCode();
                                        
                                } else {
                                        showPItypeNotB(); // if reason code is equal to A, C, D
                                }
                        }else {
                        	errorBox ("Wrong Location");
                        }
			tScanLocation.setText("");
			tScanLocation.setFocus();
                       }
			}
	
	//GUI for the reason code which PI type is B..
	//For the quantity its needed to have a 0 always..
	//the user can leave the catalogue and option number null  but not the quantity...
	private void showReasonCode(){
		if (layout.topControl != pReasonCode){
			layout.topControl = pReasonCode;
			contentPanel.layout();
			shell.setDefaultButton(bReasonCodeOK);
			
			tReasonCodeQuantity.addListener(SWT.Verify, new Listener() {
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
		tReasonCodeQuantity.setText("");
		tReasonCodeCat.setText("");
		tReasonCodeOpt.setText("");
		tReasonCodeCat.setEditable(false);
		tReasonCodeOpt.setEditable(false);
		tReasonCodeQuantity.setFocus();
		
		
	}
	
	//GUI from the reasoncode whic PI type is A,C and D...
	private void showPItypeNotB (){	
		
		if (layout.topControl != pNotB){
			layout.topControl = pNotB;
			contentPanel.layout();
			shell.setDefaultButton(bNotB);
			
			//function used so that the user can't enter a character in the numeric field.
			tNotBqty.addListener(SWT.Verify, new Listener() {
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
		
		
		tNotBcat.setText ("");
		tNotBopt.setText ("");
		tNotBqty.setText("");
		tNotBcat.setText(tNotBcat.getText().toUpperCase());
		tNotBopt.setText(tNotBopt.getText().toUpperCase());
		tNotBcat.setFocus();
		
}

	//this is the real code..
	private void submitAndValidate (){
		if (tScanLocation.getText().length()== 8){
			submitScanLocation();
		
		}else{
			errorBox ("Wrong Location");
		}
		tScanLocation.setText("");
		tScanLocation.setFocus();
	}
	
	//submitting and validating scanned location...
	private void submitReasonCode (){
		//ask if this is a user input (textbox ) or label..
		//if (stockauditService.sendReasonCode(tReasonCodeCat.getText(), tReasonCodeOpt.getText(), tReasonCodeQuantity.getText())){
			//errorBox ("Save");
                if (StockAuditShell.isNumeric(tReasonCodeQuantity.getText()) == true)
                {
                        cat[counts] = tReasonCodeCat.getText();
                        opt[counts] = tReasonCodeOpt.getText();
                        quan[counts] = tReasonCodeQuantity.getText();
                        counts ++;
                        showPScanLocation();
                }else{
                    errorBox ("Quantity must be valid number.");
                    tNotBqty.setFocus();
                }
		 //else {
		//	errorBox ("Quantity is null");
		//}
            //    tReasonCodeQuantity.setFocus();
           //     tReasonCodeCat.setText("");
           //     tReasonCodeOpt.setText("");
           //     tReasonCodeQuantity.setText ("");
	}


        private void submitReasonCode2 (){
		//ask if this is a user input (textbox ) or label..
		//if (stockauditService.sendReasonCode(tNotBcat.getText(), tNotBopt.getText(), tNotBqty.getText())){
			//errorBox ("Save");
                
                if (StockAuditShell.isNumeric(tNotBqty.getText()) == true)
                {
                    if (validateReasonCodes()){
                        cat[counts] = tNotBcat.getText();
                        opt[counts] = tNotBopt.getText();
                        quan[counts] = tNotBqty.getText();
                        counts ++;

                        showPScanLocation();
                    }
                }else{
                    errorBox ("Quantity must be valid number.");
                    tNotBqty.setFocus();
                }
                
	//	} else {
		//	errorBox ("Quantity is null");
		//}
           //     tNotBqty.setFocus();
       //     
              //  tNotBqty.setFocus();
	}

        private void showPFinal() {
            if (layout.topControl != pFinal) {
                    layout.topControl = pFinal;
                    contentPanel.layout();
                    shell.setDefaultButton(bFinalContinue);
            }
	}

        private void submitFinal()
        {
            if (stockauditService.sendFinal(counts, cat, opt, quan)){//alphaloc)){
                counts = 0;
              //  errorBox("End");
                showPStockPIStart();
            }
        }
      
        
        private void showPFinalNotB(){
        	if (layout.topControl != pFinalNotB){
        		layout.topControl = pFinalNotB;
        		contentPanel.layout();
        		shell.setDefaultButton(bFinalContinueNotB);
        		}
        }
        
        
      // for non B...
        private void submitNonFinal(){
        	if (stockauditService.sendFinalPINotB(counts, cat, opt, quan)){
        		counts = 0;
        		//errorBox ("End");
        		showPStockPIStart();
        	}
        }
        
        public static String ltrim(String source) {
            return source.replaceAll("^\\s+", "");
        }

        //to check if the input is numeric or not.. 
        public static boolean isNumeric(String s)
        {
            try
            {
                int i = Integer.parseInt(s);
            }
            catch(NumberFormatException nfe)
            {
                return false;
            }

            return true;
        }

        
        //reason code validation..
        private boolean validateReasonCodes()
        {
            String cat = tNotBcat.getText();
            String opt = tNotBopt.getText();
            String qty = tNotBqty.getText();

            if (cat.compareTo("") != 0 && (qty.compareTo("") == 0 || Integer.valueOf(qty).intValue() <= 0))
            {
                errorBox ("Quantity must be greater than 0.");
                
            }
           
            else{
                if (cat.compareTo("") == 0 && opt.compareTo("") == 0 && (qty.compareTo("") == 0 || Integer.valueOf(qty).intValue() != 0))
                {
                    errorBox ("Missing Details.");
                    
                }else{
                    if (opt.compareTo("") != 0 && cat.compareTo("") == 0 && qty.compareTo("") == 0)
                    {
                        errorBox ("Please enter cat and opt");
                       
                    }else{
                    	if (opt.compareTo("") != 0 && cat.compareTo("") ==0  && qty.compareTo("") != 0 )
                    	{
                    		 errorBox ("must enter a cat value");
                    		
                    	}
                    
                    else
                    {
                        return true;
                    	}
                    } tNotBcat.setFocus();
                } tNotBcat.setFocus();
            }tNotBcat.setFocus();
            
            return false;
        }
}

