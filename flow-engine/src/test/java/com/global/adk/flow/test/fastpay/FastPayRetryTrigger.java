/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-02-21 17:21 创建
 *
 */
package com.global.adk.flow.test.fastpay;

import com.google.common.collect.Maps;
import com.global.adk.flow.state.retry.FlowRetryTrigger;
import com.global.adk.flow.state.retry.RetryFailTypeEnum;
import com.global.common.lang.result.Status;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
@Component
public class FastPayRetryTrigger extends FlowRetryTrigger {
	
	private static final Logger logger = LoggerFactory.getLogger(FastPayRetryTrigger.class);
	
	@Override
	protected void beforeRetry(	String orderId, String flowName, int version, String retryNode, String targetNode,
								RetryFailTypeEnum retryFail, int retryMax, int retryTimes, Date startTime,
								Date lastRetryTime) {
	}
	
	@Override
	protected void afterRetry(	String orderId, String flowName, int version, String retryNode, String targetNode,
								RetryFailTypeEnum retryFail, int retryMax, int retryTimes, Date startTime,
								Date lastRetryTime) {
	}
	
	@Override
	protected Object target(String orderId) {
		Consumer consumer = new Consumer("极限恶女剑", 90);
		consumer.setStatus(Status.PROCESSING);
		consumer.setNode("initialize");
		Customer customer = new Customer("李根", consumer);
		
		return customer;
	}
	
	@Override
	protected Map<String, Object> attachment(String orderId, Object target) {
		Map<String, Object> attrs = Maps.newHashMap();
		attrs.put("memo", "retry it");
		
		return attrs;
	}
}
