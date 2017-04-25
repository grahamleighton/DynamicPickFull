package com.ottouk.pdcu.main.service;
import com.ottouk.pdcu.main.domain.Location;

/**
 * Tote Consol Service interface declarations.
 * @author hstd004
 *
 */

public interface ToteConsolService extends GeneralService {
	
	/**
	 * Gets the next location.
	 * @param beamLoc 8 byte beam location.
	 * @return true if a Location is returned. 
	 */
	boolean getStartLocation(String beamLoc);
	/**
	 * Sets the trolley tote ID.
	 * @param toteID ID of tote placed on trolley.
	 * @return true if set ok.
	 */
	boolean setTrolleyTote(String toteID);
	/**
	 * Adds an item to the trolley tote.
	 * @param toteItem Tote Item
	 * @return true if item added ok
	 */
	boolean addItemToTrolleyTote(String toteItem);
	/**
	 * Returns the number of items in the trolley.
	 * @return count of items.
	 */
	int  getTrolleyToteItemCount();
	/**
	 * Returns the maximum number of items that will fit in a tote
	 * @return count of items.
	 */
	int  getToteCapacity();
	/**
	 * Confirms that the location has been emptied.
	 * @param beamLoc 8 byte location beam
	 * @return true if next location available 
	 */
	boolean locationEmptied(String beamLoc);
	/**
	 * Confirms the location scanned is the location
	 * requested.
	 * @param beamLoc 8 byte location beam.
	 * @return true if match
	 */
	boolean confirmLocation(String beamLoc);
	/**
	 * Used to determine which tote has been scanned.
	 * @param toteID - Scanned 10 byte tote id.
	 * @return TOTE_ID_TROLLEY if tote id matches the trolley tote
	 * 	TOTE_ID_LOCATION if tote id matches the location tote or
	 *  TOTE_ID_NEITHER if tote id does not match located tote or trolley tote. 
	 */
	int whichToteScanned(String toteID);
	/**
	 * Builds and transmits the tote build message to the server. 
	 * @param beamLoc numeric beam location
	 * @return true if putaway ok
	 */
	boolean totePutaway(String beamLoc);
	/**
	 * Get the last tote scanned on consol run.
	 * @return String of tote id.
	 */
	String getLastTote();
	/**
	 * Sets the last tote id of last tote scanned on consol run.
	 * @param toteID String of tote id
	 * @return true or false
	 */
	boolean setLastTote(String toteID);
	/**
	 * Returns the Location object.
	 * @return Location object
	 */
	Location getLocationObj();
	/**
	 * Process location response information.
	 * @param rsp Server response to location emptied msg.
	 */
	void processLocationInfo(String rsp);
	/**
	 * Returns the current trolley tote ID.
	 * @return String of trolley tote ID.
	 */
	String getTrolleyTote();
	/**
	 * Test whether the id passed is a valid tote number
	 * or a valid item.
	 * @param id the id passed.
	 * @return 0 if neither tote or item, 1 if valid tote and 2 if valid item.
	 */
	int isValidItemOrTote(String id);
	/**
	 * Returns whether the tote is full or not
	 * @return true if tote is full
	 */
	boolean isToteFull();
	
}
