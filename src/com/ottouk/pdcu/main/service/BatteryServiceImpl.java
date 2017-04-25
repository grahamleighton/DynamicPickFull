/**
 * 
 */
package com.ottouk.pdcu.main.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ottouk.pdcu.main.dao.BatteryDAO;
import com.ottouk.pdcu.main.dao.BatteryDAOImpl;
import com.ottouk.pdcu.main.domain.BatteryStatus;

/**
 * @author dis065
 *
 */
public class BatteryServiceImpl implements BatteryService {

	/**
	 * Number of battery status checks to keep.
	 */
	private static final int BATTERY_STATUSES_LIST_SIZE = 5;

	/**
	 * Minimum time interval (ms) between battery status checks.
	 */
	private static final int BATTERY_STATUSES_MS_INTERVAL = 2000;

	/**
	 * Battery dao.
	 */
	private BatteryDAO batteryDAO;

	/**
	 * List of battery status checks.
	 */
	private List batteryStatuses;

	/**
	 * Battery status (smoothed).
	 */
	private int batteryStatus;

	/**
	 * Time of last battery status update.
	 */
	private long lastBatteryStatusTime;

	/**
	 * Current battery percentage.
	 */
	private int batteryPercent;

	/**
	 * Initialise instance variables. 
	 */
	public BatteryServiceImpl() {
		// Implement batteryDAO interface
		batteryDAO = new BatteryDAOImpl();

		// Initialise batteryStatuses
		batteryStatuses = new ArrayList();
		while (batteryStatuses.size() < BATTERY_STATUSES_LIST_SIZE) {
			batteryStatuses.add(new Integer(BatteryStatus.HIGH));
		}

		// Initialise batteryStatus
		batteryStatus = BatteryStatus.HIGH;
	}

	/**
	 * Update battery status and percentage. The status value is smoothed over
	 * time to mitigate the effects of erratic hardware readings. This value can
	 * then be used to alert the user of deteriorating battery condition.
	 * 
	 * @return <code>true</code> if the battery status has changed,
	 *         <code>false</code> if it is unchanged.
	 */
	public boolean checkBattery() {

		// Get current battery percentage
		batteryPercent = batteryDAO.getBatteryPercent();

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastBatteryStatusTime > BATTERY_STATUSES_MS_INTERVAL) {
			lastBatteryStatusTime = currentTime;

			// Get current battery status
			int currentBatteryStatus = batteryDAO.getBatteryStatus();
			if (isPercentageCritical(batteryPercent)) {
				currentBatteryStatus = BatteryStatus.MORIBUND;
			}
			
			// Update battery status list (FIFO)
			batteryStatuses.remove(0);
			batteryStatuses.add(new Integer(currentBatteryStatus));

			// Check battery status list consistency
			boolean consistent = true;
			for (Iterator iterator = batteryStatuses.iterator(); iterator.hasNext();) {
				if (((Integer) iterator.next()).intValue() != currentBatteryStatus) {
					consistent = false;
					break;
				}
			}

			if (consistent && (currentBatteryStatus != batteryStatus)) {
				// update battery status and report change
				batteryStatus = currentBatteryStatus;
				return true;
			}

		}

		// report battery status unchanged
		return false;
	}
	
	/*private void addGraceTime() {
		lastBatteryStatusTime += 90000;
	}*/
	
	/**
	 * @param percentage value to check
	 * @return <code>true</code> if <code>percentage</code> is below critical threshold
	 */
	private boolean isPercentageCritical(int percentage) {
		return (percentage < BATTERY_PERCENTAGE_CRITICAL);
	}
	
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
	public boolean isPercentageCritical() {
		if (isPercentageCritical(batteryDAO.getBatteryPercent())) {
			return true;
		} else {
			// Battery may have been changed. Arbitrarily set status until
			// subsequent checkBattery() calls have enough readings to gauge
			// this properly.
			batteryStatus = BatteryStatus.LOW;
			return false;
		}
	}
	
	/**
	 * @return battery status (smoothed)
	 */
	public int getStatus() {
		return batteryStatus;
	}

	/**
	 * @return battery percentage (at time of last <code>checkBattery()</code> call)
	 */
	public int getPercent() {
		return batteryPercent;
	}

}
