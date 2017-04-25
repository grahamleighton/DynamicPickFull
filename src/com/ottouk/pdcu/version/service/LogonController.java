package com.ottouk.pdcu.version.service;

import com.ottouk.pdcu.version.domain.Login;

/**
 * LogonController - Controller object that processes all events from the LoginUI.
 *  
 * @author dis114
 */

public class LogonController {
    
    /** instance variable for the main logon service object. */
    private VersionMgr vMgr;

    /**
     * Constructor.
     */
    public LogonController() {
        vMgr = new VersionMgr();
    }

    /**
     * OnLogin - Validates the user credentials that have been entered.
     * Called from the LoginUI upon button press and enter key.
     * 
     * @param userId - the user id entered
     * @return Login - the user credentials domain object
     * 
     * @see Login LoginUI
     */
    public final Login onLogin(final String userId) {
         
        Login domainObj = new Login();                  // new user credential object
        
                                                        // exit if quick code entered
        if (vMgr.isExitCode(userId)) {
            domainObj.setExitStatus(true);
            return domainObj;
        }
        
        // Validate logon id & ensure latest version of application is available
        
        if (vMgr.isLogonIdValid(userId)) { 
            domainObj.setErrorMsg(vMgr.getErrMsg());
            domainObj.setUserId(userId);
            domainObj.setValidCredential(true);         // Pass login validation 
            
            if (!vMgr.checkAppVersionsOK()) {           // Get Up To date version of PDCU application
                domainObj.setErrorMsg(vMgr.getErrMsg());
                domainObj.setValidCredential(false);    // Fail validation if problem updating application.         
            }
        } else {
            System.out.println("failed");
            domainObj.setValidCredential(false);        // Fail login validation
            domainObj.setErrorMsg(vMgr.getErrMsg());
        }
        domainObj.setErrorMsg(vMgr.getErrMsg());
        return domainObj;
    }

    /**
     * doSuccess - Execute the main PDCU application upon succesfull validation of the logon id.
     * 
     * @return Login - the user credentials domain object.
     */
    public final Login doSuccess() {
        Login domainObj = new Login();
        if (vMgr.invokeApp()) {                 // Inovke teh main application
            domainObj.setExitStatus(true);
            domainObj.setErrorMsg(" ");
        } else {
           domainObj.setExitStatus(false);      // return error to LoginUi if there is a problem.
           domainObj.setErrorMsg(vMgr.getErrMsg());
        }
                
        return domainObj;
    }
}
