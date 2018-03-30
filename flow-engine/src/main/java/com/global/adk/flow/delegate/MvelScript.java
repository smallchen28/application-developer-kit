/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.delegate;

import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.Flow;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-19 下午5:53<br>
 * @see
 * @since 1.0.0
 */
public interface MvelScript {
	
	String calculate(Execution execution, Flow.Key key, String nodeName);
}
