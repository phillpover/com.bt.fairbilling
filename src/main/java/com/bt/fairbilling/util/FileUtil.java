package com.bt.fairbilling.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.exception.FairBillingException;

/**
 * Utility Class to process a log file into an ArrayList<String> for easier use.
 * 
 * @author Phill Pover
 * @since 06/02/2019
 *
 */

public final class FileUtil {

	public static List<String> getLinesFromFile(String path) throws FairBillingException {
		try {
			return Files.lines(Paths.get(path)).collect(Collectors.toList());
		} catch (Exception e) {
			throw new FairBillingException(MessageKeys.ERROR_PROCESSING_LOG_FILE, e);
		}
	}
}
