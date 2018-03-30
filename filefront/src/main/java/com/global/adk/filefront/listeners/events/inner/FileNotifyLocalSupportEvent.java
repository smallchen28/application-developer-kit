/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-01 22:20 创建
 *
 */
package com.global.adk.filefront.listeners.events.inner;

import java.util.Date;

/**
 * @author karott
 */
public class FileNotifyLocalSupportEvent extends FileNotifyEvent {
	
	/**
	 * 本地文件路径
	 */
	private String localFilePath;
	
	/**
	 * 本地文件名
	 */
	private String localFileName;
	
	/**
	 * 本地文件时间
	 */
	private Date localFileTime = new Date();
	
	public String getLocalFilePath() {
		return localFilePath;
	}
	
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	
	public String getLocalFileName() {
		return localFileName;
	}
	
	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}
	
	public Date getLocalFileTime() {
		return localFileTime;
	}
	
	public void setLocalFileTime(Date localFileTime) {
		this.localFileTime = localFileTime;
	}
}
