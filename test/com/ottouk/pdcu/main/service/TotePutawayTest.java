package com.ottouk.pdcu.main.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import com.ottouk.pdcu.main.dao.Comms;
import com.ottouk.pdcu.main.domain.ToteBuildItem;
import com.ottouk.pdcu.main.domain.TotePutaway;
import com.ottouk.pdcu.main.utils.StringUtils;


/**
 * Uses JMock to stub responses from the Alpha.
 * 
 * @author dis065
 * 
 */
public class TotePutawayTest extends MockObjectTestCase {

	protected static Log logger = LogFactory.getLog("TestCase");

	private Mock mockComms;
	private Comms comms;
	private LogonService logonService;
	private TotePutawayService totePutawayService;

	private Integer unitId;
	private String operator;

	protected void setUp() throws Exception {
		super.setUp();

		logonService = new LogonServiceImpl();
		mockComms = mock(Comms.class);
		comms = (Comms) mockComms.proxy();
		((LogonServiceImpl) logonService).setComms(comms);

		unitId = new Integer(4);
		operator = "089903";

		logonService.setUnitId(unitId);
		logonService.setOperator(operator);

		totePutawayService = new TotePutawayServiceImpl();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNoFreeLocations() {

		String startLocation = "05220038"; // BB015A

		String request = buildLocationRequest(startLocation);
		String response = StringUtils.padField("ACKN", 16);
		mockTransaction(request, response);

		totePutawayService.locationRequest(startLocation);
		assertEquals("Return Code",
				TotePutawayService.RC_ERROR_NO_LOCATION_FOUND,
				totePutawayService.getReturnCode());
	}

	public void testPutawayTotesWithOnlyOneFreeLocationAvailable() {

		String startLocation = "05220038"; // BB015A
		String wrongLocation = "05221226"; // BB044A
		String correctLocation = "05226412";
		String correctAlphaLocation = "BB171A";
		String previouslyBuiltTote = "8000003229";

		String request = buildLocationRequest(startLocation);
		String response = "ACKY" + correctAlphaLocation
				+ correctLocation.substring(1, 7);
		mockTransaction(request, response);

		totePutawayService.locationRequest(startLocation);
		assertEquals("Alpha Location",
				formatAlphaLocation(correctAlphaLocation), totePutawayService
						.getAlphaLocation());

		totePutawayService.validatePutawayTote(previouslyBuiltTote);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		totePutawayService.putaway(wrongLocation);
		assertEquals("Return Code", TotePutawayService.RC_ERROR_WRONG_LOCATION,
				totePutawayService.getReturnCode());

		request = buildPutaway(previouslyBuiltTote, correctLocation);
		response = StringUtils.padField("ACKYN", 17);
		mockTransaction(request, response);

		totePutawayService.putaway(correctLocation);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());
	}

	public void testPutawayThreeTotesButOneIsUnknownToServerAndOnlyTwoFreeLocations() {

		String startLocation = "05227631"; // BB206A
		String freeLocation1 = "05247295";
		String freeAlphaLocation1 = "BC191C";
		String freeLocation2 = "05247257";
		String freeAlphaLocation2 = "BC191B";
		String previouslyBuiltTote1 = "8000003229";
		String previouslyBuiltTote2 = "8000984504";
		String unknownTote = "8000835530";

		String request = buildLocationRequest(startLocation);
		String response = "ACKY" + freeAlphaLocation1
				+ freeLocation1.substring(1, 7);
		mockTransaction(request, response);

		totePutawayService.locationRequest(startLocation);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());
		assertEquals("Alpha location", formatAlphaLocation(freeAlphaLocation1),
				totePutawayService.getAlphaLocation());

		totePutawayService.validatePutawayTote(previouslyBuiltTote1);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		request = buildPutaway(previouslyBuiltTote1, freeLocation1);
		response = "ACKYY" + freeAlphaLocation2 + freeLocation2.substring(1, 7);
		mockTransaction(request, response);

		totePutawayService.putaway(freeLocation1);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());
		assertTrue("Location follows", totePutawayService.locationFollows());
		assertEquals("Alpha location", formatAlphaLocation(freeAlphaLocation2),
				totePutawayService.getAlphaLocation());

		totePutawayService.validatePutawayTote(unknownTote);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		request = buildPutaway(unknownTote, freeLocation2);
		response = "ACK0N";
		mockTransaction(request, response);

		totePutawayService.putaway(freeLocation2);
		assertEquals("Return Code", TotePutawayService.RC_ERROR_TOTE_NOT_FOUND,
				totePutawayService.getReturnCode());
		assertEquals("Alpha location", formatAlphaLocation(freeAlphaLocation2),
				totePutawayService.getAlphaLocation());

		totePutawayService.validatePutawayTote(previouslyBuiltTote2);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		request = buildPutaway(previouslyBuiltTote2, freeLocation2);
		response = "ACKYN";
		mockTransaction(request, response);

		totePutawayService.putaway(freeLocation2);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());
		assertFalse("Location follows", totePutawayService.locationFollows());
	}

	public void testTotePutawayToALocationThatIsFullAndUseThePIFunction() {

		String startLocation = "05227631"; // BB206A
		String freeLocation1 = "05247295";
		String freeAlphaLocation1 = "BC191C";
		String freeLocation2 = "05247257";
		String freeAlphaLocation2 = "BC191B";
		String invalidTote = "7000398509";
		String validTote = "8000398509";
		String[] items = { "15243655", "15604937", "15598160", "15471871" };
		String invalidLengthItem = "1524365";
		String invalidCheckDigitItem = "15243656";
		String invalidCharacterItem = "1524a656";

		String request = buildLocationRequest(startLocation);
		String response = "ACKY" + freeAlphaLocation1
				+ freeLocation1.substring(1, 7);
		mockTransaction(request, response);

		totePutawayService.locationRequest(startLocation);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());
		assertEquals("Alpha location", formatAlphaLocation(freeAlphaLocation1),
				totePutawayService.getAlphaLocation());

		totePutawayService.validatePILocation(freeLocation2);
		assertEquals("Return Code",
				TotePutawayService.RC_ERROR_LOCATION_NOT_VALID,
				totePutawayService.getReturnCode());

		totePutawayService.validatePILocation(freeLocation1);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		totePutawayService.validatePITote(invalidTote);
		assertEquals("Return Code", TotePutawayService.RC_ERROR_INVALID_TOTE,
				totePutawayService.getReturnCode());

		totePutawayService.validatePITote(validTote);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		int i = 0;
		for (; i < items.length; i++) {
			totePutawayService.addItem((String) items[i]);
			assertEquals("Return Code", TotePutawayService.RC_OK,
					totePutawayService.getReturnCode());
			assertEquals("Tote Item Count", (i + 1), totePutawayService
					.getToteItemCount());
		}

		totePutawayService.addItem(invalidLengthItem);
		assertEquals("Return Code", TotePutawayService.RC_ERROR_INVALID_ITEM,
				totePutawayService.getReturnCode());
		assertEquals("Tote Item Count", i, totePutawayService
				.getToteItemCount());

		totePutawayService.addItem(invalidCheckDigitItem);
		assertEquals("Return Code", TotePutawayService.RC_ERROR_INVALID_ITEM,
				totePutawayService.getReturnCode());
		assertEquals("Tote Item Count", i, totePutawayService
				.getToteItemCount());

		totePutawayService.addItem(invalidCharacterItem);
		assertEquals("Return Code", TotePutawayService.RC_ERROR_INVALID_ITEM,
				totePutawayService.getReturnCode());
		assertEquals("Tote Item Count", i, totePutawayService
				.getToteItemCount());

		request = buildPIRequest(validTote, freeLocation1, items);
		response = "ACK";
		mockTransaction(request, response);

		totePutawayService.piRequest();
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());

		request = buildLocationRequest(startLocation);
		response = "ACKY" + freeAlphaLocation2 + freeLocation2.substring(1, 7);
		mockTransaction(request, response);

		totePutawayService.locationRequest(startLocation);
		assertEquals("Return Code", TotePutawayService.RC_OK,
				totePutawayService.getReturnCode());
		assertEquals("Alpha location", formatAlphaLocation(freeAlphaLocation2),
				totePutawayService.getAlphaLocation());
	}

	private String formatAlphaLocation(String alphaLocation) {
		return (alphaLocation.substring(0, 4) + "-" + alphaLocation
				.substring(4));
	}

	private String buildLocationRequest(String location) {

		TotePutaway totePutaway = new TotePutaway();
		totePutaway
				.setMessageId(TotePutawayService.TOTE_PUTAWAY_LOCATION_REQUEST_MESSAGE_ID);
		totePutaway.setUnitId(unitId);
		totePutaway.setOperator(operator);
		totePutaway.setLocation(location);
		totePutaway
				.setStatus(TotePutawayService.TOTE_PUTAWAY_LOCATION_REQUEST_STATUS);

		return totePutaway.buildLocationRequest();
	}

	private String buildPutaway(String toteId, String location) {

		TotePutaway totePutaway = new TotePutaway();
		totePutaway
				.setMessageId(TotePutawayService.TOTE_PUTAWAY_SUCCESSFUL_MESSAGE_ID);
		totePutaway.setUnitId(unitId);
		totePutaway.setOperator(operator);
		totePutaway.setLocation(location);
		totePutaway.setToteId(toteId);

		return totePutaway.buildPutaway();
	}

	private String buildPIRequest(String toteId, String location, String[] items) {

		TotePutaway totePutaway = new TotePutaway();
		totePutaway.setMessageId(TotePutawayService.TOTE_PUTAWAY_PI_MESSAGE_ID);
		totePutaway.setUnitId(unitId);
		totePutaway.setOperator(operator);
		totePutaway.setLocation(location);
		totePutaway.setToteId(toteId);

		List toteBuildItems = new ArrayList();
		ToteBuildItem toteBuildItem;
		for (int i = 0; i < items.length; i++) {
			toteBuildItem = new ToteBuildItem(items[i],
					TotePutawayService.TOTE_PUTAWAY_PI_EAN_INDICATOR);
			toteBuildItems.add(toteBuildItem);
		}
		totePutaway.setToteBuildItems(toteBuildItems);

		return totePutaway.buildPIRequest();
	}

	private void mockTransaction(String request, String response) {

		logger.info("request : " + request);
		logger.info("response: " + response);

		mockComms.stubs().method("transact").with(eq(request)).will(
				returnValue(true));

		mockComms.stubs().method("responseStartsWith").with(eq("ACK")).will(
				returnValue(response.startsWith("ACK") ? true : false));

		mockComms.stubs().method("getErrorMessage").will(returnValue(""));

		mockComms.stubs().method("getResponse").will(returnValue(response));
	}

}
