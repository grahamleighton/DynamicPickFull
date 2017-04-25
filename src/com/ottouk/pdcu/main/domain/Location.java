package com.ottouk.pdcu.main.domain;

/**
 * Interface for Location class.
 * @author hstd004
 *
 */
public interface Location {
	/**
	 * Sets the Location fields.
	 * @param alpha - Alpha Location
	 * @param num - Numeric Location 
	 */
	void setLocation(String alpha, String num);
	/**
	 * Sets the Location fields by splitting the param into 2.
	 * @param alphanum Location passed as 6 byte alpha 
	 * followed by 6 byte numeric
	 */
	void setLocationAlphaNum(String alphanum);
	/**
	 * Retrieves the Alpha location in display format.
	 * @return Alpha in format XX99-9X
	 */
	String getDisplayLocation();
	/**
	 * Retrieves the numeric portion of the location.
	 * @return Numeric 6 byte location
	 */
	String getNumericLocation();
	/**
	 * Retrieves the Alpha 6 byte location.
	 * @return Alpha 6 byte location
	 */
	String getAlphaLocation();
	/**
	 * validates that the location passed matches the location in the class.
	 * @param loc Passed location
	 * @return true if match
	 */
	boolean matchLocation(String loc);
	/**
	 * Validates that the location passed has a valid check digit.
	 * @param loc Location passed can be 6 or 8 byte
	 * @return true if valid
	 */
	boolean validCheckDigit(String loc);
	/**
	 * Returns true if valid location held in class.
	 * @return true or false
	 */
	boolean isLocationAvailable();
	/**
	 * extracts the 6 byte location if its 8 bytes.
	 * returns the 6 bytes if it is 6.
	 * @param numLoc Numeric Location
	 * @return String of 6 byte location
	 * @return
	 */
	String extractLocation(String numLoc);
	/**
	 * Returns the last location used.
	 * @param displayFormat true =  normal (XX999X) or false = display (XX99-9X)
	 * @return String of Alpha location in format XX99-9X
	 */
	String getLastLocationAlpha(boolean displayFormat);
	/**
	 * Returns the last location used.
	 * @return String of Numeric location in format 999999.
	 */
	String getLastLocationNumeric();
	/**
	 * Checks if location passed is a match to the last location scanned.
	 * @param beamLoc 6 or 8 byte numeric location.
	 * @return true if match
	 */
	boolean isMatchToLastLocation(String beamLoc);
	
}
