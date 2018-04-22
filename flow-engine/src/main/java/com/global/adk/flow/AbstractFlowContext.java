/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow;

import com.alibaba.fastjson.JSON;
import com.global.adk.common.exception.FlowException;
import com.global.adk.common.exception.KitNestException;
import com.global.adk.flow.engine.FlowEngine;
import com.global.adk.flow.module.Flow;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * 活动上下文骨架实现
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-1-19 下午5:35<br>
 * @see
 * @since 1.0.0
 */
public abstract class AbstractFlowContext extends PathMatchingResourcePatternResolver implements FlowContext,
																						Lifecycle,
																						ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(FlowContext.class);
	
	private FlowDefXmlDocumentReader flowDefXmlDocumentReader = new FlowDefXmlDocumentReader(this);
	
	protected FlowEngine flowEngine;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化引擎
		this.flowEngine = new FlowEngine();
		
		AutowireCapableBeanFactory autowireBeanFactory = applicationContext.getAutowireCapableBeanFactory();
		
		autowireBeanFactory.autowireBeanProperties(flowEngine, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
		
		autowireBeanFactory.initializeBean(flowEngine, FlowEngine.class.getName());
	}
	
	@Override
	public synchronized void loadDefinition(String location) {
		
		if (logger.isInfoEnabled()) {
			logger.info("流程定义加载配置路径:{}", location);
		}
		try {
			Resource[] resources = getResources(location);
			
			for (int i = 0, j = resources.length; i < j; i++) {
				Resource resource = resources[i];
				if (logger.isInfoEnabled()) {
					logger.info("加载配置文件:{}", resource.getURL());
				}
				if (!resource.exists()) {
					throw new FlowException(String.format("%s流程定义文件不存在", resource.getURL()));
				}
				
				flowDefXmlDocumentReader.analyze(resource);
			}
			
		} catch (IOException e) {
			throw new FlowException(String.format("加载配置过程中出现错误，path=%s", location), e);
		}
	}
	
	@Override
	public void registry(Flow flow) {
		try {
			//委托给引擎完成
			flowEngine.registry(flow);
			if (logger.isInfoEnabled()) {
				logger.info("注册流程定义成功Flow={}", JSON.toJSONString(flow, true));
			}
		} catch (Exception e) {
			if (e instanceof KitNestException) {
				throw (KitNestException) e;
			}
			throw new FlowException(String.format("注册流程定义过程出错，Flow=%s,Version=%s", flow.getName(), flow.getVersion()),
				e);
		}
	}
	
	@Override
	public void destroy() throws Exception {
		
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
