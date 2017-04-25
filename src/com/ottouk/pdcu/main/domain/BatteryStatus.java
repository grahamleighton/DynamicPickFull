package com.ottouk.pdcu.main.domain;


/**
 * @author dis065
 *
 */
public final class BatteryStatus {
	
	/**
	 * Equivalent to Power.BATTERY_HIGH.
	 */
	public static final int HIGH = 1;
	
	/**
	 * Equivalent to Power.BATTERY_LOW.
	 */
	public static final int LOW = 2;
	
	/**
	 * Equivalent to Power.BATTERY_CRITICAL.
	 */
	public static final int CRITICAL = 3;
	
	/**
	 * batteryPercent < BATTERY_PERCENTAGE_CRITICAL.
	 */
	public static final int MORIBUND = 4;
	
	
	/**
	 * Private empty constructor.
	 */
	private BatteryStatus() {
	};
	
}
