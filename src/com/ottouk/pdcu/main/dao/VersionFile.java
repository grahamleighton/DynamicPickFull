package com.ottouk.pdcu.main.dao;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.ottouk.pdcu.main.domain.Version;
import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Read properties file into Version class.
 * 
 * @author dis065
 *
 */
public class VersionFile {
	
	/**
	 * Properties file.
	 */
	private String iniFile;
	
	/**
	 * Properties class loaded from iniFile. 
	 */
	private Properties properties;
	
	/**
	 * Version populated from properties class.
	 */
	private Version version;
	

	/**
	 * Constructor.
	 * 
	 * @param iniFile properties file to read
	 */
	public VersionFile(String iniFile) {
		
		this.iniFile = iniFile;
		properties = new Properties();
		
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(iniFile));
			properties.load(in);
		} catch (FileNotFoundException e) {
			properties = null;
		} catch (IOException e) {
			properties = null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					properties = null;
				}
			}
			StringUtils.log(properties, true);
		}
	}
	
	/**
	 * Return instance of Version class with fields populated from properties file.
	 * 
	 * @return Version instance 
	 */
	public Version getVersion() {
		
		if (version != null) {
			return version;
		}

		if (properties == null) {
			return null;
		}
		
		int channels;
		int basePort;
		try {
			channels = Integer.parseInt(properties.getProperty("CHANNELS")); 
			basePort = Integer.parseInt(properties.getProperty("BASEPORT"));
		} catch (NumberFormatException e) {
			return null;
		}
		
		boolean debug = new Boolean(properties.getProperty("DEBUG")).booleanValue();
		
		version =  new Version(
					channels,
					basePort,
					properties.getProperty("SERVER"), 
					properties.getProperty("LOGONAPPNAME"), 
					properties.getProperty("LOGON"), 
					properties.getProperty("MAINAPPNAME"), 
					properties.getProperty("MAINAPP"),
					debug);
		
		StringUtils.setDebug(debug);
		
		return version;
	}
	
	/**
	 * Get properties file that was passed to the constructor.
	 * 
	 * @return properties file
	 */
	public String getIniFile() {
        return this.iniFile;
    }

}
