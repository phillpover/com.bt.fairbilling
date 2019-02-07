package com.bt.fairbilling.util;

import java.util.ArrayList;
import java.util.List;

import com.bt.fairbilling.exception.FairBillingException;
import com.bt.fairbilling.model.LogRecord;

/**
 * Utility Class that processes the List<String> of lines from the log
 * file supplied to the Application and returns a List<LogRecord> for
 * easier handling of the entries in the log.
 * 
 * @author Phill Pover
 * @since 06/02/2019
 *
 */

public final class LogRecordUtil {

	public static List<LogRecord> getLogRecordsFromFilePath(String path) throws FairBillingException {
		List<String> logEntries = FileUtil.getLinesFromFile(path);
		
		List<LogRecord> logRecords = new ArrayList<>();
		
		for(String entry : logEntries) {
			try {
				logRecords.add(new LogRecord(entry));
			} catch(FairBillingException fbe) {
				// Fail Gracefully but continue
			}
		}
		
		return logRecords;	
	}
}
