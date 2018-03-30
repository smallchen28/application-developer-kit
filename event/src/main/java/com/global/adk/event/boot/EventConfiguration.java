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

package com.global.adk.event.boot;

import com.global.adk.event.EventSupport;
import com.global.adk.event.NotifierBus;
import com.yjf.common.concurrent.MonitoredThreadPool;
import com.yjf.common.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.global.adk.common.boot.CommonConfiguration;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/23 下午下午4:00<br>
 * @see
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ EventProperties.class })
@ConditionalOnProperty(value = CommonConfiguration.ADK_EVENT, matchIfMissing = false)
public class EventConfiguration implements ApplicationContextAware, InitializingBean {
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private EventProperties eventProperties;
	
	private MonitoredThreadPool monitoredThreadPool;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	//-FIXME 下次版本迭代时，需要将这部分代码重构移至common包，合并容器异步处理以及事件总线异步处理。
	@Override
	public void afterPropertiesSet() throws Exception {
		//没有配置独立的线程池，又打开了异步处理开关
		if (eventProperties.isAsyncEnable()) {
			try {
				String beanName = eventProperties.getThreadPoolBeanName();
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
				
				threadPoolPropertyValues.add("corePoolSize", eventProperties.getCorePoolSize());
				threadPoolPropertyValues.add("keepAliveSeconds",
					eventProperties.getKeepAliveSeconds());
				threadPoolPropertyValues.add("maxPoolSize", eventProperties.getMaxPoolSize());
				threadPoolPropertyValues.add("queueCapacity", eventProperties.getQueueCapacity());
				
				threadPoolPropertyValues.add("threadNamePrefix", "ADK-EXEC");
				threadPoolPropertyValues.add("enableGaugeMetric", true);
				threadPoolPropertyValues.add("enableTimerMetric", true);
				
				factory.registerBeanDefinition("com.yiji.adk.event.async.threadpool", db);
				this.monitoredThreadPool = factory.getBean("com.yiji.adk.event.async.threadpool",
					MonitoredThreadPool.class);
			}
		}
	}
	
	@Bean
	public NotifierBus notifierBus() {
		return new NotifierBus(monitoredThreadPool);
	}
	
	@Bean
	public EventSupport eventSupport() {
		return new EventSupport();
	}
}
