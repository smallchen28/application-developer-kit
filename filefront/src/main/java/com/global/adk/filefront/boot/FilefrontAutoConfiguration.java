/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 19:48 创建
 *
 */
package com.global.adk.filefront.boot;

import com.alibaba.dubbo.config.*;
import com.global.adk.api.service.FileNotifyService;
import com.global.adk.common.boot.CommonConfiguration;
import com.global.adk.event.NotifierBus;
import com.global.adk.filefront.dal.mapper.FileDbOperator;
import com.global.adk.filefront.listeners.FileEventBus;
import com.global.adk.filefront.listeners.RequestFileNotifyListener;
import com.global.adk.filefront.listeners.ResponseFileNotifyListener;
import com.global.adk.filefront.provider.FileNotifyProvider;
import com.global.adk.filefront.provider.invoker.RequestFileNotifyInvoker;
import com.global.adk.filefront.schedule.RequestFileNotifySchedule;
import com.global.adk.filefront.schedule.ResponseFileNotifySchedule;
import com.global.adk.filefront.schedule.task.FileTaskExecutor;
import com.global.adk.filefront.support.config.FileConfigParser;
import com.global.adk.filefront.support.function.FunctionFactory;
import com.global.adk.filefront.support.transaction.FileTransactionManager;
import com.yjf.common.spring.ApplicationContextHolder;
import com.yjf.scheduler.service.api.ScheduleCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author karott
 */
@Configuration
@EnableConfigurationProperties({ FilefrontProperties.class })
@ConditionalOnProperty(value = CommonConfiguration.ADK_FILEFRONT, matchIfMissing = false)
public class FilefrontAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private FilefrontProperties filefrontProperties;
	
	private volatile boolean exported = false;
	
	@Bean
	@ConditionalOnMissingBean(FunctionFactory.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FunctionFactory functionFactory() {
		return new FunctionFactory();
	}
	
	@Bean
	@ConditionalOnMissingBean(FileTransactionManager.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FileTransactionManager fileTransactionManager(PlatformTransactionManager platformTransactionManager) {
		return new FileTransactionManager(platformTransactionManager);
	}
	
	@Bean
	@ConditionalOnMissingBean(FileDbOperator.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FileDbOperator fileDbOperator() {
		return new FileDbOperator(filefrontProperties.getDialect());
	}
	
	@Bean
	@ConditionalOnMissingBean(FileTaskExecutor.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FileTaskExecutor fileTaskExecutor() {
		return new FileTaskExecutor(filefrontProperties.getCoreThreads(), filefrontProperties.getMaxThreads(),
			filefrontProperties.getKeepAliveTime(), filefrontProperties.getCapacity());
	}
	
	@Bean
	@ConditionalOnMissingBean(FileEventBus.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FileEventBus fileEventBus(NotifierBus notifierBus) {
		
		return new FileEventBus(notifierBus);
	}
	
	@Bean
	@ConditionalOnMissingBean(RequestFileNotifyListener.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public RequestFileNotifyListener requestFileNotifyListener(FileEventBus fileEventBus) {
		RequestFileNotifyListener listener = new RequestFileNotifyListener();
		fileEventBus.register(listener);
		return listener;
	}
	
	@Bean
	@ConditionalOnMissingBean(ResponseFileNotifyListener.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public ResponseFileNotifyListener responseFileNotifyListener(FileEventBus fileEventBus) {
		ResponseFileNotifyListener listener = new ResponseFileNotifyListener();
		
		fileEventBus.register(listener);
		return listener;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FileConfigParser fileConfigContext() {
		FileConfigParser configContext = new FileConfigParser();
		configContext.initConfigs(filefrontProperties.getConfigLocation());
		return configContext;
	}
	
	@Bean
	@ConditionalOnMissingBean(RequestFileNotifyInvoker.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public RequestFileNotifyInvoker requestFileNotifyInvoker() {
		return new RequestFileNotifyInvoker();
	}
	
	@Bean
	@ConditionalOnMissingBean(FileNotifyProvider.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public FileNotifyProvider fileNotifyProvider() {
		return new FileNotifyProvider();
	}
	
	@Bean
	@ConditionalOnMissingBean(RequestFileNotifySchedule.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public RequestFileNotifySchedule requestFileNotifySchedule() {
		
		return new RequestFileNotifySchedule();
	}
	
	@Bean
	@ConditionalOnMissingBean(ResponseFileNotifySchedule.class)
	@ConditionalOnProperty(prefix = CommonConfiguration.ADK_FILEFRONT_PREFIX, name = "enable", matchIfMissing = false)
	public ResponseFileNotifySchedule responseFileNotifySchedule() {
		return new ResponseFileNotifySchedule();
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (exported) {
			return;
		}
		
		if (filefrontProperties.isEnableProvider()) {
			ApplicationConfig applicationConfig = ApplicationContextHolder.get().getBean(ApplicationConfig.class);
			RegistryConfig registryConfig = ApplicationContextHolder.get().getBean("registryConfig",
				RegistryConfig.class);
			ProtocolConfig protocolConfig = ApplicationContextHolder.get().getBean(ProtocolConfig.class);
			ProviderConfig providerConfig = ApplicationContextHolder.get().getBean(ProviderConfig.class);
			
			startFileNotifyDubbo(applicationConfig, registryConfig, protocolConfig, providerConfig);
			startRequestFileNotifyDubbo(applicationConfig, registryConfig, protocolConfig, providerConfig);
			startResponseFileNotifyDubbo(applicationConfig, registryConfig, protocolConfig, providerConfig);
			
			exported = true;
		}
	}
	
	private void startFileNotifyDubbo(	ApplicationConfig applicationConfig, RegistryConfig registryConfig,
										ProtocolConfig protocolConfig, ProviderConfig providerConfig) {
		FileNotifyProvider provider = ApplicationContextHolder.get().getBean(FileNotifyProvider.class);
		
		ServiceConfig<FileNotifyService> serviceConfig = new ServiceConfig<>();
		serviceConfig.setInterface(FileNotifyService.class);
		serviceConfig.setApplication(applicationConfig);
		serviceConfig.setRegistry(registryConfig);
		serviceConfig.setProtocol(protocolConfig);
		serviceConfig.setProvider(providerConfig);
		
		serviceConfig.setRef(provider);
		serviceConfig.setGroup(filefrontProperties.getDubboGroup());
		serviceConfig.setVersion(filefrontProperties.getDubboVersion());
		
		serviceConfig.export();
	}
	
	private void startRequestFileNotifyDubbo(	ApplicationConfig applicationConfig, RegistryConfig registryConfig,
												ProtocolConfig protocolConfig, ProviderConfig providerConfig) {
		RequestFileNotifySchedule provider = ApplicationContextHolder.get().getBean(RequestFileNotifySchedule.class);
		
		ServiceConfig<ScheduleCallBackService> serviceConfig = new ServiceConfig<>();
		serviceConfig.setInterface(ScheduleCallBackService.class);
		serviceConfig.setApplication(applicationConfig);
		serviceConfig.setRegistry(registryConfig);
		serviceConfig.setProtocol(protocolConfig);
		serviceConfig.setProvider(providerConfig);
		
		serviceConfig.setRef(provider);
		serviceConfig.setGroup(filefrontProperties.getDubboGroup() + ".request.schedule");
		serviceConfig.setVersion(filefrontProperties.getDubboVersion());
		
		serviceConfig.export();
	}
	
	private void startResponseFileNotifyDubbo(	ApplicationConfig applicationConfig, RegistryConfig registryConfig,
												ProtocolConfig protocolConfig, ProviderConfig providerConfig) {
		ResponseFileNotifySchedule provider = ApplicationContextHolder.get().getBean(ResponseFileNotifySchedule.class);
		
		ServiceConfig<ScheduleCallBackService> serviceConfig = new ServiceConfig<>();
		serviceConfig.setInterface(ScheduleCallBackService.class);
		serviceConfig.setApplication(applicationConfig);
		serviceConfig.setRegistry(registryConfig);
		serviceConfig.setProtocol(protocolConfig);
		serviceConfig.setProvider(providerConfig);
		
		serviceConfig.setRef(provider);
		serviceConfig.setGroup(filefrontProperties.getDubboGroup() + ".response.schedule");
		serviceConfig.setVersion(filefrontProperties.getDubboVersion());
		
		serviceConfig.export();
	}
}
