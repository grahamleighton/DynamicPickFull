package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.dao.Comms;
import com.ottouk.pdcu.version.service.PDCUConstants;

public class GeneralServiceImpl implements GeneralService {
	
	protected static Comms comms;
	protected static Integer unitId;
	protected static String operator;
	protected static String versionFile;
	
	protected int returnCode;
	
	
	public boolean transact(String message) {
		if (comms == null) {
			return returnCode(RC_COMMS_NOT_CONNECTED);
		} else if (!comms.transact(message)) {
			return returnCode(RC_COMMS_TRANSACTION_ERROR);
		} else if (!comms.responseStartsWith("ACK")) {
			return returnCode(RC_COMMS_RESPONSE_ERROR);
		} else if (!(comms.getErrorMessage() == "")) {
			return returnCode(RC_COMMS_OTHER_ERROR);
		} else {
			return returnCode(RC_OK);
		}
	}
	
	public boolean returnCode(int returnCode) {
		
		this.returnCode = returnCode;
		
		return (returnCode == RC_OK);
	}
	
	public String getResponse() {
		return comms.getResponse();
	}
	
	public int getReturnCode() {
		return returnCode;
	}


	public Comms getComms() {
		return comms;
	}

	public void setComms(Comms comms) {
		GeneralServiceImpl.comms = comms;
	}

	public Integer getUnitId() {
	
		
		return unitId;
	}

	public void setUnitId(Integer unitId) {
//		int mid = 120;
//	int tmpid = 4;
//		int tmid = 150;
//		if (PDCUConstants.OS_WIN_XP) {
			
//			if (unitId.compareTo(Integer.valueOf(mid)) > 0) {
//				unitId = Integer.valueOf(tmpid);
//			}
//			if (unitId.compareTo(Integer.valueOf(tmid)) > 0) {
//			unitId = Integer.valueOf(tmpid);
//			}
//      }
		GeneralServiceImpl.unitId = unitId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		GeneralServiceImpl.operator = operator;
	}
	
	public String getVersionFile() {
		return versionFile;
	}

	public void setVersionFile(String versionFile) {
		GeneralServiceImpl.versionFile = versionFile;
	}

}
