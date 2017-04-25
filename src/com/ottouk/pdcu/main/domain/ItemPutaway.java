package com.ottouk.pdcu.main.domain;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Domain object for <code>TotePutaway</code> interface. Contains all required
 * request and response fields, and methods for the manipulation thereof.
 * 
 * @author dis065
 * 
 */
public class ItemPutaway extends Header {

	/**
	 * Default constructor.
	 */
	int IPACount=0;
	
	/**
	 * Constructor.
	 */
	public ItemPutaway() {
		IPACount = 0;
	}
	/**
	 * Increment IPACount.
	 */
	public final void addCount() {
		IPACount = IPACount + 1;
	}
	/**
	 * Returns count of items scanned.
	 * @return Item Count
	 */
	public final int getCount() {
		return IPACount;
	}
	/**
	 * Build item putaway request.
	 * @param item Item Number
	 * @return String message
	 */
	public final String buildItemPutaway(final String item) {
		
		String req = buildHeader();
		
		req += StringUtils.padNumber(item, 9);
		
		return req;
	}
}
