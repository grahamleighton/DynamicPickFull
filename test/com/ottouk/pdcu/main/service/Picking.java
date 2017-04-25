package com.ottouk.pdcu.main.service;

import junit.framework.TestCase;

public class Picking extends TestCase {

	private PickingService ps;
	private LogonService ls;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		ps = new PickingServiceImpl();
		ls = new LogonServiceImpl();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
	public void testSimpleStartEndWalk() {
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.addWalkNo("1234567810") );
		assertEquals("1234567810",ps.getWalk());
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.endWalkNo("1234567890") );
		assertEquals( "", ps.getWalk());
	}
	public void testSimpleStartWithEndWalk() {
		assertEquals( PickingService.RC_WARN_NOT_A_START_WALK,  ps.addWalkNo("1234567890") );
		
	}
	public void testSimpleWalk() {
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.addWalkNo("0807011910") );
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.endWalkNo("0807011990") );
		
	}
	public void testSimpleStartWrongEndWalk() {
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.addWalkNo("1234567810") );
		assertEquals("1234567810",ps.getWalk());
		assertEquals( PickingService.RC_WARN_WALK_ALREADY_STARTED,  ps.endWalkNo("1234567810") );
		assertEquals("1234567810",ps.getWalk());
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.endWalkNo("1234567890") );
		assertEquals( "", ps.getWalk());
	}
	public void testSimpleStartMultipleWalks() {
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.addWalkNo("1234567810") );
		assertEquals("1234567810",ps.getWalk());
		assertEquals( PickingService.RC_WARN_WALK_NOT_STARTED,  ps.endWalkNo("1234567710") );
		assertEquals("1234567810",ps.getWalk());
		assertEquals( PickingService.RC_WARN_WALK_NOT_STARTED,  ps.endWalkNo("1234567790") );
		assertEquals("1234567810",ps.getWalk());
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.endWalkNo("1234567890") );
	}
	public void testProcessWalkScan() {
		assertEquals(PickingService.RC_WALK_ADDED_OK,ps.processWalkScan("1234567810"));
		assertEquals(PickingService.RC_WARN_WALK_NOT_STARTED,ps.processWalkScan("1234567710"));
		assertEquals(PickingService.RC_WALK_ADDED_OK,ps.processWalkScan("1234567890"));
	}
	public void testValidWalkScan() {
		assertEquals(true,ps.validWalkScan("1234567810"));
		assertEquals(false,ps.validWalkScan("1234567820"));
		assertEquals(false,ps.validWalkScan("1234567"));
		assertEquals(false,ps.validWalkScan("  "));
		assertEquals(false,ps.validWalkScan("          "));

		assertEquals(false,ps.validWalkScan("1234567800"));
		assertEquals(false,ps.validWalkScan("1234567820"));
		assertEquals(false,ps.validWalkScan("1234567830"));
		assertEquals(false,ps.validWalkScan("1234567840"));
		assertEquals(false,ps.validWalkScan("1234567850"));
		assertEquals(false,ps.validWalkScan("1234567860"));
		assertEquals(false,ps.validWalkScan("1234567870"));
		assertEquals(false,ps.validWalkScan("1234567880"));
		assertEquals(true,ps.validWalkScan("1234567890"));
	}
	public void testErrorMessages() {
		assertEquals("Not a Valid Start Walk", ps.getErrorMsg(PickingService.RC_WARN_NOT_A_START_WALK));
		assertEquals("Walk Already Started", ps.getErrorMsg(PickingService.RC_WARN_WALK_ALREADY_STARTED));
		assertEquals("Walk Not Started", ps.getErrorMsg(PickingService.RC_WARN_WALK_NOT_STARTED));
		assertEquals("Unknown Error", ps.getErrorMsg(0));
	}
	
	public void testFullWalkCycleWithDEC()
	{
		Integer uid = new Integer(4);
		
		/**
		 * Set up the user and unit id
		 */
		String msg="";
		ps.setOperator("00669253");
		assertEquals("00669253",ps.getOperator());
		ps.setUnitId(uid);
		assertEquals(uid,ps.getUnitId());
		
		/**
		 * Log On
		 */
		ls.logon(ps.getUnitId(), ps.getOperator(),
				"172.16.8.35", 4001, 1);
		
		/**
		 * Start Picking Activity
		 */
		assertEquals(true,ls.startPickingWalk());		
		/**
		 * Start a walk , build the DEC Alpha Message 
		 * transmit it then validate response
		 */
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.addWalkNo("1234567810") );
		assertEquals("1234567810",ps.getWalk());
		msg = ps.buildMessage('1');
		System.out.println(msg);
		assertEquals("E000400669253123456781",msg);
		
		assertEquals( true,ps.sendMessage(msg));

		/**
		 * End the walk , build the DEC Alpha Message 
		 * transmit it then validate response
		 */
		assertEquals( PickingService.RC_WALK_ADDED_OK,  ps.endWalkNo("1234567890") );
		assertEquals("",ps.getWalk());

		msg = ps.buildMessage("12345678", 'r');
		System.out.println(msg);
		assertEquals("E00040066925312345678r",msg);
		assertEquals( true,ps.sendMessage(msg));

		/**
		 * Send the end picking activity and log off 
		 * msgs, validating the responses
		 */
		assertEquals(true,ls.stopPickingWalk());		
		assertEquals(true,ls.logoff());
	}
	

}
