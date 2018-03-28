/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-19 14:53 创建
 *
 */
package com.yiji.adk.biz.executor.support;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import com.yjf.common.spring.ApplicationContextHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 服务provider生成器
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public abstract class AbstractServiceAPIProviderGenerator implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private volatile boolean generated = false;

	private String baseFacadePath;

	public AbstractServiceAPIProviderGenerator(String baseFacadePath) {
		this.baseFacadePath = baseFacadePath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ApplicationContext context = ApplicationContextHolder.get();

		if (!generated) {
			synchronized (this) {
				if (!generated) {
					generate(context.getAutowireCapableBeanFactory());
				}
			}
		}
	}
	
	private void generate(AutowireCapableBeanFactory beanFactory) {
		init();
		
		List<ServiceDescriptor> services = doGenerate();
		
		addToSpringContainer(services, beanFactory);
		
		clean(services);
	}
	
	private void addToSpringContainer(List<ServiceDescriptor> services, AutowireCapableBeanFactory beanFactory) {
		logger.info("+++++++++++++++++++++生成服务列表++++++++++++++++++++++++++");
		
		services.stream().forEach((service) -> {
			logger.info("生成服务provider bean:{}", service);
			beanFactory.createBean(service.getImplemention(), AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
		});
	}
	
	private void clean(List<ServiceDescriptor> services) {
	}
	
	/**
	 * 具体子类生成
	 */
	protected abstract List<ServiceDescriptor> doGenerate();
	
	/**
	 * 初始化
	 */
	protected abstract void init();
	
	public String getBaseFacadePath() {
		return baseFacadePath;
	}
	
	public void setBaseFacadePath(String baseFacadePath) {
		this.baseFacadePath = baseFacadePath;
	}
}
