package com.ottouk.pdcu.main.service;

public interface TotePutawayService extends GeneralService {
	
	// Constants
	int LOCATION_LENGTH = 8;
	String LOCATION_PREFIX = "0";
	int TOTE_ITEM_LENGTH = 8;
	int TOTE_ID_LENGTH = 10;
	String TOTE_ID_PREFIX = "800";
	String TOTE_ID_PREFIX_2 = "50";

	
	String TOTE_PUTAWAY_LOCATION_REQUEST_MESSAGE_ID = "b";
	String TOTE_PUTAWAY_LOCATION_REQUEST_STATUS = " ";	//Single space
	String TOTE_PUTAWAY_SUCCESSFUL_MESSAGE_ID = "c";
	String TOTE_PUTAWAY_PI_MESSAGE_ID = "h";
	String TOTE_PUTAWAY_PI_EAN_INDICATOR = "N";
	
	int TOTE_PUTAWAY_MAX_ITEMS = 30;
	
	// Return codes
	int RC_ERROR_LOCATION_NOT_VALID = 200;
	int RC_ERROR_NO_LOCATION_FOUND = 201;
	int RC_ERROR_WRONG_LOCATION = 202;
	int RC_ERROR_INVALID_TOTE = 203;
	int RC_ERROR_TOTE_NOT_FOUND = 204;
	int RC_ERROR_NO_FOLLOWING_LOCATION = 205;
	int RC_ERROR_INVALID_ITEM = 206;
	int RC_ERROR_MAX_ITEMS_IN_TOTE_PUTAWAY = 207;

	
	// Methods
	boolean locationRequest(String location);
	
	String getAlphaLocation();
	
	boolean validatePutawayTote(String toteId);
	
	boolean validatePITote(String toteId);
	
	boolean putaway(String location);
	
	boolean locationFollows();
	
	boolean validatePILocation(String location);
	
	int getToteItemCount();
	
	String getToteId();
	
	boolean addItem(String toteItem);
	
	boolean piRequest();
	
	boolean piLocationFollows();
	
}
