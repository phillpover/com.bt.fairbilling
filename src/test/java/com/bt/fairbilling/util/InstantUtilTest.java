package com.bt.fairbilling.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.time.DateTimeException;
import java.time.Instant;

import org.junit.Test;

public class InstantUtilTest {
	
	private final static String validTime = "14:02:03";
	private final static String nonValidTime = "hdiqndag";

	@Test(expected=DateTimeException.class)
	public void getInstantFromTime_WhenTimeIsNull_ThenThrowDateTimeException() {
		InstantUtil.getInstantFromTime(null);
	}
	
	@Test(expected=DateTimeException.class)
	public void getInstantFromTime_WhenTimeIsNotValid_ThenThrowDateTimeException() {
		InstantUtil.getInstantFromTime(nonValidTime);
	}
	
	@Test
	public void getInstantFromTime_WhenTimeIsValid_ThenReturnsValidInstant() {
		
		Instant result = InstantUtil.getInstantFromTime(validTime);
		
		assertThat(result.toEpochMilli(), greaterThan(0l));
	}
}
