package com.ottouk.version;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ottouk.pdcu.version.dao.VersionDAO;
import com.ottouk.pdcu.version.domain.Login;
import com.ottouk.pdcu.version.service.LogonController;
import com.ottouk.pdcu.version.service.VersionMgr;

import junit.framework.TestCase;

public class TestLogonController extends TestCase {

    private Login login; 
    private LogonController controller;

    protected static Log logger = LogFactory.getLog("TestCase");

    public TestLogonController( String s )
    {
        super( s );
    }

    protected void setUp() throws Exception {
            super.setUp();
            // Delete resource file to ensure full ftp download.

            File delFile = new File("resources/version.txt");
            delFile.delete();
            controller = new LogonController();
    }

    protected void tearDown() throws Exception {
            super.tearDown();
    }

    public void testInvalidUserId() {
        for (int i=0; i< 500000000; i++);
        login = controller.onLogin("12345678");
        assertEquals(" Greater than 8 chars",login.isValidCredential(),false);
        assertEquals(login.getErrorMsg().equalsIgnoreCase(" Greater than 8 chars"),false);
        login = controller.onLogin("123456");
        assertEquals(" Less than 8 chars",login.isValidCredential(),false);
        login = controller.onLogin("12s56789");
        assertEquals("Non Numeric",login.isValidCredential(),false);
    }

    public void testValidLogon() {
        login = controller.onLogin("12345678");
        //controller.doSuccess();
        assertEquals(" Greater than 8 chars",login.isValidCredential(),false);
        login = controller.onLogin("00345678");
        //controller.doSuccess();
        assertEquals(" Greater than 8 chars",login.isValidCredential(),false);
    }

}
