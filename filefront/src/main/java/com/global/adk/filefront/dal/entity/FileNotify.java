/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 14:40 创建
 *
 */
package com.global.adk.filefront.dal.entity;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.global.adk.active.record.module.AggregateRoot;
import com.global.adk.common.Constants;
import com.global.adk.api.enums.FileServiceType;
import com.global.adk.filefront.support.consts.StatusConsts;
import com.yjf.common.util.ToString;

import java.util.Date;
import java.util.Map;

/**
 * @author karott
 */
public class FileNotify extends AggregateRoot {
	
	private long id;
	private String reqId;
	private String rspId;
	private String batchNo;
	private String tenant;
	private String bizType;
	private FileServiceType fileService = FileServiceType.FTP;
	private String status = StatusConsts.PROCESSING;
	private String state = StatusConsts.INIT;
	private String errorCode = Constants.SUCCESS_CODE;
	private String errorMsg;
	private String filePath;
	private String fileName;
	private Date fileTime = new Date();
	private String localFilePath;
	private String localFileName;
	private Date localFileTime = new Date();
	private String confirmDataStr;
	private Map<String, String> confirmData;
	private String dubboGroup;
	private String dubboVersion;
	private String partnerId;
	private String gid;
	private String merchantOrderNo;
	private Date rawAddTime = new Date();
	private Date rawUpdateTime = new Date();
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
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
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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
	
	public String getConfirmDataStr() {
		return confirmDataStr;
	}
	
	public <T> T buildConfirmDataStr() {
		if (null == confirmDataStr && null != confirmData && 0 != confirmData.size()) {
			confirmDataStr = Joiner.on(",").withKeyValueSeparator("=").join(confirmData);
		}
		
		return (T) this;
	}
	
	public <T> T buildConfirmData() {
		if ((null == confirmData || 0 == confirmData.size()) && null != confirmDataStr) {
			confirmData = Splitter.on(",").withKeyValueSeparator("=").split(confirmDataStr);
		}
		
		return (T) this;
	}
	
	public void setConfirmDataStr(String confirmDataStr) {
		this.confirmDataStr = confirmDataStr;
	}
	
	public Map<String, String> getConfirmData() {
		return confirmData;
	}
	
	public void setConfirmData(Map<String, String> confirmData) {
		this.confirmData = confirmData;
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
	
	public Date getRawAddTime() {
		return rawAddTime;
	}
	
	public void setRawAddTime(Date rawAddTime) {
		this.rawAddTime = rawAddTime;
	}
	
	public Date getRawUpdateTime() {
		return rawUpdateTime;
	}
	
	public void setRawUpdateTime(Date rawUpdateTime) {
		this.rawUpdateTime = rawUpdateTime;
	}
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
