/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.module;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-17 下午6:50<br>
 * @see
 * @since 1.0.0
 */
public class StandardActivityNode extends ActivityNode {
	
	private NodeType nodeType;
	
	public NodeType getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.global.adk.flow.module.StandardActivityNode{");
		sb.append("name=").append(getName());
		sb.append(", stateNodeType='").append(nodeType);
		sb.append(", triggerClass='").append(getTriggerClass());
		sb.append(", condition=").append(getCondition());
		sb.append('}');
		return sb.toString();
	}
}
