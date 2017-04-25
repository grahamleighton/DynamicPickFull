package com.ottouk.pdcu.main.domain;

import com.ottouk.pdcu.main.utils.StringUtils;

public class StockAudit extends Header {

	private String status;
	
private String location;

	
private String response;

	
	private String found;

	
	private String alphaLocation[] = new String[12];

	
	private Integer numericLocation[] = new Integer[12];

        private int loc_count;
	private String locationFollows;
	
	
	public void setStatus(String status) {
		this.status = status;
	}
	
       public String buildLocationRequest() {
		
		String req = buildHeader();
		//req += StringUtils.padNumber(getMid6Location(), 6);
		req += StringUtils.padNumber(status, 1);
		
		return req;
	}

	
	public String getMid6Location(String ScanLocation) {
		return StringUtils.getString(ScanLocation, 1, 7);
			}
	
	public void receiveLocationRequest(String response) {
            this.response = StringUtils.getString(response, 0, 3);
            found = StringUtils.getString(response, 3,4);

            loc_count = 0;
            if (found.compareTo("Y") == 0)
            {
                int len = 4 ;
                while (response.length() >= len + 12 && response.charAt(len) != ' ')
                {
		    if (StringUtils.getInteger(response, len, len + 6 ).intValue() == 0) break;
                    numericLocation[loc_count] = StringUtils.getInteger(response, len, len + 6 );
                    //String.valueOf(numericLocation[loc_count]).replace("\\B", " ");

                    System.out.println(numericLocation[loc_count]);
                    len += 6;
                    // System.out.println(numericLocation);

                        //alphaLocation [loc_count] = StringUtils.getString(string, beginIndex, endIndex)
                    alphaLocation[loc_count] = StringUtils.getString(response, len, len + 6);
                    len += 6;

                    System.out.println(alphaLocation [loc_count]);
                    //System.out.println(alphaLocation);

                    len++ ; //for ignoring the PI type for the server response...

                    loc_count = loc_count + 1;
                }
            }
	}
	
	public String getFound() {
		return found;
	}

	public String getLocationFollows() {
		return locationFollows;
	}

        public int getLocationCount()
        {
            return loc_count;
        }

	public String getAlphaLocation(int index) {
		//System.out.println(alphaLocation [index]);
		return alphaLocation[index];
		
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public Integer getNumericLocation(int index) {
		//System.out.println(numericLocation[index]);
		return numericLocation[index];
		
	}
		

}

