package com.ottouk.pdcu.main.service;

public interface ToteBuildService extends GeneralService {
	
	// Constants
	int TOTE_ITEM_LENGTH = 8;
	int TOTE_ID_LENGTH = 10;
	String TOTE_ID_PREFIX = "800";
	
	String TOTE_BUILD_MESSAGE_ID = "a";
	String TOTE_BUILD_TYPE = "N";
	String TOTE_BUILD_EAN_INDICATOR = "N";
	
	int MAX_ITEMS_IN_TOTE = 15;
	
	// Return codes
	int RC_WARN_TOTE_PREVIOUSLY_USED = 100;
	int RC_WARN_ITEM_PREVIOUSLY_USED = 101;
	int RC_WARN_MIS_SCAN = 102;
	int RC_WARN_TOTE_FULL = 103;
	
	int RC_ERROR_INVALID_SCAN = 200;
	int RC_ERROR_INVALID_TOTE = 201;
	int RC_ERROR_MAX_ITEMS_IN_TOTE = 202;
	int RC_ERROR_TOTE_STILL_OPEN = 203;

	
	// Methods
	boolean checkTotePreviouslyUsed(String toteId);
	
	boolean addTote(String toteId);
	
	boolean addToteAgain(String toteId);
	
	boolean addScan(String scan);

	int getToteItemCount();
	
	
	String getToteId();
	
	int getReturnCode();
	
	boolean addScanUnlimited(String scan);

	boolean endTote(String toteId);
	
	/**
	 * Sets the tote type.
	 * 
	 * @param toteType either "N" (Normal) or "C" (Consol)
	 */
	void setToteType(String toteType);
	
	/**
	 * Test whether the id passed is a valid item.
	 * @param id the id passed.
	 * @return true if valid
	 */
	boolean validToteItem(String id);
	
	/**
	 * Method to empty list of tote ids used.
	 * Mainly to provide functionality to Consolidation activity.
	 */
	void clearList();
	
	
}

