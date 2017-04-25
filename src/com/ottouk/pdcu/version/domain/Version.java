package com.ottouk.pdcu.version.domain;
/**
 * Domain Object to hold the version application data. The data is obtained from the versin.txt file.
 * 
 * @author dis114
 *
 */
public class Version {
                /** The version number of the application. */
    private float appV;
                /** The name of the application.*/
    private String appName;
                /** The address of the server that the application communicates with , i.e. Dec Alpha */
    private String server;
                /** The first port number that is available to the application. */
    private String basePort;
                /** The number of connections that are allowed per port. */
    private String channels;
                /** The debug flag, that drives if the log file is to be pulled to the ftp server */
    private boolean debugFlag;
    
    /**
     * Debug Flag getter
     * @return true if flag is set
     */
    public boolean isDebugFlag() {
       return debugFlag;
    }
    /**
     * Debug flag setter.
     * @param debugFlag
     */
    public void setDebugFlag(boolean debugFlag) {
       this.debugFlag = debugFlag;
    }
    /**
     * Server Getter.
     * @return String - the server address.
     */
    public final String getServer() {
        return server;
    }
    /**
     * Server setter.
     * @param myServer - the server address.
     */
    public final void setServer(final String myServer) {
        this.server = myServer;
    }
    /**
     * BasePort getter.
     * 
     * @return String - the first available port number on the server.
     */
    public final String getBasePort() {
        return basePort;
    }
    /**
     * BasePort Setter.
     * @param myBasePort - the first available port number on the server.
     */
    public final void setBasePort(final String myBasePort) {
        this.basePort = myBasePort;
    }
    /**
     * Channels getter.
     * @return String - the number of channels that are allowed per port.
     */
    public final String getChannels() {
        return channels;
    }
    /**
     * Channels setter.
     * 
     * @param myChannels - the number of channels that are allowed per port.
     */
    public final void setChannels(final String myChannels) {
        this.channels = myChannels;
    }
    /**
     * Application version getter.
     * 
     * @return float - the required version of the application
     */
    public final float getAppV() {
        return appV;
    }
    /**
     * Application version setter.
     * 
     * @param myAppV - the required version of the application
     */
    public final void setAppV(final float myAppV) {
        this.appV = myAppV;
    }
    /**
     * Application Name getter.
     * 
     * @return String - the application name that requires a version check.
     */
    public final String getAppName() {
        return appName;
    }
    /**
     * Application Name setter.
     * 
     * @param myAppName - the application name that requires a version check.
     */
    public final void setAppName(final String myAppName) {
        this.appName = myAppName;
    }
    
   
 
  
}
