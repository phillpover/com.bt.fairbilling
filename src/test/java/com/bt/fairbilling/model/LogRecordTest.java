package com.bt.fairbilling.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.constants.StartEnd;
import com.bt.fairbilling.exception.FairBillingException;
import com.bt.fairbilling.util.InstantUtil;

public class LogRecordTest {
	
	private final static String customerName = "ALICE99";
	private final static StartEnd startEnd = StartEnd.START;
	private final static String timeString = "14:02:03";
	private Instant time;
	private final static String workingLogLine = "14:02:03 ALICE99 Start";
	private final static String notEnoughPartsLogLine = "14:02:03";
	private final static String incorrectTimeFormatLogLine = "140203 ALICE99 Start";
	private final static String noStartEndLogLine = "14:02:03 ALICE99 ";
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void createLogRecord_WhenNoError_ThenCreatesLogRecord() throws FairBillingException {
		time = InstantUtil.getInstantFromTime(timeString);
		
		LogRecord result = new LogRecord(workingLogLine);
		
		assertThat(result.getCustomerName(), equalTo(customerName));
		assertThat(result.getStartEnd(), equalTo(startEnd));
		assertThat(result.getTime(), equalTo(time));
	}
	
	@Test
	public void createLogRecord_WhenLogLineIncomplete_ThenThrowsFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_ENTRY);
		
		new LogRecord(notEnoughPartsLogLine);
	}
	
	@Test
	public void createLogRecord_WhenTimeIncorrectlyFormatted_ThenThrowsFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_ENTRY);
		
		new LogRecord(incorrectTimeFormatLogLine);
	}
	
	@Test
	public void createLogRecord_WhenNoStartEndIdentifyer_ThenThrowsFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_ENTRY);
		
		new LogRecord(noStartEndLogLine);
	}
}
