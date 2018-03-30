/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.exception;

import com.global.adk.common.Constants;
import com.yjf.common.lang.result.Status;

/**
 * 挂起错误，当该异常产生时，不会回滚事务！
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月22日 下午4:25:10<br>
 */
public class SuspendException extends SystemException {
	
	private static final long serialVersionUID = -5255790453997896414L;

	public SuspendException() {
	}

	public SuspendException(String message) {
		super(message);
	}

	public SuspendException(Throwable throwable) {
		super(throwable);
	}

	public SuspendException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SuspendException(String message, Status status) {
		super(message, status);
	}

	public SuspendException(String message, Status status, Throwable throwable) {
		super(message, status, throwable);
	}

	public SuspendException(String message, String errorCode) {
		super(message, errorCode);
	}

	public SuspendException(String message, String errorCode, Throwable throwable) {
		super(message, errorCode, throwable);
	}

	public SuspendException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}

	public SuspendException(String message, String errorCode, Status status, Throwable throwable) {
		super(message, errorCode, status, throwable);
	}

	@Override
	protected Status initStatus() {
		
		return Status.PROCESSING;
	}
	
	@Override
	protected String defaultErrorCode() {
		
		return Constants.ERROR_CODE_SUPPEND;
	}
}
