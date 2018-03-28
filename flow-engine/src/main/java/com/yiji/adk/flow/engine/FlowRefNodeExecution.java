/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.engine;

import com.yiji.adk.flow.module.FlowNode;
import com.yiji.adk.flow.module.FlowRef;

import java.sql.Timestamp;

/**
 * 子流程节点执行器
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-8-11 下午5:25<br>
 * @see
 * @since 1.0.0
 */
public class FlowRefNodeExecution implements NodeExecution {
	
	private final Execution execution;
	
	private final FlowRef ref;
	
	public FlowRefNodeExecution(Execution execution, FlowRef ref) {
		this.execution = execution;
		this.ref = ref;
		execution.setCurrentNodeExecution(this);
	}
	
	@Override
	public void execute() {
		//激活新流程
		Execution subExecution = execution.getEngine().execute(ref.getSubFlowName(), null, ref.getVersion(),
			execution.getTarget(), execution.getAttachment());
		
		//关联子流程
		execution.setSubExecution(subExecution);
		
		//激活流转
		ref.getCondition().execute(execution);
	}
	
	@Override
	public FlowNode currentNode() {
		return ref;
	}
	
	@Override
	public String decision() {
		return null;//置为空
	}
	
	@Override
	public Timestamp getEndTime() {
		return execution.getSubExecution().getEndTime();
	}
	
	@Override
	public Timestamp getStartTime() {
		return execution.getSubExecution().getStartTime();
	}
	
	@Override
	public Throwable getError() {
		return null;
	}
	
	@Override
	public ExecutionStatus getStatus() {
		return null;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(
			"com.yiji.adk.flow.engine.FlowRefNodeExecution{");
		sb.append("decision='").append(decision()).append('\'');
		sb.append(", endTime=").append(getEndTime());
		sb.append(", error=").append(getError());
		sb.append(", execution=").append(execution);
		sb.append(", ref=").append(ref);
		sb.append(", currentNode=").append(currentNode());
		sb.append(", startTime=").append(getStartTime());
		sb.append(", status=").append(getStatus());
		sb.append('}');
		return sb.toString();
	}
}
