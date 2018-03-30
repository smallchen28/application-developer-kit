/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.delegate;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-30 下午3:44<br>
 * @see
 * @since 1.0.0
 */

public abstract class AbstractListenerDelegate implements ListenerDelegate, Comparable {
	
	private int priority;
	
	private Object target;
	
	public AbstractListenerDelegate(Object target, int priority) {
		this.target = target;
		this.priority = priority;
	}
	
	public Object getTarget() {
		return target;
	}
	
	@Override
	public int compareTo(Object o) {
		AbstractListenerDelegate other = (AbstractListenerDelegate) o;
		return priority == other.priority ? 0 : priority > other.priority ? 1 : -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AbstractListenerDelegate that = (AbstractListenerDelegate) o;

		if (target != null ? !target.equals(that.target) : that.target != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return target != null ? target.hashCode() : 0;
	}
}
