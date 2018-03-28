/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 20:55 创建
 *
 */
package com.yiji.adk.filefront.listeners.events;

import com.yiji.adk.filefront.listeners.events.inner.FileNotifyEvent;

/**
 * 请求文件通知事件.文件处理异常时发出事件
 * 
 * <p/>
 * 通过{@link com.yiji.adk.filefront.listeners.events.ResponsetFileNotifyErrorEvent#errorCode}区分错误类型
 *
 * @author karott
 */
public class ResponsetFileNotifyErrorEvent extends FileNotifyEvent {
	
	/**
	 * 错误码,区分错误类型.比如文件不存在、文件下载异常
	 */
	private String errorCode;
	
	/**
	 * 错误描述
	 */
	private String errorMessage;
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
