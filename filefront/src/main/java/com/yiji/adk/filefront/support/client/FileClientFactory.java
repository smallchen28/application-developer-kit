/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-05 15:10 创建
 *
 */
package com.yiji.adk.filefront.support.client;

import com.google.common.collect.Maps;
import com.yiji.adk.filefront.boot.FilefrontProperties;
import com.yiji.adk.filefront.exceptions.FileBizException;
import com.yiji.adk.api.enums.FileServiceType;
import com.yiji.adk.filefront.support.config.FileConfigContext;
import com.yiji.adk.filefront.support.config.FileConfigParser;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

/**
 * @author karott
 */
public abstract class FileClientFactory {
	private static final Logger logger = LoggerFactory.getLogger(FileClientFactory.class);
	
	private static Map<String, FileClient> clients = Maps.newConcurrentMap();
	public static Map<String, FtpEntry> ftpConfigs = Maps.newConcurrentMap();
	public static Map<String, FtpEntry> ftpReadConfigs = Maps.newConcurrentMap();
	
	public static void initFtpConfigs(FilefrontProperties.Ftp ftp) {
		FtpEntry.build(ftpConfigs, ftp);
		ftpConfigs.keySet().stream().forEach((tenant) -> ftpConfigs.get(tenant).check(tenant));
	}
	
	public static void initFtpReadConfigs(FilefrontProperties.Ftp ftp) {
		FtpEntry.build(ftpReadConfigs, ftp);
		ftpReadConfigs.keySet().stream().forEach((tenant) -> ftpReadConfigs.get(tenant).check(tenant));
	}
	
	public static FileClient client(String bizType, String tenant) {
		
		if (clients.containsKey(bizType)) {
			return clients.get(bizType);
		}
		
		String fileService = FileConfigParser.attrByNode(bizType, FileConfigContext.SYSTEM,
			FileConfigContext.FILE_SERVICE);
			
		if (FileServiceType.FTP.name().equals(fileService.toUpperCase())) {
			FtpClient ftpClient = new FtpClient(bizType, getFtpHost(bizType, tenant), getFtpPort(bizType, tenant),
				getFtpUser(bizType, tenant), getFtpPassword(bizType, tenant));
			clients.put(bizType, ftpClient);
			
			return ftpClient;
		}
		
		throw new FileBizException(String.format("业务类型对应的客户端不存在.[%s]", bizType));
	}
	
	public static String clientFileName(String bizType, String origName) {
		return origName;
	}
	
	public static boolean localFileExists(String path, String name) {
		File file = new File(path, name);
		return file.exists();
	}
	
	public static String createDirecotryByBizAndTime(String bizType, String configDir) {
		StringBuilder builder = new StringBuilder(configDir);
		builder.append("/").append(bizType);
		
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		
		builder.append("/").append(year);
		builder.append("/").append(month);
		builder.append("/").append(day);
		
		builder.append("/");
		
		return builder.toString();
	}
	
	private static String getFtpHost(String bizType, String tenant) {
		Map<String, FtpEntry> configs = ftpConfigsByBizType(bizType);
		if (null == configs.get(tenant)) {
			throw new FileBizException(String.format("tenant为%s,bizType为%s的配置条目不存在", tenant, bizType));
		}
		
		return configs.get(tenant).getHost();
	}
	
	private static String getFtpPort(String bizType, String tenant) {
		Map<String, FtpEntry> configs = ftpConfigsByBizType(bizType);
		if (null == configs.get(tenant)) {
			throw new FileBizException(String.format("tenant为%s,bizType为%s的配置条目不存在", tenant, bizType));
		}
		
		return configs.get(tenant).getPort();
	}
	
	private static String getFtpUser(String bizType, String tenant) {
		Map<String, FtpEntry> configs = ftpConfigsByBizType(bizType);
		if (null == configs.get(tenant)) {
			throw new FileBizException(String.format("tenant为%s,bizType为%s的配置条目不存在", tenant, bizType));
		}
		
		return configs.get(tenant).getUser();
	}
	
	private static String getFtpPassword(String bizType, String tenant) {
		Map<String, FtpEntry> configs = ftpConfigsByBizType(bizType);
		if (null == configs.get(tenant)) {
			throw new FileBizException(String.format("tenant为%s,bizType为%s的配置条目不存在", tenant, bizType));
		}
		
		return configs.get(tenant).getPassword();
	}
	
	private static Map<String, FtpEntry> ftpConfigsByBizType(String bizType) {
		String direction = FileConfigParser.attrByNode(bizType, FileConfigParser.FILE_CONFIG,
			FileConfigParser.DIRECTION);
		if ("parse".equals(direction)) {
			return ftpReadConfigs;
		}
		
		if ("assemble".equals(direction)) {
			return ftpConfigs;
		}
		
		throw new FileBizException(
			String.format("业务类型为%s的direction配置异常,当前值为%s，只能配置为parse或者assemble", bizType, direction));
	}
}
