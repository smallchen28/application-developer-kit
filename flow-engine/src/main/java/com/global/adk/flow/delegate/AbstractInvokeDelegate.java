/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.global.adk.flow.delegate;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-26 下午3:06<br>
 * @see
 * @since 1.0.0
 */
public abstract class AbstractInvokeDelegate implements InvokeDelegate {
	
	private Object target;
	
	public AbstractInvokeDelegate(Object target) {
		this.target = target;
	}
	
	public Object getTarget() {
		return target;
	}
	
}
