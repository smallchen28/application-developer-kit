/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-01 22:40 创建
 *
 */
package com.global.adk.filefront.exceptions;

import com.global.common.lang.result.Status;

/**
 * 响应文件通知异常,响应文件通知相关的监听处理异常统一用此异常
 * 
 * @author karott
 */
public class ResponseFileNotifyException extends FileBizException {
	private static final long serialVersionUID = -5231201806427722831L;
	
	public ResponseFileNotifyException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
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
