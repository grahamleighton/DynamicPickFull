package com.ottouk.pdcu.version.dao;

/**
 * Format fields using Java <= 1.3 methods.
 * 
 * @author dis065
 * 
 */
public final class StringUtils {
	
	/**
	 * Utility class should not have public constructor.
	 */
	private StringUtils() {
	} 

	/**
	 * 
	 */
	public static final char LEFT_JUSTIFY = 'L';
	/**
	 * 
	 */
	public static final char RIGHT_JUSTIFY = 'R';
	/**
	 * 
	 */
	public static final char PAD_SPACE = ' ';
	/**
	 * 
	 */
	public static final char PAD_ZERO = '0';

	
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
	 * @param string
	 * @param beginIndex the begining index, inclusive
	 * @param endIndex the ending index, exclusive
	 * @return
	 */
	public static Integer getInteger(String string, int beginIndex, int endIndex) {
		
		try {
			return new Integer(getString(string, beginIndex, endIndex));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * @param string
	 * @param beginIndex the begining index, inclusive
	 * @param endIndex the ending index, exclusive
	 * @return
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
	 * @param str
	 * @return
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
	
}
