/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.exception;

import com.yjf.common.lang.result.Status;

/**
 * 初始化出错
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月22日 下午4:35:51<br>
 */
public class InitializerException extends NestError {
	
	private static final long serialVersionUID = 4926215091532499389L;

	public InitializerException() {
	}

	public InitializerException(String message) {
		super(message);
	}

	public InitializerException(Throwable throwable) {
		super(throwable);
	}

	public InitializerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InitializerException(String message, Status status) {
		super(message, status);
	}

	public InitializerException(String message, Status status, Throwable throwable) {
		super(message, status, throwable);
	}

	public InitializerException(String message, String errorCode) {
		super(message, errorCode);
	}

	public InitializerException(String message, String errorCode, Throwable throwable) {
		super(message, errorCode, throwable);
	}

	public InitializerException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}

	public InitializerException(String message, String errorCode, Status status, Throwable throwable) {
		super(message, errorCode, status, throwable);
	}

}
