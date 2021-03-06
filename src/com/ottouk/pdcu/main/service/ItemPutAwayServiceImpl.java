package com.ottouk.pdcu.main.service;

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
		System.out.println("itemputaway comms msg");
		System.out.println(msg);
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
		
		StringUtils.log("location [" + location +"]");
		msg = "i"
				+ StringUtils.padNumber(unitId, four) + "," 
				+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY) + ","
				+ StringUtils.padField(StringUtils.getString(location,1,8), six, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY) + ","
				+ StringUtils.padField(item, thirteen, StringUtils.PAD_ZERO,
						StringUtils.LEFT_JUSTIFY) + ","
				+ EANIndicator;
		StringUtils.log("tote top up comms msg");
		StringUtils.log(msg);

		msg = "i"
			+ StringUtils.padNumber(unitId, four)  
			+ StringUtils.padField(operator, eight, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY) 
			+ StringUtils.padField(StringUtils.getString(location,1,8), six, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY)
			+ StringUtils.padField(item, thirteen, StringUtils.PAD_ZERO,
					StringUtils.LEFT_JUSTIFY) 
				+ EANIndicator;
	StringUtils.log("tote top up comms msg");
	StringUtils.log(msg);
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

}
