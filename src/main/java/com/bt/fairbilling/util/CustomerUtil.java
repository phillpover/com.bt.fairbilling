package com.bt.fairbilling.util;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bt.fairbilling.constants.StartEnd;
import com.bt.fairbilling.model.Customer;
import com.bt.fairbilling.model.LogRecord;

/**
 * Utility Class to process the LogRecords from the input log into Customers and Sessions.
 * It performs two separate passes on the data to first initialise the Sessions
 * and then end Sessions with a matching End LogRecord. Start records are
 * initialised with the End Time of the last record in the log until matched
 * with an End Session, if possible. Any End Sessions that cannot be matched with
 * an existing Session are initialised with the Start Time of the first record in
 * the log.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 *
 */

public final class CustomerUtil {
	
	private static Map<String, Customer> customers;

	public static Map<String, Customer> getCustomerListFromLogRecords(List<LogRecord> logRecords, Instant logStart, Instant logEnd) {
		customers = new HashMap<>();
		startSessions(logRecords, logEnd);
		endSessions(logRecords, logStart);
		
		return customers;
	}
	
	private static void startSessions(List<LogRecord> logRecords, Instant logEnd) {
		for (LogRecord logRecord : logRecords) {
			if (logRecord.getStartEnd().equals(StartEnd.START)) {
				Customer customer = getCustomer(logRecord);
				
				customer.addSessionStart(logRecord.getTime(), logEnd);
				
				customers.put(logRecord.getCustomerName(), customer);
			}
		}
	}
	
	private static void endSessions(List<LogRecord> logRecords, Instant logStart) {
		for (LogRecord logRecord : ListUtil.reverseList(logRecords)) {
			if (logRecord.getStartEnd().equals(StartEnd.END)) {
				Customer customer = getCustomer(logRecord);
	
				customer.addSessionEnd(logRecord.getTime(), logStart);
				
				customers.put(logRecord.getCustomerName(), customer);
			}
		}
	}
	
	private static Customer getCustomer(LogRecord logRecord) {
		Customer customer;
		if (customers.containsKey(logRecord.getCustomerName())) {
			customer = customers.get(logRecord.getCustomerName());
		} else {
			customer = new Customer(logRecord.getCustomerName());
		}
		return customer;
	}
}
