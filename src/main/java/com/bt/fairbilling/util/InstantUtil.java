package com.bt.fairbilling.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Utility class to convert Time Strings of format HH:mm:ss into an Instant for easier
 * handling and comparison.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 *
 */

public final class InstantUtil {

	public static Instant getInstantFromTime(String time) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate currentDay = LocalDate.now();
		
		TemporalAccessor temporalAccessor = dateTimeFormatter.parse(dateFormatter.format(currentDay) + " " + time);
		LocalDateTime parsedDateTime = LocalDateTime.from(temporalAccessor);
		ZonedDateTime zonedDateTime = ZonedDateTime.of(parsedDateTime, ZoneId.systemDefault());
		
		return Instant.from(zonedDateTime);
	}
}
