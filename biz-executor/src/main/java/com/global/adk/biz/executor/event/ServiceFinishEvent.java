/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.event;

import com.global.adk.biz.executor.ActivityExecutorContainer;
import com.global.adk.biz.executor.ServiceContext;

/**
 * 服务完结事件
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see ExecutorEvent
 * @history hasuelee创建于2014年9月17日 下午8:56:43<br>
 */
@SuppressWarnings("rawtypes")
public class ServiceFinishEvent implements ExecutorEvent<ActivityExecutorContainer, ServiceContext<?, ?>> {
	
	private ActivityExecutorContainer executor;
	
	private ServiceContext<?, ?> serviceContext;
	
	public ServiceFinishEvent(ActivityExecutorContainer executor, ServiceContext<?, ?> serviceContext) {
		
		this.executor = executor;
		this.serviceContext = serviceContext;
	}
	
	@Override
	public ActivityExecutorContainer source() {
		
		return executor;
	}
	
	@Override
	public ServiceContext value() {
		
		return serviceContext;
	}
}
