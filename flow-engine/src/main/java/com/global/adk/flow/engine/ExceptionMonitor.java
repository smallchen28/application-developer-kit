/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.engine;

import com.global.adk.flow.module.Flow;
import com.global.adk.flow.module.FlowNode;

/**
 * 异常监听器
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-5-13 下午5:47<br>
 * @see
 * @since 1.0.0
 */
public interface ExceptionMonitor {
	
	void catcher(Flow flow, FlowNode node, Execution execution, Throwable throwable);
}
