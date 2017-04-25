package com.ottouk.pdcu.version.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * AppLock. Class that is used to ensure only one version of a specified application is 
 * currently executing.
 * 
 * @author dis114
 *
 */
public class AppLock {

	/**
	 * A lock File to be used to ensure only one instance of the application is
	 * running.
	 */
	private static FileWriter lock;
	
	/**
	 *  file object for the lock file.
	 */
	private static File file;
	
		
	/**
	 * @return <code>true</code> if the Main lock file is already in use by
	 *         another instance of the application, <code>false</code> if the
	 *         lock file does not exist or is not in use.
	 */
	public static boolean mainAppAlreadyRunning() {
		return appAlreadyRunning(PDCUConstants.PDCU_MAIN_LOCK);
	}
	
	/**
	 * @return <code>true</code> if the Boot lock file is already in use by
	 *         another instance of the application, <code>false</code> if the
	 *         lock file does not exist or is not in use.
	 */
	public static boolean bootAppAlreadyRunning() {
		return appAlreadyRunning(PDCUConstants.PDCU_BOOT_LOCK);
	}
	
	/**
	 * Method to check if an instance of this main application is already
	 * running. Utilises a lock file to ensure that only one instance is
	 * running.
	 * 
	 * @param lockFile -
	 *            the full pathname of the file that is to be checked for
	 *            existence.
	 * @return - true if the lock file is already in use by another instance of
	 *         the application, false if the lock file does not exist or is not in use.
	 */
	public static boolean appAlreadyRunning(final String lockFile) {

		try {
			file = new File(lockFile);

			if (file.exists()) {

				// file exists - check if it is in use.
				if (file.delete()) {
					// Delete Successful - so lockFile was not in use for another instance.
					System.out.println(lockFile + " deleted!");
				} else {
					// Delete unsuccessful - lockFile in use by another instance
					System.out.println("An instance of this application is already running.");
					return true;
				}
			}
		} catch (Exception nop) {
			// Assume lock file is not in use by another instance on failure
			nop.printStackTrace();
			return false;
		}

		// file doesn't exist or is not in use
		try {
			// Open file for writing - this will prevent any other app from accessing it
			lock = new FileWriter(lockFile); // the same lock file name.
			System.out.println(lockFile + " opened");
			return false;
		} catch (IOException nop) {
			// Assume lock file is not in use by another instance on failure
			nop.printStackTrace();
			return false;
		}
	}

	/**
	 * Deletes the lock file.
	 */
	public static void releaseLock() {

		try {
			lock.close();
			if (file.exists()) {
				if (file.delete()) {	// Delete the lock file.         
					System.out.println(file.getName() + " deleted!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
