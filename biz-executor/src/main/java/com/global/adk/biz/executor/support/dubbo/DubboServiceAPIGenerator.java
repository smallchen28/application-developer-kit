/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-19 15:06 创建
 *
 */
package com.yiji.adk.biz.executor.support.dubbo;

import com.google.common.collect.Lists;
import com.yiji.adk.api.annotation.DubboServiceAPI;
import com.yiji.adk.biz.executor.ExecutorContainer;
import com.yiji.adk.biz.executor.support.AbstractServiceAPIProviderGenerator;
import com.yiji.adk.biz.executor.support.ServiceDescriptor;
import com.yjf.common.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * dubbo provider生成
 * 
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class DubboServiceAPIGenerator extends AbstractServiceAPIProviderGenerator {
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	private ExecutorContainer executorContainer;
	
	public DubboServiceAPIGenerator(String baseFacadePath, ExecutorContainer executorContainer) {
		super(baseFacadePath);
		this.executorContainer = executorContainer;
	}
	
	@Override
	protected List<ServiceDescriptor> doGenerate() {
		List<ServiceDescriptor> services = Lists.newArrayList();
		
		if (StringUtils.isNotBlank(getBaseFacadePath())) {
			String[] facadePaths = getBaseFacadePath().split(",");
			
			Resource[] resources;
			for (String path : facadePaths) {
				try {
					resources = resourcePatternResolver.getResources(path + "/**/*.class");
					
					for (Resource resource : resources) {
						String fileName = resource.getURL().getFile();
						String className = className(fileName);
						
						ServiceDescriptor descriptor = createServiceDescriptor(className);
						if (null != descriptor) {
							services.add(descriptor);
						}
					}
					
				} catch (IOException e) {
					throw new IllegalStateException("dubbo provider meta资源扫描异常", e);
				}
			}
		}
		
		return services;
	}
	
	private String className(String fileName) {
		if (fileName.contains("jar!")) {
			return fileName.substring(fileName.lastIndexOf("jar!") + "jar!/".length()).replace("/", ".")
				.replace(".class", "");
		}
		
		return fileName.substring(fileName.lastIndexOf("classes") + "classes/".length()).replace("/", ".")
			.replace(".class", "");
	}
	
	private ServiceDescriptor createServiceDescriptor(String className) {
		ServiceDescriptor service = null;
		DubboServiceAPI api;
		Class<?> serviceClass = null;
		try {
			serviceClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			//nothing
		}
		
		if (null != serviceClass) {
			api = serviceClass.getDeclaredAnnotation(DubboServiceAPI.class);
			if (null != api) {
				service = new ServiceDescriptor(
					DubboProviderGenerateFactory.createImplementionByService(serviceClass, api), serviceClass,
					api.group(), api.version(), "dubbo");
			}
		}
		
		return service;
	}
	
	@Override
	protected void init() {
	}
}
