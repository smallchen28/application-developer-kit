/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.global.adk.common.exception;

/**
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-7
 * @version 1.0.0
 * @see
 * @history <table>
 */
public class PlanTaskException extends KitNestException {
	
	private static final long serialVersionUID = 896955662997602048L;
	
	public PlanTaskException(String msg) {
		super(msg);
	}

	public PlanTaskException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
