package com.yiji.adk.biz.executor.event;

import com.yiji.adk.biz.executor.ActivityExecutorContainer;

public class InitEvent implements ExecutorEvent<ActivityExecutorContainer, Void> {
	
	private ActivityExecutorContainer activityExecutorContainer;
	
	public InitEvent(ActivityExecutorContainer activityExecutorContainer) {
		this.activityExecutorContainer = activityExecutorContainer;
	}
	
	@Override
	public ActivityExecutorContainer source() {
		
		return activityExecutorContainer;
	}
	
	@Override
	public Void value() {
		
		return null;
	}
	
}
