/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.yiji.adk.flow.engine.Execution;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-9 下午6:20<br>
 * @see
 * @since 1.0.0
 */
public interface Node {
	
	void validate();
	
	void execute(Execution execution);
	
	void initialize(Flow flow);
}
