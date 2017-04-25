package com.ottouk.pdcu.main.service;

/**
 * Interface for ToteAuditService.
 * @author hstd004
 *
 */
public interface ToteAuditService extends GeneralService {
	
	// Constants
	/**
	 * Location length.
	 */
	int LOCATION_LENGTH = 8;
	/**
	 * Length of standard return item. 
	 */
	int TOTE_ITEM_LENGTH = 8;
	/**
	 * Length of tote id.
	 */
	int TOTE_ID_LENGTH = 10;
	/**
	 * Tote ID prefix used for validation.
	 */
	String TOTE_ID_PREFIX = "800";
	/**
	 * Tote ID 2nd prefix used for validation.
	 */
	String TOTE_ID_PREFIX_2 = "50";
	/**
	 * Location stub length - used to extract first 4 characters of location.
	 */
	int LOCATION_STUB_LENGTH = 4;
	/**
	 * Length of alphanumeric location .
	 */
	int ALPHA_LOCATION_LENGTH = 6;
	/**
	 * used as an index for messaging.
	 */
	int LOCATION_END_POINT = 7;
	/**
	 * ID of message when transacting with server for audit location request.
	 */
	String TOTE_AUDIT_LOCATION_REQUEST_MESSAGE_ID = "g";
	/**
	 * ID of message when transacting a successful PI.
	 */
	String TOTE_PUTAWAY_PI_MESSAGE_ID = "h";
	/**
	 * Standard EAN indicator.
	 */
	String TOTE_PUTAWAY_PI_EAN_INDICATOR = "N";
	
	
	// Return codes
	/**
	 * Error Code : Location not valid.
	 */
	int RC_ERROR_LOCATION_NOT_VALID = 200;
	/**
	 * Error Code : no Location found.
	 */
	int RC_ERROR_NO_LOCATION_FOUND = 201;
	/**
	 * Error Code : Tote not valid.
	 */
	int RC_ERROR_INVALID_TOTE = 203;
	/**
	 * Error Code : Invalid item. 
	 */
	int RC_ERROR_INVALID_ITEM = 206;
	/**
	 * Error code : Item previously used.
	 */
	int RC_WARN_ITEM_PREVIOUSLY_USED = 101;
	/**
	 * Error code : Mis scan.
	 */
	int RC_WARN_MIS_SCAN = 102;

	
	/**
	 * Adds an item to the tote being audited.
	 * @param toteItem A tote item to add to the tote
	 * @return true if tote item passed is added ok
	 */
	boolean addItem(String toteItem);
	/**
	 * Sends the results of the tote audit to the server.
	 * if the server responds to the putaway will then call 
	 * locationRequest to retrieve the next location to be 
	 * audited
	 * @return true if the server responded ok "ACK"
	 */
	boolean audit();
	/**
	 * @return String of Alpha Location in format AA99-9A
	 */
	String getAlphaLocation();
	/**
	 * @return String of the Numeric location sent from the server
	 */
	String getNumericLocation();
	/**
	 * Returns the tote id.
	 * @return String of the tote id
	 */
	String getToteId();
	/**
	 * Returns the amount of items currenty in the tote.
	 * @return integer value of number of items
	 */
	int getToteItemCount();
	/**
	 *  @return Returns true if there is a next location to audit
	 *   
	 */
	boolean isLocationAvailable();
	// Methods
	/**
	 * High level routine to request the next 
	 * location for auditing from the server.
	 * @param location - either the 
	 * location scanned as a start point or the location of the last tote PI'd
	 * @return boolean true if the function was 
	 * successful i.e a location wasa returned from the server
	 */
	boolean locationRequest(String location);
	/**
	 * Wrapper function for ToteAudit's receiveLocationRequest.
	 * @param rsp - contains the response from the server after a 
	 * call to locationRequest
	 */
	void receiveLocationRequest(String rsp);
	/**
	 * Sets the tote id.
	 * @param toteID The tote id passed 
	 */
	void setToteId(String toteID);
	/**
	 * Checks that the location scanned matches the location.
	 * that the server expected
	 * @param location Location scanned
	 * @return true if the server location and location passed match
	 */
	boolean validatePILocation(String location);
	
	/**
	 * Validates that the tote scanned for auditing is a valid tote.
	 * @param toteId The tote scanned
	 * @return true if tote is valid
	 */
	boolean validatePITote(String toteId);
	
}
