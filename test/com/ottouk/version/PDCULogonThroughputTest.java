package com.ottouk.version;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import junit.framework.Test;

public class PDCULogonThroughputTest {
    
    public static Test suite() {

        int maxUsers = 45;
        long maxElapsedTime = 5000;
        Test testCase = new TestLogonController("testValidLogon");
        Test loadTest = new LoadTest(testCase, maxUsers);
        Test timedTest = new TimedTest(loadTest, maxElapsedTime);

        return timedTest;
    }

}
