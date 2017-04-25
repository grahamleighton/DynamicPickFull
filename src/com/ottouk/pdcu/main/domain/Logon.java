package com.ottouk.pdcu.main.domain;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Domain object for <code>Logon</code> interface. Contains all required
 * request and response fields, and methods for the manipulation thereof.
 * 
 * @author dis065
 * 
 */
public class Logon extends Header {

	// Request fields
	
	/**
	 * Request field. 
	 */
	private String action;
	
	/**
	 * Request field.
	 */
	private String version;
	
	/**
	 * Request field.
	 */
	private String filler;
	
	/**
	 * Request field.
	 */
	private String aisle;
	
	/**
	 * Activity code.
	 */
	private String activityCode;
	
	// Response fields
	
	/**
	 * Response field.  Reads "ACK" if response is OK.
	 */
	private String response;
	
	/**
	 * Response field.  Timestamp, format DDMMYYHHMM.
	 */
	private String timeStamp;

	//

	/**
	 * Build logon request.
	 * 
	 * @return logon request
	 */
	public String buildLogon() {

		String req = buildHeader();
		req += StringUtils.padField(action, 1);
		req += StringUtils.padField(version, 1);
		req += StringUtils.padField(filler, 40);

		return req;
	}

	/**
	 * Build Activity request.
	 * 
	 * @return activity request
	 */
	public String buildActivity() {

		String req = buildHeader();
		req += StringUtils.padNumber(aisle, 3);
		req += StringUtils.padField(activityCode, 1);
		req += StringUtils.padField(action, 1);

		return req;
	}
	
	/**
	 * Populate response fields.
	 * 
	 * @param response Comms response
	 */
	public void receiveResponse(String response) {
		this.response = StringUtils.getString(response, 0, 3);
		timeStamp = StringUtils.getString(response, 3, 15);
	}

	//
	
	/**
	 * @return action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return filler
	 */
	public String getFiller() {
		return filler;
	}

	/**
	 * @param filler filler
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}

	/**
	 * @return aisle
	 */
	public String getAisle() {
		return aisle;
	}

	/**
	 * @param aisle aisle
	 */
	public void setAisle(String aisle) {
		this.aisle = aisle;
	}

	/**
	 * @return activityCode
	 */
	public String getActivityCode() {
		return activityCode;
	}

	/**
	 * @param activityCode activityCode
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * @return response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response response
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp timeStamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}
