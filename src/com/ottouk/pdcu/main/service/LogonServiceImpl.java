package com.ottouk.pdcu.main.service;

import com.ottouk.pdcu.main.dao.CommsImpl;
import com.ottouk.pdcu.main.dao.VersionFile;
import com.ottouk.pdcu.main.domain.Logon;
import com.ottouk.pdcu.main.domain.Version;
import com.ottouk.pdcu.main.utils.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LogonServiceImpl extends GeneralServiceImpl implements LogonService {
	
	private Version version;
	
	private Logon logon;
	private Logon activity;
	private Logon logoff;

	
	public boolean validateOperator(String operator) {
		
		if (operator.length() != OPERATOR_LENGTH) {
			return returnCode(RC_ERROR_OPERATOR_NUMBER);
		}
		
		for (int i = 0; i < operator.length(); i++) {
			if (!Character.isDigit(operator.charAt(i))) {
				return returnCode(RC_ERROR_OPERATOR_NUMBER);
			}
		}
				
		return returnCode(RC_OK);
	}
	
	public boolean logon(String operator, String versionFile) {
		
		Integer unitId = StringUtils.extractNumbersFromString(getHostName());
		if ((unitId == null) || (unitId.toString().length() > 4)) {
			// Unable to determine unitId from hostName
			setUnitId(unitId);	// this field required by error message
			return returnCode(RC_ERROR_UNIT_ID);
		} else {
			return logon(unitId, operator, versionFile);
		}
	}
	
	public boolean logon(Integer unitId, String operator, String versionFile) {
		
		GeneralServiceImpl.versionFile = versionFile;
		
		version = new VersionFile(versionFile).getVersion();
		if (version == null) {
			// Missing or invalid version file
			return returnCode(RC_ERROR_VERSION_FILE);
		} else {
			logon = createLogon(unitId, operator);
			
			// Add MainApp name and version to Logon record filler (logging aid)
			logon.setFiller(StringUtils.split(version.getMainAppName(), ".jar") 
					+ " " + LogonService.SOFTWARE_VERSION_NUMBER);
			
			return logon();
		}
	}

	public boolean logon(Integer unitId, String operator, String server,
			int basePort, int channels) {
		
		version = new Version(channels, basePort, server);
		logon = createLogon(unitId, operator);
		
		return logon();
	}
	
	private boolean logon() {
		
		setUnitId(logon.getUnitId());
		setOperator(logon.getOperator());

		if (!connect(logon.getUnitId(), version.getServer(), version.getBasePort(), version.getChannels())) {
			// Connection error
			return returnCode(RC_ERROR_CONNECT);
		} else {
			boolean transactOK = transact(logon.buildLogon());
			if (transactOK) {
				logon.receiveResponse(getResponse());
			}
			return transactOK;	// returnCode set by transact() method
		}
	}
		
	public boolean logoff() {
		return logoff(unitId, operator);
	}
	
	public boolean logoff(Integer unitId, String operator) {

		logoff = createLogoff(unitId, operator);
		
		boolean transactOK = transact(logoff.buildLogon());
		if (transactOK) {
			logoff.receiveResponse(getResponse());
			if (!disconnect()) {
				// Disconnection error
				return returnCode(RC_ERROR_DISCONNECT);
			} 
		}
		
		return transactOK;	// returnCode set by transact() method
	}
	
	private Logon createLogon(Integer unitId, String operator) {
		return createLogon(unitId, operator, LOGON);
	}
	
	private Logon createLogoff(Integer unitId, String operator) {
		return createLogon(unitId, operator, LOGOFF);
	}
	
	private Logon createLogon(Integer unitId, String operator, String action) {

		Logon logon = new Logon();
		logon.setMessageId(LOGON_MESSAGE_ID);
		logon.setUnitId(unitId);
		logon.setOperator(operator);
		logon.setAction(action);
		logon.setVersion(LOGON_VERSION);

		return logon;
	}
	
	private boolean connect(Integer unitId, String server, int basePort,
			int channels) {
		
		if (comms != null) {
			// Already connected
			return true;
		} else {
			// Connect
			comms = new CommsImpl();
			return comms.connect(server, basePort, channels, unitId);
		}
	}
	
	private boolean disconnect() {
		
		if (comms == null) {
			return false;
		} else {
			// Disconnect
			return comms.disconnect();
		}
	}

	
	public boolean startPickingWalk() {
		return performActivity(PICKING_WALK, START);
	}
	
	public boolean stopPickingWalk() {
		return performActivity(PICKING_WALK, STOP);
	}
	
	public boolean startToteAudit() {
		return performActivity(TOTE_AUDIT, START);
	}
	
	public boolean stopToteAudit() {
		return performActivity(TOTE_AUDIT, STOP);
	}
	
	public boolean startToteBuild() {
		return performActivity(TOTE_BUILD, START);
	}
	
	public boolean stopToteBuild() {
		return performActivity(TOTE_BUILD, STOP);
	}
	
	public boolean startToteConsol() {
		return performActivity(TOTE_CONSOL, START);
	}
	
	public boolean stopToteConsol() {
		return performActivity(TOTE_CONSOL, STOP);
	}
	
	public boolean startTotePutaway() {
		return performActivity(TOTE_PUTAWAY, START);
	}
	
	public boolean stopTotePutaway() {
		return performActivity(TOTE_PUTAWAY, STOP);
	}
	
	public boolean startItemPutaway() {		
		return performActivity(ITEM_PUTAWAY, START);
	}
	
	public boolean stopItemPutaway() {
		return performActivity(ITEM_PUTAWAY, STOP);
	}
	
	public boolean startCartonPutaway() {
		return performActivity(CARTON_PUTAWAY, START);
	}
	
	public boolean stopCartonPutaway() {
		return performActivity(CARTON_PUTAWAY, STOP);
	}
	public boolean startStockAudit (){
		return performActivity (STOCK_AUDIT, START);
		
	}
	
	public boolean stopStockAudit (){
		return performActivity (STOCK_AUDIT, STOP);
	}
	
	public boolean startPIPicks(){
		return performActivity (PI_PICKS, START);
		
	} 
	
	public boolean stopPIPicks (){
		return performActivity (PI_PICKS, STOP);
	}
	
	private boolean performActivity(String activityCode, String action) {
		
		activity = createActivity(activityCode, action);
		
		boolean transactOK = transact(activity.buildActivity());
		if (transactOK) {
			activity.receiveResponse(getResponse());
		}
		
		return transactOK;	// returnCode set by transact() method
	}
	
	private Logon createActivity(String activityCode, String action) {
		
		Logon activity = new Logon();
		activity.setMessageId(ACTIVITY_MESSAGE_ID);
		activity.setUnitId(unitId);
		activity.setOperator(operator);
		activity.setAction(action);
		activity.setAisle(ACTIVITY_AISLE);
		activity.setActivityCode(activityCode);
		
		return activity;
	}
	
	private String getHostName() {

		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException uhe) {
			return null;
		}
	}


}
