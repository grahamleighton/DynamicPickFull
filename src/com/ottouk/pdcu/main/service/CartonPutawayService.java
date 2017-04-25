package com.ottouk.pdcu.main.service;



public interface CartonPutawayService extends GeneralService {

	//Constant
	String CARTON_PUTAWAY_PI_MESSAGE_ID = "A";
	
	// Methods
	boolean cartonvalidation(String CartonNumber);

	boolean LocationValidation(String CartonLocation);

	boolean LocationNumberValidation(String LocationNumber );

	boolean validate(String CartonLocation, String LocationNumber, String CartonNumber);
	
	
	
	
	
}
