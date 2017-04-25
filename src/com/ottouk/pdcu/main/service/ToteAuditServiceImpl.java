package com.ottouk.pdcu.main.service;

import java.util.ArrayList;

import com.ottouk.pdcu.main.domain.ToteBuildItem;
import com.ottouk.pdcu.main.domain.ToteAudit;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

/**
 * ToteAuditServiceImpl.
 * Implements ToteAuditService
 * Provides functions to perform a PI run on totes in locations 
 * @author hstd004
 *
 */

public class ToteAuditServiceImpl 
	extends GeneralServiceImpl implements ToteAuditService {

	/**
	 * The domain ToteAudit.
	 */
	private ToteAudit pi;
	
	
	/**
	 * Constructor.
	 * Used to set up common details in the domain object ToteAudit 
	 */
	public ToteAuditServiceImpl() {
		pi = new ToteAudit();
		pi.setMessageId(TOTE_PUTAWAY_PI_MESSAGE_ID);
//		pi.setUnitId(unitId);
		pi.setOperator(operator);

	}
	/**
	 * Adds an item to the tote being audited.
	 * @param toteItem A tote item to add to the tote
	 * @return true if tote item passed is added ok
	 */
	public final boolean addItem(final String toteItem) {
		
		StringUtils.log("Tote Audit: addItem(" + toteItem + ")");
		
		if (validateItem(toteItem)) {
		
			if (pi.getToteBuildItems() == null) {
				pi.setToteBuildItems(new ArrayList());
			}
			ToteBuildItem tbi = 
					new ToteBuildItem(toteItem, TOTE_PUTAWAY_PI_EAN_INDICATOR);
			if (pi.isItemInList(tbi)) {
				return returnCode(RC_ERROR_INVALID_ITEM);
			}
			pi.getToteBuildItems().add(tbi);
		}
		return returnCode(RC_OK);
	}

	/**
	 * Sends the results of the tote audit to the server.
	 * if the server responds to the putaway will then call 
	 * locationRequest to retrieve the next location to be 
	 * audited
	 * @return true if the server responded ok "ACK"
	 */
	
	public final boolean audit() {
		String location;
		location = pi.getLocation();
		StringUtils.log("Tote Audit: putaway(" + location + ")");
		String msg = pi.buildPIComplete();
		if ( pi.isListValid() ) {
			pi.getToteBuildItems().clear();
		}
		pi.setToteId("");
		boolean transactOK = transact(msg);
		if (transactOK) {
			pi.receivePutaway(getResponse());
			
			if (pi.getResponse().equalsIgnoreCase("ACK")) {
				locationRequest(location);
			} else {
				return false;
			}
		}	

		return transactOK;	// returnCode set by transact() method
		
	}
	/**
	 * @return String of Alpha Location in format AA99-9A
	 */
	public final String getAlphaLocation() {
		
		StringUtils.log("Tote Audit: getAlphaLocation()");
		
		if (pi == null) {
			return null;
		} else {
			int locStub = LOCATION_STUB_LENGTH;
			String alphaLoc = pi.getAlphaLocation();
			String loc = StringUtils.padField(alphaLoc, ALPHA_LOCATION_LENGTH);
			return loc.substring(0, locStub) + "-" 
				+ loc.substring(locStub, ALPHA_LOCATION_LENGTH);
		}
	}

	/**
	 * @return String of the Numeric location sent from the server
	 */
	public final String getNumericLocation() {
		
		return pi.getNumericLocation();
	}

	/**
	 * Returns the amount of items currenty in the tote.
	 * @return integer value of number of items
	 */
	public final int getToteItemCount() {
			if (pi == null) {
				return 0;
			} else {
				return pi.getItemCount();
			}
	}
	/**
	 * Returns the tote id.
	 * @return String of the tote id
	 */
	public final String getToteId() {
		StringUtils.log("Tote Audit: getToteId()");
		return pi.getToteId();
	}
	/**
	 *  @return Returns true if there is a next location to audit
	 *   
	 */
	public final boolean isLocationAvailable() {
		return (pi.getFound().equalsIgnoreCase("Y"));
	}
	/**
	 * High level routine to request the next 
	 * location for auditing from the server.
	 * @param location - either the 
	 * location scanned as a start point or the location of the last tote PI'd
	 * @return boolean true if the function was 
	 * successful i.e a location wasa returned from the server
	 */
	public final boolean locationRequest(final String location) {
		pi.setUnitId(unitId);
		pi.setOperator(operator);

		StringUtils.log("Tote Audit: locationRequest(" + location + ")");
		if (validateLocation(location)) {
			return locationRequest2(location);
		} else {
			StringUtils.log("toteAudtService::locationRequest::location not valid ");
			return returnCode(RC_ERROR_LOCATION_NOT_VALID);
		}
	}
	/**
	 * High level function to receive a location from the server.
	 * Sets up the ToteAudit object and performs the server transaction
	 * @param location - either location scanned or location of 
	 * last audited tote
	 * @return true if location is available
	 */
	private boolean locationRequest2(final String location) {
		
		StringUtils.log("Tote Audit: locationRequest2(" + location + ")");
		
		pi.setLocation(location);
		pi.setMessageId(TOTE_AUDIT_LOCATION_REQUEST_MESSAGE_ID);
		boolean transactOK = transact(pi.buildLocationRequest());
	
		if (transactOK) {
			pi.receiveLocationRequest(getResponse());
			
			if (!pi.getFound().equalsIgnoreCase("Y")) {
				return returnCode(RC_ERROR_NO_LOCATION_FOUND);
			}
		}
		
		return transactOK;	// returnCode set by transact() method
	}
	/**
	 * Wrapper function for ToteAudit's receiveLocationRequest.
	 * @param rsp - contains the response from the server after a 
	 * call to locationRequest
	 */
	public final void receiveLocationRequest(final String rsp) {
		pi.receiveLocationRequest(rsp);
	}
	/**
	 * Sets the tote id.
	 * @param tote The tote id passed 
	 */
	public final void setToteId(final String tote) {
		StringUtils.log("Tote Audit : set tote id");
		pi.setToteId(tote);
		
	}
	/**
	 * Validates that the location passed is valid by calling validatelocation.
	 * Then checks that the location matches the one expected from the server
	 * @param location Location passed
	 * @return true if the location matches the server and is valid
	 */
	private boolean validateAndMatchLocation(final String location) {
		if (pi == null) {
			return false;
		} else {
			return (validateLocation(location) 
					&& location.substring(1, LOCATION_END_POINT)
						.equals(StringUtils.padNumber(
						pi.getNumericLocation(), ALPHA_LOCATION_LENGTH)));
		}
	}
	/**
	 * Validates the the tote item is of a valid format.
	 * @param toteItem Tote item passed
	 * @return true if valid
	 */
	private boolean validateItem(final String toteItem) {
		return Validate.mod11Check(toteItem, TOTE_ITEM_LENGTH);
	}
	/**
	 * Validates that the location passed has a valid check digit.
	 * @param location Location passed
	 * @return true if valid
	 */
	private boolean validateLocation(final String location) {
		return Validate.mod10Check3131(location, LOCATION_LENGTH);
	}
	/**
	 * Checks that the location scanned matches the location.
	 * that the server expected
	 * @param location Location scanned
	 * @return true if the server location and location passed match
	 */
	public final boolean validatePILocation(final String location) {
		
		
		StringUtils.log("Tote Audit: validatePILocation(" + location + ")");
		
		if (validateAndMatchLocation(location)) {
			
			pi.setMessageId(TOTE_PUTAWAY_PI_MESSAGE_ID);
			pi.setLocation(location);
			return returnCode(RC_OK);
		
		} else {
			return returnCode(RC_ERROR_LOCATION_NOT_VALID);
		}
	}

	
	/**
	 * Validates that the tote scanned for auditing is a valid tote.
	 * @param toteId The tote scanned
	 * @return true if tote is valid
	 */
	public final boolean validatePITote(final String toteId) {
		StringUtils.log("Tote Audit: validatePITote(" + toteId + ")");
		if (validateTote(toteId)) {
			pi.setToteId(toteId);
			return returnCode(RC_OK);
		} else {
			pi.setToteId("");
			return returnCode(RC_ERROR_INVALID_TOTE);
		}
	}
	/**
	 * Validates that the tote id is of a valid format.
	 * @param toteId Tote number passed
	 * @return true if valid
	 */
	private boolean validateTote(final String toteId) {
		return ( Validate.mod10Check3131(toteId, TOTE_ID_LENGTH, TOTE_ID_PREFIX) ||
			Validate.mod10Check3131(toteId, TOTE_ID_LENGTH, TOTE_ID_PREFIX_2)) ;
	}

}
