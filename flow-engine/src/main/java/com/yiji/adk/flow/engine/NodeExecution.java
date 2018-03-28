/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.engine;

import com.yiji.adk.flow.module.FlowNode;

import java.sql.Timestamp;

/**
 * 节点执行
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-5-19 下午4:08<br>
 * @see
 * @since 1.0.0
 */
public interface NodeExecution {
	
	void execute();
	
	FlowNode currentNode();
	
	String decision();
	
	Timestamp getEndTime();
	
	Timestamp getStartTime();
	
	Throwable getError();
	
	ExecutionStatus getStatus();
}
