/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.module;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-16 下午2:00<br>
 * @see
 * @since 1.0.0
 */
public abstract class ActivityNode extends FlowNode {
	
	@NotNull
	@Valid
	private Condition condition;
	
	public Condition getCondition() {
		return condition;
	}
	
	public void setCondition(Condition condition) {
		this.condition = condition;
		this.condition.setActivityNode(this);
	}
	
	@Override
	public void initialize(Flow flow) {
		getCondition().initialize(flow);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.global.adk.flow.module.ActivityStateNode{");
		sb.append("name=").append(getName());
		sb.append(", triggerClass='").append(getTriggerClass());
		sb.append(", condition=").append(condition);
		sb.append('}');
		return sb.toString();
	}


}
