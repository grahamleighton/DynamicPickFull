package com.ottouk.pdcu.main.dao;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ottouk.pdcu.main.domain.Logon;

public class CommsTest extends TestCase {

	protected static Log logger = LogFactory.getLog("TestCase");
	private Comms comms;
	private Logon logon;

	public CommsTest(String s) {
		super(s);
	}

	protected void setUp() throws Exception {
		super.setUp();

		String ipAddress = "172.16.8.35";

		int basePort = 4000;
		int channels = 10;
		Integer unitId = new Integer((int)(Math.random()*100));
		//Integer unitId = new Integer(9);

		logger.info("Unit Id: " + unitId);

		comms = new CommsImpl();
		assertEquals("Connection", true, comms.connect(ipAddress, basePort,
				channels, unitId));

		logon = new Logon();
		logon.setMessageId("H");
		logon.setUnitId(unitId);
		logon.setOperator("00089903");
		logon.setVersion("J");
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		assertEquals("Disconnection", true, comms.disconnect());
	}

	public void testLogonandOff() {

		logon.setAction("0");

		String message = logon.buildLogon();
		logger.info("Message : " + message);
		assertTrue("Transaction", comms.transact(message));

		String response = comms.getResponse();
		logger.info("Response: " + response);
		assertTrue("Response", comms.responseStartsWith("ACK"));
		assertTrue("Error", comms.getErrorMessage() == "");

		logon.setAction("r");

		message = logon.buildLogon();
		logger.info("Message : " + message);
		assertTrue("Transaction", comms.transact(message));

		response = comms.getResponse();
		logger.info("Response: " + response);
		assertTrue("Response", response.startsWith("ACK"));
		assertTrue("Error", comms.getErrorMessage() == "");
	}

}
