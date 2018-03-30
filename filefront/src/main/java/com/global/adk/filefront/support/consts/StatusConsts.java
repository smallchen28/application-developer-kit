/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 22:40 创建
 *
 */
package com.global.adk.filefront.support.consts;

/**
 * 状态常量定义
 * 
 * @author karott
 */
public interface StatusConsts {
	
	// 业务状态 
	String SUCCESS = "success";
	String PROCESSING = "processing";
	String FAIL = "fail";
	
	// 节点状态
	String INIT = "init";
	String DOWNLOAD_IGNORE = "downloadIgnore";
	String DOWNLOAD_FAIL = "downloadFail";
	String FILE_DOWNLOADED = "fileDownloaded";
	String FILE_PARSED = "fileParsed";
	
	String FILE_UPLOADED = "fileUploaded";
	String FILE_NOTIFIED = "fileNotified";
	String UPLOAD_IGNORE = "uploadIgnore";
	String NOTIFY_IGNORE = "notifyIgnore";
	
	String BIZ_JOB_FAIL = "bizJobFail";
	
	String FTP_FILE_NOT_FOUND = "comn_01_0100";
	String LOCAL_FILE_NOT_FOUND = "comn_01_0101";
}
