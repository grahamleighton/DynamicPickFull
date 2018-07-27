package com.ottouk.pdcu.main.utils;

import java.util.Calendar;

/**
 * Format fields using Java <= 1.3 methods.
 * 
 * @author dis065
 * 
 */
public final class StringUtils {
	
	/**
	 * Left justify.
	 */
	public static final char LEFT_JUSTIFY = 'L';
	
	/**
	 * Right justify.
	 */
	public static final char RIGHT_JUSTIFY = 'R';
	
	/**
	 * Space pad.
	 */
	public static final char PAD_SPACE = ' ';
	
	/**
	 * Zero pad.
	 */
	public static final char PAD_ZERO = '0';
	
	/**
	 * This field must be set to <code>true</code> for the <code>log()</code>
	 * methods to write to the system output stream.
	 */
	private static boolean debug;
	
	/**
	 * Calendar object, required for <code>log()</code> datestamp.
	 */
	private static Calendar rightNow;
	
	
	/**
	 * Private empty constructor.
	 */
	private StringUtils() {
	} 
	
	
	/**
	 * Left-pad field with zeroes.
	 * 
	 * @param field
	 *            field to be padded
	 * @param requiredFieldlength
	 *            required length of field
	 * @return field padded (or truncated) to requiredFieldLength
	 */
	public static String padNumber(final Integer field,
			final int requiredFieldlength) {
		return padNumber((field == null ? null : String.valueOf(field)), requiredFieldlength);
	}
	
	/**
	 * Left-pad field with zeroes.
	 * 
	 * @param field
	 *            field to be padded
	 * @param requiredFieldlength
	 *            required length of field
	 * @return field padded (or truncated) to requiredFieldLength
	 */
	public static String padNumber(final int field,
			final int requiredFieldlength) {
		return padNumber(String.valueOf(field), requiredFieldlength);
	}
	
	/**
	 * Left-pad field with zeroes.
	 * 
	 * @param field
	 *            field to be padded
	 * @param requiredFieldlength
	 *            required length of field
	 * @return field padded (or truncated) to requiredFieldLength
	 */
	public static String padNumber(final String field,
			final int requiredFieldlength) {
		return padField(field, requiredFieldlength, PAD_ZERO, RIGHT_JUSTIFY);
	}

	/**
	 * Right-pad field with spaces.
	 * 
	 * @param field
	 *            field to be padded
	 * @param requiredFieldlength
	 *            required length of field
	 * @return field padded (or truncated) to requiredFieldLength
	 */
	public static String padField(final String field,
			final int requiredFieldlength) {
		return padField(field, requiredFieldlength, PAD_SPACE, LEFT_JUSTIFY);
	}

	/**
	 * Pad field (left or right) with any character.
	 * 
	 * @param field
	 *            field to be padded (or truncated)
	 * @param requiredFieldlength
	 *            required length of field
	 * @param padChar
	 *            character to pad field with
	 * @param justification
	 *            either Right justify field and pad to left, or Left justify
	 *            field and pad to right
	 * @return field padded (or truncated) to requiredFieldLength
	 */
	public static String padField(String field, final int requiredFieldlength,
			final char padChar, char justification) {

		// Default justification
		if ((justification != LEFT_JUSTIFY) 
				&& (justification != RIGHT_JUSTIFY)) {
			justification = LEFT_JUSTIFY;
		}

		// Default field
		if (field == null) {
			field = "";
		} /*else {
			field = field.trim();
		}*/
		 

		// Truncate oversized fields and return
		int length = field.length();
		if (length > requiredFieldlength) {
			if (justification == RIGHT_JUSTIFY) {
				field = field.substring(length - requiredFieldlength, length);
			} else {
				field = field.substring(0, requiredFieldlength);
			}
			return field;
		}

		// Pad undersized fields
		String padString = "";
		while ((length + padString.length()) < requiredFieldlength) {
			padString += padChar;
		}
		if (justification == RIGHT_JUSTIFY) {
			field = padString + field;
		} else {
			field += padString;
		}

		return field;
	}
	
	/**
	 * Extract Integer from String.
	 * 
	 * @param string the string to be parsed
	 * @param beginIndex the beginning index, inclusive
	 * @param endIndex the ending index, exclusive
	 * @return Integer representation of substring
	 */
	public static Integer getInteger(String string, int beginIndex, int endIndex) {
		
		try {
			return new Integer(getString(string, beginIndex, endIndex));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Extract substring from string.
	 * 
	 * @param string the string to be parsed
	 * @param beginIndex the beginning index, inclusive
	 * @param endIndex the ending index, exclusive
	 * @return substring
	 */
	public static String getString(String string, int beginIndex, int endIndex) {
		
		String substring = null;
		
		try {
			substring = string.substring(beginIndex, endIndex);
		} catch (IndexOutOfBoundsException e) {
			substring = null;
		}
		
		return (substring == null ? null : substring.trim());
	}
	
	/**
	 * Extract and concatenate all the digits in a string.
	 * 
	 * @param str the string to be parsed
	 * @return Integer
	 */
	public static Integer extractNumbersFromString(String str) {
		
		if (str == null) {
	        return null;
	    }

	    StringBuffer strBuff = new StringBuffer();
	    char c;
	    
	    for (int i = 0; i < str.length(); i++) {
	        c = str.charAt(i);
	        
	        if (Character.isDigit(c)) {
	            strBuff.append(c);
	        }
	    }
	    
	    try {
			return new Integer(strBuff.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Extract and concatenate all the digits in a string.
	 * 
	 * @param str the string to be parsed
	 * @return Integer
	 */
	public static String getNumbersFromString(String str) {
		
		if (str == null) {
	        return null;
	    }

	    String strBuff = "";
	    char c;
	    
	    for (int i = 0; i < str.length(); i++) {
	        c = str.charAt(i);
	        
	        if (Character.isDigit(c)) {
	            strBuff = strBuff + c;
	        }
	    }
	    
	    return strBuff;
	    
	}

	
	/**
	 * String find and replace.
	 * 
	 * @param str the string to be searched 
	 * @param find the substring to search for
	 * @param replace the replacement substring
	 * @return new String
	 */
	public static String replace(String str, String find, String replace) {

		String newStr = "";
		
		int beginIndex = 0;
		int endIndex = 0;
		while (true) {
			endIndex = str.indexOf(find, beginIndex);
			
			if (endIndex == -1) {
				newStr += str.substring(beginIndex);
				break;
			} else {
				newStr += str.substring(beginIndex, endIndex);
				newStr += replace;
				beginIndex = endIndex + find.length();
			}
		}
		
		return newStr;
	}
	
	/**
	 * Split string, returning first section.
	 * 
	 * @param str the string to be split
	 * @param splitStr the string to split on
	 * @return substring of string up to the split
	 */
	public static String split(String str, String splitStr) {
		
		if (str == null || splitStr == null || splitStr.length() == 0) {
	        return str;
	    }
		
		try {
			return str.substring(0, str.indexOf(splitStr));
		} catch (IndexOutOfBoundsException e) {
			return str;
		}
	}
	
	/**
	 * Return current time as year/month/day hours:minutes:seconds.milliseconds.
	 * 
	 * @return time stamp
	 */
	public static String getTimeStamp() {

		rightNow = Calendar.getInstance();

		return (padNumber(rightNow.get(Calendar.YEAR), 4)
				+ "-"
				+ padNumber(rightNow.get(Calendar.MONTH) + 1, 2)
				+ "-"
				+ padNumber(rightNow.get(Calendar.DAY_OF_MONTH), 2)
				+ " "
				+ padNumber(rightNow.get(Calendar.HOUR_OF_DAY), 2)
				+ ":"
				+ padNumber(rightNow.get(Calendar.MINUTE), 2)
				+ ":"
				+ padNumber(rightNow.get(Calendar.SECOND), 2)
				+ "."
				+ padNumber(rightNow.get(Calendar.MILLISECOND), 3));
	}
	
	/**
	 * Write time stamp to console if debugging is enabled.
	 */
	public static void log() {
		log("");
	}
	
	/**
	 * Prefix int with time stamp and write to console if debugging is enabled.
	 * 
	 * @param str
	 *            The object to be written
	 */
	public static void log(int str) {
		log("" + str);
	}
	
	/**
	 * Prefix object with time stamp and write to console if debugging is enabled.
	 * 
	 * @param str
	 *            The object to be written
	 */
	public static void log(Object str) {
		log(str, debug);
	}
	
	/**
	 * Prefix object with time stamp and write to console.
	 * 
	 * @param str
	 *            The object to be written
	 * @param debug
	 *            set to <code>true</code> to write regardless of whether
	 *            debugging is enabled
	 */
	public static void log(Object str, boolean debug) {
		if (debug) {
			
			try {
				System.out.println(getTimeStamp() + " " + str);
			} catch (RuntimeException e) {
				// Error while writing to system output stream.  Turn off further logging.
				setDebug(false);
			}
			
		}
	}

	/**
	 * Getter for <code>debug</code> field. 
	 * 
	 * @return true if debugging is enabled
	 */
	public static boolean isDebug() {
		return debug;
	}

	/**
	 * Setter for <code>debug</code> field. 
	 * 
	 * @param debug boolean
	 */
	public static void setDebug(boolean debug) {
		StringUtils.debug = debug;
	}
}
