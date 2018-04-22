/*
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

package com.global.adk.biz.executor.boot;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.biz.executor.ActivityExecutorContainer;
import com.global.adk.biz.executor.ExecutorContainer;
import com.global.adk.biz.executor.monitor.ExceptionMonitor;
import com.global.adk.biz.executor.monitor.StandardExceptionMonitor;
import com.global.adk.biz.executor.regcode.RegistryCodeVerify;
import com.global.adk.biz.executor.support.dubbo.DubboServiceAPIGenerator;
import com.global.adk.common.boot.CommonConfiguration;
import com.global.boot.core.Apps;
import com.global.common.concurrent.MonitoredThreadPool;
import com.global.common.util.StringUtils;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/23 下午下午4:00<br>
 * @see
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ ExecutorProperties.class })
@ConditionalOnProperty(value = CommonConfiguration.ADK_BIZ_EXECUTOR, matchIfMissing = false)
public class ExecutorConfiguration implements ApplicationContextAware, InitializingBean {
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private ExecutorProperties executorProperties;
	
	@Autowired(required = false)
	private DomainFactory domainFactory;
	
	private MonitoredThreadPool monitoredThreadPool;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//没有配置独立的线程池，又打开了异步处理开关
		if (executorProperties.isAsyncEnable()) {
			try {
				String beanName = executorProperties.getThreadPoolBeanName();
				monitoredThreadPool = StringUtils.isEmpty(beanName)
					? applicationContext.getBean(MonitoredThreadPool.class)
					: applicationContext.getBean(beanName, MonitoredThreadPool.class);
			} catch (NoSuchBeanDefinitionException e) {
				//如果不存在线程池定义，那么就自行创建，并委托给容器管理
				DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext
					.getAutowireCapableBeanFactory();
					
				RootBeanDefinition db = new RootBeanDefinition(MonitoredThreadPool.class);
				db.setScope(BeanDefinition.SCOPE_SINGLETON);
				
				MutablePropertyValues threadPoolPropertyValues = new MutablePropertyValues();
				db.setPropertyValues(threadPoolPropertyValues);
				
				threadPoolPropertyValues.add("corePoolSize", executorProperties.getCorePoolSize());
				threadPoolPropertyValues.add("keepAliveSeconds", executorProperties.getKeepAliveSeconds());
				threadPoolPropertyValues.add("maxPoolSize", executorProperties.getMaxPoolSize());
				threadPoolPropertyValues.add("queueCapacity", executorProperties.getQueueCapacity());
				
				threadPoolPropertyValues.add("threadNamePrefix", "ADK-EXEC");
				threadPoolPropertyValues.add("enableGaugeMetric", true);
				threadPoolPropertyValues.add("enableTimerMetric", true);
				
				factory.registerBeanDefinition("com.global.adk.biz.executor.async.threadpool", db);
				this.monitoredThreadPool = factory.getBean("com.global.adk.biz.executor.async.threadpool",
					MonitoredThreadPool.class);
			}
		}
	}
	
	@Bean
	@ConditionalOnMissingBean(ActivityExecutorContainer.class)
	public ActivityExecutorContainer activityExecutorContainer(	DBPlugin dbPlugin, ExceptionMonitor exceptionMonitor,
																PlatformTransactionManager transactionManager,
																RegistryCodeVerify registryCodeVerify) {
																
		ActivityExecutorContainer activityExecutorContainer = new ActivityExecutorContainer();
		activityExecutorContainer.setDbPlugin(dbPlugin);
		activityExecutorContainer.setMonitor(exceptionMonitor);
		activityExecutorContainer.setRegistryCodeVerify(registryCodeVerify);
		activityExecutorContainer.setDomainFactory(domainFactory);
		activityExecutorContainer.setTransactionManager(transactionManager);
		activityExecutorContainer.setThreadPoolTaskExecutor(monitoredThreadPool);
		return activityExecutorContainer;
	}
	
	@Bean
	@ConditionalOnMissingBean(ExceptionMonitor.class)
	public ExceptionMonitor exceptionMonitor() {
		return new StandardExceptionMonitor("ERROR", Apps.getAppName());
	}
	
	@Bean
	@ConditionalOnMissingBean(RegistryCodeVerify.class)
	public RegistryCodeVerify registryCodeVerify() {
		//-FIXME lambda统一修改
		return new RegistryCodeVerify() {
			@Override
			public boolean validate(String serviceName, Object parameter) {
				return true;
			}
		};
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DubboServiceAPIGenerator dubboServiceAPIGenerator(ExecutorContainer executorContainer) {
		return new DubboServiceAPIGenerator(executorProperties.getProviderMetaPath(), executorContainer);
	}
	
}
