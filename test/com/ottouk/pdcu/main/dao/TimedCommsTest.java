package com.ottouk.pdcu.main.dao;

import junit.framework.Test;

import com.clarkware.junitperf.TimedTest;

public class TimedCommsTest {

    public static Test suite() {
        
        long maxElapsedTime = 2000;
        Test testCase = new CommsTest("testLogonandOff");
        Test timedTest = new TimedTest(testCase, maxElapsedTime);

        return timedTest;
    }
    
public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
    }
 

}
