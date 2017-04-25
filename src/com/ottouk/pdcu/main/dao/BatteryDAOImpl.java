package com.ottouk.pdcu.main.dao;

import com.ottouk.pdcu.main.domain.BatteryStatus;

import creme.Power;

/**
 * Retrieve battery status and percentage from HHT using cremex.jar and dll.
 * 
 * @author dis065
 * 
 */
public class BatteryDAOImpl implements BatteryDAO {
	
	/**
	 * <code>true</code> if O/S is WindowsCE (implying platform is HHT).
	 */
	private static final boolean OS_WIN_CE = osWinCE();
	
	
	/**
	 * @return <code>true</code> if O/S is WindowsCE (implying platform is HHT)
	 */
	private static boolean osWinCE() {
		try {
			return System.getProperty("os.name").endsWith("CE");
		} catch (SecurityException e) {
			// Unknown
			return false;
		}
	}

	/**
	 * @return battery status
	 */
	public int getBatteryStatus() {
		if (OS_WIN_CE) {
			// Assume JVM is CrEme and the cremex jar and dll are available
			switch (Power.getBatteryStatus()) {
			case Power.BATTERY_CRITICAL:
				return BatteryStatus.CRITICAL;
			case Power.BATTERY_LOW:
				return BatteryStatus.LOW;
			default:
				return BatteryStatus.HIGH;
			}
		} else {
			return BatteryStatus.HIGH;
		}
	}
	
	/**
	 * @return battery percentage
	 */
	public int getBatteryPercent() {
		if (OS_WIN_CE) {
			// Assume JVM is CrEme and the cremex jar and dll are available
			return Power.getBatteryPercent();
		} else {
			return 100;
		}
	}

}
