package com.ottouk.pdcu.version.service;

/**
 * PDCUConstants - Constant definition for the PDCU Application.
 * 
 * @author dis114
 *
 */

public final class PDCUConstants {

	/** 
	 * Logon Application Version no. 
	 */
	public static final String LOGON_VERSION = "2.3";

	/**
	 * PDCU ini file name..
	 */
	public static final String HHTFILE = "hht.ini";

	/** 
	 * Logon Screen Button Font. 
	 */
	public static final int LOGON_BUTTON_FONT = 14;

	/** 
	 * Logon Screen Label Font. 
	 */
	public static final int LOGON_LABEL_FONT = 14;

	/**
	 * <code>true</code> if O/S is WindowsCE (implying platform is HHT).
	 */
	public static final boolean OS_WIN_CE = osWinCE();

	/**
	 * <code>true</code> if O/S is WindowsXP (implying platform is PC).
	 */
	public static final boolean OS_WIN_XP = osWinXP();

	/**
	 * O/S specific pathname to the location of the jars.
	 */
	public static final String GUN_DIR = gunDir();

	/**
	 * O/S specific pathname to the location of the PDCU ini file.
	 */
	public static final String GUN_RESOURCEDIR = gunResourcedir();

	/**
	 * Boot app lock file.
	 */
	public static final String PDCU_BOOT_LOCK = GUN_RESOURCEDIR
			+ "PDCUBoot.lock";

	/**
	 * Main app lock file.
	 */
	public static final String PDCU_MAIN_LOCK = GUN_RESOURCEDIR
			+ "PDCUMain.lock";

	/**
	 * Version file.
	 */
	public static final String PDCU_VERSION_FILE = GUN_RESOURCEDIR
			+ "version.txt";

	/**
	 * O/S specific command required to execute jar.
	 */
	public static final String RUN_JAVA = runJava();

	/**
	 * Private constructor.
	 */
	private PDCUConstants() {
	}

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
	 * @return <code>true</code> if O/S is WindowsXP (implying platform is PC)
	 */
	private static boolean osWinXP() {
		try {
			return System.getProperty("os.name").endsWith("XP");
		} catch (SecurityException e) {
			// Unknown
			return false;
		}
	}

	/**
	 * @return O/S specific <code>GUN_DIR</code> location
	 */
	private static String gunDir() {
		if (OS_WIN_CE) {
			return "/Dynamic/";
		} else if (OS_WIN_XP) {
			return "C:/build/DynamicPick/";
		} else {
			// OS_LINUX
			return "";
		}
	}

	/**
	 * @return O/S specific <code>GUN_RESOURCEDIR</code> location
	 */
	private static String gunResourcedir() {
		if (OS_WIN_CE) {
			return GUN_DIR + "Resources/";
		} else if (OS_WIN_XP) {
			return GUN_DIR + "resources/";
		} else {
			// OS_LINUX
			return GUN_DIR + "resources/";
		}
	}

	/**
	 * @return O/S specific command required to execute jar
	 */
	private static String runJava() {
		if (OS_WIN_CE) {
			// Throw -sp:0 switch to hide CrEme splash screen
			// Throw -Of switch to redirect console to \jscpout.txt
			return "/Windows/CrEme/bin/CrEme.exe -sp:0 -Of -jar ";
		} else if (OS_WIN_XP) {
			return "java -jar ";
		} else {
			// OS_LINUX
			return "java -jar ";
		}
	}

}
