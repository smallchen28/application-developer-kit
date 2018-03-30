/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.exception;

public class DomainException extends KitNestException {
	
	private static final long serialVersionUID = 340945332221750690L;
	
	public DomainException(String arg0, Throwable arg1) {
		
		super(arg0, arg1);
	}
	
	public DomainException(String arg0) {
		
		super(arg0);
	}
	
	public DomainException(Throwable arg0) {
		
		super(arg0);
	}
	
}
