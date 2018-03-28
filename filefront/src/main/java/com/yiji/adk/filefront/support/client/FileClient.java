/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.filefront.support.client;

import java.io.File;

/**
 * 文件客户端访问服务
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2013-11-11
 * @version 1.0.0
 */
public interface FileClient {
	
	File download(String sourceDirectory, String sourceName);
	
	byte[] downloadBytes(String sourceDirectory, String sourceName);
	
	File upload(String localDirectory, String localName);
	
	boolean checkFileFromFTP(String sourceDirectory, String sourceName);
	
}
