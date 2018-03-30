/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.exception;

import com.yjf.common.lang.result.Status;

/**
 * 业务错误
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月22日 下午4:21:19<br>
 */
public abstract class BizException extends SystemException {
	
	private static final long serialVersionUID = 2240361467371094578L;
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
	
	public BizException() {
	}
	
	public BizException(String message) {
		super(message);
	}
	
	public BizException(Throwable throwable) {
		super(throwable);
	}
	
	public BizException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public BizException(String message, Status status) {
		super(message, status);
	}
	
	public BizException(String message, Status status, Throwable throwable) {
		super(message, status, throwable);
	}
	
	public BizException(String message, String errorCode) {
		super(message, errorCode);
	}
	
	public BizException(String message, String errorCode, Throwable throwable) {
		super(message, errorCode, throwable);
	}
	
	public BizException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}
	
	public BizException(String message, String errorCode, Status status, Throwable throwable) {
		super(message, errorCode, status, throwable);
	}
	
	@Override
	protected Status initStatus() {
		
		return Status.FAIL;
	}
	
}
