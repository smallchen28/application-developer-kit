/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-04 21:29 创建
 *
 */
package com.global.adk.filefront.support.config;

/**
 * @author karott
 */
public interface FileConfigContext {
	
	String DEFAULT_FTP = "defaultFtp";
	
	String FILE_CONFIG = "//fileConfig";
	String BIZ_TYPE = "@bizType";
	String DIRECTION = "@direction";
	String TENANT = "@tenant";
	
	String SYSTEM = "//fileConfig/system";
	String EXTENDS = "@extends";
	String FILE_SERVICE = "@fileService";
	
	String SERVER = "//fileConfig/system/server";
	
	String HOST = "//fileConfig/system/server/host";
	String PORT = "//fileConfig/system/server/port";
	String USERNAME = "//fileConfig/system/server/username";
	String PASSWORD = "//fileConfig/system/server/password";
	
	String TRANSPORT = "//fileConfig/system/server/transport";
	String FTP = "//fileConfig/system/server/transport/ftp";
	String PASSIVE = "@passive";
	String CHARSET = "@charset";
	String SO_TIMEOUT = "@soTimeout";
	String RECV_BUFFER = "@recvBuffer";
	String SEND_BUFFER = "@sendBuffer";
	String SO_LINGER = "@soLinger";
	String SO_LINGER_TIME = "@soLinterTime";
	String TCP_NO_DELAY = "@tcpNoDelay";
	
	String DOWNLOAD = "//fileConfig/system/path/download";
	String DOWNLOAD_OPTION = "@isDownload";
	String UPLOAD = "//fileConfig/system/path/upload";
	String UPLOAD_OPTION = "@isUpload";
}