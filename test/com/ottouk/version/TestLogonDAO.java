package com.ottouk.version;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ottouk.pdcu.version.dao.VersionDAO;
import com.ottouk.pdcu.version.service.VersionMgr;

public class TestLogonDAO extends TestCase {

	protected static Log logger = LogFactory.getLog("TestCase");
	private VersionDAO vDAO;
	private VersionMgr vMgr;

	protected void setUp() throws Exception {
		super.setUp();
		vMgr = new VersionMgr();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		//   assertEquals("Disconnection", true, );
	}

	public void testLogon() {
		assertEquals("Up to date Logon", vMgr.checkLogonVersionsOK(), true);

		vMgr.invokeLogon();
	}
}
