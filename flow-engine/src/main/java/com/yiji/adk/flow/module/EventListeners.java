/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-29 下午6:23<br>
 * @see
 * @since 1.0.0
 */
public class EventListeners {
	
	private List<EventListener> listeners = Lists.newArrayList();
	
	public void addListener(EventListener listener) {
		listeners.add(listener);
	}
	
	public List<EventListener> listeners() {
		return listeners;
	}
	
}
