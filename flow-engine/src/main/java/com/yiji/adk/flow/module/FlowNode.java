/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.yiji.adk.flow.engine.Execution;
import com.yiji.adk.flow.engine.StandardNodeExecution;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-13 下午3:02<br>
 * @see
 * @since 1.0.0
 */
public abstract class FlowNode extends AbstractNode {
	
	private boolean traceLog;
	
	@NotBlank
	private String name;
	
	//可以没有任何处理（before、after、execute），
	private String triggerClass;
	
	/**
	 * 关联重试节点
	 */
	private RetryNode retryNode;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTriggerClass() {
		return triggerClass;
	}
	
	public void setTriggerClass(String triggerClass) {
		this.triggerClass = triggerClass;
	}
	
	public boolean isTraceLog() {
		return traceLog;
	}
	
	public void setTraceLog(boolean traceLog) {
		this.traceLog = traceLog;
	}
	
	@Override
	public void execute(Execution execution) {
		new StandardNodeExecution(execution, this).execute();
	}
	
	public RetryNode getRetryNode() {
		return retryNode;
	}
	
	public void setRetryNode(RetryNode retryNode) {
		this.retryNode = retryNode;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.module.FlowNode{");
		sb.append("name='").append(name).append('\'');
		sb.append(", triggerClass='").append(triggerClass).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
