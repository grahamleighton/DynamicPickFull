package com.ottouk.pdcu.main.domain;

import java.util.List;
import com.ottouk.pdcu.main.service.MainConstants;

import com.ottouk.pdcu.main.utils.StringUtils;
/**
 * ToteAudit.
 * Contains the items within a tote and it's location 
 * @author hstd004
 *
 */
public class ToteAudit extends Header {

	// Input fields
	
	/**
	 * location of tote.
	 */
	private String location;
	/**
	 * Tote id being audited.
	 */
	private String toteId;
//	private String status;
	/**
	 * List of items held in the tote.
	 */
	private List toteBuildItems;
	
	// Output fields
	/**
	 * Response from server, first 3 characters normally "ACK".
	 */
	private String response;
	/**
	 * part of response from server , if "Y" then location available or "found".
	 */
	private String found;
	/**
	 * The alphanumeric location.
	 */
	private String alphaLocation;
	/**
	 * The numeric location.
	 */
	private String numericLocation;
	/**
	 * Success.
	 */
	private String success;
	/**
	 * used to identify if this tote item has already been scanned.
	 * @param item Tote item
	 * @return true if already in tote
	 */
	public final boolean isItemInList(final ToteBuildItem item) {
		return (toteBuildItems.contains(item));
	}
	/**
	 * Builds a PI header message.
	 * @return String containing header message
	 */
	public final String buildPI() {
		
		String req = buildHeader();
		
		req += StringUtils.padNumber(getMid6Location(), 
				MainConstants.EMBEDDED_LOCATION_LENGTH);
		req += StringUtils.padNumber(toteId, MainConstants.TOTE_ID_LENGTH);
		
		return req;
	}
	/**
	 * Builds a string containing the location request.
	 * @return String containing request
	 */
	public final String buildLocationRequest() {
		
		String req = buildHeader();
		req += StringUtils.padNumber(getMid6Location(), 
				MainConstants.EMBEDDED_LOCATION_LENGTH);
		
		return req;
	}
	
	/**
	 * Builds a PI complete message after tote has been audited.
	 * @return String containing message ready to transmit
	 */
	public final String buildPIComplete() {
		
		String req = buildPI();
		
		ToteBuildItem toteBuildItem = null;
		for (int itemCount = getItemCount(), i = 0; i <= itemCount; i++) {
			if (i < itemCount) {
				toteBuildItem = (ToteBuildItem) toteBuildItems.get(i);
			} else {
				// Append empty item to indicate end-of-record
				toteBuildItem = new ToteBuildItem();
			}
			req += StringUtils.padField(toteBuildItem.getToteItem(), 
						MainConstants.TOTE_ITEM_LENGTH);
			req += StringUtils.padField(toteBuildItem.getEanIndicator(), 1);
		}
		System.out.println("[" + req + "]");
		return req;
	}
	/**
	 * Returns the Alpha Location.
	 * @return Alpha Location
	 */
	public final String getAlphaLocation() {
		return alphaLocation;
	}
	/**
	 * Return whether the server response contained a location or not.
	 * @return "Y" or "N"
	 */
	public final String getFound() {
		return found;
	}
	/*
		public String getStatus() {
			return status;
		}
	
		public void setStatus(String status) {
			this.status = status;
		}
	*/
		/**
		 * Returns the number of items in the tote.
		 * @return Item count number 
		 */
		public final int getItemCount() {
			if (toteBuildItems == null) {
				return 0;
			}
			return (toteBuildItems.size());
		}
	/**
	 * Returns the location.
	 * @return String of the location
	 */
	public final String getLocation() {
		return location;
	}
	/**
	 * Returns the middle 6 characters of the 8 character location.
	 * @return String of the location
	 */
	public final String getMid6Location() {
		return StringUtils.getString(location, 1,  MainConstants.SEVEN);
	}
	/**
		 * Returns the numeric location.
		 * @return String of the numeric location
		 */
		public final String getNumericLocation() {
			return numericLocation;
		}
	/**
	 * First 3 bytes of response to server messages.
	 * @return 3 byte string
	 */
	public final String getResponse() {
		return response;
	}
	/**
	 * Returns the success field.
	 * @return Success field
	 */
	public final String getSuccess() {
		return success;
	}

	public final boolean isListValid() {
		if ( toteBuildItems == null ) {
			return false;
		}
		return true;
			
	}
	/**
		 * Returns the list of tote items.
		 * @return List of items
		 */
		public final List getToteBuildItems() {
			return toteBuildItems;
		}
		
	/**
	 * Tote ID.
	 * @return String of ToteId
	 */
	public final String getToteId() {
		return toteId;
	}
	/**
	 * Processes the server response to the location request.
	 * @param resp The server response
	 */
	public final void receiveLocationRequest(final String resp) {
		this.response = 
				StringUtils.getString(resp, 0, MainConstants.RESPONSE_LENGTH);
		found = StringUtils.getString(resp, 
				MainConstants.AUDIT_FOUND_START_INDEX, 
				MainConstants.AUDIT_FOUND_END_INDEX);
		if (found.equalsIgnoreCase("Y")) {
			alphaLocation = StringUtils.getString(resp, 
					MainConstants.AUDIT_ALPHALOC_START_INDEX, 
					MainConstants.AUDIT_ALPHALOC_END_INDEX);
			numericLocation = StringUtils.getString(resp, 
					MainConstants.AUDIT_NUMLOC_START_INDEX, 
					MainConstants.AUDIT_NUMLOC_END_INDEX);
		} else {
			alphaLocation = "      ";
			numericLocation = "000000";
		}
	}
	

	/**
	 * Stores the first 3 bytes of the response to the PI for checking later.
	 * @param resp Response from server
	 */
	public final void receivePIRequest(final String resp) {
		this.response = StringUtils.getString(resp, 
				0, MainConstants.RESPONSE_LENGTH);
	}
	/**
	 * Processes the server response to the tote pi putaway.
	 * @param resp Response from the server
	 */
	public final void receivePutaway(final String resp) {
		this.response = 
			StringUtils.getString(resp, 0, MainConstants.RESPONSE_LENGTH);
	}
	/**
	 * Sets the Alpha location.
	 * @param alphaLoc String of the location to set to
	 */
	public final void setAlphaLocation(final String alphaLoc) {
		this.alphaLocation = alphaLoc;
	}
	
	/**
	 */
	public final void setFound(final String locfound) {
		this.found = locfound;
	}
	/**
	 * Sets the location in the class.
	 * @param loc location passed
	 */
	public final void setLocation(final String loc) {
		this.location = loc;
	}
	/**
	 * Sets the numeric location.
	 * @param numLocation Location to set to
	 */
	public final void setNumericLocation(final String numLocation) {
		this.numericLocation = numLocation;
	}
	/**
	 * Sets the response field.
	 * @param rsp String to set response to
	 */
	public final void setResponse(final String rsp) {
		this.response = rsp;
	}
	/**
	 * Sets the success field.
	 * @param successvalue String to set "success" to
	 */
	public final void setSuccess(final String successvalue) {
		this.success = successvalue;
	}
	/**
	 * Sets the item list within the tote.
	 * @param toteItems list of items
	 */
	public final void setToteBuildItems(final List toteItems) {
		this.toteBuildItems = toteItems;
	}
	/**
	 * Sets the tote id .
	 * @param toteID Tote Id passed
	 */
	public final void setToteId(final String toteID) {
		this.toteId = toteID;
	}
}
