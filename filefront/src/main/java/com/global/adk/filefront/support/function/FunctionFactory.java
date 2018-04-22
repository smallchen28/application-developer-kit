/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-10-08 10:25 创建
 *
 */
package com.global.adk.filefront.support.function;

import com.google.common.collect.Maps;
import com.global.adk.filefront.exceptions.FileBizException;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Map;

/**
 * @author karott
 */
public class FunctionFactory implements BeanPostProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(FunctionParser.class);
	
	private Map<String, ConvertFunction> functions = Maps.newConcurrentMap();
	
	public String eval(String functionBeanName, Map<String, Object> params) {
		if (!functions.containsKey(functionBeanName)) {
			throw new FileBizException(String.format("不存在对应的函数:%s", functionBeanName));
		}
		
		return functions.get(functionBeanName).convert(params);
	}
	
	public String eval(ConvertFunction function, Map<String, Object> params) {
		return FunctionParser.parse(function, params);
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		if (bean instanceof ConvertFunction) {
			if (functions.containsKey(beanName)) {
				logger.warn("注册转换函数重复,将忽略:{}", beanName);
				return bean;
			}
			
			functions.putIfAbsent(beanName, (ConvertFunction) bean);
			logger.info("注册转换函数:{}", beanName);
		}
		
		return bean;
	}
}
