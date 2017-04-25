package com.ottouk.pdcu.main.service;

public interface PIPicksService extends GeneralService {

	boolean send (String PIpicks);
	
	
	String getDataFlag();
	
	
	String getAlphaLoc ();
	
	
	String getNumLoc();
	
	
	boolean send2 (String status);
	
	
	String getItemNumber ();
	
	boolean sendmessage1();
	
	
	
}	