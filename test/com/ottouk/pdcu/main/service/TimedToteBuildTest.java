package com.ottouk.pdcu.main.service;

import junit.framework.Test;

import com.clarkware.junitperf.TimedTest;

public class TimedToteBuildTest {

    public static Test suite() {
        
        long maxElapsedTime = 3000;
        Test testCase = new ToteBuildTest("test2");
        Test timedTest = new TimedTest(testCase, maxElapsedTime);

        return timedTest;
    }
    
public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
    }
 

}
