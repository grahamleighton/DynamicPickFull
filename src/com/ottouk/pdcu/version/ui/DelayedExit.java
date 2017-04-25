package com.ottouk.pdcu.version.ui;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.ottouk.pdcu.version.service.AppLock;
import com.ottouk.pdcu.version.service.PDCUConstants;

/**
 * Class that is used to delay the exit of an application. 
 * Waits a specified amount of seconds before checking for the existence of a certain file. If
 * file exists then the current application exits. 
 * 
 * @author dis114
 *
 */
public final class DelayedExit {
	/** Timer object used to delay the checking for existence of file. */
	private static Timer timer;

	/**
	 * private empty constructor.
	 */
	private DelayedExit() {

	}

	/**
	 * exitToBoot. Schedules a task that performs waits for the existence of the Boot lock file.
	 * 
	 * @param seconds - number of seconds to wait before checking for the file.
	 */
	public static void exitToBoot(final int seconds) {

		timer = new Timer();
		timer.schedule(new ToDoTask(PDCUConstants.PDCU_BOOT_LOCK),
				seconds * 1000);
	}

	/**
	 * exitToMain. Schedules a task that performs waits for the existence of the Main lock file.
	 * 
	 * @param seconds - number of seconds to wait before checking for the file.
	 */
	public static void exitToMain(final int seconds) {

		timer = new Timer();
		timer.schedule(new ToDoTask(PDCUConstants.PDCU_MAIN_LOCK),
				seconds * 1000);
	}

	/**
	 * The Scheduled task.
	 * 
	 * @author dis114
	 */
	static class ToDoTask extends TimerTask {

		/** the filename that has to exist before teh application is exited. */
		private String checkFile;

		/**
		 * Constructor.
		 * 
		 * @param exitFile - the file top be checked for.
		 */
		public ToDoTask(final String exitFile) {
			checkFile = exitFile;
		}

		/**
		 * Method that is run after the scheduled time specified in the TODOTask call. 
		 */
		public void run() {
			System.out.println("Exiting....  Waiting for " + checkFile);

			File file = new File(checkFile);

			while (!file.exists()) { // loop until file found
				//System.out.println("Waiting...");
			}

			System.out.println("ABOUT TO EXIT");

			timer.cancel(); // Terminate the thread
			AppLock.releaseLock(); // Clear down the application lock file.
			System.exit(0);

		}
	}
}
