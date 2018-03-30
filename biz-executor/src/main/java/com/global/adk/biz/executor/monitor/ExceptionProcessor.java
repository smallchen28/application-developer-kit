/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.monitor;

import com.global.adk.biz.executor.ServiceContext;
import org.slf4j.Logger;

/**
 * 异常处理器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月25日 下午1:36:27<br>
 */
public interface ExceptionProcessor {
	
	boolean proceed(Throwable throwable, ServiceContext<?, ?> serviceContext, ExceptionProcessorChain chain);
	
	void init(ExceptionProcessorConfig config);
	
	Logger logger();
	
	void setLogger(Logger logger);
	
	String systemName();
	
	void setSystemName(String systemName);
}
