package com.global.adk.common.exception;

public class RuleException extends RuntimeException {
	
	private static final long serialVersionUID = -2721438124095186541L;
	
	public RuleException() {
		
		super();
	}
	
	public RuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public RuleException(String message, Throwable cause) {
		
		super(message, cause);
	}
	
	public RuleException(String message) {
		
		super(message);
	}
	
	public RuleException(Throwable cause) {
		
		super(cause);
	}
	
}
