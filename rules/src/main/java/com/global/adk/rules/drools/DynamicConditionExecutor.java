/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.rules.drools;

import com.global.adk.rules.drools.module.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Observable;

/**
 * 动态解析执行器，分别groovy，动态编译支持。
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月10日
 * @version 1.0.0
 */
public abstract class DynamicConditionExecutor extends Observable implements ApplicationContextAware {
	
	protected Logger logger = LoggerFactory.getLogger(DynamicConditionExecutor.class);
	
	protected ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	public abstract ExecutorWrapper generateExecutorWrapper(Condition condition);
	
	public abstract boolean execute(long identity, Object compareValue, String symbol, Object... parameters);
}
