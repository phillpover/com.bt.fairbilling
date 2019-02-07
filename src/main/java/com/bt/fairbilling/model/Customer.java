package com.bt.fairbilling.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.bt.fairbilling.util.ListUtil;

/**
 * Customer Object detailing the customerName and Session information.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 * 
 */

public class Customer {
	
	private String customerName;
	private List<Session> sessions;
	
	public Customer(String customerName) {
		this.customerName = customerName;
		sessions = new ArrayList<>();
	}
	
	public void addSessionStart(Instant sessionStartTime, Instant logEnd) {
		sessions.add(new Session(sessionStartTime, logEnd));
	}
	
	public void addSessionEnd(Instant sessionEndTime, Instant logStart) {
		Boolean endTimeSet = false;
		for (Session session : ListUtil.reverseList(sessions)) {
			if (!endTimeSet && !session.hasEndTimeBeenSet() && 
					session.getStartTime().compareTo(sessionEndTime) < 0 ) {
				session.setEndTime(sessionEndTime);
				session.setEndTimeFlag(true);
				endTimeSet = true;
			}
		}
		if (!endTimeSet) {
			sessions.add(new Session(logStart, sessionEndTime));
		}
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<Session> getSessions() {
		return sessions;
	}
	
	public String printOutput() {
		return customerName + " " + sessions.size() + " " + totalSessionTime();
	}
	
	private long totalSessionTime() {
		long sessionTime = 0;
		
		for (Session session : sessions) {
			sessionTime += Duration.between(session.getStartTime(), session.getEndTime()).getSeconds();
		}
		
		return sessionTime;
	}
}
