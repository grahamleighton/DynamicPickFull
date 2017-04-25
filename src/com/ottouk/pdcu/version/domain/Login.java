package com.ottouk.pdcu.version.domain;
/**
 * Domain Object to hold the user login credentiuals.
 * 
 * @author dis114
 *
 */
public class Login {
        /** User id to be validated. */
    private String userId;
        /** Flag indicating if an application exit is required. */
    private boolean exitStatus;
        /** Placeholder for error messages produced by the validation routine. */
    private String errorMsg;
        /** Flag indicating if validation succeeded. */
    private boolean validCredential;
    
    /**
     * ValidCredential Getter.
     * @return true if login validation succeeds, false otherwise.
     */
    public final boolean isValidCredential() {
        return validCredential;
    }
    /**
     * ValidCredential Setter.
     * @param myValidCredential - true if OK, false if otherwise.
     */
    public final void setValidCredential(final boolean myValidCredential) {
        this.validCredential = myValidCredential;
    }
    /**
     * User Id getter.
     * @return String the user id.
     */
    
    public final String getUserId() {
        return userId;
    }
    
    /**
     * User Id Setter.
     * @param myUserId - the user id.
     */
    public final void setUserId(final String myUserId) {
        this.userId = myUserId;
    }
    
    /**
     * Exit Status getter.
     * @return true if application exit requested, false if otherwise.
     */
    public final boolean isExitStatus() {
        return exitStatus;
    }
    /**
     * Exit Status setter.
     * @param myExitStatus - true if application exit requested, false if otherwise.
     */
    public final void setExitStatus(final boolean myExitStatus) {
        this.exitStatus = myExitStatus;
    }
    
    /**
     * Error Message getter.
     * @return String - The Error message.
     */
    public final String getErrorMsg() {
        return errorMsg;
    }
    /**
     * Error Message setter.
     * @param myErrorMsg - The Error message.
     */
    public final void setErrorMsg(final String myErrorMsg) {
        this.errorMsg = myErrorMsg;
    }
}
