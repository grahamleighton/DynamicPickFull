package com.ottouk.pdcu.main.service;


/**
 * @author dis065
 *
 */
public interface BatteryService {
	
	/**
	 * Critical percentage below which users should be forced to replace
	 * the battery. Typically the critical-status warning message is triggered
	 * at 10%, therefore this figure should be below that.
	 */
	int BATTERY_PERCENTAGE_CRITICAL = 9;
	
	// Methods

	/**
	 * Update battery status and percentage.
	 * 
	 * @return <code>true</code> if the battery status has changed,
	 *         <code>false</code> if it is unchanged.
	 */
	boolean checkBattery();
	
	/**
	 * Check if battery percentage is critical. This check can be used to force
	 * the user to change the battery and is in addition to the battery status
	 * being critical (which merely advises the user to change the battery). If
	 * this method erroneously returns a false result, the user may be able to
	 * continue with a dangerously low battery!
	 * 
	 * @return <code>true </code> if real-time battery percentage is below
	 *         critical threshold
	 */
	boolean isPercentageCritical();
	
	/**
	 * @return battery status
	 */
	int getStatus();
	
	/**
	 * @return battery percentage
	 */
	int getPercent();

}
