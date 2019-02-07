package com.bt.fairbilling.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;

import org.junit.Test;

public class CustomerTest {
	
	private final static String customerName = "CUSTOMER_NAME";
	
	private final static Instant instant1 = Instant.ofEpochSecond(100l);
	private final static Instant instant2 = Instant.ofEpochSecond(10000l);
	private final static Instant instant3 = Instant.ofEpochSecond(200l);
	private final static Instant instant4 = Instant.ofEpochSecond(20000l);
	private final static Instant logStartInstant = Instant.ofEpochSecond(1l);
	private final static Instant logEndInstant = Instant.ofEpochSecond(1000000000);

	@Test
	public void addSessionStart_WhenNoError_ThenAddsNewSession() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant1, logEndInstant);
		
		assertThat(customer.getSessions().size(), equalTo(1));
		assertThat(customer.getSessions().get(0).getStartTime(), equalTo(instant1));
		assertThat(customer.getSessions().get(0).getEndTime(), equalTo(logEndInstant));
	}
	
	@Test
	public void addSessionEnd_WhenNoStartSession_ThenAddsNewSession() {
		Customer customer = new Customer(customerName);
		customer.addSessionEnd(instant2, logStartInstant);
		
		assertThat(customer.getSessions().size(), equalTo(1));
		assertThat(customer.getSessions().get(0).getStartTime(), equalTo(logStartInstant));
		assertThat(customer.getSessions().get(0).getEndTime(), equalTo(instant2));
	}
	
	@Test
	public void addSessionEnd_WhenMatchingStartSession_ThenUpdatesSession() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant1, logEndInstant);
		customer.addSessionEnd(instant2, logStartInstant);
		
		assertThat(customer.getSessions().size(), equalTo(1));
		assertThat(customer.getSessions().get(0).getStartTime(), equalTo(instant1));
		assertThat(customer.getSessions().get(0).getEndTime(), equalTo(instant2));
	}
	
	@Test
	public void addSessionEnd_WhenStartSessionAfterEndSession_ThenAddsNewSession() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant2, logEndInstant);
		customer.addSessionEnd(instant1, logStartInstant);
		
		assertThat(customer.getSessions().size(), equalTo(2));
		assertThat(customer.getSessions().get(0).getStartTime(), equalTo(instant2));
		assertThat(customer.getSessions().get(0).getEndTime(), equalTo(logEndInstant));
		assertThat(customer.getSessions().get(1).getStartTime(), equalTo(logStartInstant));
		assertThat(customer.getSessions().get(1).getEndTime(), equalTo(instant1));
	}
	
	@Test
	public void addSessionEnd_WhenMultipleMatchingStartSessions_ThenUpdatesSessionWithClosestStartTimeToEndTime() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant1, logEndInstant);
		customer.addSessionStart(instant3, logEndInstant);
		customer.addSessionEnd(instant2, logStartInstant);
		
		assertThat(customer.getSessions().size(), equalTo(2));
		assertThat(customer.getSessions().get(0).getStartTime(), equalTo(instant1));
		assertThat(customer.getSessions().get(0).getEndTime(), equalTo(logEndInstant));
		assertThat(customer.getSessions().get(1).getStartTime(), equalTo(instant3));
		assertThat(customer.getSessions().get(1).getEndTime(), equalTo(instant2));
	}
	
	@Test
	public void addSessionEnd_WhenMultipleMatchingStartSessionsAndAddingMultipleEndSessions_ThenUpdatesEachSessionOnceOnly() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant1, logEndInstant);
		customer.addSessionStart(instant3, logEndInstant);
		customer.addSessionEnd(instant4, logStartInstant);
		customer.addSessionEnd(instant2, logStartInstant);
		
		assertThat(customer.getSessions().size(), equalTo(2));
		assertThat(customer.getSessions().get(0).getStartTime(), equalTo(instant1));
		assertThat(customer.getSessions().get(0).getEndTime(), equalTo(instant2));
		assertThat(customer.getSessions().get(1).getStartTime(), equalTo(instant3));
		assertThat(customer.getSessions().get(1).getEndTime(), equalTo(instant4));
	}
	
	@Test
	public void printOutput_WhenNoSessions_ThenReturnsDetailsAsString() {
		Customer customer = new Customer(customerName);
		
		String result = customer.printOutput();
		
		assertThat(result, equalTo(customerName + " " + 0 + " " + 0));
	}
	
	@Test
	public void printOutput_WhenOneSession_ThenReturnsDetailsAsString() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant1, logEndInstant);
		customer.addSessionEnd(instant2, logStartInstant);
		
		String result = customer.printOutput();
		
		assertThat(result, equalTo(customerName + " " + 1 + " " + (10000l - 100l)));
	}
	
	@Test
	public void printOutput_WhenMultipleSessions_ThenReturnsDetailsAsString() {
		Customer customer = new Customer(customerName);
		customer.addSessionStart(instant1, logEndInstant);
		customer.addSessionEnd(instant2, logStartInstant);
		customer.addSessionStart(instant3, logEndInstant);
		customer.addSessionEnd(instant4, logStartInstant);
		
		String result = customer.printOutput();
		
		assertThat(result, equalTo(customerName + " " + 2 + " " + ((10000l - 100l) + (20000l - 200l))));
	}
}
