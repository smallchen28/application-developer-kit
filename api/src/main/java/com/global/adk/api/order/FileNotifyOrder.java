/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-24 21:24 创建
 *
 */
package com.global.adk.api.order;

import com.google.common.collect.Maps;
import com.global.adk.api.enums.FileServiceType;
import com.yjf.common.service.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * @author karott
 */
public class FileNotifyOrder extends com.yjf.common.service.OrderBase {
	
	/**
	 * 请求id,批次号
	 */
	@NotBlank
	@Size(min = 20, max = 20)
	private String reqId;
	
	/**
	 * 关联id,响应id
	 */
	@Size(max = 20)
	private String rspId;
	
	/**
	 * 租户，大类型
	 */
	@NotBlank
	@Size(max = 32)
	private String tenant;
	
	/**
	 * 类型，业务类型
	 */
	@NotBlank
	@Size(max = 64)
	private String bizType;
	
	/**
	 * 文件服务类型
	 */
	@NotNull
	private FileServiceType fileService = FileServiceType.FTP;
	
	/**
	 * 文件路径,绝对路径
	 */
	@NotBlank
	@Size(max = 1024)
	private String filePath;
	
	/**
	 * 文件名
	 */
	@NotBlank
	@Size(max = 128)
	private String fileName;
	
	/**
	 * 文件时间戳
	 */
	@NotNull
	private Date fileTime = new Date();
	
	/**
	 * 回执dubbo分组，为空表示不需要回执通知
	 */
	@Size(max = 32)
	private String dubboGroup;
	
	/**
	 * 回执dubbo版本
	 */
	@Size(max = 16)
	private String dubboVersion;
	
	/**
	 * 确认数据. 确认数据用，与业务确定，可随业务要求扩展
	 *
	 * <p/>
	 * 会在文件处理各个阶段发出相关事件(并把此数据带上)，以便业务方扩展一些校验业务
	 */
	private final Map<String, String> confirmData = Maps.newHashMap();
	
	public String getReqId() {
		return reqId;
	}
	
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	
	public String getRspId() {
		return rspId;
	}
	
	public void setRspId(String rspId) {
		this.rspId = rspId;
	}
	
	public String getTenant() {
		return tenant;
	}
	
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	public String getBizType() {
		return bizType;
	}
	
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public FileServiceType getFileService() {
		return fileService;
	}
	
	public void setFileService(FileServiceType fileService) {
		this.fileService = fileService;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Date getFileTime() {
		return fileTime;
	}
	
	public void setFileTime(Date fileTime) {
		this.fileTime = fileTime;
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
	
	public Map<String, String> getConfirmData() {
		return confirmData;
	}
}
