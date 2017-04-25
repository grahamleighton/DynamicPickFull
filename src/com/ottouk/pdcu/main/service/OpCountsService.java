package com.ottouk.pdcu.main.service;

public interface OpCountsService extends GeneralService {
	
	// Constants
	String OP_COUNTS_MESSAGE_ID = "o";
	int OP_COUNTS_FIELD_LENGTH = 4;
	
	// Return codes
	int RC_ERROR_NO_OP_COUNTS = 200;

	
	// Methods
	boolean requestOpCounts();
	
	String getBuild();

	String getBuilt();

	String getPutaway();

	String getTotePutawayItems();

	String getConsolLocations();

	String getPILocations();

	String getPIItems();

	String getConsolItems();

}
