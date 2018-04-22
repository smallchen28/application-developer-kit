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

/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-24 17:36 创建
 *
 */
package com.global.adk.flow.boot;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.global.adk.common.boot.CommonConfiguration;
import com.global.adk.common.exception.FlowException;
import com.global.adk.common.jdbc.DialectUtil;
import com.global.boot.core.Apps;
import com.global.common.util.StringUtils;

/**
 * 流程引擎配置选项
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午6:59<br>
 * @see
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = CommonConfiguration.ADK_FLOW_ENGINE_PREFIX)
public class FlowEngineProperties implements BeanClassLoaderAware, InitializingBean {
	
	/** 是否启用此组件(默认:false) */
	private boolean enable = false;
	
	/** 指定流程文件路径(例如：classpath*:/flow/*.xml) */
	private String location;
	
	private ClassLoader beanClassLoader;
	
	/**
	 * 是否启用重试,启用后会同时启用dubbo服务
	 */
	private boolean enableRetry = false;
	private boolean enableRetryProvider = true;
	private String dubboGroup = Apps.getAppName() + ".flow.retry";
	private String dubboVersion = "1.0";
	private String dialect;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (enableRetry) {
			
			setDialect(DialectUtil.dialectByDSUrl());
			if (null != dialect
				&& (StringUtils.notEquals(dialect, "mysql") && StringUtils.notEquals(dialect, "oracle"))) {
				throw new FlowException("dialect只支持mysql或oracle");
			}
		}
		
		if (enableRetryProvider) {
			if (!enableRetry) {
				//静默设置关闭
				enableRetryProvider = false;
			}
		}
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}
	
	public boolean isEnableRetry() {
		return enableRetry;
	}
	
	public void setEnableRetry(boolean enableRetry) {
		this.enableRetry = enableRetry;
	}
	
	public String getDubboGroup() {
		return dubboGroup;
	}
	
	public void setDubboGroup(String dubboGroup) {
		this.dubboGroup = dubboGroup;
	}
	
	public String getDubboVersion() {
		return dubboVersion;
	}
	
	public void setDubboVersion(String dubboVersion) {
		this.dubboVersion = dubboVersion;
	}
	
	public String getDialect() {
		return dialect;
	}
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	public boolean isEnableRetryProvider() {
		return enableRetryProvider;
	}
	
	public void setEnableRetryProvider(boolean enableRetryProvider) {
		this.enableRetryProvider = enableRetryProvider;
	}
}