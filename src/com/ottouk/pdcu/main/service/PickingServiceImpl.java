package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.domain.Picking;
import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Picking Service Implementation. Handles services on behalf of PickingShell
 * 
 * @author hstd004
 * 
 */
public class PickingServiceImpl extends GeneralServiceImpl implements
		PickingService {
	/**
	 * Instance of Picking.
	 */
	private Picking pickWalk;

	/**
	 * Gets the walk in the Picking class. Simply calls the Picking getWalk
	 * method
	 * 
	 * @return String of the walk in the class
	 */

	/**
	 * Constructor.
	 */
	public PickingServiceImpl() {
		pickWalk = new Picking();
		// System.out.println("Picking Service Started");
	}

	public final String getWalk() {
		return (pickWalk.getWalk());
	}

	/**
	 * Adds a walk to the Picking class providing it is valid.
	 * 
	 * @param pWalk -
	 *            Walk passed to routine
	 * @return RC_WALK_ADDED_OK if walk is ok or same as existing wlak in class
	 *         RC_WARN_WALK_ALREADY_STARTED if this is a different start walk to
	 *         the start walk already present in Picking class
	 *         RC_WARN_NOT_A_START_WALK if the walk passed is not a start walk
	 *         i.e. 9th character is not '1'
	 * 
	 */
	public final int addWalkNo(final String pWalk) {
		// System.out.println("Adding walk...");
		if (!pickWalk.validStartWalk(pWalk)) { 
			return RC_WARN_NOT_A_START_WALK;
		}
		if (pickWalk.iswalkStart()) {
			if (pickWalk.getWalk().compareTo(pWalk) == 0) {
				return RC_WALK_ADDED_OK;
			} else {
				return RC_WARN_WALK_ALREADY_STARTED;
			}
		}
		if (pickWalk.iswalkStart()) {
			return RC_WARN_NOT_A_START_WALK;
		}
		pickWalk.setwalk(pWalk);
		return RC_WALK_ADDED_OK;
	}

	/**
	 * Builds a Picking message to transmit to the DEC Alpha.
	 * 
	 * @param startStop -
	 *            Indicates whether a start or stop walk message should be
	 *            generated, Values : '1' is a start and 'r' is a stop
	 * @return String - contains the built message ready for transacting
	 */
	public final String buildMessage(final char startStop) {
		final int eight = 8;
		return (buildMessage(getWalk().substring(0, eight), startStop));
	}

	/**
	 * Builds a Picking message to transmit to the DEC Alpha.
	 * 
	 * @param startStop -
	 *            Indicates whether a start or stop walk message should be
	 *            generated, Values : '1' is a start and 'r' is a stop
	 * @param walk -
	 *            Independent walk number to build message with
	 * @return String - contains the built message ready for transacting
	 */
	public final String buildMessage(final String walk, final char startStop) {
		String msg;
		final int four = 4;
		final int eight = 8;
		msg = "E"
				+ StringUtils.padNumber(unitId, four)
				+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY);
		msg = msg + walk + startStop;
		return msg;
	}

	/**
	 * Sends the message passed to the DEC Alpha.
	 * 
	 * @param msg -
	 *            String containing message to be sent
	 * @return - true if message sent OK , false if not
	 */
	public final boolean sendMessage(final String msg) {
		return (transact(msg));
	}

	/**
	 * Checks that the walk passed is a valid end walk. Will be valid if Walk in
	 * class by calling IsValidEndWalk
	 * 
	 * @param walk -
	 *            Walk number passed
	 * @return int - Value indicating action taken
	 */
	public final int endWalkNo(final String walk) {
		if (pickWalk.getWalk().compareTo(walk) == 0) {
			return RC_WARN_WALK_ALREADY_STARTED;
		}
		if (pickWalk.isValidEndWalk(walk)) {
			pickWalk.endWalk();
			return RC_WALK_ADDED_OK;
		}
		return RC_WARN_WALK_NOT_STARTED;
	}

	/**
	 * Calls the validWalkScan method in class Picking.
	 * 
	 * @param walk -
	 *            Walk number passed
	 * @return boolean - true if valid , false if not
	 */
	public final boolean validWalkScan(final String walk) {
		return (pickWalk.validWalkScan(walk));
	}

	/**
	 * Main method for dealing with walk scans. Will either add or end a walk
	 * passed after validation
	 * 
	 * @param walk -
	 *            Walk passed to routine
	 * @return int - Value indication action taken
	 */
	public final int processWalkScan(final String walk) {
		// System.out.println("ProcessWalkScan:: Walk passed " + walk);

		if (pickWalk.isWalkEmpty()) {
			return (addWalkNo(walk));
		} else {
			return (endWalkNo(walk));
		}
	}

	/**
	 * Creates an error message string from an error number passed.
	 * 
	 * @param msg -
	 *            Message number passed
	 * @return String - Contains corresponding error message
	 */
	public final String getErrorMsg(final int msg) {
		if (msg == RC_WARN_NOT_A_START_WALK) {
			return ("Not a Valid Start Walk");
		}
		if (msg == RC_WARN_WALK_ALREADY_STARTED) {
			return ("Walk Already Started");
		}
		if (msg == RC_WARN_WALK_NOT_STARTED) {
			return ("Walk Not Started");
		}

		return ("Unknown Error");
	}

}
