package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

/***
 * 
 * @author hstd004
 *
 */
public class LocationDetailServiceImpl 
	extends GeneralServiceImpl 
	implements LocationDetailService {

	/***
	 * holds the numeric location.
	 */
	private String numericLocation;
	/***
	 * holds the numeric location in full with cd.
	 */
	private String numericLocation8;
	/***
	 * holds the alphanumeric location.
	 */
	private String alphaLocation;
	/***
	 * retrieves the numeric location
	 * @return String Numeric location
	 */
	public final String getNumericLocation()
	{
		return numericLocation;
	}
	private void setNumericLocation ( final String numLoc ) {
		numericLocation = numLoc;
		setCheckDigit();
	}
	
	private void setCheckDigit()
	{
		if ( numericLocation.length() == MainConstants.SIX )
		{
			String locStub = "0" + numericLocation;
			
			
			numericLocation8 = locStub + "0";
			if ( Validate.mod10Check3131(numericLocation8, MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "1";
			if ( Validate.mod10Check3131(numericLocation8, MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "2";
			if ( Validate.mod10Check3131(numericLocation8, MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "3";
			if ( Validate.mod10Check3131(numericLocation8, MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "4";
			if ( Validate.mod10Check3131(numericLocation8, MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "5";
			if ( Validate.mod10Check3131(numericLocation8 , MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "6";
			if ( Validate.mod10Check3131(numericLocation8 , MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "7";
			if ( Validate.mod10Check3131(numericLocation8, MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "8";
			if ( Validate.mod10Check3131(numericLocation8 , MainConstants.LOCATION_LENGTH)) {
				return;
			}
			numericLocation8 = locStub + "9";
			if ( Validate.mod10Check3131(numericLocation8 , MainConstants.LOCATION_LENGTH)) {
				return;
			}

			
		}
	}
	/***
	 * retrieves the numeric location as 8 characters
	 * @return String Numeric location
	 */
	public final String getNumericLocation8()
	{
		
		return numericLocation8;
	}
	/***
	 * Retrieves the AlphaNUmeric location
	 * @return String Alphanumeric location
	 */
	public final String getAlphaLocation()
	{
		return alphaLocation;
	}
	/***
	 * 
	 * @param loc
	 * @return
	 */
	public final boolean isLocationValidNumericOrAlpha(final String loc) {
		return ( 
				isNumericLocationValid ( loc ) || 
				isAlphaLocationValid( loc ) 
				);
		
	}
	
	public final boolean isNumericLocationValid(final String loc)
	{
		String numberString = "";
		
		if ( loc.length() > 0 )
		{
			numberString = StringUtils.getNumbersFromString(loc);
			if ( numberString.length() == loc.length() )
			{
				if ( numberString.length() == MainConstants.SIX ) {
					return true;
				}
				if ( numberString.length() == MainConstants.EIGHT ) {
					return true;
				}
				return false;
			}
		}
		
		return false;
	}
	
	public final boolean isAlphaLocationValid(final String loc)
	{
		if ( loc.length() != 6 ) {
			return false;
		}

		String ch1 = loc.substring(0,1);    /* (A)A001A */
		
	
		if ( ch1.equalsIgnoreCase("0")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("1")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("2")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("3")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("4")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("5")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("6")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("7")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("8")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("9")) {
			return false;
		}

		ch1 = loc.substring(1,2); 			/* A(A)001A */
		
		
		if ( ch1.equalsIgnoreCase("0")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("1")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("2")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("3")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("4")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("5")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("6")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("7")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("8")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("9")) {
			return false;
		}

		ch1 = loc.substring(5,6);		/* AA001(A) */
		
		if ( ch1.equalsIgnoreCase("0")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("1")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("2")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("3")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("4")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("5")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("6")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("7")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("8")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("9")) {
			return false;
		}

		ch1 = loc.substring(2,3);		/* AA(0)01A */

		if ( ch1.equalsIgnoreCase("A")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("B")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("C")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("D")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("E")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("F")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("G")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("H")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("I")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("J")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("K")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("L")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("M")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("N")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("O")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("P")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Q")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("R")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("S")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("T")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("U")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("V")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("W")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("X")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Y")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Z")) {
			return false;
		}
		
		ch1 = loc.substring(3,4);		/* AA0(0)1A */

		if ( ch1.equalsIgnoreCase("A")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("B")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("C")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("D")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("E")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("F")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("G")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("H")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("I")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("J")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("K")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("L")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("M")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("N")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("O")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("P")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Q")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("R")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("S")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("T")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("U")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("V")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("W")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("X")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Y")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Z")) {
			return false;
		}
		
		ch1 = loc.substring(4,5);		/* AA00(1)A */

		if ( ch1.equalsIgnoreCase("A")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("B")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("C")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("D")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("E")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("F")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("G")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("H")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("I")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("J")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("K")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("L")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("M")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("N")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("O")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("P")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Q")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("R")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("S")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("T")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("U")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("V")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("W")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("X")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Y")) {
			return false;
		}
		if ( ch1.equalsIgnoreCase("Z")) {
			return false;
		}

		
		return true;
	}
	
	/***
	 * Gets the location details from the DEC Alpha
	 * @param loc String location either 6 numeric or 6 alpha style location
	 * @return LocaiotnImpl class
	 */
	public final boolean getLocationDetails(final String loc )
	{
	
		LocationImpl l = new LocationImpl();
		
		numericLocation = "";
		alphaLocation = "";
		
		String loc2 = l.getLocationString(loc);
		if (loc2.length() > 0 )
		{
			String msg;
			msg = "["
				+ StringUtils.padNumber(unitId, MainConstants.FOUR)
				+ StringUtils.padField(operator, MainConstants.EIGHT, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY);
			msg = msg + loc;
			
			sendMessage(msg);

			String resp = getServerResponse();
		
			if ( resp.length() > MainConstants.THREE )
			{
				/* 
				 *  01234567890123456
				 *  ACKY123456AA001A 
				 * 	
				 * */
				if ( StringUtils.getString(resp,0,MainConstants.FOUR).equalsIgnoreCase("ACKY") )
				{
					l.setLocation(resp.substring(4,10), resp.substring(10,16));
			//		System.out.println("l.getNumericLocation()");
					
					setNumericLocation(resp.substring(10,16));
					alphaLocation = resp.substring(4,10);
					
			//		System.out.println(l.getNumericLocation());

			//		System.out.println("l.getAlphaLocation()");
			//		System.out.println(l.getAlphaLocation());
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * Sends the message passed to the DEC Alpha.
	 * 
	 * @param msg -
	 *            String containing message to be sent
	 * @return - true if message sent OK , false if not
	 */
	private final boolean sendMessage(final String msg) {
		return (transact(msg));
	}
	
	private String getServerResponse() {
		return getResponse();
	}
	
	

}
