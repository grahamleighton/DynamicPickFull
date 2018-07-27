package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.domain.Location;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;


/**
 * Tote Consol Service implementation.
 * @author hstd004
 *
 */
public class ToteConsolServiceImpl extends GeneralServiceImpl
	implements ToteConsolService  {

	/**
	 * Holds the trolley tote information.
	 */
	private ToteBuildService tbs;
	/**
	 * A location object.
	 */
	private LocationImpl l;
	
	/**
	 * The last located tote scanned.
	 */
	private String lastTote;
	/*
	 * Returns the capacity of the tote. Default is MAX_ITEMS
	 * but if tote starts with '50' will look at position 3 and 4
	 * and work out dynamic value and cap it at 30 .
	 * To have more than 30 would require a change on the DEC Alpha
	 * and the mainframe. 
	 */
	public final int getToteCapacity() {
		int capacity = MainConstants.MAX_ITEMS;
		if ( tbs.getToteId().substring(0, 2).equalsIgnoreCase("50")) {
			capacity = Integer.parseInt(tbs.getToteId().substring(2, 4));
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
		if (tbs.getToteItemCount() < maxInThisTote) {
			return false;
		}	
		return true;
	}

	/** 
	 * Adds an item to the trolley.
	 * @param toteItem - Tote item to add to trolley.
	 * @return true if added ok
	 */
	public final boolean addItemToTrolleyTote(final String toteItem) {
		if (toteItem.length() == MainConstants.TOTE_ITEM_EAN_LENGTH) {
			return (tbs.addScan(toteItem));
		} 
		if (toteItem.length() == MainConstants.TOTE_ITEM_ITRK_LENGTH) {
			return (tbs.addScan(toteItem));
		}
		
		return false;
		
	}

	/** 
	 * 	Confirms that location passed matches location expected.
	 * @return true if match
	 * @param beamLoc Scanned beam location
	 */
	public final boolean confirmLocation(final String beamLoc) {
		if (!l.validCheckDigit(beamLoc)) {
			return false;
		}
		return (l.matchLocation(beamLoc));
	}
	/**
	 * Builds the start location request.
	 * @param numLoc numeric location
	 * @return String of request
	 */
	private String buildStartLocationRequest(final String numLoc) {
		String msg;
		msg = "";
		msg = msg + "f" 
			+ StringUtils.padNumber(getUnitId(), MainConstants.FOUR);
		msg = msg + StringUtils.padNumber(getOperator(), 
					MainConstants.OPERATOR_LENGTH);
		msg = msg + numLoc;        // Location to check from
		msg = msg + "0000000000";  // Tote ID
		msg = msg + " ";           // Request
		msg = msg + "N";           // Tote Match Field
		msg = msg + "Y";           // Request more locations 
		msg = msg + "00";          // Item count
		return msg;
	}
	/**
	 * Builds the start location request.
	 * @param alphaLoc alphanumeric 6 byte location
	 * @return String of request
	 */
	private String buildLocationEmptiedMessage(final String alphaLoc) {
		String msg;
		msg = "";
		msg = msg + "f" 
			+ StringUtils.padNumber(getUnitId(), MainConstants.FOUR);
		msg = msg + StringUtils.padNumber(getOperator(), 
					MainConstants.OPERATOR_LENGTH);
		msg = msg + alphaLoc;        // Location to check from
		msg = msg + "0000000000";  // Tote ID
		msg = msg + "C";           // Request
		msg = msg + "Y";           // Tote Match Field
		msg = msg + "Y";           // Request more locations 
		msg = msg + "00";		   // Item Count
		
		return msg;
	}
	/**
	 * Builds the Consol Putaway message.
	 * @param alphaLoc 6 byte Alpha Numeric location
	 * @param toteID Tote ID located
	 * @param itemCount Number of items in tote
	 * @return String of request
	 */
	private String buildConsolPutawayMessage(
				final String alphaLoc, 
				final String toteID, 
				final int itemCount) {
		String msg;
		msg = "";
		msg = msg + "f" 
			+ StringUtils.padNumber(getUnitId(), MainConstants.FOUR);
		msg = msg + StringUtils.padNumber(getOperator(), 
					MainConstants.OPERATOR_LENGTH);
		msg = msg + alphaLoc;        // Location to check from
		msg = msg + toteID;        // Tote ID
		msg = msg + "P";           // Request
		msg = msg + "Y";           // Tote Match Field
		msg = msg + "Y";           // Request more locations 
		msg = msg + StringUtils.padNumber(itemCount, 2);
								   // Count of items in tote
		
		return msg;
	}
	/** 
	 *  using the beam location passed it builds and 
	 *  transmits to the server a location request.
	 *  @param beamLoc location scanned 8 byte
	 *  @return true if a start location is available.
	 */
	public final boolean getStartLocation(final String beamLoc) {
		String msg = "";
		msg = buildStartLocationRequest(l.extractLocation(beamLoc));
		comms.transact(msg);
		processResponse();
		return l.isLocationAvailable();
	}

	/**
	 * Returns number of items in the trolley.
	 * @return Count of items
	 */
	public final int getTrolleyToteItemCount() {
		
		return tbs.getToteItemCount();
	}

	/**
	 * Sends a location emptied message to the server.
	 * @param beamLoc - Numeric location emptied.
	 * @return true if next location available.
	 */
	public final boolean locationEmptied(final String beamLoc) {
		String msg = "";
		msg = buildLocationEmptiedMessage(l.extractLocation(beamLoc));
		comms.transact(msg);
		processResponse();
		return l.isLocationAvailable();
	}
	/**
	 * validates the tote id.
	 * @param toteId Tote ID Number
	 * @return true or false
	 */
	private boolean validToteId(final String toteId) {
		return (Validate.mod10Check3131(toteId, 
				MainConstants.TOTE_ID_LENGTH, "800") || 
				Validate.mod10Check3131(toteId,MainConstants.TOTE_ID_LENGTH,"50"));
	}
		/** 
	 * Sets the ID of the tote on the trolley.
	 * @param toteID ID of tote scanned on trolley
	 * @return true if set ok
	 */
	public final boolean setTrolleyTote(final String toteID) {
		tbs.clearList();
		tbs.setToteType("C");
		return (tbs.addTote(toteID));
	}
	/**
	 * Gets the tote id from totebuildservice.
	 * @return String of tote id
	 */
	public final String getTrolleyTote() {
		return tbs.getToteId();
	}
	/**
	 * Returns an integer indicating which tote was scanned.
	 * @param toteID A tote id scanned
	 * @return TOTE_ID_TROLLEY if match tote on trolley
	 * TOTE_ID_LOCATION if last tote scanned in location
	 * TOTE_ID_NEITHER if not match to trolley or last location
	 */
	public final int whichToteScanned(final String toteID) {
		if (toteID.equalsIgnoreCase(tbs.getToteId())) {
			return MainConstants.TOTE_ID_TROLLEY;
		}
		if (toteID.equalsIgnoreCase(lastTote)) {
			return MainConstants.TOTE_ID_LOCATION;
		}
		return MainConstants.TOTE_ID_NEITHER;
	}
	
	/**
	 * Processes location information.
	 * @param rsp - Response from server when emptying location.
	 */
	public final void processLocationInfo(final String rsp) {
		if (rsp.startsWith("ACK")) {
			if (rsp.substring(MainConstants.THREE, 
					MainConstants.FOUR).equalsIgnoreCase("Y")) {
				StringUtils.log("Setting location with [" 
						 + rsp.substring(MainConstants.FOUR, 
								MainConstants.FOUR 
								+ MainConstants.SIX 
								+ MainConstants.SIX)
						+ "]");
				l.setLocationAlphaNum(
						rsp.substring(MainConstants.FOUR, 
								MainConstants.FOUR 
								+ MainConstants.SIX 
								+ MainConstants.SIX));
			} else {
				l.setLocation("", "");
			}
		}
	}
	/**
	 * Processes server response to consol messages.
	 */
	private void processResponse() {
		String rsp = comms.getResponse();
		processLocationInfo(rsp);
	}
	/**
	 * Builds and transmits the tote build message to the server. 
	 * @param beamLoc Numeric beam location
	 * @return true if putaway ok
	 */
	public final boolean totePutaway(final String beamLoc) {
		String msg = "";
		int ic = 0;
		String tmpTote = tbs.getToteId();
		ic = getTrolleyToteItemCount();
		
		tbs.endTote(tbs.getToteId());

		
		tbs.addTote(lastTote);
		lastTote = tmpTote;
		
		msg = buildConsolPutawayMessage(
				l.extractLocation(beamLoc), tmpTote, ic);
		comms.transact(msg);
		processResponse();
		return l.isLocationAvailable();
		
	}
	
	/**
	 * Get the last tote scanned on consol run.
	 * @return String of tote id.
	 */
	public final String getLastTote() {
		return lastTote;
	}
	/**
	 * Sets the last tote id of last tote scanned on consol run.
	 * @param toteID String of tote id
	 * @return true or false
	 */
	public final boolean setLastTote(final String toteID) {
		if (validToteId(toteID)) {
			lastTote = toteID;
			return true;
		}
		return false;
	}
	/**
	 * Returns the location object.
	 * @return Returns the location object
	 */
	public final Location getLocationObj() {
		return l;
	}
	
	/**
	 * Test whether the id passed is a valid tote number
	 * or a valid item.
	 * @param id the id passed.
	 * @return 0 if neither tote or item, 1 if valid tote and 2 if valid item.
	 */
	public final int isValidItemOrTote(final String id) {
		
		if (id.length() != MainConstants.TOTE_ID_LENGTH 
				&& id.length() != MainConstants.TOTE_ITEM_ITRK_LENGTH) {
			return MainConstants.VALID_NEITHER;
		}
			
		if (validToteId(id)) {
			return MainConstants.VALID_TOTE;
		}
		if (Validate.mod11Check(id, MainConstants.TOTE_ITEM_ITRK_LENGTH)) {
			return MainConstants.VALID_ITEM;
		}
		
		return MainConstants.VALID_NEITHER;

	}
	
	/**
	 *  Constructor .
	 */
	public ToteConsolServiceImpl() {
		super();

		tbs = new ToteBuildServiceImpl();
		tbs.setToteType("C");
		l = new LocationImpl();
		
	}



}
