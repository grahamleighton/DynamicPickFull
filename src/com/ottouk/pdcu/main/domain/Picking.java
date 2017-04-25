package com.ottouk.pdcu.main.domain;


/**
 * Class Picking .
 * used to hold current picking walk scanned and validation
 */

public class Picking {
	/**
	 * Walk holds the current walk scanned.
	 */
	private String walk;
	/**
	 * walk length expected.
	 */
	private final int walklength = 10;
	/**
	 * index of walk type start.
	 */
	private final int walktypestart = 8;
	/**
	 * index of walk type end.
	 */
	private final int walktypeend = 9;
	
	/**
	 * Constructor.
	 */
	public Picking() {
		walk = "";
//		System.out.println("PickWalk initialised");
	}
	/**
	 * Returns the current walk value.  
	 * @return Walk
	 */
	public final String getWalk() {
		return walk;
	}
	/**
	 * Validates the Walk Scan 
	 * 1. Length = walklength ( 10 )
	 * 2. Has to be all numeric
	 * 3. Ninth character has to be a '1' or a '9'
	 * @param pwalk - walk passed to routine
	 * @return true if valid walk , false if not
	 */
	public final boolean validWalkScan(final String pwalk) {
		String pwalktype;
//		System.out.println("ValidwalkScan :: check 1...");
		if (pwalk.length() != walklength) {
			return false;
		}
//		System.out.println("ValidwalkScan :: check 2...");
		if (pwalk.compareTo("0000000000") < 0  
				|| pwalk.compareTo("9999999999") > 0) {
			return false;
		}
//		System.out.println("ValidwalkScan :: check 3...");
		pwalktype = pwalk.substring(walktypestart, walktypeend);
		
		if (pwalktype.compareTo("1") != 0  && pwalktype.compareTo("9") != 0) {
			return false;
		}
		return true;
			
	}
	/**
	 * Validates the Walk Scan as a Start walk 
	 * 1. Length = walklength ( 10 )
	 * 2. Has to be all numeric
	 * 3. Ninth character has to be a '1' 
	 * @param pwalk - walk passed to routine
	 * @return true if valid walk , false if not
	 */
	public final boolean validStartWalk(final String pwalk) {
		String pwalktype;
		if (pwalk.length() != walklength) {
			return false;
		}
		if (pwalk.compareTo("0000000000") < 0  
				|| pwalk.compareTo("9999999999") > 0) {
			return false;
		}
		pwalktype = pwalk.substring(walktypestart, walktypeend);
		
		if (pwalktype.compareTo("1") != 0) {
			return false;
		}
		return true;
			
	}
	/**
	 * Sets the Walk in the class only if the passed walk is valid.
	 * @param pwalk walk passed
	 */
	public final void setwalk(final String pwalk) {
		if (validWalkScan(pwalk)) {
//			System.out.println("picking::Setwalk walk =");
//			System.out.println(walk);
			this.walk = pwalk;
		}
	}
	/**
	 * Checks to see if the walk has been started. If 
	 * there is a walk in the class then it must be a start
	 * walk
	 * @return true if walk is in the class
	 */
	public final boolean iswalkStart() {
		if (walk.compareTo("") == 0) {
			return false;
		}
		return true;
	}
	/**
	 * Checks to see it the walk in the class is empty.
	 * If empty then can accept new start walk
	 * @return true if empty , false if not
	 */
	public final boolean isWalkEmpty() {
//		System.out.println("Picking::iswalkEmpty");
		if (this.walk.compareTo("") == 0) {
			return true;
		}
		return false;
	}
	/**
	 * Checks to see if the walk passed actually contains
	 * data.
	 * @param pwalk - Wlak passed to routine
	 * @return true if valid data
	 */
	public final boolean isWalkEmpty(final String pwalk) {
		if (pwalk.compareTo("") == 0) {
			return true;
		}
		return false;
	}
	/**
	 * Sets the class walk to empty.
	 */
	public final void endWalk() {
		walk = "";
	}
	/**
	 * Checks to see if the walk passed is 
	 * 1. a valid walk i.e has some data
	 * 2. is a valid end walk for the walk in the class 
	 * ( compares first 8 characters )
	 * 
	 * @param pwalk - end walk passed
	 * @return true if it is a valid end walk
	 */
	public final boolean isValidEndWalk(final String pwalk) {
		String walktype = "";
		final int wLength = 8; /* Length of actual walk number if pwalk */  
		final int wStart = 0;  /* Start index of walk number in pwalk */
		String pwalkval = "";
		
//		System.out.println("IsValidEndwalk:Check 1...");
		if (isWalkEmpty(pwalk)) {
			return false;
		}
//		System.out.println("IsValidEndwalk:Check 2...");
		if (isWalkEmpty()) {
			return false;
		}
		walktype = pwalk.substring(walktypestart, walktypeend);
//		System.out.println("IsValidEndwalk:Check 3...");
		pwalkval = pwalk.substring(wStart, wLength);
		if (this.walk.substring(wStart, wLength).compareTo(pwalkval) == 0) {
//			System.out.println("IsValidEndwalk:Check 4...");
			if (walktype.compareTo("9") == 0) {
				return true; 
			}
//			System.out.println("IsValidEndwalk:Not end in 9");
		}
		return false;
	}
}
