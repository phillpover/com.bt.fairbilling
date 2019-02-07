package com.bt.fairbilling;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.exception.FairBillingException;
import com.bt.fairbilling.model.Customer;
import com.bt.fairbilling.model.LogRecord;
import com.bt.fairbilling.util.ArgumentsUtil;
import com.bt.fairbilling.util.CustomerUtil;
import com.bt.fairbilling.util.LogRecordUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ArgumentsUtil.class, CustomerUtil.class, LogRecordUtil.class})
public class FairBillingTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	private final String arg = "./log.txt";
	private final String[] ARGS = { arg };
	private List<String> arguments = new ArrayList<>();
	
	private Instant instant1 = Instant.ofEpochSecond(1549460000);
	private Instant instant2 = Instant.ofEpochSecond(1549460100);
	
	private String customer1 = "CUSTOMER_ONE";
	private String customer2 = "CUSTOMER_TWO";
	
	private String customer1PrintOutput = "CUSTOMER_ONE_PRINT_OUTPUT";
	private String customer2PrintOutput = "CUSTOMER_TWO_PRINT_OUTPUT";
	
	private List<LogRecord> logRecords = new ArrayList<>();
	
	private Map<String, Customer> customers = new HashMap<>();
	
	@Mock
	private LogRecord mockLogRecord1;
	
	@Mock
	private LogRecord mockLogRecord2;
	
	@Mock
	private Customer mockCustomer1;
	
	@Mock
	private Customer mockCustomer2;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() {
		initMocks(this);
		mockStatic(ArgumentsUtil.class, CustomerUtil.class, LogRecordUtil.class);
		
		System.setOut(new PrintStream(outContent));
	}
	
	@Test
	public void main_WhenNoError_ThenProcessesArguments() throws FairBillingException {
		mockArgs();
		mockLogRecords();
		mockCustomers();
		
		FairBilling.main(ARGS);
		
		verifyStatic(ArgumentsUtil.class);
		ArgumentsUtil.getArguments(ARGS);
	}
	
	@Test
	public void main_WhenNoArguments_ThenThrowFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.PATH_TO_LOG_MISSING);
	    
		FairBilling.main(new String[] {""});
	}
	
	@Test
	public void main_WhenArgumentsHasValidFilePath_ThenGetLogRecordsFromFile() throws FairBillingException {
		mockArgs();
		mockLogRecords();
		mockCustomers();
		
		FairBilling.main(ARGS);
		
		verifyStatic(LogRecordUtil.class);
		LogRecordUtil.getLogRecordsFromFilePath("./log.txt");
	}
	
	@Test
	public void main_WhenArgumentsHasValidPathAndLogFileIsEmpty_ThenThrowFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.NO_ENTRIES_IN_LOG);
	    
		mockArgs();
		when(LogRecordUtil.getLogRecordsFromFilePath("./log.txt")).thenReturn(logRecords);
		
		FairBilling.main(ARGS);
	}
	
	@Test
	public void main_WhenArgumentsHasValidFilePathAndLogRecordsReturned_ThenGetCustomerListFromLogRecords() throws FairBillingException {
		mockArgs();
		mockLogRecords();
		mockCustomers();
		
		FairBilling.main(ARGS);
		
		verifyStatic(CustomerUtil.class);
		CustomerUtil.getCustomerListFromLogRecords(logRecords, instant1, instant2);
	}
	
	@Test
	public void main_WhenArgumentsHasValidFilePathAndLogRecordsReturnedAndNoCustomerRecordsReturned_ThenThrowFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.NO_VALID_ENTRIES);
	    
		mockArgs();
		mockLogRecords();
		
		FairBilling.main(ARGS);
		
		verifyStatic(CustomerUtil.class);
		CustomerUtil.getCustomerListFromLogRecords(logRecords, instant1, instant2);
	}
	
	@Test
	public void main_WhenArgumentsHasValidFilePathAndLogRecordsReturnedAndCustomersReturned_ThenPrintCustomersToStrings() throws FairBillingException {
		mockArgs();
		mockLogRecords();
		mockCustomers();
		
		when(mockCustomer1.printOutput()).thenReturn(customer1PrintOutput);
		when(mockCustomer2.printOutput()).thenReturn(customer2PrintOutput);
		
		FairBilling.main(ARGS);
		
		assertThat(outContent.toString(), containsString(customer1PrintOutput));
		assertThat(outContent.toString(), containsString(customer2PrintOutput));
	}
	
	private void mockArgs() {
		when(ArgumentsUtil.getArguments(ARGS)).thenReturn(arguments);
		arguments.add(arg);
	}
	
	private void mockLogRecords() throws FairBillingException {
		logRecords.add(mockLogRecord1);
		when(mockLogRecord1.getTime()).thenReturn(instant1);
		logRecords.add(mockLogRecord2);
		when(mockLogRecord2.getTime()).thenReturn(instant2);
		when(LogRecordUtil.getLogRecordsFromFilePath("./log.txt")).thenReturn(logRecords);
	}
	
	private void mockCustomers() {
		customers.put(customer1, mockCustomer1);
		customers.put(customer2, mockCustomer2);
		when(CustomerUtil.getCustomerListFromLogRecords(logRecords, instant1, instant2)).thenReturn(customers);
	}

}
