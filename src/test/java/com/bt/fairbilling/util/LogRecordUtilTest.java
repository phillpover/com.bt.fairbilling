package com.bt.fairbilling.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.exception.FairBillingException;
import com.bt.fairbilling.model.LogRecord;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtil.class)
public class LogRecordUtilTest {
	
	private final static String filePath = "/path/to/file";
	private final static String logLineOne = "14:02:03 ALICE99 Start";
	private final static String logLineTwo = "14:02:34 ALICE99 End";
	private final static String logLineThree = "14:03:02 CHARLIE Start";
	private final static String brokenLogLine = "BROKEN_LOG_LINE";
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() {
		initMocks(this);
		mockStatic(FileUtil.class);
	}

	@Test
	public void getLogRecordsFromFilePath_WhenExceptionGettingLogLines_ThenReturnsFairBillingException() throws FairBillingException {
		when(FileUtil.getLinesFromFile("")).thenThrow(new FairBillingException(MessageKeys.ERROR_PROCESSING_LOG_FILE));
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_FILE);
	    
		LogRecordUtil.getLogRecordsFromFilePath("");
	}
	
	@Test
	public void getLogRecordsFromFilePath_WhenNoLogLinesReturned_ThenReturnsEmptyList() throws FairBillingException {
		when(FileUtil.getLinesFromFile(filePath)).thenReturn(new ArrayList<String>());
		
		List<LogRecord> result = LogRecordUtil.getLogRecordsFromFilePath(filePath);
		
		assertThat(result, IsEmptyCollection.empty());
	}
	
	@Test
	public void getLogRecordsFromFilePath_WhenExceptionCreatingLogRecord_ThenDoesNotAddLogRecord() throws FairBillingException {
		List<String> logLines = new ArrayList<>();
		logLines.add(brokenLogLine);
		
		when(FileUtil.getLinesFromFile(filePath)).thenReturn(logLines);
		
		List<LogRecord> result = LogRecordUtil.getLogRecordsFromFilePath(filePath);
		
		assertThat(result, IsEmptyCollection.empty());
	}
	
	@Test
	public void getLogRecordsFromFilePath_WhenExceptionCreatingLogRecord_ThenAddsOtherLogRecords() throws FairBillingException {
		List<String> logLines = new ArrayList<>();
		logLines.add(logLineOne);
		logLines.add(brokenLogLine);
		logLines.add(logLineTwo);
		
		when(FileUtil.getLinesFromFile(filePath)).thenReturn(logLines);
		
		List<LogRecord> result = LogRecordUtil.getLogRecordsFromFilePath(filePath);
		
		assertThat(result.size(), equalTo(2));
	}
	
	@Test
	public void getLogRecordsFromFilePath_WhenOneLogLineReturned_ThenReturnsListWithOneLogRecord() throws FairBillingException {
		List<String> logLines = new ArrayList<>();
		logLines.add(logLineOne);
		
		when(FileUtil.getLinesFromFile(filePath)).thenReturn(logLines);
		
		List<LogRecord> result = LogRecordUtil.getLogRecordsFromFilePath(filePath);
		
		assertThat(result.size(), equalTo(1));
	}
	
	@Test
	public void getLogRecordsFromFilePath_WhenMultipleLogLinesReturned_ThenReturnsListWithAllLogRecord() throws FairBillingException {
		List<String> logLines = new ArrayList<>();
		logLines.add(logLineOne);
		logLines.add(logLineTwo);
		logLines.add(logLineThree);
		
		when(FileUtil.getLinesFromFile(filePath)).thenReturn(logLines);
		
		List<LogRecord> result = LogRecordUtil.getLogRecordsFromFilePath(filePath);
		
		assertThat(result.size(), equalTo(3));
	}
}
