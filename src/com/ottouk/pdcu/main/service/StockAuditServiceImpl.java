package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.domain.StockAudit;
//import com.ottouk.pdcu.main.ui.StockAuditShell;
import com.ottouk.pdcu.main.ui.StockAuditShell;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;
public class StockAuditServiceImpl extends GeneralServiceImpl implements StockAuditService {

	private StockAuditService stockauditservice;

	private String messageId = "L";
	
	private String Header = "F";
	
	//private StockAudit locationRequest;
	
	//private StockAudit successful;
	
	public StockAuditServiceImpl pi;
	
	
	public StockAuditShell sc;
	
	private String status;

	public String location;

	public String m;

	private String response;
	
	private String numeric;
	
//public StockAuditServiceImpl stockloc;
	
	private String found;

	public String alphaLocation[] = new String[11];


	public Integer numericLocation[] = new Integer[11];

	public int loc_count;

	private String locationFollows;
        private String reasonCode;
	
        
       public boolean scanLocationvalidation (String scanLocation){
    	   return Validate.mod10Check3131(scanLocation, 8);
       }
       
      
      public boolean sendFinalPINotB(int a , String Notcat[], String Notopt[], String Notquan[]){
    	  if (a > 0) 
    	  {
    		  String finalmessagePINotB = new String ("");
    		  
    		  finalmessagePINotB = StringUtils.padField(Header, 1);
    		  finalmessagePINotB += StringUtils.padNumber(getUnitId(),4); // Current HHT value..
              finalmessagePINotB += StringUtils.padNumber(getOperator(),8);
    		  
             
              
              for (int i = 0 ; i < 10; i ++) // for displaying the x10 group..
              {
                  if (i < a)
                  {
                      finalmessagePINotB += StringUtils.padNumber(getNumericLocation(i),6); //alpha location...
                      finalmessagePINotB += StringUtils.padNumber(Notcat[i].toUpperCase(), 6); //for catalogue number..
                      finalmessagePINotB += StringUtils.padNumber(Notopt[i].toUpperCase(), 3); // for option number..
                      finalmessagePINotB += StringUtils.padNumber(Notquan[i], 5); //for quantity..
                      finalmessagePINotB += StringUtils.padNumber(reasonCode,1); // Reason code from request made earlier..
                       
                  }//else{
                    //  message += PI_EMPTY_BUFFER;
                  //	}
              	}
              	finalmessagePINotB += PI_EMPTY_BUFFER; // Empty buffer.. //this will add spaces and not 0 so i took off the padnumber coz if there is a pad number it will return a 0...
           
              	System.out.println (finalmessagePINotB);
              
              	boolean transactOK = transact(finalmessagePINotB);
             		
              	
              	if (transactOK) {
              	//get response from the server.....
              		String NotBresponse = getResponse();

              		System.out.println (NotBresponse); //displaying the response on the console..
              	
                  return returnCode(RC_OK);   
              }
              errorBox("Error occured during sending message back to server.");
          }

          return true;   
      }
       
      //sending the gathered information  to the server from the cat and option number togther with the quantity ...
        public boolean sendFinal(int n, String cat[], String opt[], String quan[]){ // String alphaloc[])
            StringUtils.log ("Sending final message to the server ");
            if (n > 0)
            {
                String finmessage = new String ("");

                finmessage += StringUtils.padField(Header, 1); // F
                finmessage += StringUtils.padNumber(getUnitId(),4); // Current HHT value..
                finmessage += StringUtils.padNumber(getOperator(),8); // personnel number=..

                for (int i = 0 ; i < 10; i ++) // for displaying the x10 group..
                {
                    if (i < n)
                    {

                        finmessage += StringUtils.padNumber(getNumericLocation(i),6); //alpha location...
                        finmessage += cat[i].valueOf(PI_REQUEST_NOTB_Cat); //for catalogue number..
                        finmessage += opt[i].valueOf(PI_REQUEST_NOTB_Opt); // for option number..
                        finmessage += StringUtils.padNumber(quan[i], 5); //for quantity..
                        finmessage += StringUtils.padNumber(reasonCode,1); // Reason code from request made earlier..
                         
                    }//else{
                      //  message += PI_EMPTY_BUFFER;
                    //	}
                	}
                	finmessage += PI_EMPTY_BUFFER; // Empty buffer.. //this will add spaces and not 0 so i took off the padnumber coz if there is a pad number it will return a 0...
             
                	System.out.println (finmessage);
                
                	boolean transactOK = transact(finmessage);
               		
                	
                	if (transactOK) {
                	//get response from the server.....
                		String Response = getResponse();
 
                		System.out.println (Response); //displaying the response on the console..
                	
                    return returnCode(RC_OK);   
                }
                errorBox("Error occured during sending message back to server.");
            }

            return true;   
        }
        
        
        
	//Server message....
	public boolean send (String StockAudit){
                reasonCode = StockAudit;
		StringUtils.log ("Stock Audit Message, server message ");
		//send message to the server..
		if (StockAudit.charAt(0) >='A' && StockAudit.charAt(0)<='Z'){
			
				String a = StockAudit;
				a = a.toUpperCase();
				System.out.println(a);
			
				
			//sending message request to the server...
			String PImessage = new String("");
			PImessage += StringUtils.padField(messageId ,1);
			PImessage += StringUtils.padNumber(getUnitId(),4);
			PImessage += StringUtils.padNumber(getOperator(),8);
			PImessage += StringUtils.padNumber (a,1);
			
			
			
			System.out.println(PImessage);
			// System.out.println (StockAudit);
			
			//if it is okay received the message "ACK"
			boolean transactOK = transact(PImessage);
			
			if (transactOK) {
				String RSP = getResponse();

				pi = new StockAuditServiceImpl();
				pi.receiveLocationRequest(RSP);
				//System.out.println(
				// RSP.replaceAll("\\D", ""));
				System.out.println (RSP);
				
				return returnCode (RC_OK); 

			}
			errorBox("Error occured during signaling to server.");
			}
	
		return true;
		
	}

	private void errorBox(String string) {
		// TODO Auto-generated method stub

	}
	
	public void setStatus(String status) {
		this.status = status;
	}


	public void receiveLocationRequest(String response) {
		
		StringUtils.log ("Server Respond");
		
		this.response = StringUtils.getString(response, 0, 3);
		found = StringUtils.getString(response, 3,4);
			
		loc_count = 0;
		
		//if (loc_count < 10 ){ 

			// loc_count =0 ;
			// System.out.println (loc_count);
		
		if (found.compareToIgnoreCase("Y") == 0)
		{
			int len = 4 ;
			while (response.length() >= len + 12 && response.charAt(len) != ' ')
                
			{	  
				
			System.out.println(loc_count);
			if (StringUtils.getInteger(response, len, len + 6 ).intValue() == 0) break;
				//get the numeric Location ..
				numericLocation[loc_count] = StringUtils.getInteger(response, len, len + 6);

				//get the 0 to print out so that the numeric location will be 6 characters long...
				Integer n = numericLocation [loc_count];
				numeric = String.valueOf(n);
				if (numeric.length() < 6 )
					for (int i = 0; i < 6 - numeric.length();i ++)
						numeric = "0" + numeric;
				System.out.println(numeric);
				len += 6;
				
				//get the alphaLocation / LocationDisplay..
				alphaLocation[loc_count] = StringUtils.getString(response, len, len + 6 );
				m = alphaLocation [loc_count];
				len += 6;
				
				System.out.println(alphaLocation[loc_count]);
				len++; //for ignoring the PI type(B) for the server response...
				loc_count = loc_count + 1;	
				}    	
			}
		}
	//} 

	//public String getNumeric(int index){
	//	return numeric;
	//}
	
	public String getFound() {
		return found;
	}

	
	public String getLocationFollows() {
		return locationFollows;
	}
	
	public String getAlphaLocation(int index) {
		//System.out.println(alphaLocation [index]);
		return pi.alphaLocation[index];
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public Integer getNumericLocation(int index) {
		//System.out.println(numericLocation[index]);
		return pi.numericLocation[index];
	}

	public int getLocationCount(){
		return pi.loc_count;
	}
	
	
	//check if the input numbers are validated using the right validation ...
	public boolean ScanLocationValidation (String ScanLocation){
		return Validate.mod10Check3131(ScanLocation, 8);
	}

	public boolean PImessageIDnotB (String StockAudit){
		if (StockAudit.equals(stockauditservice.PI_STOCKAUDIT_LOCATION_REQUEST_MESSAGE_ID)){	
		}
		return true;
	}





}