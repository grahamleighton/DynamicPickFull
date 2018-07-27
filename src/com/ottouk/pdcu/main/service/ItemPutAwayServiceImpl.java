package com.ottouk.pdcu.main.service;

import java.util.List;

import com.ottouk.pdcu.main.domain.Location;
import com.ottouk.pdcu.main.utils.StringUtils;

public class ItemPutAwayServiceImpl extends GeneralServiceImpl implements ItemPutAwayService {

	public void buildSendItemPutaway(String text) {
		
		String msg;
		final int four = 4;
		final int eight = 8;

		msg = "B"
				+ StringUtils.padNumber(unitId, four)
				+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY);
		msg = msg + StringUtils.getString(text,0,9);
//		System.out.println("itemputaway comms msg");
//		System.out.println(msg);
		sendMessage(msg);
	}


/*	public LocationImpl getLocationDetails(String loc )
	{
	
		LocationImpl l = new LocationImpl();
		
		String msg;
		
		String loc2 = l.getLocationString(loc);
		if (loc2.length() > 0 )
		{
			msg = "["
				+ StringUtils.padNumber(unitId, MainConstants.FOUR)
				+ StringUtils.padField(operator, MainConstants.EIGHT, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY);
			msg = msg + loc;
			sendMessage(msg);

			String resp = getServerResponse();
		
			if ( resp.length() > MainConstants.THREE )
			{
				 
				 *  01234567890123456
				 *  ACKY123456AA001A 
				 * 	
				 * 
				if ( StringUtils.getString(resp,0,MainConstants.FOUR).equalsIgnoreCase("ACKY") )
				{
					l.setLocation(resp.substring(4,10), resp.substring(10,16));
					System.out.println("l.getNumericLocation()");

					System.out.println(l.getNumericLocation());

					System.out.println("l.getAlphaLocation()");
					System.out.println(l.getAlphaLocation());
				}
			}
		}
		
		
		return l;
	}
*/	
	
	public void buildSendToteTopUpRequest(String location,String RequestType)
	{
		String msg;
		final int four = 4;
		final int eight = 8;
		String loc = "";
		
		if ( location.length() < 7)
		{
			loc = location;
		}
		else
		{
			loc = StringUtils.getString(location, 1, 7);
		}

		msg = "q"
				+ StringUtils.padNumber(unitId, four)
				+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY);
		msg = msg + loc;
		msg = msg + StringUtils.getString(RequestType,0,1);
//		System.out.println("tote request message");
//		System.out.println(msg);
		sendMessage(msg);
		
	}
	
	
	public void buildSendMultiItemToteTopUp(List listItems , String location) {
			String msg;
			final int four = 4;
			final int eight = 8;
			final int six = 6;
			int loop = 0;
			

		msg = "s"
			+ StringUtils.padNumber(unitId, four)  
			+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY) 
			+ StringUtils.padField(location, six, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY)
			+ StringUtils.padNumber(listItems.size(),2);
		
		while ( loop < listItems.size() )
		{
			String s = (String)listItems.get(loop);
			msg = msg + StringUtils.padField(s, eight,StringUtils.PAD_ZERO, StringUtils.LEFT_JUSTIFY);
			loop++;		
		}
			
//	StringUtils.log("tote top up comms msg");
//	StringUtils.log(msg);
	sendMessage(msg);
			
	}
	public void buildSendToteTopUp(String item , String location, int ScanLength) {
		
		String msg;
		final int four = 4;
		final int eight = 8;
		final int six = 6;
		final int thirteen = 13;
		String EANIndicator = "N";
		
		if ( ScanLength == thirteen ) {
			EANIndicator = "Y";
		}


/*
typedef struct
{
        char base_station[2];
        char pdcu_id[4];
        char operator[8];
        char location[6];
        char item[13];
        char EAN;
} tote_topup_t;

'i' message

 */		
		
//		StringUtils.log("location [" + location +"]");
		msg = "i"
				+ StringUtils.padNumber(unitId, four) + "," 
				+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY) + ","
				+ StringUtils.padField(StringUtils.getString(location,1,8), six, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY) + ","
				+ StringUtils.padField(item, thirteen, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY) + ","
				+ EANIndicator;
//		StringUtils.log("tote top up comms msg");
//		StringUtils.log(msg);

		msg = "i"
			+ StringUtils.padNumber(unitId, four)  
			+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY) 
			+ StringUtils.padField(StringUtils.getString(location,1,8), six, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY)
			+ StringUtils.padField(item, thirteen, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY) 
				+ EANIndicator;
//	StringUtils.log("tote top up comms msg");
//	StringUtils.log(msg);
		sendMessage(msg);
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
	
	public String getServerResponse() {
		return getResponse();
	}
	
	

}
