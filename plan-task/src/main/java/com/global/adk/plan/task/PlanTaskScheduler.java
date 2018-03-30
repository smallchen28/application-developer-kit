/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.plan.task;

import com.global.adk.plan.task.module.PlanTask;

/**
 * 计划任务调度接口实现
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月30日 上午11:46:35<br>
 */
public class PlanTaskScheduler {
	
	private PlanTask planTaskPrototype;
	
	private PlanTaskTrigger trigger;
	
	private TriggerContext triggerContext = new TriggerContext();
	
	public PlanTaskScheduler(PlanTask planTask, PlanTaskTrigger trigger) {
		
		this.planTaskPrototype = planTask;
		this.trigger = trigger;
		
	}

	public PlanTask getPlanTaskPrototype() {
		
		return planTaskPrototype;
	}
	
	public void setPlanTaskPrototype(PlanTask planTaskPrototype) {
		
		this.planTaskPrototype = planTaskPrototype;
	}
	
	public PlanTaskTrigger getTrigger() {
		
		return trigger;
	}
	
	public void setTrigger(PlanTaskTrigger trigger) {
		
		this.trigger = trigger;
	}
	
	public TriggerContext getTriggerContext() {
		
		return triggerContext;
	}
	
	public void setTriggerContext(TriggerContext triggerContext) {
		
		this.triggerContext = triggerContext;
	}
	
	@Override
	public String toString() {
		
		return "PlanTaskScheduler [planTaskPrototype=" + planTaskPrototype + ", trigger=" + trigger
				+ ", triggerContext=" + triggerContext + "]";
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planTaskPrototype == null) ? 0 : planTaskPrototype.hashCode());
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
		PlanTaskScheduler other = (PlanTaskScheduler) obj;
		if (planTaskPrototype == null) {
			if (other.planTaskPrototype != null)
				return false;
		} else if (!planTaskPrototype.equals(other.planTaskPrototype))
			return false;
		return true;
	}
	
}
