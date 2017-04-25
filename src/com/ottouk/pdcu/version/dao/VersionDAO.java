package com.ottouk.pdcu.version.dao;

import com.ottouk.pdcu.version.domain.Version;

/**
 * Data Access Interface that specifies the API to be used when utilising the version.txt files
 * to control the versioning of the MAINAPP application and the LOGON_APP application.
 * 
 * Sample version.txt file is below:-
 * 
 * LOGON=9.9                    // The required version of the logon application
 * LOGONAPPNAME=PDCUBoot.jar    // The name of the logon application.
 * MAINAPP=2.0                  // The required version of the main application
 * MAINAPPNAME=PDCUMain.jar     // The name of the main application.
 * SERVER=172.16.8.35           // The address of the server that the clients communicate with.
 * BASEPORT=4000                // Starting port number on the server for these applications
 * CHANNELS=3                   // Number of connections per port.
 * 
 * @author dis114
 * 
 */
public interface VersionDAO {

    /**
     * Helper method that checks if the application file already exists on the client.
     * 
     * @param application - Full pathname of the application.
     * 
     * @return - true if application found, false otherwise.
     */
    boolean appExists(String application);
    
    /**
     * 
     * Current Version details Getter. Accesses the current version.txt file and populates 
     * the version domain object. 
     * 
     * @return Version  - domain object containing the details contained within
     *                    the required version.txt file.
     *                 
     * @see Version
     */
    
     Version getAppVersion();

    /**
     * Parameterised Current Version details Getter.Accesses the current version.txt file and populates 
     * the version domain object.  
     * 
     * @param  fName    - The location of the current version.txt file.
     * @return Version  - domain object containing the details contained within
     *                    the required version.txt file.
     *                 
     * @see Version
     */
     
     Version getAppVersion(String fName);
    
     /**
      * Getter for the clients application name.
      * 
      * @return String - The main application name contained within the version.txt file.
      */
     
    String getGUNAPPNAME();
    
    /** 
     * Getter for GunResourceDir.
     * 
     * @return String - The full pathname on the client where the resource files reside.
     */
    
    String getGUNRESOURCEDIR();
    
    /**
     * Getter from GunId.
     * 
     * @return gunId.
     */
    
    String getGunId();
    
    /**
     * getLatestAppVersion - Method responsible for obtaining the latest version details of the application
     *                       from the ftp server.
     *                       
     * @param unitId - The id of the current client/scanner/gun.
     * @return Version - domain object containing the details contained within
     *                   the required version.txt file. Return null if fail.
     */
    
    Version getLatestAppVersion(String unitId);
    
    /**
     * Downloads the latest version of the specified Application.
     * 
     * @param newV - Version  the domain object containing the version details of the required application.
     * @return true - download succeeded, false - download failed.
     * 
     * @see PDCUftp Version
     */
    
    boolean getUpToDateVersion(Version newV);
    /**
     * Getter for versionFile.
     * 
     * @return String the version file name.
     */
    
    String getVERSIONFILE();
    
    /**
     * Updates the current version.txt  file with the latest application details in the version_xxx.txt details.
     * 
     * @param version - Version domain object containing the latest version details from teh ftp server.
     * @param unitId  - The client id
     * 
     * @return true - current version.txt was successfully updated, otherwise false.
     * 
     */

    boolean updateVersionFile(Version version, String unitId);
    
    /**
     * Updates the the log file to the ftp server
     */
    void grabLogFile(String source);
}
