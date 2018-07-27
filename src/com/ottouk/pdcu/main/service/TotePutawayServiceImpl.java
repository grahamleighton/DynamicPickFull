package com.ottouk.pdcu.main.service;

import java.util.ArrayList;

import com.ottouk.pdcu.main.domain.ToteBuildItem;
import com.ottouk.pdcu.main.domain.TotePutaway;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

public class TotePutawayServiceImpl extends GeneralServiceImpl implements TotePutawayService {

	private TotePutaway locationRequest;
	private TotePutaway successful;
	private TotePutaway pi;
	private LocationImpl loc;
	
	
	
	public TotePutawayServiceImpl()
	{
		loc = new LocationImpl();
	}
	
	public boolean locationRequest(String location) {
		
		StringUtils.log("Tote Putaway: locationRequest(" + location + ")");
		
		if (validateLocation(location)) {
			return locationRequest2(location);
		} else {
			return returnCode(RC_ERROR_LOCATION_NOT_VALID);
		}
	}
	
	private boolean locationRequest2(String location) {
		
		StringUtils.log("Tote Putaway: locationRequest2(" + location + ")");
		
		locationRequest = newTotePutaway(TOTE_PUTAWAY_LOCATION_REQUEST_MESSAGE_ID, location);
		locationRequest.setStatus(TOTE_PUTAWAY_LOCATION_REQUEST_STATUS);
		boolean transactOK = transact(locationRequest.buildLocationRequest());

		if (transactOK) {
			locationRequest.receiveLocationRequest(getResponse());
			
			if (!locationRequest.getFound().equalsIgnoreCase("Y")) {
				return returnCode(RC_ERROR_NO_LOCATION_FOUND);
			}
		}
		
		return transactOK;	// returnCode set by transact() method
	}
	
	
	
	public String getAlphaLocation() {
		
		StringUtils.log("Tote Putaway: getAlphaLocation()");
		
		if (locationRequest == null) {
			return null;
		} else {
			String location = StringUtils.padField(locationRequest.getAlphaLocation(), 6);
			return location.substring(0, 4)	+ "-" + location.substring(4);
		}
	}
	
	
	
	
	
	public boolean putaway(String location) {
		
		StringUtils.log("Tote Putaway: putaway(" + location + ")");
		
		if (validateAndMatchLocation(location)) {

			successful.setLocation(location);

			boolean transactOK = transact(successful.buildPutaway());
			if (transactOK) {
				successful.receivePutaway(getResponse());
				
				if (!successful.getSuccess().equalsIgnoreCase("Y")) {
					return returnCode(RC_ERROR_TOTE_NOT_FOUND);
				}
			}	

			return transactOK;	// returnCode set by transact() method

		} else {
			return returnCode(RC_ERROR_WRONG_LOCATION);
		}
	}
	
	public boolean locationFollows() {
		
		StringUtils.log("Tote Putaway: locationFollows()");
		
		if (successful.getLocationFollows().equalsIgnoreCase("Y")) {
			
			// Update for subsequent getAlphaLocation() calls to work
			locationRequest = successful;
			return returnCode(RC_OK);

		} else {
			return returnCode(RC_ERROR_NO_FOLLOWING_LOCATION);
		}
	}
	
	
	public boolean validatePILocation(String location) {
		
		StringUtils.log("Tote Putaway: validatePILocation(" + location + ")");
		
		if (validateAndMatchLocation(location)) {
			
			pi = newTotePutaway(TOTE_PUTAWAY_PI_MESSAGE_ID, location);
			return returnCode(RC_OK);
		
		} else {
			return returnCode(RC_ERROR_LOCATION_NOT_VALID);
		}
	}
	
	public boolean piRequest() {
		
		StringUtils.log("Tote Putaway: piRequest()");

		boolean transactOK = transact(pi.buildPIRequest());
		if (transactOK) {
			pi.receivePIRequest(getResponse());
		}

		return transactOK;
	}
	
	public boolean piLocationFollows() {
		
		StringUtils.log("Tote Putaway: piLocationFollows()");
		
		String location = StringUtils.padNumber(locationRequest.getNumericLocation(), 6);
		return locationRequest2(" " + location + " ");
	}
	
	public boolean addItem(String toteItem) {
		
		StringUtils.log("Tote Putaway: addItem(" + toteItem + ")");
		
		if (validateItem(toteItem)) {
		
			if (pi.getToteBuildItems() == null) {
				pi.setToteBuildItems(new ArrayList());
			}
			
			if (pi.getItemCount() < TOTE_PUTAWAY_MAX_ITEMS) {
				pi.getToteBuildItems().add(new ToteBuildItem(toteItem, TOTE_PUTAWAY_PI_EAN_INDICATOR));
				return returnCode(RC_OK);
			} else {
				return returnCode(RC_ERROR_MAX_ITEMS_IN_TOTE_PUTAWAY);
			}
		
		} else {
			return returnCode(RC_ERROR_INVALID_ITEM);
		}
	}
	
	private TotePutaway newTotePutaway(String messageId, String location) {
		
		TotePutaway totePutaway = new TotePutaway();
		totePutaway.setMessageId(messageId);
		totePutaway.setUnitId(unitId);
		totePutaway.setOperator(operator);
		totePutaway.setLocation(location);
		
		return totePutaway;
	}

	public boolean validatePutawayTote(String toteId) {
		
		StringUtils.log("Tote Putaway: validatePutawayTote(" + toteId + ")");
		
		if (validateTote(toteId)) {

			successful = newTotePutaway(TOTE_PUTAWAY_SUCCESSFUL_MESSAGE_ID, "");
			successful.setToteId(toteId);
			return returnCode(RC_OK);
			
		} else {
			return returnCode(RC_ERROR_INVALID_TOTE);
		}
	}
	
	public boolean validatePITote(String toteId) {
		
		StringUtils.log("Tote Putaway: validatePITote(" + toteId + ")");
		
		if (validateTote(toteId)) {

			pi.setToteId(toteId);
			return returnCode(RC_OK);
			
		} else {
			return returnCode(RC_ERROR_INVALID_TOTE);
		}
	}
	
	private boolean validateLocation(String location) {
		if ( loc.isAlphaLocationValid(location)) {
			return true;
		}
		
		//return Validate.mod10Check3131(location, LOCATION_LENGTH);
		return Validate.mod10Check3131(location, LOCATION_LENGTH, LOCATION_PREFIX);
	}
	
	
	private boolean validateAndMatchLocation(String location) {
		if (locationRequest == null) {
			return false;
		} else {
			return (validateLocation(location) 
					&& location.substring(1, 7).equals(StringUtils.padNumber(locationRequest.getNumericLocation(), 6)));
		}
	}
	
	private boolean validateTote(String toteId) {
		return ( Validate.mod10Check3131(toteId, TOTE_ID_LENGTH, TOTE_ID_PREFIX) ||
				Validate.mod10Check3131(toteId, TOTE_ID_LENGTH, TOTE_ID_PREFIX_2)) ;
	}
	
	private boolean validateItem(String toteItem) {
		return Validate.mod11Check(toteItem, TOTE_ITEM_LENGTH);
	}
	
	public int getToteItemCount() {
		return (pi == null ? 0 : pi.getItemCount());
	}
	
	public String getToteId() {
		
		StringUtils.log("Tote Putaway: getToteId()");
		
		return pi.getToteId();
	}



}
