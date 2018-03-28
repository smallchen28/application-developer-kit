/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.common.exception;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-2-27 下午11:44<br>
 * @see
 * @since 1.0.0
 */
public class FlowException extends KitNestException {
	
	public FlowException() {
	}
	
	public FlowException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public FlowException(String arg0) {
		super(arg0);
	}
	
	public FlowException(Throwable arg0) {
		super(arg0);
	}
}
