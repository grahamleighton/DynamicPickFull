package com.ottouk.pdcu.main.dao;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

public class ThrouhputCommsTest {

    public static Test suite() {

        int maxUsers = 10;
        long maxElapsedTime = 5000;
        Test testCase = new CommsTest("testLogonandOff");
        Test loadTest = new LoadTest(testCase, maxUsers);
        Test timedTest = new TimedTest(loadTest, maxElapsedTime);

        return timedTest;
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        }
    
}
