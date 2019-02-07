package com.bt.fairbilling.model;

import java.time.Instant;

/**
 * Represents a complete Session of the Customer, with Instants representing
 * the Start and End times of the Session, with a Boolean flag used to check
 * whether the Session's End time has been updated. Session should be initiated
 * with the End time from the final Record of the log and updated if a matching
 * Session End time is found.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 *
 */

public class Session {

	private Instant startTime;
	private Instant endTime;
	private Boolean endTimeSet = false;
	
	public Session(Instant startTime, Instant endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Boolean hasEndTimeBeenSet() {
		return endTimeSet;
	}

	public void setEndTimeFlag(Boolean endTimeSet) {
		this.endTimeSet = endTimeSet;
	}
}
