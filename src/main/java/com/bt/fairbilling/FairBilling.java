package com.bt.fairbilling;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.exception.FairBillingException;
import com.bt.fairbilling.model.Customer;
import com.bt.fairbilling.model.LogRecord;
import com.bt.fairbilling.util.ArgumentsUtil;
import com.bt.fairbilling.util.CustomerUtil;
import com.bt.fairbilling.util.LogRecordUtil;

/**
 * Fair Billing Application
 * Takes an input argument of a path to a log file of user session start and end times
 * and outputs a list of each user with the total number of sessions that user has
 * performed, and the total time in seconds of these sessions.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 *
 */

public class FairBilling {
	
	private static Instant logStart;
	private static Instant logEnd;
	
	private static List<LogRecord> logRecords;
	private static Map<String, Customer> customers;

	public static void main(String[] args) throws FairBillingException {
		List<String> arguments = ArgumentsUtil.getArguments(args);
		
		if (!arguments.isEmpty()) {
			logRecords = LogRecordUtil.getLogRecordsFromFilePath(arguments.get(0));
			
			if (!logRecords.isEmpty()) {
				setLogStartAndEnd();
				
				customers = CustomerUtil.getCustomerListFromLogRecords(logRecords, logStart, logEnd);
				
				if (!customers.isEmpty()) {
					for(String customerName : customers.keySet()) {
						System.out.println(customers.get(customerName).printOutput());
					}
				} else {
					throw new FairBillingException(MessageKeys.NO_VALID_ENTRIES);
				}
			} else {
				throw new FairBillingException(MessageKeys.NO_ENTRIES_IN_LOG);
			}
		} else {
			throw new FairBillingException(MessageKeys.PATH_TO_LOG_MISSING);
		}
	}
	
	private static void setLogStartAndEnd() {
		logStart = logRecords.get(0).getTime();
		logEnd = logRecords.get(logRecords.size()-1).getTime();
	}
}
