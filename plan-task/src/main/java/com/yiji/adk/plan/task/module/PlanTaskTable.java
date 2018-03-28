/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.yiji.adk.plan.task.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 执行任务列表定义
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 */
@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanTaskTable {
	
	@XmlElement(name = "task")
	private List<PlanTask> tasks;
	
	public List<PlanTask> getTasks() {
		
		return tasks;
	}
	
	public void setTasks(List<PlanTask> tasks) {
		
		this.tasks = tasks;
	}
	
	@Override
	public String toString() {
		
		return "PlanTaskTable [tasks=" + tasks + "]";
	}
	
}
