


package com.ottouk.pdcu.main.domain;

import java.util.List;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Domain object for <code>TotePutaway</code> interface. Contains all required
 * request and response fields, and methods for the manipulation thereof.
 * 
 * @author dis065
 * 
 */
public class PIPick extends Header {

	// Input fields

	/**
	 * Request field.
	 */
	private String location;

	/**
	 * Request field.
	 */
	private String toteId;

	/**
	 * Request field.
	 */
	private String status;

	/**
	 * Request field.
	 */
	private List toteBuildItems;
	
	// Output fields

	/**
	 * Response field.
	 */
	private String response;

	/**
	 * Response field.
	 */
	private String found;

	/**
	 * Response field.
	 */
	private String alphaLocation;

	/**
	 * Response field.
	 */
	private Integer numericLocation;

	/**
	 * Response field.
	 */
	private String success;

	/**
	 * Response field.
	 */
	private String locationFollows;

	//

	/**
	 * Build putaway request.
	 * 
	 * @return putaway request
	 */
	public String buildPutaway() {
		
		String req = buildHeader();
		req += StringUtils.padNumber(getMid6Location(), 6);
		req += StringUtils.padNumber(toteId, 10);
		
		return req;
	}
	
	
	/**
	 * Build location request.
	 * 
	 * @return location request
	 */
	public String buildLocationRequest() {
		
		String req = buildHeader();
		req += StringUtils.padNumber(getMid6Location(), 6);
		req += StringUtils.padNumber(status, 1);
		
		return req;
	}
	/**
	 * Build PI request.
	 * 
	 * @return PI request
	 */
	public String buildPIRequest() {
		
		String req = buildPutaway();
		
		ToteBuildItem toteBuildItem = null;
		for (int itemCount = getItemCount(), i = 0; i <= itemCount; i++) {
			if (i < itemCount) {
				toteBuildItem = (ToteBuildItem) toteBuildItems.get(i);
			} else {
				// Append empty item to indicate end-of-record
				toteBuildItem = new ToteBuildItem();
			}
			req += StringUtils.padField(toteBuildItem.getToteItem(), 13);
			req += StringUtils.padField(toteBuildItem.getEanIndicator(), 1);
		}
		
		return req;
	}
	
	/**
	 * Populate putaway response fields.
	 * 
	 * @param response Comms response
	 */
	public void receivePutaway(String response) {
		this.response = StringUtils.getString(response, 0, 3);
		success = StringUtils.getString(response, 3, 4);
		locationFollows = StringUtils.getString(response, 4, 5);
		alphaLocation = StringUtils.getString(response, 5, 11);
		numericLocation = StringUtils.getInteger(response, 11, 17);
	}
	
	/**
	 * Populate location response fields
	 * .
	 * @param response Comms response
	 */
	public void receiveLocationRequest(String response) {
		this.response = StringUtils.getString(response, 0, 3);
		found = StringUtils.getString(response, 3, 4);
		alphaLocation = StringUtils.getString(response, 4, 10);
		numericLocation = StringUtils.getInteger(response, 10, 16);
	}
	
	
	
	/**
	 * Populate PI response fields.
	 * 
	 * @param response Comms response
	 */
	public void receivePIRequest(String response) {
		this.response = StringUtils.getString(response, 0, 3);
	}
	
	/**
	 * @return The middle six characters of the (eight character) location
	 */
	public String getMid6Location() {
		return StringUtils.getString(location, 1, 7);
	}
	
	// Getters and setters

	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return toteId
	 */
	public String getToteId() {
		return toteId;
	}

	/**
	 * @param toteId toteId
	 */
	public void setToteId(String toteId) {
		this.toteId = toteId;
	}


	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return number of tote build items in tote
	 */
	public int getItemCount() {
		return (toteBuildItems == null ? 0 : toteBuildItems.size());
	}

	/**
	 * @return toteBuildItems
	 */
	public List getToteBuildItems() {
		return toteBuildItems;
	}

	/**
	 * @param toteBuildItems toteBuildItems
	 */
	public void setToteBuildItems(List toteBuildItems) {
		this.toteBuildItems = toteBuildItems;
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
	 * @return found
	 */
	public String getFound() {
		return found;
	}

	/**
	 * @param found found
	 */
	public void setFound(String found) {
		this.found = found;
	}

	/**
	 * @return alphaLocation
	 */
	public String getAlphaLocation() {
		return alphaLocation;
	}

	/**
	 * @param alphaLocation alphaLocation
	 */
	public void setAlphaLocation(String alphaLocation) {
		this.alphaLocation = alphaLocation;
	}

	/**
	 * @return numericLocation
	 */
	public Integer getNumericLocation() {
		return numericLocation;
	}

	/**
	 * @param numericLocation numericLocation
	 */
	public void setNumericLocation(Integer numericLocation) {
		this.numericLocation = numericLocation;
	}

	/**
	 * @return success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * @param success success
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @return locationFollows
	 */
	public String getLocationFollows() {
		return locationFollows;
	}

	/**
	 * @param locationFollows locationFollows
	 */
	public void setLocationFollows(String locationFollows) {
		this.locationFollows = locationFollows;
	}
}