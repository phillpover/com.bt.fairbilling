package com.bt.fairbilling.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class to parse the String Array of the Application's argument into an
 * ArrayList<String> for easier use and cleaner code.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 *
 */

public final class ArgumentsUtil {

	public static List<String> getArguments(String[] args) {
		List<String> arguments = new ArrayList<>();
		if (args.length > 0) {
			for (String arg : args) {
				arguments.add(arg);
			}
		}
		return arguments;
	}
}
