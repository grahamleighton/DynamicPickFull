package com.ottouk.pdcu.main.service;



import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

public class CartonPutawayServiceImpl extends GeneralServiceImpl implements CartonPutawayService {

	//String message used to get the messageID that will be send in the server...
	private String messageId ="A";

	//check if the input numbers are validated using the right validation ...
	public boolean cartonvalidation(String CartonNumber) {
		//System.out.println(Validate.mod10Check3131(CartonNumber, 10));
		return Validate.mod10Check3131(CartonNumber, 10);
	}

	//check if the input numbers are validated using the right validation ...
	public boolean LocationValidation(String CartonLocation) {
		return Validate.mod10Check3131(CartonLocation, 10);
	}

	//check if the input numbers are validated using the right validation ...
	public boolean LocationNumberValidation(String LocationNumber) {
		return Validate.mod10Check3131(LocationNumber, 8);
	}

	//validation of the 6 bytes extracted & server communication...
	public boolean validate(String CartonLocation, String LocationNumber, String CartonNumber)
	{
		StringUtils.log("Carton Putaway Service Message:");

		if (CartonLocation.length() == 10 && LocationNumber.length() == 8)
		{

			String loc_4_9 = String.valueOf(CartonLocation).substring(3,9);
			String middle_num = String.valueOf(LocationNumber).substring(1,7);

			//comparing the 6bytes extracted from Carton location and Location Number
			if(loc_4_9.compareTo(middle_num) == 0)
			{
				//sending message request to the server...
				String message = new String("");
				message += StringUtils.padField(messageId , 1);
				message += StringUtils.padNumber(getUnitId(), 4);
				message += StringUtils.padNumber(getOperator(), 8);
				message += StringUtils.padNumber(CartonLocation.substring(0, 9), 9);
				message += StringUtils.padNumber(CartonNumber.substring(0,9),9);

				System.out.println(message);
				
				
				//if it is okay received the message "ACK"
				boolean transactOK = transact(message);
				if (transactOK) {
					return returnCode(RC_OK);
				}
				errorBox("Error occured during signaling to server.");
			}
			else
			{
				errorBox("They don't match.");
				return returnCode(RC_COMMS_TRANSACTION_ERROR);
			}
		}
		else
		{
			errorBox("Barcodes are invalid.");
			return returnCode(RC_COMMS_TRANSACTION_ERROR);
		}
		return true;
	}

	//errorbox message GUI...
	private void errorBox(String string) {
		// TODO Auto-generated method stub
	}
}
