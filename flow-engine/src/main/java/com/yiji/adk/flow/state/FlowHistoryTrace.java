/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/7-17:24 创建
 *
 */

package com.yiji.adk.flow.state;

import com.yjf.common.id.GID;

import java.util.Date;

/**
 * 历史执行
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class FlowHistoryTrace extends FlowTrace {
	
	/**
	 * 完成时间
	 */
	private Date endTime;
	
	/**
	 * 错误信息
	 */
	private String error;
	
	public FlowHistoryTrace() {
		this(GID.newGID());
	}
	
	public FlowHistoryTrace(String eventId) {
		this(eventId, new Date());
	}
	
	public FlowHistoryTrace(String eventId, Date eventTime) {
		super(eventId, eventTime);
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}