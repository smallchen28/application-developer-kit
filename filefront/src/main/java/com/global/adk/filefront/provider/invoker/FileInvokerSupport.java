/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 14:13 创建
 *
 */
package com.global.adk.filefront.provider.invoker;

import com.global.adk.biz.executor.AbstractInvokeService;
import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.filefront.dal.mapper.FileDbOperator;
import com.global.adk.filefront.listeners.FileEventBus;
import com.global.adk.filefront.schedule.task.FileTaskExecutor;
import com.global.common.lang.result.StandardResultInfo;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author karott
 */
public abstract class FileInvokerSupport<P, R extends StandardResultInfo> extends AbstractInvokeService<P, R> {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected FileEventBus fileEventBus;
	@Autowired
	protected FileDbOperator fileDbOperator;
	@Autowired
	protected FileTaskExecutor fileTaskExecutor;
	
	@Override
	public void before(ServiceContext<P, R> serviceContext) {
	
	}
	
	@Override
	public void after(ServiceContext<P, R> serviceContext) {
	
	}
	
	@Override
	public void end(ServiceContext<P, R> serviceContext) {
	
	}
	
}
