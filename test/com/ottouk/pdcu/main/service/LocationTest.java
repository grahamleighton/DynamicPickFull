package com.ottouk.pdcu.main.service;

import junit.framework.TestCase;

public class LocationTest extends TestCase {
	
	private LocationImpl l;
	

	public LocationTest(String name) {
		super(name);
		l = new LocationImpl();
	}

	protected void setUp() throws Exception {
		super.setUp();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testGetAlphaLocation() {
		l.setLocation("AA134D", "134001");
		assertEquals("AA134D", l.getAlphaLocation());
	}

	public final void testGetDisplayLocation() {
		l.setLocation("AA134D", "134001");
		assertEquals("AA13-4D", l.getDisplayLocation());
		
	}

	public final void testGetNumericLocation() {
		l.setLocation("AA134D", "134001");
		assertEquals("134001", l.getNumericLocation());
		
		
	}

	public final void testMatchLocation() {
		l.setLocation("AA134D", "134001");
		assertEquals(true, l.matchLocation("134001"));
		assertEquals(false, l.matchLocation("134002"));
		assertEquals(true, l.matchLocation("01340013"));
		assertEquals(false, l.matchLocation("01340023"));
		assertEquals(false, l.matchLocation("01340"));
		
	}

	public final void testSetLocation() {
		l.setLocation("AA134D", "135789");
		assertEquals("AA134D", l.getAlphaLocation());
		assertEquals("135789", l.getNumericLocation());
	}

	public final void testSetLocationAlphaNum() {
		l.setLocationAlphaNum("AA134D135789");
		assertEquals("AA134D", l.getAlphaLocation());
		assertEquals("135789", l.getNumericLocation());
		l.setLocationAlphaNum("AA134D135789000");
		assertEquals("AA134D", l.getAlphaLocation());
		assertEquals("135789", l.getNumericLocation());
		l.setLocationAlphaNum("AA134D1357");
		assertEquals("", l.getAlphaLocation());
		assertEquals("", l.getNumericLocation());
		
	}

	public final void testValidCheckDigit() {
		assertEquals(true, l.validCheckDigit("01340013"));
		assertEquals(false, l.validCheckDigit("01340014"));
	}
	
	public final void testisLocationAvailable() {
		l.setLocation("AA134D", "134001");
		assertEquals(true, l.isLocationAvailable());
		l.setLocation("", "");
		assertEquals(false, l.isLocationAvailable());
	}
	public final void testextractLocation() {
		assertEquals("134001", l.extractLocation("01340013"));
		assertEquals("134001", l.extractLocation("134001"));
		
	}
	public final void testgetLastLocationAlpha() {
		l.setLocationAlphaNum("AA134D135789");
		assertEquals("AA134D", l.getAlphaLocation());
		assertEquals("135789", l.getNumericLocation());
		assertEquals("AA13-4D", l.getLastLocationAlpha(true));
		assertEquals("AA134D", l.getLastLocationAlpha(false));
		assertEquals("135789", l.getLastLocationNumeric());
		l.setLocation("YC064D", "134001");
		assertEquals("YC064D", l.getAlphaLocation());
		assertEquals("134001", l.getNumericLocation());
		
		assertEquals("AA13-4D", l.getLastLocationAlpha(true));
		assertEquals("AA134D", l.getLastLocationAlpha(false));
		assertEquals("135789", l.getLastLocationNumeric());
	}
	public final void testisMatchToLastLocation() {
		l.setLocationAlphaNum("AA134D135789");
		assertEquals("AA134D", l.getAlphaLocation());
		assertEquals("135789", l.getNumericLocation());
		assertEquals("AA13-4D", l.getLastLocationAlpha(true));
		assertEquals("135789", l.getLastLocationNumeric());
		
		assertEquals(true, l.isMatchToLastLocation("135789"));
		assertEquals(false, l.isMatchToLastLocation("135788"));
		
		l.setLocation("YC064D", "134001");
		assertEquals("YC064D", l.getAlphaLocation());
		assertEquals("134001", l.getNumericLocation());
		
		assertEquals("AA13-4D", l.getLastLocationAlpha(true));
		assertEquals("135789", l.getLastLocationNumeric());

		assertEquals(false, l.isMatchToLastLocation("134001"));
		assertEquals(true, l.isMatchToLastLocation("135789"));
	}

}
