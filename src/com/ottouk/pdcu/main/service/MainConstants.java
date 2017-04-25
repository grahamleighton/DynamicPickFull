/**
 * 
 */
package com.ottouk.pdcu.main.service;

/**
 * @author hstd004
 *
 */
public final class MainConstants {
	/**
	 * Length of TOTE ID.
	 */
	public static final int TOTE_ID_LENGTH = 10;
	/**
	 * Length of numeric part of barcode for location.
	 */
	public static final int EMBEDDED_LOCATION_LENGTH = 6;
	/**
	 * Length of tote item.
	 */
	public static final int TOTE_ITEM_LENGTH = 13;
	/**
	 * Length of EAN number bar code.
	 */
	public static final int TOTE_ITEM_EAN_LENGTH = 13;
	/**
	 * Length of normal item tracking bar code.
	 */
	public static final int TOTE_ITEM_ITRK_LENGTH = 8;
	/**
	 * Length of response in message , typically ACK.
	 */
	public static final int RESPONSE_LENGTH = 3;
	/**
	 * Start of "found" index in response to audit location request .
	 */
	public static final int AUDIT_FOUND_START_INDEX = 3;
	/**
	 * End of "found" index in response to audit location request .
	 */
	public static final int AUDIT_FOUND_END_INDEX = 4;
	/**
	 * Start of "alpha location" index in response to audit location request .
	 */
	public static final int AUDIT_ALPHALOC_START_INDEX = 4;
	/**
	 * End of "alpha location" index in response to audit location request .
	 */
	public static final int AUDIT_ALPHALOC_END_INDEX = 10;
	/**
	 * Start of "alpha location" index in response to audit location request .
	 */
	public static final int AUDIT_NUMLOC_START_INDEX = 10;
	/**
	 * End of "alpha location" index in response to audit location request .
	 */
	public static final int AUDIT_NUMLOC_END_INDEX = 16;
	/**
	 * Constant for maximum items in a consol tote.
	 */
	public static final int MAX_ITEMS = 12;
	/**
	 * Constant "3".
	 */
	public static final int THREE = 3;
	/**
	 * Constant "4".
	 */
	public static final int FOUR = 4;
	/**
	 * Constant "5".
	 */
	public static final int FIVE = 5;
	/**
	 * Constant "6".
	 */
	public static final int SIX = 6;
	/**
	 * Constant "7".
	 */
	public static final int SEVEN = 7;
	/**
	 * Constant "9".
	 */
	public static final int NINE = 9;
	/**
	 * Constant "12".
	 */
	public static final int TWELVE = 12;
	/**
	 * Operator Number Length.
	 */
	public static final int OPERATOR_LENGTH = 8;
	/**
	 * Location Length.
	 */
	public static final int LOCATION_LENGTH = 8;
	/**
	 * Maximum HHT Number.
	 */
	public static final int HHT_MAX = 120;
	/**
	 * Valid tote.
	 */
	public static final int VALID_TOTE = 1;
	/**
	 * Valid item.
	 */
	public static final int VALID_ITEM = 2;
	/**
	 * Invalid item or tote.
	 */
	public static final int VALID_NEITHER = 0;
	/**
	 * Tote Scanned Constant : Tote on trolley.
	 */
	public static final int TOTE_ID_TROLLEY = 0;
	/**
	 * Tote Scanned Constant : Tote in location.
	 */
	public static final int TOTE_ID_LOCATION = 1;
	/**
	 * Tote Scanned Constant : Tote not recognised as trolley or location.
	 */
	public static final int TOTE_ID_NEITHER = 2;

	/**
	 * Private Constructor.
	 */
	private MainConstants() {
		
	}

}
