package com.ottouk.pdcu.main.service;

public interface StockAuditService extends GeneralService {

	//Constant
	int RC_ERROR_LOCATION_NOT_VALID = 200;
	String PI_STOCKAUDIT_LOCATION_REQUEST_MESSAGE_ID = "B";
	//String PI_STOCKAUDIT_LOCATION_REQUEST_STATUS = " ";	//Single space
	int RC_ERROR_NO_LOCATION_FOUND = 201;
	int RC_ERROR_NO_FOLLOWING_LOCATION = 205;
	int RC_ERROR_TOTE_NOT_FOUND = 204;
	String PI_EMPTY_BUFFER ="                 ";//18 spaces...
	int RC_ERROR_WRONG_LOCATION = 202;
	String PI_REQUEST_NOTB_Cat = "      ";
	String PI_REQUEST_NOTB_Opt = "   ";
		
	// Methods
	boolean send (String StockAudit);

	//boolean validateScanLocation (String ScanLocation);
	
	//boolean validateAndMaychscanLocation (Integer ScanLocation);
	
	//String getAlpha();
	
	//String getAlphaValue ();

	
	String getAlphaLocation(int index);
	
	boolean ScanLocationValidation (String ScanLocation);
	
	//boolean validate (String ScanLocation );
	
	Integer getNumericLocation (int index);
	
	//String getNumeric(int index);
	
	//boolean sendReasonCode (String Catalogue, String Option, String Qty);
	
	boolean PImessageIDnotB (String StockAudit);
	
	int getLocationCount();
	
	boolean sendFinal(int n, String cat[], String opt[], String quan[] );//String alphaloc[]);
	
	boolean scanLocationvalidation (String scanLocation);
	
	boolean sendFinalPINotB(int a , String Notcat[], String Notopt[],String Notquan[]);
	//boolean sendFinalNotB (int a , String Noncat[], String Nonopt[], String Nonquan[]);
	
	//boolean getMid6Location(String ScanLocation);
}	