package com.ottouk.pdcu.main.service;
/**
 * Picking Service Class.
 * @author hstd004
 *
 */
public interface PickingService  extends GeneralService {
	
	/**
	 * Picking ID for Activity Message.
	 */
	String PICKING_MESSAGE_ID = "C";
	/**
	 * Signals that a walk has not been started.
	 */
	int RC_WARN_WALK_NOT_STARTED = 100;
	/**
	 * Signals that a walk is not a start walk ( 9th char not '1' ).
	 */
	int RC_WARN_NOT_A_START_WALK = 101;
	/**
	 * Signals that a walk has already been started. i.e picking class
	 * contains a different walk.
	 */
	int RC_WARN_WALK_ALREADY_STARTED = 102;
	/**
	 * Signals that a walk has been added to the Picking class
	 * successfully.
	 */
	int RC_WALK_ADDED_OK = 103;
	/**
	 * Signals that a walk has been entered and is invalid
	 * successfully.
	 */
	int RC_ERROR_INVALID_SCAN = 200;

	// Methods
	/**
	 * Adds a walk to the Picking class
	 * providing it is valid.
	 * @param pWalk - Walk passed to routine
	 * @return 
	 * RC_WALK_ADDED_OK if walk is ok or 
	 * same as existing wlak in class
	 * RC_WARN_WALK_ALREADY_STARTED if this is a
	 * different start walk to the start walk
	 * already present in Picking class
	 * RC_WARN_NOT_A_START_WALK if the walk passed 
	 * is not a start walk i.e. 9th character is not '1'
	 * 
	 */
	int addWalkNo(String pWalk);
	/**
	 * Checks that the walk passed is a valid end walk.
	 * Will be valid if Walk in class by calling IsValidEndWalk
	 *  @param walk - Walk number passed 
	 *  @return int - Value indicating action taken
	 */	
	int endWalkNo(String walk);
	/**
	 * Main method for dealing with walk scans.
	 * Will either add or end a walk passed after
	 * validation
	 * @param walk - Walk passed to routine
	 * @return int - Value indication action taken
	 */
	int processWalkScan(String walk);
	/**
	 * Creates an error message string from an error 
	 * number passed.
	 * @param msg - Message number passed
	 * @return String - Contains corresponding error message
	 */
	String getErrorMsg(int msg);
	/**
	 * Calls the validWalkScan method in class
	 * Picking.
	 * @param walk - Walk number passed
	 * @return boolean - true if valid , false if not
	 */
	boolean validWalkScan(String walk);
	/**
	 * Gets the walk in the Picking class.
	 * Simply calls the Picking getWalk method
	 * @return String of the walk in the class
	 */
	String getWalk();
	/**
	 * Builds a Picking message to transmit to the 
	 * DEC Alpha.
	 * @param startStop - Indicates whether a start or stop walk message 
	 * should be generated, Values : '1' is a start and 'r' is a stop
	 * @return String - contains the built message ready for transacting
	 */ 
	String buildMessage(char startStop);
	/**
	 * Builds a Picking message to transmit to the 
	 * DEC Alpha.
	 * @param startStop - Indicates whether a start or stop walk message
	 * should be generated, Values : '1' is a start and 'r' is a stop
	 * @param walk - Independent walk number to build message with  
	 * @return String - contains the built message ready for transacting
	 */ 
	String buildMessage(String walk, char startStop);
	/**
	 * Sends the message passed to the DEC Alpha.
	 * @param msg - String containing message to be sent
	 * @return - true if message sent OK , false if not
	 */
	boolean sendMessage(String msg);
	

	

}
