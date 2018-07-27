package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.dao.Comms;

public interface GeneralService {
	
	// Constants
	String SOFTWARE_VERSION_NUMBER = "8.0";

	// Return codes
	int RC_OK = 0;
	int RC_COMMS_NOT_CONNECTED = 900;
	int RC_COMMS_TRANSACTION_ERROR = 901;
	int RC_COMMS_RESPONSE_ERROR = 902;
	int RC_COMMS_OTHER_ERROR = 903;

	
	// Methods
	boolean transact(String message);
	
	boolean returnCode(int returnCode);
	
	int getReturnCode();

	Comms getComms();

	void setComms(Comms comms);

	Integer getUnitId();

	void setUnitId(Integer unitId);
	
	String getOperator();

	void setOperator(String operator);
	
	String getVersionFile();

	void setVersionFile(String versionFile);
	
}
