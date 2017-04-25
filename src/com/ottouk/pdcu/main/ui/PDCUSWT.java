package com.ottouk.pdcu.main.ui;

import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.version.service.AppLock;
import com.ottouk.pdcu.version.service.VersionMgr;
import com.ottouk.pdcu.version.ui.DelayedExit;

/**
 * Main class.
 * 
 * @author dis065
 */
public final class PDCUSWT {
	
	/**
	 * 
	 */
	private PDCUSWT() {
	}
	
	/**
	 * @param args
	 *            args[0] unitId (optional), args[1] operator (optional, though
	 *            must be provided if unitId is), args[2] versionFile
	 *            (mandatory)
	 * 
	 */
	public static void main(String[] args) {
		
		// Temp!!!
		// If 3 args supplied, drop arg[0] (unitId).
		if (args.length < 1) {

			if (System.getProperty("os.name").endsWith("")) {
				// Windows XP
//				args = new String[] { "7", "00249173", "C:\\Documents and Settings\\hstd004\\My Documents\\DynamicFull\\DynamicPickFull\\DynamicPickFull\\resources\\version.txt" };
				args = new String[] { "7", "00472247", "h:\\vdi\\java\\Dynamic\\DynamicPickFull\\resources\\version.txt" };
				
				} else {
				// Windows CE
				args = new String[] { "7", "00602043", "/Dynamic/Resources/version.txt" };
			}
		}
		
		if (args.length == 3) {
			String[] newArgs = new String[args.length - 1];
			for (int i = 0; i < newArgs.length; i++) {
				newArgs[i] = args[i + 1];
			}
			args = newArgs;
		}
		
		// Temporarily Turn on logging. This flag will be overridden as
		// appropriate when the versionFile is read (as part of logon).
		StringUtils.setDebug(true);
		
		// Log input args
		switch (args.length) {
		case 0:
			StringUtils.log("{PDCUMain} No input args!");
			break;
		case 1:
			StringUtils.log("{PDCUMain} Input arg.  VersionFile: "	+ args[0]);
			break;
		case 2:
			StringUtils.log("{PDCUMain} Input args.  Operator: " + args[0] 
					+ ", versionFile: " + args[1]);
			break;
		default:
			StringUtils.log("{PDCUMain} Input args.  UnitId: " + args[0] 
					+ ", operator: " + args[1] 
					+ ", versionFile: "	+ args[2]);
			break;
		}

		
		try {
			if (AppLock.mainAppAlreadyRunning()) {
				// Main app already running so exit!
				System.exit(0);
			} else {
				// Run Main app
				SplashScreen.show();
				new LogonShell(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SplashScreen.show();
		} finally {
			//System.exit(0);
			// Pass back to Boot app

			VersionMgr versionMgr = new VersionMgr();
			if (!versionMgr.checkLogonVersionsOK()) {
				GeneralShell.errorBox("WARNING",
						"You may not be running the latest boot application");
			}
			versionMgr.invokeLogon();
			
			DelayedExit.exitToBoot(3);
		}
	}
	
}
