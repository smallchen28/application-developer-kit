/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor;

import com.global.adk.common.exception.InitializerException;
import com.yjf.common.lang.result.StandardResultInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 骨架实现
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月19日 下午3:42:55<br>
 */
public abstract class AbstractInvokeService<PARAM, RESULT extends StandardResultInfo> implements
																					InvokeService<PARAM, RESULT>,
																					InitializingBean,
																					ApplicationContextAware {
	
	protected ApplicationContext applicationContext;
	
	protected String invockServiceName;
	
	public void setBeanName(String name) {
		
		this.invockServiceName = name;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
																			throws org.springframework.beans.BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		Map<String, ActivityExecutorContainer> contianers = applicationContext.getBeansOfType(
			ActivityExecutorContainer.class, false, true);
		
		if (contianers == null || contianers.size() == 0)
			throw new InitializerException("ActivityExecutorContianer容器配置错误，无法获取spring对应的bean..");
		
		for (Entry<String, ActivityExecutorContainer> each : contianers.entrySet()) {
			each.getValue().registerInvockService(this);
		}
	}
	
	public String getInvockServiceName() {
		
		return invockServiceName;
	}
}
