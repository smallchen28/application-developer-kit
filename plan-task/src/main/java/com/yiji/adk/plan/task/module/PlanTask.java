/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.yiji.adk.plan.task.module;

import com.yiji.adk.plan.task.statement.ExecutorStatus;

import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.TreeSet;

/**
 * 计划任务
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 */
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanTask {
	
	/** 任务名称 */
	@XmlAttribute(name = "name", required = true)
	private String taskName;
	
	@XmlTransient
	private long identity;
	
	/** 计划任务类型 */
	@XmlElement(name = "task-type", defaultValue = "cron", required = true)
	private TaskType taskType;
	
	/** 计划任务过期时间 */
	@XmlElement(name = "task-expiration", defaultValue = "-1L")
	private long taskExpiration;
	
	/** 是否允许重复执行 */
	@XmlElement(name = "repat-flag", defaultValue = "false")
	private boolean repeatFlag;
	
	/** cron表达式 */
	@XmlElement(name = "task-exp", required = true)
	private String taskExp;
	
	/** 条目类名称 */
	@XmlElement(name = "exec-max-count", required = true)
	private int execMaxCount;
	
	/** 执行上下文 */
	@XmlElement(name = "exec-context", defaultValue = "")
	private String exeContext;
	
	/** 执行摘要 */
	@XmlElement(name = "exec-memo")
	private String execMemo;
	
	/** 任务创建描述 */
	@XmlElement(name = "crt-jobd")
	private String crtJobd;
	
	@XmlElementWrapper(name = "statements")
	@XmlElement(name = "statement")
	private TreeSet<PlanTaskStatement> statements;
	
	@XmlTransient
	private Timestamp rawAddTime;
	
	//- 部分内部运行信息
	@XmlTransient
	private Timestamp execStartTime;
	
	@XmlTransient
	private Timestamp execLastTime;
	
	@XmlTransient
	private Timestamp execNextTime;
	
	@XmlTransient
	private String currentStatName;
	
	@XmlTransient
	private ExecutorStatus execStatus = ExecutorStatus.INIT;
	
	@XmlTransient
	private int exeCount = 0;
	
	@XmlEnum
	public enum TaskType {
		cron,
		interval
	}
	
	public String getTaskName() {
		
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		
		this.taskName = taskName;
	}
	
	public long getIdentity() {
		
		return identity;
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
	}
	
	public TaskType getTaskType() {
		
		return taskType;
	}
	
	public void setTaskType(TaskType taskType) {
		
		this.taskType = taskType;
	}
	
	public long getTaskExpiration() {
		
		return taskExpiration;
	}
	
	public void setTaskExpiration(long taskExpiration) {
		
		this.taskExpiration = taskExpiration;
	}
	
	public boolean isRepeatFlag() {
		
		return repeatFlag;
	}
	
	public void setRepeatFlag(boolean repeatFlag) {
		
		this.repeatFlag = repeatFlag;
	}
	
	public String getTaskExp() {
		
		return taskExp;
	}
	
	public void setTaskExp(String taskExp) {
		
		this.taskExp = taskExp;
	}
	
	public String getExeContext() {
		
		return exeContext;
	}
	
	public void setExeContext(String exeContext) {
		
		this.exeContext = exeContext;
	}
	
	public String getExecMemo() {
		
		return execMemo;
	}
	
	public void setExecMemo(String execMemo) {
		
		this.execMemo = execMemo;
	}
	
	public String getCrtJobd() {
		
		return crtJobd;
	}
	
	public void setCrtJobd(String crtJobd) {
		
		this.crtJobd = crtJobd;
	}

	public TreeSet<PlanTaskStatement> getStatements() {
		return statements;
	}

	public void setStatements(TreeSet<PlanTaskStatement> statements) {
		this.statements = statements;
	}

	public Timestamp getRawAddTime() {
		
		return rawAddTime;
	}
	
	public void setRawAddTime(Timestamp rawAddTime) {
		
		this.rawAddTime = rawAddTime;
	}
	
	public Timestamp getExecStartTime() {
		
		return execStartTime;
	}
	
	public void setExecStartTime(Timestamp execStartTime) {
		
		this.execStartTime = execStartTime;
	}
	
	public Timestamp getExecLastTime() {
		
		return execLastTime;
	}
	
	public void setExecLastTime(Timestamp execLastTime) {
		
		this.execLastTime = execLastTime;
	}
	
	public Timestamp getExecNextTime() {
		
		return execNextTime;
	}
	
	public void setExecNextTime(Timestamp execNextTime) {
		
		this.execNextTime = execNextTime;
	}
	
	public String getCurrentStatName() {
		
		return currentStatName;
	}
	
	public void setCurrentStatName(String currentStatName) {
		
		this.currentStatName = currentStatName;
	}
	
	public ExecutorStatus getExecStatus() {
		
		return execStatus;
	}
	
	public void setExecStatus(ExecutorStatus execStatus) {
		
		this.execStatus = execStatus;
	}
	
	@Override
	public String toString() {
		
		return "PlanTask [taskName=" + taskName + ", identity=" + identity + ", taskType=" + taskType
				+ ", taskExpiration=" + taskExpiration + ", repeatFlag=" + repeatFlag + ", taskExp=" + taskExp
				+ ", execMaxCount=" + execMaxCount + ", exeContext=" + exeContext + ", execMemo=" + execMemo
				+ ", crtJobd=" + crtJobd + ", statements=" + statements + ", rawAddTime=" + rawAddTime
				+ ", execStartTime=" + execStartTime + ", execLastTime=" + execLastTime + ", execNextTime="
				+ execNextTime + ", currentStatName=" + currentStatName + ", execStatus=" + execStatus + ", exeCount="
				+ exeCount + "]";
	}
	
	public int getExeCount() {
		
		return exeCount;
	}
	
	public void setExeCount(int exeCount) {
		
		this.exeCount = exeCount;
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
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
		PlanTask other = (PlanTask) obj;
		if (taskName == null) {
			if (other.taskName != null)
				return false;
		} else if (!taskName.equals(other.taskName))
			return false;
		return true;
	}
	
	public int getExecMaxCount() {
		
		return execMaxCount;
	}
	
	public void setExecMaxCount(int execMaxCount) {
		
		this.execMaxCount = execMaxCount;
	}
	
}
