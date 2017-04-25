package com.ottouk.pdcu.main.service;

import org.jmock.core.constraint.IsNull;

import com.ottouk.pdcu.main.utils.StringUtils;

import junit.framework.TestCase;

public class TestToteConsolService extends TestCase {

	private ToteConsolServiceImpl toteCS;
	private LogonService ls;
	
	
	public TestToteConsolService(String name) {
		super(name);
		toteCS = new ToteConsolServiceImpl();
		toteCS.setOperator("00669253");
		System.out.println(toteCS.getOperator());
		toteCS.setUnitId(Integer.valueOf(MainConstants.FOUR));
		ls = new LogonServiceImpl();
		System.out.println("Setup");
		
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		StringUtils.log(">> Starting set up");
		StringUtils.log("Logging onto server");
		ls.logon(toteCS.getUnitId(), toteCS.getOperator(),
				"172.16.12.17", 3004, 1);
		/**
		 * Start Consol Activity
		 */
		StringUtils.log("Starting Tote Consol activity...");
		assertEquals(true, ls.startToteConsol());

		StringUtils.log(">> Ending set up");

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		StringUtils.log(">> Starting tearDown");
		StringUtils.log("Stopping Tote Consol activity...");
		assertEquals(true, ls.stopToteConsol());
		
		assertEquals(true,ls.logoff());
		StringUtils.log(">> Stopping tearDown");
		
	}

	public final void testSetTrolleyTote() {
		toteCS.setOperator("00669253");
		assertEquals("00669253", toteCS.getOperator());
//		assertEquals(true,toteCS.setTrolleyTote("8000000006"));
//		assertEquals("8000000006",toteCS.getTrolleyTote());
//		toteCS.setTrolleyTote("8000000006");
	}

	public final void testAddItemToTrolleyTote() {
		System.out.println("Creating and closing empty tote");
		toteCS.setTrolleyTote("8000000006");
		assertEquals(0, toteCS.getTrolleyToteItemCount());
		toteCS.setTrolleyTote("8000000006"); // This will end the tote.
		System.out.println("Creating tote with items ...");
		toteCS.setTrolleyTote("8000000006"); // This will create new tote.
		toteCS.addItemToTrolleyTote("93523208");
		assertEquals(1, toteCS.getTrolleyToteItemCount());
		assertEquals(true, toteCS.addItemToTrolleyTote("93308000"));
		assertEquals(2, toteCS.getTrolleyToteItemCount());
		assertEquals(false, toteCS.addItemToTrolleyTote("93308001"));
		assertEquals(2, toteCS.getTrolleyToteItemCount());
		assertEquals(true, toteCS.addItemToTrolleyTote("93607465"));
		assertEquals(3, toteCS.getTrolleyToteItemCount());
		assertEquals(false, toteCS.addItemToTrolleyTote("9330"));
		assertEquals(false, toteCS.addItemToTrolleyTote("9"));
		assertEquals(false, toteCS.addItemToTrolleyTote("8000000006"));
		assertEquals(3, toteCS.getTrolleyToteItemCount());
		System.out.println("Closing tote with 3 items");
		toteCS.setTrolleyTote("8000000006");
//		assertEquals(0, toteCS.getTrolleyToteItemCount());
		
	}

	public final void testSetLastTote() {
		toteCS.setLastTote("8000003229");
		assertEquals("8000003229", toteCS.getLastTote());
	}
	
	public final void testGetStartLocation() {
		String beamLoc = "01303698";     // YA102C
		
		boolean result = toteCS.getStartLocation(beamLoc);
		boolean locavail = toteCS.getLocationObj().isLocationAvailable();
		
		assertEquals(locavail, result);
		
	}



	public final void testGetTrolleyToteItemCount() {
		/*
		 * Reuse testAddItemToTrolleyTote as does the same tests.
		 */
		testAddItemToTrolleyTote();
	}
	/**
	 * Tests emptying a location.
	 */
	public final void testLocationEmptied() {
		boolean result = toteCS.locationEmptied("130361");
		boolean locavail = (
				toteCS.getLocationObj().getAlphaLocation().length() != 0
				);
		if (result) {
			StringUtils.log("result = true");
		} else {
			StringUtils.log("result = false");
		}
		if (locavail) {
			StringUtils.log("locavail = true");
		} else {
			StringUtils.log("locavail = false");
		}
		assertEquals(result, locavail);
		if (locavail) {
			StringUtils.log("Actually emptying location ");
			StringUtils.log(toteCS.getLocationObj().getAlphaLocation());
			result = toteCS.locationEmptied(
					toteCS.getLocationObj().getNumericLocation());
			locavail = (
					toteCS.getLocationObj().getAlphaLocation().length() != 0);
			
			if (result) {
				StringUtils.log("result = true");
			} else {
				StringUtils.log("result = false");
			}
			if (locavail) {
				StringUtils.log("locavail = true");
			} else {
				StringUtils.log("locavail = false");
			}
			assertEquals(result, locavail);
			
			/**
			 * Force a no new location from server to test end of run.
			 */
			toteCS.processLocationInfo("ACKN");
			result = false;
			locavail = (
					toteCS.getLocationObj().getAlphaLocation().length() != 0);
			
			if (result) {
				StringUtils.log("result = true");
			} else {
				StringUtils.log("result = false");
			}
			if (locavail) {
				StringUtils.log("locavail = true");
			} else {
				StringUtils.log("locavail = false");
			}
			assertEquals(result, locavail);
			
			
		} else {
			StringUtils.log("no further locations available");
		}
		
		
	}

	public final void testConfirmLocation() {
		toteCS.processLocationInfo("ACKYYA024A130042");
		assertEquals(true, toteCS.confirmLocation("01300420"));
		assertEquals(false, toteCS.confirmLocation("01320420"));
		assertEquals(false, toteCS.confirmLocation("01303636"));
		toteCS.processLocationInfo("ACKN");
		assertEquals(false, toteCS.confirmLocation("01300420"));
		assertEquals(false, toteCS.confirmLocation("01320420"));
		assertEquals(false, toteCS.confirmLocation("01303636"));
		
	}

	public final void testWhichToteScanned() {
		toteCS.setLastTote("8000003229");
		assertEquals("8000003229",toteCS.getLastTote());
		assertEquals(true,toteCS.setTrolleyTote("8000000006"));
		assertEquals("8000000006", toteCS.getTrolleyTote());

		
		assertEquals(MainConstants.TOTE_ID_LOCATION, 
				toteCS.whichToteScanned("8000003229"));
		assertEquals(MainConstants.TOTE_ID_TROLLEY, 
				toteCS.whichToteScanned("8000000006"));
		assertEquals(MainConstants.TOTE_ID_NEITHER, 
				toteCS.whichToteScanned("8000000004"));
		
	}
	public final void testisValidItemOrTote() {
		assertEquals(MainConstants.VALID_TOTE, 
				toteCS.isValidItemOrTote("8000000006"));
		assertEquals(MainConstants.VALID_ITEM , 
				toteCS.isValidItemOrTote("93308000"));
		
	}
	public final void testTotePutaway() {
		/*
		 * Build a tote with 3 items then put that tote away in location.
		 */
		toteCS.setTrolleyTote("8000000006");
		toteCS.addItemToTrolleyTote("93308000");
		toteCS.addItemToTrolleyTote("93607465");
		toteCS.addItemToTrolleyTote("93523208");
		toteCS.addItemToTrolleyTote("93523207");
		assertEquals("8000000006", toteCS.getTrolleyTote());
		assertEquals(3,toteCS.getTrolleyToteItemCount());
		toteCS.setLastTote("8000003229");
		assertEquals("8000003229",toteCS.getLastTote());
		toteCS.totePutaway("01303698");
	}

}
