package com.ottouk.pdcu.main.service;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;

public class LoadToteBuildTest {

	public static Test suite() {

		int users = 1;
		Test testCase = new ToteBuildTest("test2");
		Test loadTest = new LoadTest(testCase, users);

		return loadTest;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
}
