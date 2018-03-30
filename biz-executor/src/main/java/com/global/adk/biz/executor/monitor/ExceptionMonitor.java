/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.monitor;

import com.global.adk.biz.executor.ServiceContext;

/**
 * 错误监听器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasulee创建于2014年9月16日 下午12:29:39<br>
 */
public interface ExceptionMonitor {

	public void catcher(Throwable e, ServiceContext<?, ?> serviceContext);
}
