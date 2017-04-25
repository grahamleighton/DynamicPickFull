package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.domain.OpCounts;
import com.ottouk.pdcu.main.utils.StringUtils;

public class OpCountsServiceImpl extends GeneralServiceImpl implements OpCountsService {
	
	OpCounts opCounts;
	

	public boolean requestOpCounts() {
		
		opCounts = new OpCounts();
		opCounts.setMessageId(OP_COUNTS_MESSAGE_ID);
		opCounts.setUnitId(unitId);
		opCounts.setOperator(operator);
		
		boolean transactOK = transact(opCounts.buildOpCountsRequest());
		if (transactOK) {
			opCounts.receiveOpCounts(getResponse());
			
			/*if (!opCounts.getOk().equalsIgnoreCase("Y")) {
				return returnCode(RC_ERROR_NO_OP_COUNTS);
			}*/			
		}

		return transactOK;	// returnCode set by transact() method
	}
	
	public String getBuild() {
		return formatCount(opCounts.getBuild());
	}

	public String getBuilt() {
		return formatCount(opCounts.getBuilt());
	}

	public String getConsolItems() {
		return formatCount(opCounts.getConsolItems());
	}

	public String getConsolLocations() {
		return formatCount(opCounts.getConsolLocations());
	}

	public String getPIItems() {
		return formatCount(opCounts.getPiItems());
	}

	public String getPILocations() {
		return formatCount(opCounts.getPiLocations());
	}

	public String getPutaway() {
		return formatCount(opCounts.getPutaway());
	}

	public String getTotePutawayItems() {
		return formatCount(opCounts.getTotePutawayItems());
	}
	
	private String formatCount(Integer count) {
		return formatCount(count, OP_COUNTS_FIELD_LENGTH);
	}
	
	private String formatCount(Integer count, int requiredFieldLength) {
		return StringUtils.padNumber(count, requiredFieldLength);
	}

}
