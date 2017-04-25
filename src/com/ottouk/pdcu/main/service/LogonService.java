package com.ottouk.pdcu.main.service;

public interface LogonService extends GeneralService {
	
	// Constants
	int OPERATOR_LENGTH = 8;
	int ACTIVITY_LENGTH = 2;
	
	String LOGON_MESSAGE_ID = "H";
	String LOGON_VERSION = "J";
	String LOGON = "0";
	String LOGOFF = "r";
	
	String ACTIVITY_MESSAGE_ID = "I";
	String ACTIVITY_AISLE = "000";
	String PICKING_WALK = "C";
	String TOTE_AUDIT = "P";
	String TOTE_BUILD = "X";
	String TOTE_CONSOL = "f";
	String TOTE_PUTAWAY = "X";
	String ITEM_PUTAWAY = "B";
	String CARTON_PUTAWAY = "A";
	String START = "0";
	String STOP = "r";
	String STOCK_AUDIT = "L";
	String PI_PICKS = "j";
	
	
	int BUILD_CODE = 40;
	int PUTAWAY_CODE = 41;
	int CONSOL_CODE = 42;
	int AUDIT_CODE = 43;
	int PICKING_CODE = 31;
	int OPERATOR_COUNTS_CODE = 66;
	int LOG_OFF_CODE = 79;
	int LOG_OFF_CODE_PUTAWAY = 99;
	int CARTONPUTAWAY_CODE = 17;
	int RETURNSPUTAWAY = 23;
	int STOCK_CODE = 48;
	int TOTE_TOP_UP = 21;
	
	
	//need to know the code for PI_PICKS
	int PI_PICKS_CODE = 45;

	// Return codes
	int RC_ERROR_INSUFFICIENT_ARGS = 201;
	int RC_ERROR_UNIT_ID = 202;
	int RC_ERROR_OPERATOR_NUMBER = 203;
	int RC_ERROR_VERSION_FILE = 204;
	int RC_ERROR_CONNECT = 205;
	int RC_ERROR_DISCONNECT = 206;
	
	
	boolean validateOperator(String operator);
	
	
	boolean logon(String operator, String versionFile);
	
	boolean logon(Integer unitId, String operator, String versionFile);
	
	boolean logon(Integer unitId, String operator, String server, int basePort, int channels);


	boolean logoff();
	
	
	boolean startPickingWalk();

	boolean stopPickingWalk();

	boolean startToteAudit();

	boolean stopToteAudit();

	boolean startToteBuild();

	boolean stopToteBuild();

	boolean startToteConsol();

	boolean stopToteConsol();

	boolean startTotePutaway();

	boolean stopTotePutaway();
	
	boolean startItemPutaway();
	
	boolean stopItemPutaway();
	
	boolean startCartonPutaway();
	
	boolean stopCartonPutaway();
	
	boolean startStockAudit();
	
	boolean stopStockAudit();
	
	boolean startPIPicks();
	
	boolean stopPIPicks();
	
	
	
}
