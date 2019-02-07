package com.bt.fairbilling.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.bt.fairbilling.constants.StartEnd;
import com.bt.fairbilling.model.Customer;
import com.bt.fairbilling.model.LogRecord;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListUtil.class)
public class CustomerUtilTest {
	
	private String customerName1 = "CUSTOMER_NAME_ONE";
	private String customerName2 = "CUSTOMER_NAME_TWO";
	
	@Mock
	private LogRecord mockLogRecord1;
	
	@Mock
	private LogRecord mockLogRecord2;
	
	@Before
	public void setUp() {
		initMocks(this);
		mockStatic(ListUtil.class);
	}

	@Test
	public void getCustomerListFromLogRecords_WhenLogRecordsEmpty_ThenReturnsEmptyCustomerMap() {
		Map<String, Customer> result = CustomerUtil.getCustomerListFromLogRecords(new ArrayList<LogRecord>(), Instant.now(), Instant.now());
		
		assertThat(result.size(), equalTo(0));
	}
	
	@Test
	public void getCustomerListFromLogRecords_WhenLogRecordsHasOneValidRecord_ThenReturnsCustomerMapWithOneCustomer() {
		List<LogRecord> logRecords = new ArrayList<>();
		logRecords.add(mockLogRecord1);
		
		when(mockLogRecord1.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord1.getCustomerName()).thenReturn(customerName1);
		
		Map<String, Customer> result = CustomerUtil.getCustomerListFromLogRecords(logRecords, Instant.now(), Instant.now());
		
		assertThat(result.get(customerName1), instanceOf(Customer.class));
	}
	
	@Test
	public void getCustomerListFromLogRecords_WhenLogRecordsHasTwoValidStartRecordsForSameCustomer_ThenReturnsCustomerMapWithOneCustomer() {
		List<LogRecord> logRecords = new ArrayList<>();
		logRecords.add(mockLogRecord1);
		logRecords.add(mockLogRecord2);
		
		when(mockLogRecord1.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord1.getCustomerName()).thenReturn(customerName1);
		
		when(mockLogRecord2.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord2.getCustomerName()).thenReturn(customerName1);
		
		Map<String, Customer> result = CustomerUtil.getCustomerListFromLogRecords(logRecords, Instant.now(), Instant.now());
		
		assertThat(result.get(customerName1), instanceOf(Customer.class));
		assertThat(result.size(), equalTo(1));
	}
	
	@Test
	public void getCustomerListFromLogRecords_WhenLogRecordsHasTwoValidStartRecordsForDifferentCustomers_ThenReturnsCustomerMapWithTwoCustomers() {
		List<LogRecord> logRecords = new ArrayList<>();
		logRecords.add(mockLogRecord1);
		logRecords.add(mockLogRecord2);
		
		when(mockLogRecord1.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord1.getCustomerName()).thenReturn(customerName1);
		
		when(mockLogRecord2.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord2.getCustomerName()).thenReturn(customerName2);
		
		Map<String, Customer> result = CustomerUtil.getCustomerListFromLogRecords(logRecords, Instant.now(), Instant.now());
		
		assertThat(result.get(customerName1), instanceOf(Customer.class));
		assertThat(result.get(customerName2), instanceOf(Customer.class));
		assertThat(result.size(), equalTo(2));
	}
	
	@Test
	public void getCustomerListFromLogRecords_WhenLogRecordsHasAValidStartRecordAndEndRecordForOneCustomer_ThenReturnsCustomerMapWithOneCustomer() {
		List<LogRecord> logRecords = new ArrayList<>();
		logRecords.add(mockLogRecord1);
		logRecords.add(mockLogRecord2);
		
		List<LogRecord> logRecordsReversed = new ArrayList<>();
		logRecordsReversed.add(mockLogRecord2);
		logRecordsReversed.add(mockLogRecord1);
		
		when(mockLogRecord1.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord1.getCustomerName()).thenReturn(customerName1);
		when(mockLogRecord1.getTime()).thenReturn(Instant.now());
		
		when(mockLogRecord2.getStartEnd()).thenReturn(StartEnd.END);
		when(mockLogRecord2.getCustomerName()).thenReturn(customerName1);
		when(mockLogRecord2.getTime()).thenReturn(Instant.now());
		
		when(ListUtil.reverseList(logRecords)).thenReturn(logRecordsReversed);
		
		Map<String, Customer> result = CustomerUtil.getCustomerListFromLogRecords(logRecords, Instant.now(), Instant.now());
		
		assertThat(result.get(customerName1), instanceOf(Customer.class));
		assertThat(result.size(), equalTo(1));
	}
	
	@Test
	public void getCustomerListFromLogRecords_WhenLogRecordsHasAValidStartRecordForCustomer1AndEndRecordForCustomer2_ThenReturnsCustomerMapWithTwoCustomers() {
		List<LogRecord> logRecords = new ArrayList<>();
		logRecords.add(mockLogRecord1);
		logRecords.add(mockLogRecord2);
		
		List<LogRecord> logRecordsReversed = new ArrayList<>();
		logRecordsReversed.add(mockLogRecord2);
		logRecordsReversed.add(mockLogRecord1);
		
		when(mockLogRecord1.getStartEnd()).thenReturn(StartEnd.START);
		when(mockLogRecord1.getCustomerName()).thenReturn(customerName1);
		when(mockLogRecord1.getTime()).thenReturn(Instant.now());
		
		when(mockLogRecord2.getStartEnd()).thenReturn(StartEnd.END);
		when(mockLogRecord2.getCustomerName()).thenReturn(customerName2);
		when(mockLogRecord2.getTime()).thenReturn(Instant.now());
		
		when(ListUtil.reverseList(logRecords)).thenReturn(logRecordsReversed);
		
		Map<String, Customer> result = CustomerUtil.getCustomerListFromLogRecords(logRecords, Instant.now(), Instant.now());
		
		assertThat(result.get(customerName1), instanceOf(Customer.class));
		assertThat(result.get(customerName2), instanceOf(Customer.class));
		assertThat(result.size(), equalTo(2));
	}
	
}
