package com.ottouk.pdcu.main.service;

import java.util.List;

public interface ItemPutAwayService {

	int ITEM_NOT_VALID = 1;
	int DUPLICATE_ITEM = 2;
	int LOCATION_LENGTH = 8;
	int LOCATION_MISMATCH_ERROR = 4;
	int LOCATION_NOT_VALID = 5;
	int LOCATION_NOT_FOUND = 6;
	
	
	void buildSendItemPutaway(String text);
	void buildSendToteTopUp(String item , String location, int ScanLength);
	void buildSendMultiItemToteTopUp(List itemlist , String location);
	void buildSendToteTopUpRequest(String location,String RequestType);
	String getServerResponse();
	

}
