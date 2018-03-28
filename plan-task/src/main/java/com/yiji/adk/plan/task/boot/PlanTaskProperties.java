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

package com.yiji.adk.plan.task.boot;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yiji.adk.common.boot.CommonConfiguration;
import org.springframework.core.io.ClassPathResource;

/**
 * 计划任务配置项
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午6:59<br>
 * @see
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = CommonConfiguration.ADK_PLAN_TASK_PREFIX)
public class PlanTaskProperties implements BeanClassLoaderAware, InitializingBean {
	
	/** 是否启用此组件(默认:false) */
	private boolean enable = false;

	/** 任务文件路径(不可为空，例：/plantask-test.xml，根目录为classpath) */
	private ClassPathResource location;

	/** 任务表前缀(默认：app_kit_) */
	private String tableNamePre="app_kit_";

	/** 任务自增序列(默认为：seq_app_kit_plan_task) */
	private String incrementName="seq_app_kit_plan_task";

	/** 核心线程数(默认:2) */
	private int corePoolSize = 2;

	/** 最大线程数(默认:10) */
	private int maxPoolSize = 10;

	/** 限制线程保持时间(默认:300秒) */
	private int keepAliveSeconds = 300;

	/** 线程队列容量(默认:50) */
	private int queueCapacity = 50;


	private ClassLoader beanClassLoader;



	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}
	
	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public ClassPathResource getLocation() {
		return location;
	}

	public void setLocation(ClassPathResource location) {
		this.location = location;
	}

	public String getIncrementName() {
		return incrementName;
	}

	public void setIncrementName(String incrementName) {
		this.incrementName = incrementName;
	}

	public String getTableNamePre() {
		return tableNamePre;
	}

	public void setTableNamePre(String tableNamePre) {
		this.tableNamePre = tableNamePre;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
}