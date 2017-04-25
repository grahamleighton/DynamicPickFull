package com.ottouk.version;

import junit.framework.Test;
import com.clarkware.junitperf.TimedTest;

public class PDCULogonTimedTest {

	public static Test suite() {

		long maxElapsedTime = 1000;
		Test testCase = new TestLogonController("testValidLogon");
		Test timedTest = new TimedTest(testCase, maxElapsedTime);

		return timedTest;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
