/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.delegate;

import com.yiji.adk.flow.module.Flow;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-19 下午5:58<br>
 * @see
 * @since 1.0.0
 */
public interface InvokeSupport {
	
	void proceed(Flow.Key flowKey, String nodeName, String target);
	
}
