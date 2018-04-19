/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-08 23:07 创建
 *
 */
package com.global.adk.filefront.exceptions;

import com.global.common.lang.result.Status;

/**
 * @author karott
 */
public class ResponseFileNotifyIdemException extends ResponseFileNotifyException {
	
	public ResponseFileNotifyIdemException(String message, String errorCode, Status status) {
		super(message, errorCode, status);
	}
}
