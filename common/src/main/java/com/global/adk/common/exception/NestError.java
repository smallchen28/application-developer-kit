/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.exception;

import com.global.adk.common.Constants;
import com.global.boot.core.Apps;
import com.global.common.lang.result.StandardResultInfo;
import com.global.common.lang.result.Status;

/**
 * 系统内部异常，此处应表现为无法识别的错误 。
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月22日 下午2:38:02<br>
 */
public class NestError extends SystemException {
	
	private static final long serialVersionUID = 305654875908658046L;

	public NestError() {
	}

	public NestError(String message) {
		super(message);
	}

	public NestError(Throwable throwable) {
		super(throwable);
	}

	public NestError(String message, Throwable throwable) {
		super(message, throwable);
	}

	public NestError(String message, Status status) {
		super(message, status);
	}

	public NestError(String message, Status status, Throwable throwable) {
		super(message, status, throwable);
	}

	public NestError(String message, String errorCode) {
		super(message, errorCode);
	}

	public NestError(String message, String errorCode, Throwable throwable) {
		super(message, errorCode, throwable);
	}

	public NestError(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}

	public NestError(String message, String errorCode, Status status, Throwable throwable) {
		super(message, errorCode, status, throwable);
	}

	@Override
	protected Status initStatus() {
		
		return Status.FAIL;
	}
	
	@Override
	protected String defaultErrorCode() {
		
		return Constants.ERROR_CODE_NEST;
	}

}
