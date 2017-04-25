package com.ottouk.version;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;

public class PDCULogonLoadTest {

	public static Test suite() {

		int users = 10;
		Test testCase = new TestLogonController("testValidLogon");
		Test loadTest = new LoadTest(testCase, users);

		return loadTest;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
}
