package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.service.LogonService;
import com.ottouk.pdcu.main.service.LogonServiceImpl;
import com.ottouk.pdcu.main.utils.StringUtils;


/**
 * @author dis065
 *
 */


public class LogonShell extends GeneralShell {

//	private LogonShell logon;
	private boolean showLogon;
	private String unitId;
	private String operator;
	private String versionFile;
	
	private StackLayout layout;
	
	private Composite contentPanel;
	private Composite logonPage;
	private Composite menuPage;
	private Composite NextPage;
	
	
	private Text tLogon;
	private Text tMenu;
	private Text tMenuNext;
	
	
	private Button bLogonOK;
	private Button bMenuOK;
	private Button bMenuToteBuild;
	private Button bMenuTotePutaway;
	private Button bMenuConsol;
	private Button bMenuAudit;
	private Button bMenuPicking;
	private Button bMenuCartonPutaway;
	private Button bMenuEscape;
	private Button bReturns;
	private Button bMore;
	private Button bNextPageStock;
	private Button bPIPicks;
	private Button bMenuOKNext;
	private Button bMenuBack;
	private Button bMenuEscapeNext;
	private Button bMenuTopup;
	private Button bMenuDummy;
	public boolean ShowMenu2;
	
	private static final String LOGON_TITLE = "Logon";
	private static final String MENU_TITLE = "Menu";
	private static final String NEXT_PAGE_TITLE = "More";
	
	
	public LogonShell() {
		
		super();
		
	}
	
	
	public LogonShell(String[] args) {
		super(args);
	}
	
	
	public boolean initialise(String[] args) {
		
		if (logonService == null) {
			
			logonService = new LogonServiceImpl();
			System.out.println(args.length);
			switch (args.length) {
			case 0:
				// versionFile is mandatory!
				errorBox(LOGON_TITLE, getErrorMessage(LogonService.RC_ERROR_INSUFFICIENT_ARGS));
				return false;
			case 1:
				// Prompt user for operator number.
				// Default unitId from hostName.
				// Use supplied versionFile.
				showLogon = true;
				versionFile = args[0];
				return true;
			case 2:
				// Use supplied operator number.
				// Default unitId from hostName.
				// Use supplied versionFile.
				showLogon = false;
				ShowMenu2 = false;
				operator = args[0];
				versionFile = args[1];
				return logon();
			default:
				// Use supplied operator number.
				// Use supplied unitId.
				// Use supplied versionFile.
				showLogon = false;
				unitId = args[0];
				operator = args [1];
				versionFile = args[2];
				return logon();
			}
			
		} else {
			//System.out.println("Back from menu");
			//System.out.println(args.length);
			
			//used to force the program to go back to the second page...
			if (args.length > 0) {
				if (args[0] == "1"){
					ShowMenu2 = true;
				} else {
					ShowMenu2 = false;
				}	
			} 

			
//			if (args.length == 0) {
//			ShowMenu2 = true;	
	//		} 
			return true;
		}
	}

		
	public String setTitle() {
		return null;
	}
	
	private String formatTitle(String title) {

		Integer uId = logonService.getUnitId();
		String unitId = null;

		if (uId != null) {
			unitId = StringUtils.padNumber(uId, 4);
		} else if (this.unitId != null) {
			unitId = StringUtils.padNumber(this.unitId, 4);
		} else {
			unitId = StringUtils.padField(null,  5);
		}

		return ("" 
				// Show unit id
				+ unitId 
				+ StringUtils.padField(null, 5)
				// Show title
				+ title 
				+ StringUtils.padField(null, 5)
				// Show software version number 
				+ "V"
				+ LogonService.SOFTWARE_VERSION_NUMBER
				// Indicate if test server
				+ (logonService.getComms().getServer().equals("172.16.8.35") ? "T" : "")
				);
	}
	
	public void handleListenerEvent(Event event) {
		if (event.widget == bLogonOK) {
			logonPageSubmit();
		} else if (event.widget == bMenuEscape) {
			performActivity(LogonService.LOG_OFF_CODE);
		} else if (event.widget == bMenuToteBuild) {
			performActivity(LogonService.BUILD_CODE);
		} else if (event.widget == bMenuTotePutaway) {
			performActivity(LogonService.PUTAWAY_CODE);
		} else if (event.widget == bMenuConsol) {
			performActivity(LogonService.CONSOL_CODE);
		} else if (event.widget == bMenuAudit) {
			performActivity(LogonService.AUDIT_CODE);
		} else if (event.widget == bMenuPicking) {
			performActivity(LogonService.PICKING_CODE);
		} else if (event.widget == bMenuCartonPutaway) {
			performActivity(LogonService.CARTONPUTAWAY_CODE);
		} else if (event.widget == bReturns) {
			performActivity(LogonService.RETURNSPUTAWAY);
		} else if (event.widget == bMenuTopup) {
			performActivity(LogonService.TOTE_TOP_UP);
		} else if (event.widget == bMenuOK) {
			performActivity(tMenu.getText());
		} else if (event.widget == bMore){
			showNextPage();///
		//} else if (event.widget == bMenuBack){
		//	showMenuPage();
		} else if (event.widget == bMenuOKNext){
			performActivity (tMenuNext.getText());
		} else if (event.widget == bMenuEscapeNext){
			showMenuPage();
		} else if (event.widget ==  bNextPageStock ){
			performActivity (LogonService.STOCK_CODE);
		} else if (event.widget == bPIPicks){
			performActivity (LogonService.PI_PICKS_CODE);
		}
	}
	
	public void createContents() {
		
		// create a composite for the pages to share
		contentPanel = new Composite(shell, SWT.NONE);
	    layout = new StackLayout();
	    contentPanel.setLayout(layout);
	    contentPanel.setLayoutData(shell.getLayoutData());
	    //System.out.println("during");
	    
	    
	    // create the logon page
	    logonPage = new Composite(contentPanel, SWT.NONE);
	    GridLayout logonGridLayout = new GridLayout();
	    logonGridLayout.numColumns = 1;
	    logonGridLayout.makeColumnsEqualWidth = true;
	    logonPage.setLayout(logonGridLayout);
	    
		newLabel(logonPage, "Personnel Number");
		
		tLogon = newText(logonPage, LogonService.OPERATOR_LENGTH + 1);
		bLogonOK = newButton(logonPage, "OK");
	    
	    
	    // create the menu page
	    menuPage = new Composite(contentPanel, SWT.NONE);
	    GridLayout menuGridLayout = new GridLayout();
	    menuGridLayout.numColumns = 2;
	    menuGridLayout.makeColumnsEqualWidth = true;
	    menuPage.setLayout(menuGridLayout);
	    
		bMenuToteBuild = newButton(menuPage, "Build");
		bMenuTotePutaway = newButton(menuPage, "Putaway");
		bMenuConsol = newButton(menuPage, "Consol");
		bMenuAudit = newButton(menuPage, "Audit");
		bMenuPicking = newButton(menuPage, "Picking");
		bReturns =  newButton(menuPage, "IPA");
		bMenuCartonPutaway =  newButton(menuPage, "CPA");
		
		
		bMore = newButton(menuPage,"More");
		Group group = new Group(menuPage, SWT.NONE);
		group.setText("Select Activity");
		group.setFont(new Font(DISPLAY, "Arial", 10, SWT.BOLD));
		
		GridLayout groupGridLayout = new GridLayout();
		groupGridLayout.numColumns = 2;
		group.setLayout(groupGridLayout);
		group.setLayoutData(shell.getLayoutData());

		tMenu = newText(group, LogonService.ACTIVITY_LENGTH);
		bMenuOK = newButton(group, "OK");

		bMenuEscape = newButton(menuPage, "Quit");
		
		
		//create the nextpage
		NextPage = new Composite (contentPanel, SWT.NONE);
		GridLayout nextGridLayout = new GridLayout();
		nextGridLayout.numColumns = 2;
		//nextGridLayout.makeColumnsEqualWidth = true;
		NextPage.setLayout (nextGridLayout);
		
		
		bNextPageStock = newButton (NextPage, "PI");
		bPIPicks = newButton (NextPage," PI Picks");
		bMenuTopup = newButton (NextPage,"Top Up");
		bMenuDummy = newButton (NextPage,"(Empty)");
		//bMenuBack = newButton (NextPage, "Back");
	
		
		Group Nextgroup = new Group(NextPage, SWT.NONE);
		Nextgroup.setText("Select Activity");
		Nextgroup.setFont(new Font(DISPLAY, "Arial", 10, SWT.BOLD));
		
		
		GridLayout NextgroupGridLayout = new GridLayout();
		NextgroupGridLayout.numColumns = 2;
		Nextgroup.setLayout(NextgroupGridLayout);
		//Nextgroup.layout( changed);
		Nextgroup.setLayoutData(shell.getLayoutData());

		tMenuNext = newText(Nextgroup, LogonService.ACTIVITY_LENGTH);
		bMenuOKNext = newButton(Nextgroup, "OK");
	
		
		bMenuEscapeNext = newButton(NextPage, "Back");
		
		// set start page
		if (showLogon) {
			showLogonPage();
		} else if (ShowMenu2) {
			showNextPage();		
		} else {
			showMenuPage();
		}
	}
	
	private void logonPageSubmit() {
		
		operator = tLogon.getText();
		
		if (logonService.validateOperator(operator)) {
			
			if (logon()) {
				showMenuPage();
			} else {
				shellClose();
			}
			
		} else {
			errorBox(LOGON_TITLE);
			showLogonPage();
		}
	}
	
	
	
	private void showLogonPage() {

		if (layout.topControl != logonPage) {
			layout.topControl = logonPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bLogonOK);
		    resetTitle(formatTitle(LOGON_TITLE));
		}
		
		tLogon.setText("");
		tLogon.setFocus();
	}
	
	void showMenuPage() {

		if (layout.topControl != menuPage) {
			layout.topControl = menuPage;
		    contentPanel.layout();
		    shell.setDefaultButton(bMenuOK);
		    resetTitle(formatTitle(MENU_TITLE));
		}
		
		tMenu.setText("");
		tMenu.setFocus();
		//System.out.println("showmenupage");
	}
	
	 void showNextPage(){
		if (layout.topControl !=NextPage){
			//System.out.println("shownextopage x");
			layout.topControl = NextPage;
			contentPanel.layout ();
			shell.setDefaultButton(bMenuOKNext);
			resetTitle (formatTitle(NEXT_PAGE_TITLE));
			
		} 
		//System.out.println("shownextopage");
		tMenuNext.setText("");
		tMenuNext.setFocus();
		//System.out.println("shownextopage 2");
	}
	
	private void performActivity(String text) {
		
		int code = -1;
		try {
			code = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			// do nothing
		} finally {
			performActivity(code);
		}
	}
	
	private void performActivity(int code) {
		
		String args[] = new String[3];

		switch (code) {
		
		case LogonService.BUILD_CODE:
			shellClose();
			new ToteBuildShell();
			args[0] = "0";
			new LogonShell(args);
			break;
		case LogonService.PUTAWAY_CODE:
			shellClose();
			new TotePutawayShell();
			args[0] = "0";
			new LogonShell(args);
			break;
		case LogonService.CONSOL_CODE:
//			tMenu.setText("");
//			tMenu.setFocus();
			shellClose();
			new ToteConsolShell();
			args[0] = "0";
			new LogonShell(args);
			break;
		case LogonService.AUDIT_CODE:
			shellClose();
			new ToteAuditShell();
			args[0] = "0";
			new LogonShell(args);
//			tMenu.setText("");
//			tMenu.setFocus();
			break;
		case LogonService.PICKING_CODE:
//			tMenu.setText("");
//			tMenu.setFocus();
			shellClose();
			new PickingShell();
			args[0] = "0";
			new LogonShell(args);
			break;
		case LogonService.CARTONPUTAWAY_CODE:
			shellClose();
		    new CartonPutawayShell();
		    args[0] = "0";
		    new LogonShell(args);
			break;

		case LogonService.OPERATOR_COUNTS_CODE:
			shellClose();
		    new OpCountsShell();
		    args[0] = "0";
		    new LogonShell(args);
			break;
		case LogonService.RETURNSPUTAWAY:
			shellClose();
		    new ItemPutawayShell();
		    args[0] = "0";
		    new LogonShell(args);
			break;
		case LogonService.TOTE_TOP_UP:
			shellClose();
		    new ToteTopupShell();
		    args[0] = "0";
		    new LogonShell(args);
			break;
		case LogonService.STOCK_CODE:
			args[0] = "1";
			shellClose();
			new StockAuditShell();
			new LogonShell(args);
			break;
		//need int value:	
		case LogonService.PI_PICKS_CODE:
			args[0] = "1";
			shellClose();
			new PIPicksShell();
			new LogonShell(args);
			//showNextPage();
			//new showNextPage();
			//logon.showNextPage();
			//new NextPage;
			//LogonShell.disposeDisplay();
			//LogonShell.this.showNextPage();
			break;
			
		case LogonService.LOG_OFF_CODE:
			if (questionBox("Are you sure you want to quit?") == SWT.YES) {
				logoff();
			} else {
				tMenu.setText("");
				tMenu.setFocus();
			}
			break;
		case LogonService.LOG_OFF_CODE_PUTAWAY:
			if (questionBox("Are you sure you want to quit?") == SWT.YES) {
				logoff();
			} else {
				tMenu.setText("");
				tMenu.setFocus();
			}
			break;
		default:
			tMenu.setText("");
			tMenu.setFocus();
			break;
		}
	}
	
	protected int errorBox(String text) {
		return errorBox(text, getErrorMessage(logonService.getReturnCode()));
	}
	
	private String getErrorMessage(int returnCode) {
		
		String errorMessage;

		switch (returnCode) {
		case LogonService.RC_ERROR_INSUFFICIENT_ARGS:
			errorMessage = "Insufficient parameters to logon";
			break;
		case LogonService.RC_ERROR_UNIT_ID:
			errorMessage = "Invalid unit id:\n"
					+ (unitId == null ? "" + logonService.getUnitId() : unitId);
			break;
		case LogonService.RC_ERROR_OPERATOR_NUMBER:
			errorMessage = "Personnel number must be 8 digits";
			break;
		case LogonService.RC_ERROR_VERSION_FILE:
			errorMessage = "Missing or invalid file:\n"
					+ StringUtils.replace(versionFile, "/", " /").trim();
			break;
		//case LogonService.RC_ERROR_CONNECT:
		//case LogonService.RC_ERROR_DISCONNECT:
		case LogonService.RC_COMMS_NOT_CONNECTED:
		case LogonService.RC_COMMS_TRANSACTION_ERROR:
		case LogonService.RC_COMMS_RESPONSE_ERROR:
		case LogonService.RC_COMMS_OTHER_ERROR:
			errorMessage = "Network communication error.\nPlease re-try.";
			break;
		default:
			errorMessage = "Unexpected logon error: " + returnCode;
			break;
		}
		
		return errorMessage;
	}
	
	private boolean logon() {
		
		boolean logonOK;
		while (true) {
			
			if (unitId == null) {
				logonOK = logonService.logon(operator, versionFile);
			} else {
				logonOK = logonService.logon(new Integer(unitId), operator, versionFile);
			}
			
			if (logonOK) {
				// Logon successful
				StringUtils.log("logged on");
				return true;
			} else {

				// Error
				int rc = logonService.getReturnCode();
				if ((rc == LogonService.RC_COMMS_TRANSACTION_ERROR) || (rc == LogonService.RC_ERROR_CONNECT)) {
					if (questionBox(LOGON_TITLE, "Unable to logon.\nRetry?") == SWT.NO) {
						return false;
					} else {
						showWaitCursor();
					}
				} else {
					// Show other error
					errorBox(LOGON_TITLE);
					return false;
				}
				
			}
			
		}
	}
	
	private void logoff() {

		while (true) {
			if (logonService.logoff()) {
				// Logoff successful
				StringUtils.log("logged off");
				break;
			} else {

				// Error
				int rc = logonService.getReturnCode();
				if ((rc == LogonService.RC_COMMS_TRANSACTION_ERROR)
						|| (rc == LogonService.RC_ERROR_DISCONNECT)) {
					if (questionBox("Unable to logoff.\nRetry?") == SWT.NO) {
						// Continue without logoff
						break;
					} else {
						showWaitCursor();
					}
				} else {
					errorBox(MENU_TITLE);
					// Continue without logoff
					break;
				}

			}
		}

		shellClose();
	}
	
}
