/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.yiji.adk.common.exception.FlowException;
import com.yiji.adk.flow.engine.Execution;

import java.util.List;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-18 下午1:45<br>
 * @see
 * @since 1.0.0
 */
public class NodeRef extends ActivityNode {
	
	private FlowNode flowNode;
	
	public NodeRef(String ref) {
		setName(ref);
	}
	
	@Override
	public void execute(Execution execution) {
		if(flowNode == null){
			Flow flow = execution.getCurrentFlow();
			throw new FlowException(String.format("flow=%s,version=%s流程定义中不存在transition的引用节点ref=%s",flow.getName(),flow.getVersion(),getName()));
		}
		flowNode.execute(execution);
	}
	
	@Override
	public void initialize(Flow flow) {
		
		//对特殊的循环引用需要单独处理,否则将递归调用到死。
		if (flowNode == null) {
			List<ActivityNode> nodes = flow.getNodes();
			
			for (AbstractNode node : nodes) {
				String nodeName = ((ActivityNode) node).getName();
				if (nodeName != null && nodeName.equals(getName())) {
					flowNode = (FlowNode) node;
					flowNode.initialize(flow);
					return;
				}
			}
			
			EndNode endNode = flow.getEndNode();
			if (endNode.getName().equals(getName())) {
				this.flowNode = endNode;
			}
			
			//非空检查，不允许为空的情况发生
			if (flowNode == null) {
				throw new FlowException(String.format("flow=%s,version=%s,node=%s引用不存在……", flow.getName(),
					flow.getVersion(), getName()));
			}
		}
	}
	
	public FlowNode getFlowNode() {
		return flowNode;
	}
	
	@Override
	public String getTriggerClass() {
		return flowNode == null ? null : flowNode.getTriggerClass();
	}
	
	@Override
	public void setTriggerClass(String triggerClass) {
		if (flowNode == null) {
			throw new FlowException("NodeRef赋予TriggerClass出错flowNode = null");
		}
		flowNode.setTriggerClass(triggerClass);
	}
	
}
