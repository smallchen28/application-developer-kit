/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.global.adk.plan.task.module;

import javax.xml.bind.annotation.*;
import java.sql.Timestamp;

/**
 * 计划任务条目
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 */
@XmlRootElement(name = "statement")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanTaskStatement implements Comparable<PlanTaskStatement> {
	
	@XmlTransient
	private long planTaskIdentity;
	
	/** 任务条目名称 */
	@XmlAttribute(name = "task-state-name", required = true)
	private String taskStateName;
	
	/** 条目类名称 */
	@XmlAttribute(name = "state-class-type", required = true)
	private String stateClassType;
	
	/** 运行时信息 */
	@XmlAttribute(name = "exec-info")
	private String execInfo;
	
	/** 创建描述 */
	@XmlAttribute(name = "crt-jobd", required = true)
	private String crtJobd;
	
	/** 执行优先级 */
	@XmlAttribute(name = "exec-priority", required = true)
	private int execPriority;
	
	//- 持久层数据
	
	/** 执行状态 */
	@XmlTransient
	private String execStatus;
	
	/** 开始执行 */
	@XmlTransient
	private Timestamp startTIme;
	
	/** 结束时间 */
	@XmlTransient
	private Timestamp endTime;
	
	/** 执行次数 */
	@XmlTransient
	private int execCount;
	
	public long getPlanTaskIdentity() {
		
		return planTaskIdentity;
	}
	
	public void setPlanTaskIdentity(long planTaskIdentity) {
		
		this.planTaskIdentity = planTaskIdentity;
	}
	
	public String getTaskStateName() {
		
		return taskStateName;
	}
	
	public void setTaskStateName(String taskStateName) {
		
		this.taskStateName = taskStateName;
	}
	
	public String getStateClassType() {
		
		return stateClassType;
	}
	
	public void setStateClassType(String stateClassType) {
		
		this.stateClassType = stateClassType;
	}
	
	public String getCrtJobd() {
		
		return crtJobd;
	}
	
	public void setCrtJobd(String crtJobd) {
		
		this.crtJobd = crtJobd;
	}
	
	public int getExecPriority() {
		
		return execPriority;
	}
	
	public void setExecPriority(int execPriority) {
		
		this.execPriority = execPriority;
	}
	
	public String getExecStatus() {
		
		return execStatus;
	}
	
	public void setExecStatus(String execStatus) {
		
		this.execStatus = execStatus;
	}
	
	public String getExecInfo() {
		
		return execInfo;
	}
	
	public void setExecInfo(String execInfo) {
		
		this.execInfo = execInfo;
	}
	
	public Timestamp getStartTIme() {
		
		return startTIme;
	}
	
	public void setStartTIme(Timestamp startTIme) {
		
		this.startTIme = startTIme;
	}
	
	public Timestamp getEndTime() {
		
		return endTime;
	}
	
	public void setEndTime(Timestamp endTime) {
		
		this.endTime = endTime;
	}
	
	public int getExecCount() {
		
		return execCount;
	}
	
	public void setExecCount(int execCount) {
		
		this.execCount = execCount;
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (planTaskIdentity ^ (planTaskIdentity >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanTaskStatement other = (PlanTaskStatement) obj;
		if (planTaskIdentity != other.planTaskIdentity)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(PlanTaskStatement o) {
		
		return this.execPriority > o.execPriority ? 1 : this.execPriority == o.execPriority ? 0 : -1;
	}
	
	@Override
	public String toString() {
		
		return "PlanTaskStatement [planTaskIdentity=" + planTaskIdentity + ", taskStateName=" + taskStateName
				+ ", stateClassType=" + stateClassType + ", execInfo=" + execInfo + ", crtJobd=" + crtJobd
				+ ", execPriority=" + execPriority + ", execStatus=" + execStatus + ", startTIme=" + startTIme
				+ ", endTime=" + endTime + ", execCount=" + execCount + "]";
	}
	
}
