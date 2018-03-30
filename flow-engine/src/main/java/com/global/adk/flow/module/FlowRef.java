/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.module;

import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.engine.FlowRefNodeExecution;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-17 下午6:42<br>
 * @see
 * @since 1.0.0
 */
public class FlowRef extends ActivityNode {
	
	private String subFlowName;
	
	private int version;
	
	public FlowRef(String name, String subFlowName, int version) {
		setName(name);
		this.subFlowName = subFlowName;
		this.version = version;
	}
	
	@Override
	public void execute(final Execution execution) {

		new FlowRefNodeExecution(execution, this).execute();
	}
	
	public String getSubFlowName() {
		return subFlowName;
	}
	
	public int getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.global.adk.flow.module.FlowRef{");
		sb.append("name='").append(getName());
		sb.append(", subFlowName='").append(subFlowName);
		sb.append(", version='").append(version);
		sb.append(", triggerClass='").append(getTriggerClass());
		sb.append('}');
		return sb.toString();
	}
	
}
