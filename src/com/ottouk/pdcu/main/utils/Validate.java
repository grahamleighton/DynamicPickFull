package com.ottouk.pdcu.main.utils;

/**
 * Barcode validation routines.
 * 
 * @author dis065
 * 
 */
public final class Validate {

	/**
	 * Utility class should not have public constructor.
	 */
	private Validate() {
	}

	/**
	 * Perform mod10 check.
	 * 
	 * @param field the field to check
	 * @param length the required field length
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise
	 */
	public static boolean mod10Check3131(String field, int length) {
		return mod10Check3131(field, length, null);
	}
	
	public static boolean mod10pirequest(String field)
	{
		if (field.length() != 1) return false;
		if (field.charAt(0) < 65 || field.charAt(0) > 90) return false;
		return true;
	}
	/**
	 * Perform mod10 check.
	 * 
	 * @param field the field to check
	 * @param length the required field length
	 * @param startsWith string that the field must start with 
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise
	 */
	public static boolean mod10Check3131(String field, int length, String startsWith) {
		return mod10Check3131(field, new Integer(length), startsWith);
	}
	
	/**
	 * Perform mod10 check.
	 * 
	 * @param field the field to check
	 * @param length the required field length
	 * @param startsWith string that the field must start with 
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise
	 */
	public static boolean mod10Check3131(String field, Integer length, String startsWith) {
		
		if (field == null) {
			return false;
		}
		if ((length != null) && (length.intValue() != field.length())) {
			return false;
		}
		if (startsWith != null && !field.startsWith(startsWith)) {
			return false;
		}

		// Valid3131
		int total = 0;
		for (int factor = 3, i = 0; i < field.length(); i++) {
			total = total + ((field.charAt(i) - '0') * factor);
			factor = (factor == 3 ? 1 : 3);
		}

		int check = total % 10;

		return (check == 0 || check == 10);
	}

	/**
	 * Perform mod11 check.
	 * 
	 * @param field the field to check
	 * @param length the required field length
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise
	 */
	public static boolean mod11Check(String field, int length) {
		return mod11Check(field, new Integer(length));
	}
	
	/**
	 * Perform mod11 check.
	 * 
	 * @param field the field to check
	 * @param length the required field length
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise
	 */
	public static boolean mod11Check(String field, Integer length) {
		
		if (field == null) {
			return false;
		}
		int fieldLength = field.length();
		if ((length != null) && (length.intValue() != fieldLength)) {
			return false;
		}

		int total = 0;
		for (int i = 0, j = fieldLength; i < (fieldLength - 1); i++, j--) {
			total = total + ((field.charAt(i) - '0') * j);
		}

		int check = total % 11;
		if (check > 9) {
			check = 0;
		}

		return (check == (field.charAt(fieldLength - 1) - '0'));
	}
	/**
	 * Mod Check utility to return the check digit for a string.
	 * @param field - String of numbers
	 * @param length - length of string
	 * @return integer containing check digit value
	 */
	public static int mod10Check3131CD(final String field, final int length) {
		
		int total = 0;
		for (int factor = 3, i = 0; i < field.length(); i++) {
			total = total + ((field.charAt(i) - '0') * factor);
			factor = (factor == 3 ? 1 : 3);
		}

		int check = total % 10;
		check = 10 - check;
		
		if ( check == 10 )
			check = 0;
		return check ;
		
	}

	
}
