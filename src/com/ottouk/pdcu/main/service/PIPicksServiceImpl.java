package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;

public class PIPicksServiceImpl extends GeneralServiceImpl implements PIPicksService {

		private String messageType ="k";
        private String messageId ="j";

        private String ACK;

        private String data_flag;

        private String alpha_loc;

        private String num_loc;

        private String item_num;

        public String i;
        
        String PIpicks;
      //  private String found;
     private String response;
        public int loc_count;
       
        public PIPicksServiceImpl pipicks;
     //  String status;

        public boolean scanPILocationvalidation (String scanLocation){
           return Validate.mod10Check3131(scanLocation, 8);    
      }
     
        //used to send message 1 again on the server together with message 3
      public boolean sendmessage1(){
    		StringUtils.log("Getting message1 again:");
            
    	  		String PIPickmessage1 = new String("");

               PIPickmessage1 += StringUtils.padField(messageId ,1);

               PIPickmessage1 += StringUtils.padNumber(getUnitId(),4);

               PIPickmessage1 += StringUtils.padNumber(getOperator(),8);

               PIPickmessage1 += i;
               
               //PIPickmessage += StringUtils.padNumber (getOperator(),8);
               //PImessage += StringUtils.padNumber (,1);
               
               //System.out.println (PIPickmessage1);
               
              // System.out.println(PIPickmessage1);

               //if it is okay received the message "ACK"

              boolean transactOK = transact(PIPickmessage1);       

            if (transactOK) {

                    String RSP = getResponse();

                    pipicks = new PIPicksServiceImpl();
                    pipicks.getLocationRequest(RSP);      
                    
                    //System.out.println (RSP);
                      
          
                  return returnCode (RC_OK);
             }

             errorBox("No location found.");
			
                return true;
              
              
       }
     
        
        //Send message to the server 1 ..
               public boolean send (String PIpicks){
            	   i = PIpicks.substring (1,7);
                      
            	   
                       if (PIpicks.length() == 8 && PIpicks != null)

                       {

                               //sending message request to the server...

                               String PIPickmessage = new String("");

                               PIPickmessage += StringUtils.padField(messageId ,1);

                               PIPickmessage += StringUtils.padNumber(getUnitId(),4);

                               PIPickmessage += StringUtils.padNumber(getOperator(),8);

                               PIPickmessage += (i);

                            //   System.out.println(PIPickmessage);

                              // System.out.println (PIpicks);

                               	
                               //if it zs okay received the message "ACK"

                               boolean transactOK = transact(PIPickmessage);

                               if (transactOK) {

                                      String RSP = getResponse();
                                      
                                      pipicks = new PIPicksServiceImpl();
                                      pipicks.getLocationRequest(RSP);                       
                                      
                                     // pipicks = new PIPicksServiceImpl();
                                      //pipicks.getLocationRequest (RSP);
 
                                      	//parsing response...
                                      	
                                       /* ACK = StringUtils.getString(RSP, 0, 3);

                                        data_flag = StringUtils.getString(RSP, 3, 4);

                                        alpha_loc = StringUtils.getString(RSP, 4, 10);

                                        num_loc = StringUtils.getString(RSP, 10, 16);

                                        item_num = StringUtils.getString(RSP, 16, 29);


                                        System.out.println (RSP);
                                      
                                        System.out.println (ACK);
                                        System.out.println (data_flag);
                                        System.out.println (alpha_loc);
                                        System.out.println (num_loc);
                                        System.out.println (item_num);*/
                                   
                                     // System.out.println (RSP);
                                      return returnCode (RC_OK);

                                      
                               }

 
                               else{
                            	   errorBox("No location found.");
                               }
                              
                               errorBox("No location found.");
                       }

                       return true;

               }

               
//used to send the message3 on the server..
        public boolean send2 (String status){ // message 3

        	StringUtils.log("Sending final message to the server");
        	
                       if (pipicks.alpha_loc.length() == 6 )

                       {

                               //sending message request to the server...

                               String PIPickmessage3 = new String("");

                               PIPickmessage3 += StringUtils.padField(messageType ,1);

                               PIPickmessage3 += StringUtils.padNumber(getUnitId(),4);

                               PIPickmessage3 += StringUtils.padNumber(getOperator(),8);

                               PIPickmessage3 += StringUtils.padNumber(pipicks.num_loc, 6);

                               PIPickmessage3 += StringUtils.padNumber(pipicks.item_num, 13);

                               PIPickmessage3 += StringUtils.padNumber(status, 1);
                          
                               
                              // PIPickmessage3 += StringUtils.padNumber(getResponse())
                                
                            //  PIPickmessage3 += setItemNum(item_num);

                           //  System.out.println(PIPickmessage3);
                            //  System.out.println(pipicks.num_loc);
                              // System.out.println (pipicks.item_num);

                               //if it is okay received the message "ACK"
                               boolean transactOK = transact(PIPickmessage3);
                               
                               if (transactOK) {
                            	   
                            	   String Response = getResponse();
                            	   
                            	   return returnCode(RC_OK);   
                               }		

                               errorBox("Error occured during sending message.");
                           
                       			}

                       		return true;
        		}

        
       public String getNumLoc(){
    	   return pipicks.num_loc;
       }
       
        public String getAlphaLoc (){
        	return pipicks.alpha_loc;
        }
        
        public String getDataFlag(){
            return pipicks.data_flag;
        }

        public String getItemNumber (){
        	return pipicks.item_num;
        }

        public void setItemNum(String in)
        {
            item_num = in;
        }

        private void errorBox(String string) {
               // TODO Auto-generated method stub
        }
        
        private void getLocationRequest (String response){
        	
        // 	StringUtils.log ("Server Response: ");
       
        		data_flag = StringUtils.getString(response , 3, 4);
        		
        	
        		if (data_flag.compareToIgnoreCase("Y")==0){
        			        
        		//	int len = 4;
        		//	while (response.length() >=len +12  ){
        				
        	//while (response.length() >= len && response.charAt(len) != ' '){
        //			
        		//	if (data_flag.charAt(4) != ' '){
        					 	//get response message from the server..
        					   ACK = StringUtils.getString(response, 0, 3); 
        					   
        					   data_flag = StringUtils.getString(response, 3, 4);

                               alpha_loc = StringUtils.getString(response, 4, 10);

                               num_loc = StringUtils.getString(response, 10, 16);

                               item_num = StringUtils.getString(response, 16, 29);
                               
                               //len++;
                               
                          //    System.out.println (ACK);
                          //    System.out.println (data_flag);
                          //     System.out.println (alpha_loc);
                          //   System.out.println (num_loc);
                          //     System.out.println (item_num);
                             // loc_count = loc_count +1;
        		
        				//}
        			}
        			
        		}  
        }       
//}
   