/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.plan.task;

import com.yiji.adk.plan.task.module.PlanTask;

/**
 * trigger创建工厂
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年1月16日
 * @version 1.0.0
 * @see
 */
public enum TaskTriggerFactory {
	
	CronTrigger(PlanTask.TaskType.cron) {
		@Override
		public PlanTaskTrigger newInstance(PlanTask task) {
			return new CronTaskTrigger(task.getTaskExp(), task.getTaskName());
		}
	};
	
	private TaskTriggerFactory(PlanTask.TaskType taskType) {
		
		this.taskType = taskType;
	}
	
	private PlanTask.TaskType taskType;
	
	public static TaskTriggerFactory code(PlanTask.TaskType taskType) {
		
		TaskTriggerFactory factory = null;
		
		for (TaskTriggerFactory each : values()) {
			if (each.taskType.equals(taskType)) {
				factory = each;
				break;
			}
		}
		
		return factory;
	}
	
	public abstract PlanTaskTrigger newInstance(PlanTask task);
}
