/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.biz.executor.statement;

import com.yiji.adk.biz.executor.ServiceContext;
import com.yiji.adk.biz.executor.event.InitEvent;
import com.yiji.adk.biz.executor.event.ServiceApplyEvent;
import com.yiji.adk.biz.executor.event.ServiceFinishEvent;
import com.yiji.adk.event.Subscribe;
import com.yjf.common.lang.context.OperationContext;
import com.yjf.common.lang.context.OperationContext.OperationTypeEnum;
import com.yjf.common.lang.result.Status;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 服务申请，对于申请阶段、处理阶段、出错阶段、摘要日志、运行轨迹进行处理。
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月16日 下午1:02:41<br>
 * @see
 * @since 1.0.0
 */
public class LogStatement {
	
	@Subscribe(priority = 1, isAsync = false)
	public void applyLog(ServiceApplyEvent event) {
		ServiceContext<?, ?> serviceContext = event.value();
		
		Logger logger = serviceContext.getLogger();
		
		if (logger.isInfoEnabled()) {
			logger.info("收到{},系统时间(数据库){},操作[request={},operationContext={}", serviceContext.getInvokeElement()
				.getServiceName(), serviceContext.currentTimestamp(), serviceContext.getParameter(),
				serviceContext.operationContext());
		}
	}
	
	//-  这里日志利用logger自身的异步进行处理，单独异步。
	@Subscribe(priority = 1, isAsync = false)
	public void activeTrackerLog(ServiceFinishEvent event) {
		ServiceContext<?, ?> serviceContext = event.value();
		Logger logger = serviceContext.getLogger();
		if (logger.isInfoEnabled()) {
			Map<String, String> activeTrace = serviceContext.getActiveTrace();
			if (activeTrace != null && activeTrace.size() > 0) {
				logger.info("系统活动轨迹 \n");
				
				if (activeTrace != null && !activeTrace.isEmpty()) {
					
					for (Iterator<Entry<String, String>> it = activeTrace.entrySet().iterator(); it.hasNext();) {
						Entry<String, String> activeTraceItem = it.next();
						logger.info("\t{}--> {}", activeTraceItem.getKey(), activeTraceItem.getValue());
					}
					
				}
			}
		}
	}
	
	@Subscribe(priority = 2, isAsync = false)
	public void finishLog(ServiceFinishEvent event) {
		ServiceContext<?, ?> serviceContext = event.value();
		Logger logger = serviceContext.getLogger();
		if (logger.isInfoEnabled()) {

			logger.info("处理【{}】完成![param={}###result={}]，服务耗时：{}",new Object[] { serviceContext.getInvokeElement().getServiceName(), serviceContext.getParameter(),
					serviceContext.result() ,System.currentTimeMillis() - serviceContext.getBegin()});
		}
	}
	
	@Subscribe(priority = 3, isAsync = false)
	public void accessLog(ServiceFinishEvent event) {
		ServiceContext<?, ?> serviceContext = event.value();
		Logger logger = serviceContext.getLogger();
		OperationContext context = serviceContext.operationContext();
		OperationTypeEnum operationType = context == null ? null : context.getOperationType();
		if (logger.isInfoEnabled()) {
			logger.info(
				"operationAction.code= {} , operationType={} , operationId={} , operationIp={} , operationName={} ",
				serviceContext.getInvokeElement().getServiceName(), context == null ? "" : operationType == null ? ""
					: operationType.name(), context == null ? "" : context.getOperationId(), context == null ? ""
					: context.getOperationIp(), context == null ? "" : context.getOperationName());
		}
	}
	
	@Subscribe(isAsync = false)
	public void initializer(InitEvent event) {
		
	}
	
}
