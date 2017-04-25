package com.ottouk.pdcu.version.dao;

import com.ottouk.pdcu.version.dao.StringUtils;
import com.ottouk.pdcu.version.domain.Version;
import com.ottouk.pdcu.version.service.PDCUConstants;
import com.ottouk.pdcu.version.service.PDCUftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * VersionDAOImpl - Data Access Object responsible for identifying the required version of an applications
 *                  and performing the version download.
 * @author dis114
 *
 */
public class VersionDAOImpl implements VersionDAO {
    
            /** The client id, requesting the version check. */
    private static String gunId = null;
            /** Pointer to the resource directory on the client.*/
    private static String gunResourceDir = null;
            /** The name of the file that holds the required versioning information. */
    private static String versionFile = null;
            /** The name of the Application that requires a version check. */
    private static String gunAppName = null;
            /** The source directory on the version server where the versions of the software reside. */ 
    private static String ftpDirectory = null;    
            /** The debug flag, that drives if teh log file is to be pulled to teh ftp server */
    private static boolean debugFlag = false;
            /** The name of the file that will be written on the client. */
    private String destFname;
            /** Instance variable for FTP service. */
            private PDCUftp ftp;

/**
 * Constructor - Open the hht.ini file and populate the necessary instance variables with 
 * the relevant properties.
 */
    public VersionDAOImpl() {
        // Open HHT.ini file and obtain gun number
        ClassLoader cl = VersionDAOImpl.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(PDCUConstants.HHTFILE);
        Properties props = new Properties();
        try {
            if (is != null) {
                    props.load(is);
            } else {
                props.load(new FileInputStream(PDCUConstants.GUN_RESOURCEDIR + PDCUConstants.HHTFILE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        /*
         * Obtain the unit id from the operating system.
         * If not available then get unit Id from properties file.
         */
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            
            Integer unitId = StringUtils.extractNumbersFromString(hostName);  
            if ((unitId == null) || (unitId.toString().length() > 4)) {
                // Unable to determine unitId from hostName
                gunId = props.getProperty("ID");    
            } else {
                gunId = StringUtils.padNumber(unitId, 4); 
            }
        }  catch (UnknownHostException uhe) {         
            gunId = props.getProperty("ID");
        }
      
       
        /*
         * Obtain the global parameters from the hht.ini file.
         */
        gunResourceDir = props.getProperty("GUN_INIFILEDIR");
        versionFile =   props.getProperty("VERSION_FILE");
        gunAppName = props.getProperty("GUN_APPNAME").trim();    
        ftpDirectory = props.getProperty("FTP_DIRECTORY");
       
        ftp = new PDCUftp(props.getProperty("FTP_SERVER"), props.getProperty("FTP_USER"), 
                          props.getProperty("FTP_PASS"));
    }
    /**
     * gunId Getter.
     * @return String - the current gunId.
     */
    public final String getGunId() {
        return gunId;
    }
    /**
     * No Parameter Current Version details Getter.
     * @return Version - domain object containing the details contained within
     *                   the current version.txt file. Null is returned if problem accessing
     *                   the version.txt file. 
     *          
     *                 
     * @see Version
     */
    public final Version getAppVersion() {
        
        return getAppVersion(gunResourceDir + versionFile);
    }    

    /**
     * Parameterised Current Version details Getter.
     * 
     * @param  fileName - The location of the current version.txt file.
     * @return Version  - domain object containing the details contained within
     *                    the required version.txt file.
     *                 
     * @see Version
     */
    public final Version getAppVersion(final String fileName) {
        
        // Open version file and obtain app name and version
        
        System.out.println("App Version: " + fileName);
        ClassLoader cl = VersionDAOImpl.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        Properties props = new Properties();
        try {
            if (is != null) {
                    props.load(is);
                    is.close();
            } else {
                FileInputStream input = new FileInputStream(fileName);
                props.load(input);
                input.close();
            }
        } catch (FileNotFoundException  e) { 
            e.printStackTrace();
            
            // if version.txt not exists then get latest version.txt from server .
            if (!ftp.getDataFile(ftpDirectory, versionFile, gunResourceDir + versionFile))  {
                return null;    
            }
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } 
 
        // Populate the domain object.
        
        Version v = new Version();
        v.setAppName(props.getProperty("MAINAPPNAME"));
        v.setServer(props.getProperty("SERVER"));
        v.setBasePort(props.getProperty("BASEPORT"));
        v.setChannels(props.getProperty("CHANNELS"));  
        String dbg = props.getProperty("DEBUG");
        
        if (dbg == null) {
            v.setDebugFlag(false);  
        } else {
            v.setDebugFlag(dbg.equalsIgnoreCase("TRUE") ? true : false);
        }
        System.out.println("Debug: " + v.isDebugFlag());
        try {
            float f = Float.valueOf(props.getProperty("MAINAPP").trim()).floatValue();
            v.setAppV(f);
         } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
            return null;
         } catch (Exception e) {
             System.out.println("Problem reading version file");
             return null;
         }

        return v;
    }


    /**
     * getLatestAppVersion - Method responsible for obtaining the latest version details of the application
     *                       from the ftp server.
     *                       
     * @param unitId - The id of the current client/scanner/gun.
     * @return Version - domain object containing the details contained within
     *                   the required version.txt file. Return null if fail.
     */
    public final Version getLatestAppVersion(final String unitId) {
        
        String ftpFile = null;

       // Get Latest version file from ftp server

       ftpFile = buildDestination(unitId);  // Build specific version_xxx.txt file name.       
       System.out.println("FTP FILE= " + ftpFile);
       // Look for specific version file for this gun first e.g. version_999.txt  
       if (!ftp.getDataFile(ftpDirectory, ftpFile, gunResourceDir + ftpFile)) {

                // if specific version file does not exist get the default version.txt file
                // and create on the client as version_xxx.txt
                if (!ftp.getDataFile(ftpDirectory, versionFile, gunResourceDir + ftpFile)) {
                    return null;
                }
        }
               // Populate Version domain object for the downloaded version file. 
        return this.getAppVersion(gunResourceDir + ftpFile); 

    }

    /**
     * Helper method to build the specific version file name. Uses the client/gun id from the hht.ini file.
     * 
     * @param unitId  - The client/gun id obtained from the hht.ini file.
     * @return String - The full pathname of the specific version_xxx.txt file.
     */
    private String buildDestination(final String unitId) {

        StringTokenizer st = new StringTokenizer(versionFile , ".");
        this.destFname = st.nextToken() + "_" + unitId + "." + st.nextToken();
        
        return destFname;
    }

    /**
     * Downloads the latest version of the specified Application.
     * 
     * @param v - Version  the domain object containing the version details of the required application.
     * @return true - download succeeded, false - download failed.
     * 
     * @see PDCUftp Version
     */
    public final boolean getUpToDateVersion(final Version v) {

            System.out.println("DEST FILE= " + gunAppName);
            
            // Get default version.txt file
            return (ftp.getDataFile(ftpDirectory, v.getAppName(), gunAppName));
          
    }
    
    /**
     * Updates the current version.txt  file with the latest application details in the version_xxx.txt details.
     * 
     * @param v - Version domain object containing the latest version details from teh ftp server.
     * @param unitId - The client id
     * 
     * @return true - current version.txt was successfully updated, otherwise false.
     * 
     */
    public final boolean updateVersionFile(final Version v, final String unitId) {
        
        // Only Update the MAIN APP version in the version file
        
        // Load the existing version file 
        String fileName = gunResourceDir + versionFile;
        System.out.println("Loading: " + fileName);
        ClassLoader cl = VersionDAOImpl.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        Properties props = new Properties();
        try {
            if (is != null) {
                    props.load(is);
                    is.close();
            } else {
                FileInputStream input = new FileInputStream(fileName);
                props.load(input);
                input.close();
            }
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } 

        // Update the MAINAPP property
        props.setProperty("MAINAPP", Float.toString(v.getAppV()));
        props.setProperty("MAINAPPNAME", v.getAppName());
        props.setProperty("DEBUG", v.isDebugFlag()+"");

        // Store property file with updated version number.
        try {
            props.store(new FileOutputStream(gunResourceDir + versionFile), null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String downloadedPath = gunResourceDir + buildDestination(unitId);
        File file2 = new File(downloadedPath);
        file2.delete();
        return true;
    }
    /**
     * Getter from GunId.
     * 
     * @return gunId.
     */
    public static String getGUNID() {
        return gunId;
    }
    /** Setter for GunId.
     * 
     * @param myGunid - the current Gun Id of the client.
     */
    public static void setGUNID(final String myGunid) {
        gunId = myGunid;
    }
    /** 
     * Getter for GunResourceDir.
     * 
     * @return String - The full pathname on the client where the resource files reside.
     */
    public final String getGUNRESOURCEDIR() {
        return gunResourceDir;
    }
    /**
     * Setter for gunResourceDir.
     * 
     * @param myGunResourcedir - The full pathname on the client where the resource files reside.
     */
    public static void setGUNRESOURCEDIR(final String myGunResourcedir) {
        gunResourceDir = myGunResourcedir;
    }
    
    /**
     * Getter for versionFile.
     * 
     * @return String the version file name.
     */
    public final String getVERSIONFILE() {
        return versionFile;
    }
    /**
     * Setter for versionFile.
     * 
     * @param myVersionFile - the name of the version file.
     */
    public static void setVERSIONFILE(final String myVersionFile) {
        versionFile = myVersionFile;
    }
    /**
     * Getter for the clients application name.
     * 
     * @return String - The main application name contained within the version.txt file.
     */
    public final String getGUNAPPNAME() {
        return gunAppName;
    }

    /**
     * Setter for the required application name.
     * 
     * @param myGunAppname - The main application name contained within the version.txt file.
     */
    public static void setGUNAPPNAME(final String myGunAppname) {
        gunAppName = myGunAppname;
    }
    /**
     * Getter for the destination of the application being updated.
     * 
     * @return String - the full pathname of the required destination file name. 
     */
    public final String getDestFname() {
        return destFname;
    }
    /**
     * Setter for the destination of the application being updated.
     * 
     * @param myDestFname - the full pathname of the required destination file name. 
     */
    public final void setDestFname(final String myDestFname) {
        this.destFname = myDestFname;
    }

    /**
     * Helper method that checks if the application file already exists on the client.
     * 
     * @param application - Full pathname of the application.
     * 
     * @return - true if application found, false otherwise.
     */
    public final boolean appExists(final String application) {
        File checkFile = new File(application);
        
        return checkFile.exists();
    }
    
    /**
     * grabLogFile - Uploads the clients log file to the ftp server.
     */
    public final void grabLogFile(final String src) { 
    	System.out.println(" TS = : ");
    	System.out.println(GetTimeStamp());
    	
        if (!ftp.putDataFile(ftpDirectory, "jscpout.txt", src+ "-UNIT" + getGUNID() + "_" + GetTimeStamp() + ".log")) {
            System.out.println("Failed to grab Log File - jscpout.txt to " + src + "-UNIT" + getGUNID() + ".log");
        }
    }
    /**
     * GetTimeStamp
     * Returns a time stamp of dmyhn
     * @return String of timestamp
     */
  	public String GetTimeStamp () {
		Calendar cal = new GregorianCalendar();
		String m;
	    
	    // Get the components of the date
	    int year = cal.get(Calendar.YEAR);             
	    int month = cal.get(Calendar.MONTH);          
	    int day = cal.get(Calendar.DAY_OF_MONTH);      
	    
	    int hr = cal.get(Calendar.HOUR_OF_DAY);
	    int min = cal.get(Calendar.MINUTE );  
	    int sec = cal.get(Calendar.SECOND );
	    m = Integer.toString(day) + Integer.toString(month+1) + Integer.toString(year) + Integer.toString(hr) + Integer.toString(min) + Integer.toString(sec);
	   
	    return m;
	}
   
}


