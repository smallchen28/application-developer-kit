/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 20:17 创建
 *
 */
package com.global.adk.filefront.provider;

import com.global.adk.biz.executor.ExecutorContainer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author karott
 */
public class ProviderSupport {
	
	@Autowired
	private ExecutorContainer container;
	
	public ExecutorContainer getContainer() {
		return container;
	}
	
}
