package com.ottouk.pdcu.main.dao;


/**
 * Retrieve battery status and percentage.
 * 
 * @author dis065
 *
 */
public interface BatteryDAO {

	// Methods
	
	/**
	 * @return battery status
	 */
	int getBatteryStatus();

	/**
	 * @return battery percentage
	 */
	int getBatteryPercent();

}
