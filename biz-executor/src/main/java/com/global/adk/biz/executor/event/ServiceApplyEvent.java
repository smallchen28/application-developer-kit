/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.event;

import com.global.adk.biz.executor.ActivityExecutorContainer;
import com.global.adk.biz.executor.ServiceContext;

/**
 * 服务申请事件
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see ExecutorEvent
 * @history hasuelee创建于2014年9月16日 下午1:06:39<br>
 */
@SuppressWarnings("rawtypes")
public class ServiceApplyEvent implements ExecutorEvent<ActivityExecutorContainer, ServiceContext<?, ?>> {
	
	private ActivityExecutorContainer executor;
	
	private ServiceContext<?, ?> serviceContext;
	
	public ServiceApplyEvent(ActivityExecutorContainer executor, ServiceContext<?, ?> serviceContext) {
		
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
