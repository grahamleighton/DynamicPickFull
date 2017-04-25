package com.ottouk.pdcu.main.service;

import junit.framework.TestCase;
import com.ottouk.pdcu.main.utils.StringUtils;
import com.ottouk.pdcu.main.utils.Validate;


/**
 * test code for Audit function.
 * @author hstd004
 *
 */
public class AuditTest extends TestCase {
	/**
	 * Instance of ToteAuditService.
	 */
	private ToteAuditServiceImpl toteAuditService;
	/**
	 * Instance of LogonService.
	 */
	private LogonService ls;

	/**
	 * Constructor.
	 * @param name ????
	 */
	public AuditTest(final String name) {
		super(name);
	}
	/**
	 * Sets up the test run.
	 * Creates the toteAuditService for use in tests.
	 * Creates the logon service for use in tests.
	 * @throws Exception ??????
	 */
	protected final void setUp() throws Exception {
		super.setUp();
		toteAuditService  = new ToteAuditServiceImpl();
		ls = new LogonServiceImpl();
	}
	/**
	 * Destroys the test run.
	 * @throws Exception ????
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * Test the retrieval of the numeric location after server response.
	 */
	public final void testGetNumericLocation() {
		toteAuditService.receiveLocationRequest("ACKYBC213A524802");
		assertEquals("524802", toteAuditService.getNumericLocation());
	}

	/**
	 * Test whether a location has been passed from server after a
	 * location request.
	 */
	public final void testIsLocationAvailable() {
		toteAuditService.receiveLocationRequest("ACKYBC213A524802");
		assertEquals(true, toteAuditService.isLocationAvailable());
		toteAuditService.receiveLocationRequest("ACKN");
		assertEquals(false, toteAuditService.isLocationAvailable());
	}
	/**
	 * Test to retrieve the AlphaNumeric display format location 
	 * after the server has responded with a message containing a location.
	 */
	public final void testGetAlphaLocation() {
		toteAuditService.receiveLocationRequest("ACKYBC213A524802");
		assertEquals("BC21-3A", toteAuditService.getAlphaLocation());
	}

	/**
	 * Test to check whether a location scanned matches the location sent 
	 * from the server.
	 */
	public final void testValidatePILocation() {
		toteAuditService.receiveLocationRequest("ACKYBC213A524802");
		assertEquals(true , toteAuditService.validatePILocation("05248025"));
		assertEquals(false , toteAuditService.validatePILocation("05248032"));
	}
	/**
	 * Test that the tote id is set successfully.
	 */
	public final void testSetToteId() {
		toteAuditService.setToteId("8000000006");
		assertEquals("8000000006", toteAuditService.getToteId());
		toteAuditService.setToteId("8000000005");
		assertEquals("8000000005", toteAuditService.getToteId());
	}
	/**
	 * Test for adding an item to a tote.
	 */
	public final void testAddItem() {
		toteAuditService.setToteId("8000000006");
		assertEquals(true, toteAuditService.addItem("00000000"));
		assertEquals(true, toteAuditService.addItem("00000001"));
		assertEquals(true, toteAuditService.addItem("93308000"));
		assertEquals(2, toteAuditService.getToteItemCount());
		assertEquals(false, toteAuditService.addItem("93308000"));
		assertEquals(2, toteAuditService.getToteItemCount());
	}
	/**
	 * Test for validating a tote.
	 */
	public final void testValidatePITote() {
		assertEquals(true, toteAuditService.validatePITote("8000000006"));
		assertEquals(false, toteAuditService.validatePITote("8000000005"));
		assertEquals(false, toteAuditService.validatePITote("800000006"));
		
	}
	/**
	 * Test for retrieving the item count for a tote.
	 */
	public final void testGetToteItemCount() {
		assertEquals(true, toteAuditService.validatePITote("8000000006"));
		assertEquals(true, toteAuditService.addItem("00000000"));
		assertEquals(1, toteAuditService.getToteItemCount());
		assertEquals(false, toteAuditService.addItem("00000000"));
		assertEquals(1, toteAuditService.getToteItemCount());
		assertEquals(true, toteAuditService.addItem("93308000"));
		assertEquals(2, toteAuditService.getToteItemCount());
		assertEquals(false, toteAuditService.addItem("00000000"));
		assertEquals(2, toteAuditService.getToteItemCount());
		assertEquals(true, toteAuditService.addItem("93607465"));
		assertEquals(MainConstants.THREE, toteAuditService.getToteItemCount());
		
	}
	/**
	 * Test to retrieve the Tote ID after a tote validation.
	 */
	public final void testGetToteId() {
		assertEquals(true, toteAuditService.validatePITote("8000000006"));
		assertEquals("8000000006", toteAuditService.getToteId());
		assertEquals(false, toteAuditService.validatePITote("8000000005"));
		assertEquals("", toteAuditService.getToteId());
	}	

	/**
	 * Function to simulate PI'ing 3 tote items in a given location.
	 * From the start location will attempt to get 3 locations from
	 * the server to PI
	 * @param startLocation
	 */
	private void piCycle(final String startLocation) {
		boolean rsp = toteAuditService.locationRequest(startLocation); 
		if (toteAuditService.isLocationAvailable()) {
			assertEquals(true, rsp);
		} else {
			assertEquals(
			"Location not available but request returned a true value,"
			+ "should return false", 
			false, rsp);
		}
		
		int i = 0;
		
		while (i < MainConstants.THREE) {
			StringUtils.log("Pass " + Integer.toString(i + 1) + " ....");
			if (toteAuditService.isLocationAvailable()) {
				int cd = Validate.mod10Check3131CD("0"  
					+ toteAuditService.getNumericLocation(), 
					MainConstants.SEVEN);
				assertEquals(true, 
					toteAuditService.validatePILocation("0"
							+ toteAuditService.getNumericLocation()
							+ Integer.toString(cd)));
				toteAuditService.setToteId("8000003229");
				assertEquals("8000003229", toteAuditService.getToteId());
				toteAuditService.addItem("93308000");
				toteAuditService.addItem("93607465");
				toteAuditService.addItem("93523208");
				assertEquals(MainConstants.THREE, 
					toteAuditService.getToteItemCount());
				assertEquals(true, toteAuditService.audit());
			} else {
				i = MainConstants.THREE;
			}
			i++;
		}
		
	}
	
	
	/**
	 * Test to connect to the server and possibly get or fake PI cycle.
	 */
	public final void testCompleteCycleWithDEC() {
		Integer uid = new Integer(MainConstants.SEVEN);
		
		/**
		 * Set up the user and unit id
		 */
		toteAuditService.setOperator("00669253");
		assertEquals("00669253", toteAuditService.getOperator());
		toteAuditService.setUnitId(uid);
		assertEquals(uid, toteAuditService.getUnitId());

		/**
		 * Log On
		 */
		StringUtils.log("Logging onto server");
		ls.logon(toteAuditService.getUnitId(), toteAuditService.getOperator(),
				"172.16.8.35", 4001, 1);
		/**
		 * Start Audit Activity
		 */
		StringUtils.log("Starting Tote Audit activity...");
		assertEquals(true, ls.startToteAudit());
		
		/*
		 * Request a location from start point 134001
		 */
		// Start by requesting an invalid location
		assertEquals(false, toteAuditService.locationRequest("01402473"));  
		// Now request a valid location
//		piCycle("01402476");   //YF075B
		piCycle("05008063");   //AA273B
//		piCycle("00988537");   //WE222D
		
		/**
		 * End Audit Activity
		 */
		
		StringUtils.log("Ending Tote Audit activity...");
		assertEquals(true, ls.stopToteAudit());
		
		/**
		 * Log off server
		 */
		StringUtils.log("Logging off...");
		assertEquals(true, ls.logoff());
		
		
		
	}
}
