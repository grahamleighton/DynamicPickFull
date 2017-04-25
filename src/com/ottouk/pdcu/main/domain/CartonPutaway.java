package com.ottouk.pdcu.main.domain;

import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Domain object for <code>CartonPutaway</code> interface. Contains all required
 * request and response fields, and methods for the manipulation thereof.
 * 
 * @author 
 * 
 */
public class CartonPutaway extends Header {

	private String CartonLocation;
	
	private String CartonNumber;

	//private String response;
	
	/**
	 * Builds a PI header message.
	 * @return String containing header message
	 */
	public final String buildMessage() {
		
		String message = new String();
		message += StringUtils.padField(getMessageId(), 1);
		message += StringUtils.padNumber(getUnitId(), 4);
		message += StringUtils.padNumber(getOperator(), 8);
		message += StringUtils.padNumber (CartonLocation.substring(0,9),9);
		message += StringUtils.padNumber(CartonNumber.substring(0,9),9);
		
		
		
		boolean transactOK = transact(message);
		if (transactOK) {
		
	}
		return message;
		
		

}

	private boolean transact(String message) {
		// TODO Auto-generated method stub
		return false;
	}
}