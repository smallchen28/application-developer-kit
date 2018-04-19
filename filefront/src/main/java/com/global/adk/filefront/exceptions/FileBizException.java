/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-04 22:34 创建
 *
 */
package com.global.adk.filefront.exceptions;

import com.global.adk.common.exception.BizException;
import com.global.common.lang.result.Status;

/**
 * @author karott
 */
public class FileBizException extends BizException {
	private static final long serialVersionUID = -3685029729787451260L;
	
	public FileBizException() {
		super();
	}
	
	public FileBizException(String message) {
		super(message);
	}
	
	public FileBizException(Throwable throwable) {
		super(throwable);
	}
	
	public FileBizException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public FileBizException(String message, Status status) {
		super(message, status);
	}
	
	public FileBizException(String message, Status status, Throwable throwable) {
		super(message, status, throwable);
	}
	
	public FileBizException(String message, String errorCode) {
		super(message, errorCode);
	}
	
	public FileBizException(String message, String errorCode, Throwable throwable) {
		super(message, errorCode, throwable);
	}
	
	public FileBizException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}
	
	public FileBizException(String message, String errorCode, Status status, Throwable throwable) {
		super(message, errorCode, status, throwable);
	}
	
	@Override
	protected Status initStatus() {
		return Status.FAIL;
	}
	
	@Override
	protected String defaultErrorCode() {
		return null;
	}
}
