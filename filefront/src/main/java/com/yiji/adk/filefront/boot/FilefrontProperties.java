/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 19:53 创建
 *
 */
package com.yiji.adk.filefront.boot;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yiji.adk.common.boot.CommonConfiguration;
import com.yiji.adk.common.jdbc.DialectUtil;
import com.yiji.adk.filefront.exceptions.FileFrontException;
import com.yiji.adk.filefront.support.client.FileClientFactory;
import com.yiji.adk.filefront.support.client.FtpEntry;
import com.yiji.boot.core.AppConfigException;
import com.yiji.boot.core.Apps;
import com.yjf.common.util.StringUtils;

/**
 * @author karott
 */
@ConfigurationProperties(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX)
public class FilefrontProperties implements InitializingBean {
	
	/**
	 * 是否启用此组件.默认不启用
	 */
	private boolean enable = false;
	
	/**
	 * 是否启用provider服务
	 */
	private boolean enableProvider = false;
	
	/**
	 * dubbo分组,不配置默认获取applicationName
	 */
	private String dubboGroup = Apps.getAppName();
	
	/**
	 * dubbo版本,不配置默认是1.0
	 */
	private String dubboVersion = "1.0";
	
	/**
	 * 数据库类型,必须;oracle,mysql
	 */
	private String dialect;
	
	/**
	 * 文件处理线程数
	 */
	private int coreThreads = 16;
	
	/**
	 * 最大线程数
	 */
	private int maxThreads = 50;
	
	/**
	 * 存活时间,秒
	 */
	private int keepAliveTime = 240;
	
	/**
	 * 队列数量
	 */
	private int capacity = 10;
	
	/**
	 * 文件配置路径
	 */
	private String configLocation = "classpath*:/file/config/**/*.xml";
	
	/**
	 * ftp配置
	 */
	private Ftp ftp = new Ftp();
	
	/**
	 * ftp读配置
	 */
	private Ftp ftpRead = new Ftp();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initCheck();
		initFileConfigs();
	}
	
	private void initCheck() {
		if (enableProvider) {
			if (!enable) {
				throw new FileFrontException("启用[enableProvider]时必须启用[enable]");
			}
		}
		
		setDialect(DialectUtil.dialectByDSUrl());
		if (null != dialect && (StringUtils.notEquals(dialect, "mysql") && StringUtils.notEquals(dialect, "oracle"))) {
			throw new AppConfigException("只支持oracle和mysql");
		}
	}
	
	private void initFileConfigs() {
		try {
			FileClientFactory.initFtpConfigs(ftp);
			FileClientFactory.initFtpReadConfigs(ftpRead);
		} catch (Exception e) {
			throw new FileFrontException(FtpEntry.CHECK_MSG, e);
		}
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public boolean isEnableProvider() {
		return enableProvider;
	}
	
	public void setEnableProvider(boolean enableProvider) {
		this.enableProvider = enableProvider;
	}
	
	public String getDubboGroup() {
		return dubboGroup;
	}
	
	public void setDubboGroup(String dubboGroup) {
		this.dubboGroup = dubboGroup;
	}
	
	public String getDubboVersion() {
		return dubboVersion;
	}
	
	public void setDubboVersion(String dubboVersion) {
		this.dubboVersion = dubboVersion;
	}
	
	public String getDialect() {
		return dialect;
	}
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	public int getCoreThreads() {
		return coreThreads;
	}
	
	public void setCoreThreads(int coreThreads) {
		this.coreThreads = coreThreads;
	}
	
	public int getMaxThreads() {
		return maxThreads;
	}
	
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	
	public int getKeepAliveTime() {
		return keepAliveTime;
	}
	
	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String getConfigLocation() {
		return configLocation;
	}
	
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}
	
	public Ftp getFtp() {
		return ftp;
	}
	
	public void setFtp(Ftp ftp) {
		this.ftp = ftp;
	}
	
	public Ftp getFtpRead() {
		return ftpRead;
	}
	
	public void setFtpRead(Ftp ftpRead) {
		this.ftpRead = ftpRead;
	}
	
	public static class Ftp {
		
		/**
		 * server配置代替hosts,ports,users,passwords
		 */
		private String server;
		
		private String hosts;
		private String ports;
		private String users;
		private String passwords;
		
		public String getServer() {
			return server;
		}
		
		public void setServer(String server) {
			this.server = server;
		}
		
		public String getHosts() {
			return hosts;
		}
		
		public void setHosts(String hosts) {
			this.hosts = hosts;
		}
		
		public String getPorts() {
			return ports;
		}
		
		public void setPorts(String ports) {
			this.ports = ports;
		}
		
		public String getUsers() {
			return users;
		}
		
		public void setUsers(String users) {
			this.users = users;
		}
		
		public String getPasswords() {
			return passwords;
		}
		
		public void setPasswords(String passwords) {
			this.passwords = passwords;
		}
	}
}
