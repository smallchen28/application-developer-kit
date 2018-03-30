/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.delegate;

import com.global.adk.flow.engine.Execution;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-29 下午6:33<br>
 * @see
 * @since 1.0.0
 */
public interface ListenerDelegate {
	
	void action(Execution execution, String event);
	
}
