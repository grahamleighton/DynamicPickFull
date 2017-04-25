package com.ottouk.pdcu.main.service;


import com.ottouk.pdcu.main.domain.Location;
import com.ottouk.pdcu.main.utils.Validate;

/**
 * Implements Location class.
 * @author hstd004
 *
 */
public class LocationImpl implements Location {
	/**
	 * Holds the alphanumeric 6 byte location.
	 */
	private String alpha;
	/**
	 * Holds the numeric 6 byte location.
	 */
	private String num;
	/**
	 * Holds the alphanumeric 6 byte location of the last location used.
	 */
	private String lastalpha;
	/**
	 * Holds the numeric 6 byte location of the last location used.
	 */
	private String lastnum;
	
	/**
	 * Constructor.
	 */
	public LocationImpl() {
		super();
		this.alpha = "";
		this.num = "";
		this.lastalpha = "";
		this.lastnum = "";
	}
	/**
	 * Returns the alpha location.
	 * @return alpha location
	 */
	public final String getAlphaLocation() {
		return alpha;
	}
	/**
	 * Retrieves the alpha location in display format XX99-9X.
	 * @return Alpha location in display format.
	 */
	public final String getDisplayLocation() {
		String temp;
		if (alpha.length() != MainConstants.SIX) {
			return "";
		}
		temp = alpha.substring(0, MainConstants.FOUR) + "-" 
			+ alpha.substring(MainConstants.FOUR, MainConstants.SIX);
		return temp;
	}
	/**
	 * Returns the numeric part of the location.
	 * @return String of numeric location
	 */
	public final String getNumericLocation() {
		return num;
	}
	/**
	 * Checks whether the location passed matches the location.
	 * @param loc String of numeric location to check, 
	 * 	can be 6 bytes or 8 bytes.
	 * @return true if match.
	 */
	public final boolean matchLocation(final String loc) {
		if (loc.length() == MainConstants.SIX) {
			return (loc.equalsIgnoreCase(num));
		}
		if (loc.length() == MainConstants.LOCATION_LENGTH) {
			String tmp = loc.substring(1, MainConstants.SEVEN);
			return (tmp.equalsIgnoreCase(num));
		}
		return false;
	}
	/**
	 * Sets the location in class.
	 * @param alphaLoc - 6 bytye Alpha location
	 * @param numLoc - 6 byte Numeric location
	 */
	public final void setLocation(final String alphaLoc, final String numLoc) {
		lastalpha = alpha;
		lastnum = num;
		alpha = "";
		num  =  "";
		if (alphaLoc.length() == MainConstants.SIX) {
			alpha = alphaLoc;
			if (lastalpha.length() == 0) {
				lastalpha = alpha;
			}
		} else {
			return;
		}
		if (numLoc.length() == MainConstants.SIX) {
			num = numLoc;
			if (lastnum.length() == 0) {
				lastnum = num;
			}
		} else {
			alpha = "";
			num  = "";
			return;
		}
		
	}
	/**
	 * Sets the location from a super string of alpha and numeric location.
	 * @param alphanum 12 byte string of alpha location 
	 * followed by numeric location. 
	 */
	public final void setLocationAlphaNum(final String alphanum) {
		if (alphanum.length() < MainConstants.TWELVE) {
			alpha = "";
			num  = "";
			return;
		}
		setLocation(alphanum.substring(0, MainConstants.SIX), 
				alphanum.substring(MainConstants.SIX, MainConstants.TWELVE));
	}
	/**
	 * Validates the check digit on location passed.
	 * @param loc Location to validate
	 * @return true if valid
	 */
	public final boolean validCheckDigit(final String loc) {
		if (loc.length() != MainConstants.LOCATION_LENGTH) {
			return false;
		}
		if (!loc.substring(0, 1).equalsIgnoreCase("0")) {
			return false;
		}
		return (Validate.mod10Check3131(loc, MainConstants.LOCATION_LENGTH));
	}
	/**
	 * Returns true if location is held in class.
	 * @return true or false
	 */
	public final boolean isLocationAvailable() {
		if (alpha.length() == 0) {
			return false;
		}
		return true;
	}
	/**
	 * Returns 6 byte location from location passed.
	 * @param numLoc Numeric Location either 6 or 8 byte
	 * @return String of 6 byte location
	 */
	public final String extractLocation(final String numLoc) {
		String loc = "";
		if (numLoc.length() == MainConstants.SIX) {
			return numLoc;
		}
		if (numLoc.length() == MainConstants.LOCATION_LENGTH) {
			loc = numLoc.substring(1, MainConstants.SEVEN);
		}
		return loc;
	}
	/**
	 * Returns the last location used.
	 * @return String of Alpha location in format XX99-9X
	 * @param displayFormat boolean value as whether to return the 
	 * alphalocation in display format or not.
	 */
	public final String getLastLocationAlpha(final boolean displayFormat) {
		if (displayFormat) {
			return getLastLocationDisplay();
		}
		return lastalpha;
	}
	/**
	 * Returns the last location used.
	 * @return String of Alpha location in format XX99-9X
	 */
	public final String getLastLocationDisplay() {
		String tmp = alpha;
		String displayformat = "";
		alpha = lastalpha;
		displayformat = getDisplayLocation();
		alpha = tmp;
		
		return displayformat;
	}
	/**
	 * Returns the last location used.
	 * @return String of Numeric location in format 999999.
	 */
	public final String getLastLocationNumeric() {
		return lastnum;
	}
	/**
	 * Checks if location passed is a match to the last location scanned.
	 * @param beamLoc 6 or 8 byte numeric location.
	 * @return true if match
	 */
	public final boolean isMatchToLastLocation(final String beamLoc) {
		if (beamLoc.length() == MainConstants.LOCATION_LENGTH) {
			if (!validCheckDigit(beamLoc)) {
				return false;
			}
			if (extractLocation(beamLoc).equalsIgnoreCase(lastnum)) {
				return true;
			}
		} else {
			if (beamLoc.length() == MainConstants.EMBEDDED_LOCATION_LENGTH) {
				return (beamLoc.equalsIgnoreCase(lastnum));
			}
		}
		return false;
	}


}
