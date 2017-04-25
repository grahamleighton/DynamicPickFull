package com.ottouk.pdcu.main.domain;

import java.util.List;

import com.ottouk.pdcu.main.service.MainConstants;
import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Domain object for <code>ToteBuild</code> interface. Contains all required
 * request and response fields, and methods for the manipulation thereof.
 * 
 * @author dis065
 * 
 */
public class ToteBuild extends Header {

	/**
	 * Request field.
	 */
	private String toteId;
	
	/**
	 * Request field. 
	 */
	private String toteType;
	
	/**
	 * Request field.
	 */
	private List toteBuildItems;

	//

	/**
	 * Build toteBuild request.
	 * 
	 * @return toteBuild request
	 */
	public String buildTote() {
		
		String req = buildHeader();
		req += StringUtils.padNumber(toteId, 10);
		req += StringUtils.padField(toteType, 1);
		
		int itemCount = getItemCount();
		req += StringUtils.padNumber(itemCount, 2);
		
		for (int i = 0; i < itemCount; i++) {
			ToteBuildItem toteBuildItem = (ToteBuildItem) toteBuildItems.get(i);
			req += StringUtils.padField(toteBuildItem.getToteItem(), 13);
			req += StringUtils.padField(toteBuildItem.getEanIndicator(), 1);
		}
		
		return req;
	}
	
	//
	
	/**
	 * Get toteId.
	 * 
	 * @return toteId
	 */
	public String getToteId() {
		return toteId;
	}

	/**
	 * Set toteId.
	 * 
	 * @param toteId toteId
	 */
	public void setToteId(String toteId) {
		this.toteId = toteId;
	}
	
	/**
	 * Get toteType.
	 * 
	 * @return toteType
	 */
	public String getToteType() {
		return toteType;
	}

	/**
	 * Set toteType.
	 * 
	 * @param toteType toteType
	 */
	public void setToteType(String toteType) {
		this.toteType = toteType;
	}

	/**
	 * Get itemCount.
	 * 
	 * @return itemCount
	 */
	public int getItemCount() {
		return (toteBuildItems == null ? 0 : toteBuildItems.size());
	}

	/**
	 * Get toteBuildItems.
	 * 
	 * @return toteBuildItems
	 */
	public List getToteBuildItems() {
		return toteBuildItems;
	}

	/**
	 * Set toteBuildItems.
	 * 
	 * @param toteBuildItems toteBuildItems
	 */
	public void setToteBuildItems(List toteBuildItems) {
		this.toteBuildItems = toteBuildItems;
	}
		/**
	 * Clears the tote build items listed. 
	 * Only really called when class used in Tote Consol
	 * activity.
	 */
	public void clearToteBuildItems() {
		toteBuildItems.clear();
	}
	/*
	 * Returns the capacity of the tote. Default is MAX_ITEMS
	 * but if tote starts with '50' will look at position 3 and 4
	 * and work out dynamic value and cap it at 30 .
	 * To have more than 30 would require a change on the DEC Alpha
	 * and the mainframe. 
	 */
	public final int getToteCapacity() {
		int capacity = MainConstants.MAX_ITEMS;
		if (toteId.substring(0, 2).equalsIgnoreCase("50")) {
			capacity = Integer.parseInt(toteId.substring(2, 4));
			if (capacity > 30) {
				capacity = 30;
			}
		}
		return capacity;
	}
	
	/*
	 * Returns whether this tote is full or not.
	 * If totes start with 50 will be dynamic as to how
	 * many items will fit in the tote. 
	 */
	public boolean isToteFull() {
		int maxInThisTote = getToteCapacity();
		
		if (getItemCount() < maxInThisTote) {
			return false;
		}	
		return true;
	}

	
	
}
