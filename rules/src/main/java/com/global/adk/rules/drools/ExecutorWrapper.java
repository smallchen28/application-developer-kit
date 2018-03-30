/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.rules.drools;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年9月1日
 * @version 1.0.0
 * @see
 */
public abstract class ExecutorWrapper implements ApplicationContextAware {
	
	protected ApplicationContext applicationContext;
	
	public abstract Object execute(Object[] parameters);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
