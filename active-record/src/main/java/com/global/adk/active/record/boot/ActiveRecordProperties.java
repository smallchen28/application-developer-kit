/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-24 17:36 创建
 *
 */
package com.global.adk.active.record.boot;

import com.global.adk.common.boot.CommonConfiguration;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * 活动记录集配置选项
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/20 下午下午6:59<br>
 * @see
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = CommonConfiguration.ADK_ACTIVE_RECORD_PREFIX)
public class ActiveRecordProperties implements BeanClassLoaderAware, InitializingBean {

	/** 是否启用该组件(默认:false) */
	private boolean enable = false;

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

}