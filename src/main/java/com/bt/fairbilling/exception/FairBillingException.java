package com.bt.fairbilling.exception;

/**
 * Exception Handler for Fair Billing Application
 * 
 * @author Phill Pover
 * @since 05/02/2019
 *
 */

public class FairBillingException extends Exception {

	public FairBillingException(String errorMessage, Throwable error) {
	    super(errorMessage, error);
	}
	
	public FairBillingException(String errorMessage) {
	    super(errorMessage);
	}
	
	private static final long serialVersionUID = -822215955188682269L;
}
