/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 20:28 创建
 *
 */
package com.global.adk.filefront.listeners.events.inner;

import com.google.common.collect.Maps;
import com.global.adk.filefront.dal.entity.FileNotify;
import com.global.adk.api.enums.FileServiceType;
import com.global.common.util.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 请求文件通知事件
 *
 * @author karott
 */
public class FileNotifyEvent implements Serializable {
	
	/**
	 * 请求id
	 */
	private String reqId;
	
	/**
	 * 响应id
	 */
	private String rspId;
	
	/**
	 * 批次号
	 */
	private String batchNo;
	
	/**
	 * 租户，大类型
	 */
	private String tenant;
	
	/**
	 * 类型，业务类型
	 */
	private String bizType;
	
	/**
	 * 文件服务类型
	 */
	private FileServiceType fileService = FileServiceType.FTP;
	
	/**
	 * 文件路径,绝对路径
	 */
	private String filePath;
	
	/**
	 * 文件名
	 */
	private String fileName;
	
	/**
	 * 文件时间戳
	 */
	private Date fileTime = new Date();
	
	private String partnerId;
	private String gid;
	private String merchantOrderNo;
	
	/**
	 * 确认数据
	 */
	private final Map<String, String> confirmData = Maps.newHashMap();
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private FileNotify fileNotify;
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
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
	
	public String getBatchNo() {
		return batchNo;
	}
	
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	
	public String getPartnerId() {
		return partnerId;
	}
	
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public String getGid() {
		return gid;
	}
	
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}
	
	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	
	public Map<String, String> getConfirmData() {
		return confirmData;
	}
	
	public FileNotify getFileNotify() {
		return fileNotify;
	}
	
	public void setFileNotify(FileNotify fileNotify) {
		this.fileNotify = fileNotify;
	}
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
