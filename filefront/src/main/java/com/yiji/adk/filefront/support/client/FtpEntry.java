/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-28 10:51 创建
 *
 */
package com.yiji.adk.filefront.support.client;

import com.yiji.adk.filefront.boot.FilefrontProperties;
import com.yiji.adk.filefront.exceptions.FileFrontException;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author karott
 */
public class FtpEntry {
	
	public static final String USER_SPLITTER = "/";
	public static final String PWD_SPLITTER = "@";
	public static final String HOSTS_SPLITTER = ":";
	public static final String TENANT_SPLITTER = "\\>";
	public static final String ENTRY_SPLITTER = "\\|";
	
	public static final String CHECK_MSG = "ftp配置有异常,请检查.格式：teant>user/password@host:port|";
	
	private String host;
	private String port;
	private String user;
	private String password;
	
	public static void build(Map<String, FtpEntry> ftpConfigs, FilefrontProperties.Ftp ftp) {
		if (null == ftp.getServer() || "".equals(ftp.getServer())) {
			throw new FileFrontException(CHECK_MSG);
		}
		
		String[] theArray = ftp.getServer().split(ENTRY_SPLITTER);
		for (String entry : theArray) {
			String[] tempArray = entry.split(TENANT_SPLITTER);
			String tenant = tempArray[0];
			ftpConfigs.putIfAbsent(tenant, new FtpEntry());
			
			tempArray = tempArray[1].split(USER_SPLITTER);
			ftpConfigs.get(tenant).setUser(tempArray[0]);
			
			tempArray = tempArray[1].split(PWD_SPLITTER);
			ftpConfigs.get(tenant).setPassword(tempArray[0]);
			
			tempArray = tempArray[1].split(HOSTS_SPLITTER);
			ftpConfigs.get(tenant).setHost(tempArray[0]);
			ftpConfigs.get(tenant).setPort(tempArray[1]);
		}
	}
	
	public void check(String tenant) {
		Assert.notNull(host, CHECK_MSG);
		Assert.notNull(port, CHECK_MSG);
		Assert.notNull(user, CHECK_MSG);
		Assert.notNull(password, CHECK_MSG);
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
