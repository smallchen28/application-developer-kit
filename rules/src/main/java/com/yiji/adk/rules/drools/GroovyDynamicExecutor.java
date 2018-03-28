/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.rules.drools;

import com.yiji.adk.rules.drools.module.Condition;

/**
 * Groovy实现
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月10日
 * @version 1.0.0
 * @see
 */
public class GroovyDynamicExecutor extends DynamicConditionExecutor {
	
	@Override
	public ExecutorWrapper generateExecutorWrapper(Condition condition) {
		
		return null;
	}
	
	@Override
	public boolean execute(long identity, Object compareValue, String symbol, Object... parameters) {
		
		return false;
	}
}
