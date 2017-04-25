package com.ottouk.pdcu.version.service;

import com.ottouk.pdcu.version.dao.LogonVersionDAOImpl;
import com.ottouk.pdcu.version.dao.StringUtils;
import com.ottouk.pdcu.version.dao.VersionDAO;
import com.ottouk.pdcu.version.dao.VersionDAOImpl;
import com.ottouk.pdcu.version.domain.Version;

import java.io.IOException;

/**
 * VersionMgr
 * 
 * Service object that executes the logon process and controls which versions of the
 * bootstrap application (HHT.ini/GUN_LOGONNAME) and the main PDCU application (HHT.ini/GUN_APPNAME)
 * exist and run on the client. 
 * 
 * @author dis114
 * @since 13/10/2008
 *
 */
public class VersionMgr {

	/** Error Message to be returned and displayed in the client. **/
	private String errMsg;
	/** The user id that has been entered in the logon screen. **/
	private String operatorId;
	/** The unique id of teh HHT gun being used. */
	private String unitId;
	/** Data Access Object responsible for accessing the version files and the latest 
	 *  versions of the software. */
	private VersionDAO vDAO;

	/**
	 * isLogonIdValid() - Ensures the PDCU user id is 8 characters long and only
	 *                    contains digits.
	 * 
	 * @param text - The logon id captured by the logon screen.
	 * @return true - valid user id entered, false if invalis id.
	 */
	public final boolean isLogonIdValid(final String text) {

		if (text.length() != 8) {
			System.out.println("inValid " + text);
			setErrMsg("Id must contain 8 digits");
			return false;
		} else {
			if (isStringAllDigits(text)) {
				if ( text.substring(0,2).equalsIgnoreCase("00"))
				{
					System.out.println("Valid " + text);
					setErrMsg("");
					operatorId = text;
					return true;
				} else {
					System.out.println("inValid " + text);
					setErrMsg("Id must start with 00");
					return false;
				}
			} else {
				System.out.println("inValid " + text);
				setErrMsg("Id must only contain numerics");
				return false;
			}
		}
	}

	/**
	 * Checks if a supplied string contains only digits. A character is considered
	 * to be a digit if it is not in the range '\u2000' <= ch <= '\u2FFF' and its
	 * Unicode name contains the word "DIGIT".
	 *
	 * @param checkString The String to be checked
	 * @return true if the String contains only digits, false otherwise
	 */
	private static boolean isStringAllDigits(final String checkString) {
		for (int i = 0; i < checkString.length(); i++) {
			boolean check = Character.isDigit(checkString.charAt(i));

			if (!check) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checkAppVersionsOK() - Method to ensure that the most up to date version of the
	 *                        PDCU Application (GUN_APPNAME) exists on the client.
	 * 
	 * @return true  - Log On Application up to date.
	 *         false - Log On application is not up to date.
	 */
	public final boolean checkAppVersionsOK() {

		System.out.println("CheckAppVersions...");

		//TODO Version DAO Factory - create main app DAO or Logon DAO
		vDAO = new VersionDAOImpl();

		setErrMsg("");
		unitId = vDAO.getGunId(); // Get the gun ID from the hht.ini file
		if (unitId.equals("IOERROR")) {
			setErrMsg("Could not find local hht.ini file.");
			return false;
		}

		//System.out.println(unitId);
		Version latestV;
		// Get Latest Version Numbers for this client from the ftp server.
		latestV = vDAO.getLatestAppVersion(unitId);
		if (latestV == null) {
			setErrMsg("Warning - Problem accessing server Version File - May be running an old version");
			return true;
		}

		// Get Current Version Numbers for this client.
		Version currentV = vDAO.getAppVersion();
		if (currentV == null) {
			setErrMsg("Warning - Problem accessing local Version File - May be running an old version");
			currentV = new Version();
			currentV.setAppV(0);
		}

		// Compare versions

		if (latestV.getAppV() != currentV.getAppV()) {
			// Down load latest version of the application from the ftp server.

			if (!vDAO.getUpToDateVersion(latestV)) {
				setErrMsg("Warning - Problem downloading latest Version - Running an old version");
				return true;
			}
			// Update local version file with downloaded file.
			if (!vDAO.updateVersionFile(latestV, unitId)) {
				setErrMsg("Warning - Failed to update version file.");
				return true;
			}
		}
		return true;
	}

	/**
	 * invokeApp()  - Method to ensure the latest version of the PDCU application is
	 *                present prior to invoking the application.
	 * 
	 * @return boolean - true if invocation of application succeeded, false otherwise.
	 */

	public final boolean invokeApp() {

		Version latestV = vDAO.getAppVersion();

		try {

			//  System.runFinalization();
			String params = " " + vDAO.getGunId() + " " + this.getOperatorId()
					+ " " + vDAO.getGUNRESOURCEDIR() + vDAO.getVERSIONFILE();
			System.out.println("InvokeApp..." + vDAO.getGUNAPPNAME() + params);

			// Ensure an application exists on the gun.
			if (!vDAO.appExists(vDAO.getGUNAPPNAME())) {

				if (latestV == null) {
					setErrMsg("Error - Failed To Invoke main Application: Problem reading version file");
					return false;
				}
				if (!vDAO.getUpToDateVersion(latestV)) {

					setErrMsg("Error - Failed To Invoke main Application:");

					return false;
				}

			}
			// Grab the Boot applications log file and place it on the ftp server
			if (latestV.isDebugFlag()) {
				vDAO.grabLogFile("Boot");
			}
			String run = null;
			if (vDAO.getGUNAPPNAME() instanceof String) {

				run = PDCUConstants.RUN_JAVA + vDAO.getGUNAPPNAME() + params;
			} else { // otherwise run PDCU as default

				run = PDCUConstants.RUN_JAVA + PDCUConstants.GUN_DIR
						+ "PDCUMain.jar" + params;

			}
			System.out.println("invokeApp() " + run);
			Runtime.getRuntime().exec(run);

			return true;

		} catch (IOException e) {
			setErrMsg("Error - Failed To Invoke main Application");
			System.out.println("Failed to invoke main application");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			setErrMsg("Error - Failed To Invoke main Application");
			System.out.println("Failed to invoke main application 2");
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * isExitCode() - Method to check for a secret 'exit' code in a given string.
	 * 
	 * @param  text - the string that is to be checked..
	 * 
	 * @return true  - if the secret exit key has been entered.
	 *         false - if the secret 'exit' code has not been entered.
	 */
	public final boolean isExitCode(final String text) {
		return (text.equals("11exit00"));
	}

	/**
	 * Getter for the error message that will be returned to the client.
	 * 
	 * @return errorMsg.
	 */
	public final String getErrMsg() {
		return errMsg;
	}

	/**
	 * Sets the error message that is subsequently returned to the client.
	 * 
	 * @param myErrMsg - the error message.
	 */
	public final void setErrMsg(final String myErrMsg) {
		this.errMsg = myErrMsg;
	}

	/**
	 * checkLogonVersionsOK() - Method to ensure that the most up to date version of the
	 *                          PDCU Logon Application (GUN_LOGONNAME) exists on teh client.
	 * 
	 * @return true  - Log On Application up to date.
	 *         false - Log On application is not up to date.
	 */
	public final boolean checkLogonVersionsOK() {

		System.out.println("CheckLogonVersions...");

		//TODO Version DAO Factory - create main app DAO or Logon DAO
		vDAO = new LogonVersionDAOImpl();
		setErrMsg("");
		unitId = vDAO.getGunId(); // Obtain Gun Id from hht.ini
		if (unitId.equals("IOERROR")) {
			setErrMsg("Could not find local hht.ini file.");
			return false;
		}

		//System.out.println(unitId);

		Version latestV;
		// Get Latest Version Numbers for this gun from version.txt file on the ftp server.
		latestV = vDAO.getLatestAppVersion(unitId);
		if (latestV == null) {
			setErrMsg("Warning - Problem accessing server Version File - May be running an old version");
			return true;
		}

		// Get Current Version Numbers for the application currently running on the client.
		Version currentV = vDAO.getAppVersion();
		if (currentV == null) {
			setErrMsg("Warning - Problem accessing local Version File - May be running an old version");
			// force a download
			currentV = new Version();
			currentV.setAppV(0);
		}

		// Compare versions

		if (latestV.getAppV() != currentV.getAppV()) { // If versions differ

			// Down load latest version of the application from the ftp server.
			if (!vDAO.getUpToDateVersion(latestV)) {
				setErrMsg("Warning - Problem downloading latest Version - Running an old version");
				return true;
			}
			// Update local version file with downloaded version file.
			if (!vDAO.updateVersionFile(latestV, unitId)) {
				setErrMsg("Warning - Failed to update version file.");
				return true;
			}
		}
		return true;
	}

	/**
	 * invokeLogon() - Method to ensure the latest version of the PDCU Logon application is
	 *                 present prior to invoking the PDCU Logon application.
	 * 
	 * @return boolean - true if log on succeeded, false otherwise.
	 */
	public final boolean invokeLogon() {

		Version latestV = vDAO.getAppVersion(); // Get current version details

		try {

			if (!vDAO.appExists(vDAO.getGUNAPPNAME())) { // Ensure logon app exists

				if (latestV == null) {
					setErrMsg("Error - Failed To Invoke main Application: Problem reading version file");
					return false;
				}
				// Check and download an up to date logon application if necessary
				if (!vDAO.getUpToDateVersion(latestV)) {

					setErrMsg("Error - Failed To Invoke main Application:");
					return false;
				}
			}
			// Grab the Boot applications log file and place it on the ftp server
			if (latestV.isDebugFlag()) {
				vDAO.grabLogFile("Main");
			}
			// Assume an up to date application exists.

			String run = null;
			if (vDAO.getGUNAPPNAME() instanceof String) {
				//  Create the run command.
				run = PDCUConstants.RUN_JAVA + vDAO.getGUNAPPNAME();
			} else { // otherwise run PDCU as default
				run = PDCUConstants.RUN_JAVA + PDCUConstants.GUN_DIR
						+ "PDCUBoot.jar";
			}
			
			// Temp hack to prevent PDCUBoot from overwriting jscpout.txt.  Remember to remove!
			run = StringUtils.replace(run, " -Of", "");
			
			System.out.println("invokeLogon() " + run);
			Runtime.getRuntime().exec(run); // Run the logon application.

			return true;
		} catch (IOException e) {
			System.out.println("Failed to invoke main application");
			setErrMsg("Error - Failed To Invoke main Application:");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * OperatorId Getter.
	 * 
	 * @return String - the operatorId.
	 */
	public final String getOperatorId() {
		return operatorId;
	}

	/**
	 * OperatorId setter.
	 * 
	 * @param myOperatorId - the id to be set.
	 */
	public final void setOperatorId(final String myOperatorId) {
		this.operatorId = myOperatorId;
	}

}
