package com.ottouk.pdcu.main.dao;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;

public class LoadCommsTest {
	public static Test suite() {

		int users = 45;
		Test testCase = new CommsTest("testLogonandOff");
		Test loadTest = new LoadTest(testCase, users);

		return loadTest;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
