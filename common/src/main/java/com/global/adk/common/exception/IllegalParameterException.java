/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.exception;

import com.global.adk.common.Constants;
import com.yjf.common.lang.result.Status;

/**
 * 非法请求参数错误
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月22日 下午2:23:38<br>
 */
public class IllegalParameterException extends BizException {
	
	private static final long serialVersionUID = 4700971387143842376L;

	public IllegalParameterException() {
	}

	public IllegalParameterException(String message) {
		super(message);
	}

	public IllegalParameterException(Throwable throwable) {
		super(throwable);
	}

	public IllegalParameterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IllegalParameterException(String message, Status status) {
		super(message, status);
	}

	public IllegalParameterException(String message, Status status, Throwable throwable) {
		super(message, status, throwable);
	}

	public IllegalParameterException(String message, String errorCode) {
		super(message, errorCode);
	}

	public IllegalParameterException(String message, String errorCode, Throwable throwable) {
		super(message, errorCode, throwable);
	}

	public IllegalParameterException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}

	public IllegalParameterException(String message, String errorCode, Status status, Throwable throwable) {
		super(message, errorCode, status, throwable);
	}

	@Override
	protected String defaultErrorCode() {
		
		return Constants.ERROR_CODE_ILLEGA_PARAMETER;
	}
}
