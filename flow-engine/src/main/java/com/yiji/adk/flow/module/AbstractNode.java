/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-9 下午6:34<br>
 * @see
 * @since 1.0.0
 */
public abstract class AbstractNode implements Node {

	public void validate() {
		//这里就不在做检查了，利用JSR303在Flow层做
	}

	@Override
	public void initialize(Flow flow) {
		//这里起adapter的作用，
	}
}
