package com.global.adk.event;
/**
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.yjf.common.util.StringUtils;

/**
 *
 * 修订记录： lingxu@yiji.com 2016/9/13 11:15 创建
 */
public class EventSupport implements ApplicationContextAware, InitializingBean {
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		Map<String, Object> listeners = applicationContext.getBeansWithAnnotation(Listener.class);
		
		if (listeners != null && listeners.size() > 0) {
			
			listeners.entrySet().stream().forEach(entry -> {
				
				Listener listener = entry.getValue().getClass().getAnnotation(Listener.class);
				
				Notifier notifier = applicationContext.getBean(
					StringUtils.isBlank(listener.notifier()) ? "notifierBus" : listener.notifier(),
					Notifier.class);
					
				notifier.register(entry.getValue());
				
			});
		}
		
	}
}
