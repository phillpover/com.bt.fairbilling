package com.bt.fairbilling.model;

import java.time.Instant;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.constants.StartEnd;
import com.bt.fairbilling.exception.FairBillingException;
import com.bt.fairbilling.util.InstantUtil;

/**
 * LogRecord Object takes a line from the log file provided to the application
 * and allows for easier use of the data, including whether the Log Record is
 * the Start or End of a Session, the Customer Name, and an Instant when the
 * LogRecord applies.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 */

public class LogRecord {

	private Instant time;
	private String customerName;
	private StartEnd startEnd;

	public LogRecord(String logLine) throws FairBillingException {
		try {
			String[] logEntries = logLine.split("\\s+");
			
			time = InstantUtil.getInstantFromTime(logEntries[0]);
			
			customerName = logEntries[1];
			
			if("Start".equals(logEntries[2])) {
				startEnd = StartEnd.START;
			} else {
				startEnd = StartEnd.END;
			}
		} catch (Exception e) {
			throw new FairBillingException(MessageKeys.ERROR_PROCESSING_LOG_ENTRY, e);
		}
	}

	public Instant getTime() {
		return time;
	}

	public String getCustomerName() {
		return customerName;
	}

	public StartEnd getStartEnd() {
		return startEnd;
	}
}
