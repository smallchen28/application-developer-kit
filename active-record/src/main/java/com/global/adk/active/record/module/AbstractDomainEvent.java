/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-05-12 15:27 创建
 *
 */
package com.global.adk.active.record.module;

import java.util.Date;

/**
 * 领域事件抽象基础
 * @author chenlin@yiji.com
 */
public abstract class AbstractDomainEvent {
	
	private Date eventTime;
	
	private String eventName;
	
	public AbstractDomainEvent(String eventName) {
		this.eventName = eventName;
		eventTime = new Date();
	}
	
	public Date getEventTime() {
		return eventTime;
	}
	
	public String getEventName() {
		return eventName;
	}
	
}
