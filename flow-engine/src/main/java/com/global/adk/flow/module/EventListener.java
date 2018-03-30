/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.flow.module;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-29 下午6:42<br>
 * @see
 * @since 1.0.0
 */
public class EventListener {
	
	private String listenerClass;
	
	private String description;
	
	public EventListener(String listenerClass, String description) {
		this.listenerClass = listenerClass;
		this.description = description;
	}
	
	public String getListenerClass() {
		return listenerClass;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.global.adk.flow.module.EventListener{");
		sb.append("description='").append(description).append('\'');
		sb.append(", listenerClass='").append(listenerClass).append('\'');
		sb.append('}');
		return sb.toString();
	}
	
}
