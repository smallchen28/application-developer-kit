/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.plan.task;

import java.util.Date;

/**
 * 触发器时间上下文。
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年1月23日
 * @version 1.0.0
 * @see
 */
public class TriggerContext {
	
	private Date previousExecutionTime;
	
	private Date calculateExecutionTime;
	
	private Date complateTime;
	
	public void updateTime(Date previousExecutionTime, Date calculateExectuionTime, Date complateTime) {
		
		if (previousExecutionTime != null)
			this.previousExecutionTime = previousExecutionTime;
		
		if (calculateExectuionTime != null)
			this.calculateExecutionTime = calculateExectuionTime;
		
		if (complateTime != null)
			this.complateTime = complateTime;
	}
	
	public Date getPreviousExecutionTime() {
		
		return previousExecutionTime;
	}
	
	public Date getCalculateExecutionTime() {
		
		return calculateExecutionTime;
	}
	
	public Date getComplateTime() {
		
		return complateTime;
	}
	
	public void setPreviousExecutionTime(Date previousExecutionTime) {
		
		this.previousExecutionTime = previousExecutionTime;
	}
	
	public void setCalculateExecutionTime(Date calculateExecutionTime) {
		
		this.calculateExecutionTime = calculateExecutionTime;
	}
	
	public void setComplateTime(Date complateTime) {
		
		this.complateTime = complateTime;
	}
	
}
