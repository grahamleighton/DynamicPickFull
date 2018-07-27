package com.ottouk.pdcu.main.service;

public interface LocationDetailService {

	
	
	
	String getNumericLocation();
	String getNumericLocation8();
	String getAlphaLocation();
	
	boolean isNumericLocationValid(String loc);
	boolean isAlphaLocationValid(String loc);
	
	boolean isLocationValidNumericOrAlpha(String loc);
	
	
	boolean getLocationDetails(String loc );
	

	

}
