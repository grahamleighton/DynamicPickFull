package com.ottouk.pdcu.main.domain;

/**
 * I hold the contents of the <code>version.txt</code> properties file.
 * 
 * @author dis065
 * 
 */
public class Version {

	/**
	 * Number of channels.
	 */
	private int channels;
	
	/**
	 * Base port.
	 */
	private int basePort;
	
	/**
	 * Server ip address.
	 */
	private String server;
	
	/**
	 * Logon app name.
	 */
	private String logonAppName;
	
	/**
	 * Logon app version.
	 */
	private String logon;
	
	/**
	 * Main app name.
	 */
	private String mainAppName;
	
	/**
	 * Main app version.
	 */
	private String mainApp;
	
	/**
	 * True to log debug messages, false to not.
	 */
	private boolean debug;
	
	
	/**
	 * Minimal constructor.
	 * 
	 * @param channels number of channels
	 * @param basePort base port
	 * @param server server ip address
	 */
	public Version(int channels, int basePort, String server) {
		this.channels = channels;
		this.basePort = basePort;
		this.server = server;
		this.logonAppName = null;
		this.logon = null;
		this.mainAppName = null;
		this.mainApp = null;
		this.debug = false;
	}
	
	/**
	 * Full constructor.
	 * 
	 * @param channels number of channels
	 * @param basePort base port
	 * @param server server ip address
	 * @param logonAppName logon app name
	 * @param logon logon app version
	 * @param mainAppName main app name
	 * @param mainApp main app version
	 * @param debug true to log debug messages, false to not
	 */
	public Version(int channels, int basePort, String server,
			String logonAppName, String logon, String mainAppName,
			String mainApp, boolean debug) {
		this.channels = channels;
		this.basePort = basePort;
		this.server = server;
		this.logonAppName = logonAppName;
		this.logon = logon;
		this.mainAppName = mainAppName;
		this.mainApp = mainApp;
		this.debug = debug;
	}
	
	
	/**
	 * @return channels
	 */
	public int getChannels() {
		return channels;
	}
	
	/**
	 * @param channels channels
	 */
	public void setChannels(int channels) {
		this.channels = channels;
	}
	
	/**
	 * @return basePort
	 */
	public int getBasePort() {
		return basePort;
	}
	
	/**
	 * @param basePort basePort
	 */
	public void setBasePort(int basePort) {
		this.basePort = basePort;
	}
	
	/**
	 * @return server
	 */
	public String getServer() {
		return server;
	}
	
	/**
	 * @param server server
	 */
	public void setServer(String server) {
		this.server = server;
	}
	
	/**
	 * @return logonAppName
	 */
	public String getLogonAppName() {
		return logonAppName;
	}
	
	/**
	 * @param logonAppName logonAppName
	 */
	public void setLogonAppName(String logonAppName) {
		this.logonAppName = logonAppName;
	}
	
	/**
	 * @return logon
	 */
	public String getLogon() {
		return logon;
	}
	
	/**
	 * @param logon logon
	 */
	public void setLogon(String logon) {
		this.logon = logon;
	}
	
	/**
	 * @return mainAppName
	 */
	public String getMainAppName() {
		return mainAppName;
	}
	
	/**
	 * @param mainAppName mainAppName
	 */
	public void setMainAppName(String mainAppName) {
		this.mainAppName = mainAppName;
	}
	
	/**
	 * @return mainApp
	 */
	public String getMainApp() {
		return mainApp;
	}
	
	/**
	 * @param mainApp mainApp
	 */
	public void setMainApp(String mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * @return debug
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * @param debug debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
