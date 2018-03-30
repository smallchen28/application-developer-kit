/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-04 20:53 创建
 *
 */
package com.global.adk.filefront.exceptions;

import com.global.adk.common.exception.SystemException;
import com.yjf.common.lang.result.Status;

/**
 * 文件配置异常
 * 
 * @author karott
 */
public class FileFrontException extends SystemException {
	private static final long serialVersionUID = -8799276706673068306L;

	public FileFrontException(String message) {
		super(message);
	}

	public FileFrontException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}

	public FileFrontException(String message, Throwable throwable) {
		super(message, throwable);
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
