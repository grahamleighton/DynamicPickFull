package com.ottouk.pdcu.main.service;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

public class ThrouhputToteBuildTest {

    public static Test suite() {

        int maxUsers = 15;
        long maxElapsedTime = 50000;
        Test testCase = new ToteBuildTest("test2");
        Test loadTest = new LoadTest(testCase, maxUsers);
        Test timedTest = new TimedTest(loadTest, maxElapsedTime);

        return timedTest;
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        }
    
}
