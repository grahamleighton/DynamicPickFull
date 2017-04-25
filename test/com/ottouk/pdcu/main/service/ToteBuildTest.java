package com.ottouk.pdcu.main.service;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ToteBuildTest extends TestCase {

	protected static Log logger = LogFactory.getLog("TestCase");
	private LogonService logonService;
	private ToteBuildService toteBuildService;

	public ToteBuildTest(String s) {
		super(s);
	}

	protected void setUp() throws Exception {
		super.setUp();

		Integer unitId = new Integer(7);
		// Integer unitId = new Integer((int)(Math.random()*100));
		// logger.info("Unit ID: " + unitId);
		String operator = "089903";
		String server = "172.16.8.35";
		int basePort = 4000;
		int channels = 10;

		logonService = new LogonServiceImpl();

		assertTrue("Logon", logonService.logon(unitId, operator, server,
				basePort, channels));
		logger.info("Unit ID: " + unitId + "logon RC="
				+ logonService.getReturnCode());
		assertTrue("Start Tote Build", logonService.startToteBuild());

		toteBuildService = new ToteBuildServiceImpl();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		assertTrue("Stop Tote Build", logonService.stopToteBuild());
		assertTrue("Logoff", logonService.logoff());
	}

	public void test1() {

		String tote = "8000394594";
		String item1 = "09940959";
		String item2 = "09942828";

		toteBuildService.addTote(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		toteBuildService.addScan(item1);
		assertEquals("Item added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Item added", 1, toteBuildService.getToteItemCount());

		toteBuildService.addScan(item1);
		assertEquals("Duplicate item added",
				ToteBuildService.RC_WARN_ITEM_PREVIOUSLY_USED, toteBuildService
						.getReturnCode());
		assertEquals("Duplicate item added", 1, toteBuildService
				.getToteItemCount());

		toteBuildService.addScan(item2);
		assertEquals("Second item added", ToteBuildService.RC_OK,
				toteBuildService.getReturnCode());
		assertEquals("Second item added", 2, toteBuildService
				.getToteItemCount());

		toteBuildService.addScan(tote);
		assertEquals("Tote transmitted successfully", ToteBuildService.RC_OK,
				toteBuildService.getReturnCode());
		assertEquals("Tote transmitted successfully", 0, toteBuildService
				.getToteItemCount());
	}

	public void test2() {

		String tote = "8000394594";
		String[] maxAllowedItems = { "09940959", "09942828", "09946706",
				"09948065", "09952718", "09955859", "09957285", "09957297",
				"09960053", "09961938" };
		String extraItem1 = "09976838";
		String extraItem2 = "09975184";

		toteBuildService.addTote(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		for (int i = 0; i < maxAllowedItems.length; i++) {
			toteBuildService.addScan(maxAllowedItems[i]);
			if (i == (maxAllowedItems.length - 1)) {
				assertEquals("Item added", ToteBuildService.RC_WARN_TOTE_FULL,
						toteBuildService.getReturnCode());
			} else {
				assertEquals("Item added", ToteBuildService.RC_OK,
						toteBuildService.getReturnCode());
			}
			assertEquals("Item added", (i + 1), toteBuildService
					.getToteItemCount());
		}

		toteBuildService.addScan(extraItem1);
		assertEquals("Item added", ToteBuildService.RC_ERROR_MAX_ITEMS_IN_TOTE,
				toteBuildService.getReturnCode());
		assertEquals("Item added", (ToteBuildService.MAX_ITEMS_IN_TOTE),
				toteBuildService.getToteItemCount());

		toteBuildService.addScan(extraItem2);
		assertEquals("Item added", ToteBuildService.RC_ERROR_MAX_ITEMS_IN_TOTE,
				toteBuildService.getReturnCode());
		assertEquals("Item added", (ToteBuildService.MAX_ITEMS_IN_TOTE),
				toteBuildService.getToteItemCount());

		toteBuildService.addScan(tote);
		assertEquals("Tote transmitted successfully", ToteBuildService.RC_OK,
				toteBuildService.getReturnCode());
		assertEquals("Tote transmitted successfully", 0, toteBuildService
				.getToteItemCount());
	}

	public void test3() {

		String tote = "8000394594";
		String[] items = { "09965580", "09966857", "09968684" };
		String differentTote = "8000388760";

		toteBuildService.addTote(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		for (int i = 0; i < items.length; i++) {
			toteBuildService.addScan(items[i]);
			assertEquals("Item added", ToteBuildService.RC_OK, toteBuildService
					.getReturnCode());
			assertEquals("Item added", (i + 1), toteBuildService
					.getToteItemCount());
		}

		toteBuildService.addScan(differentTote);
		assertEquals("Tote transmitted successfully",
				ToteBuildService.RC_ERROR_TOTE_STILL_OPEN, toteBuildService
						.getReturnCode());
		assertEquals("Tote transmitted successfully", items.length,
				toteBuildService.getToteItemCount());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		toteBuildService.addScan(tote);
		assertEquals("Tote transmitted successfully", ToteBuildService.RC_OK,
				toteBuildService.getReturnCode());
		assertEquals("Tote transmitted successfully", 0, toteBuildService
				.getToteItemCount());
	}

	public void test4() {

		String toteNotStarting800 = "1000394594";

		toteBuildService.addTote(toteNotStarting800);
		assertEquals("Tote added", ToteBuildService.RC_WARN_MIS_SCAN,
				toteBuildService.getReturnCode());
		assertEquals("Tote added", null, toteBuildService.getToteId());
	}

	public void test5() {

		String tote = "8000394594";

		toteBuildService.addTote(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		toteBuildService.addScan(tote);
		assertEquals("Tote closed", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote closed", null, toteBuildService.getToteId());

		toteBuildService.addTote(tote);
		assertEquals("Tote reused",
				ToteBuildService.RC_WARN_TOTE_PREVIOUSLY_USED, toteBuildService
						.getReturnCode());
	}

	public void test6() {

		String tote = "8000394594";
		String[] items = { "09940959", "09942828" };

		toteBuildService.addTote(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		toteBuildService.addScan(tote);
		assertEquals("Tote closed", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote closed", null, toteBuildService.getToteId());

		toteBuildService.addTote(tote);
		assertEquals("Tote reused",
				ToteBuildService.RC_WARN_TOTE_PREVIOUSLY_USED, toteBuildService
						.getReturnCode());

		toteBuildService.addToteAgain(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		for (int i = 0; i < items.length; i++) {
			toteBuildService.addScan(items[i]);
			assertEquals("Item added", ToteBuildService.RC_OK, toteBuildService
					.getReturnCode());
			assertEquals("Item added", (i + 1), toteBuildService
					.getToteItemCount());
		}

		toteBuildService.addScan(tote);
		assertEquals("Tote closed", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote closed", null, toteBuildService.getToteId());
	}

	public void test7() {

		String tote = "8000394594";
		String[] items = { "09940959", "09942828", "09946706", "09948065",
				"09952718" };

		toteBuildService.addTote(tote);
		assertEquals("Tote added", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote added", tote, toteBuildService.getToteId());

		for (int i = 0; i < items.length; i++) {
			toteBuildService.addScan(items[i]);
			assertEquals("Item added", ToteBuildService.RC_OK, toteBuildService
					.getReturnCode());
			assertEquals("Item added", (i + 1), toteBuildService
					.getToteItemCount());
		}

		toteBuildService.addScan(tote);
		assertEquals("Tote closed", ToteBuildService.RC_OK, toteBuildService
				.getReturnCode());
		assertEquals("Tote closed", null, toteBuildService.getToteId());

		// Need to simulate comms error.  JMock?
	}

}
