/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 21:07 创建
 *
 */
package com.yiji.adk.filefront.listeners.events;

import com.yiji.adk.filefront.listeners.events.inner.FileNotifyLocalSupportEvent;

/**
 * 响应文件通知事件.FileFront响应成功后发出事件
 * <p/>
 *
 * 1.此事件发出后，FileFront以最低优先级接收此事件，保证业务方优可以先处理该事件
 *
 * @author karott
 */
public class ResponseFileNotifyFinishedEvent extends FileNotifyLocalSupportEvent {
	
	/**
	 * dubbo分组,为空表示不通知
	 */
	private String dubboGroup;
	
	/**
	 * dubbo版本
	 */
	private String dubboVersion;
	
	public String getDubboGroup() {
		return dubboGroup;
	}
	
	public void setDubboGroup(String dubboGroup) {
		this.dubboGroup = dubboGroup;
	}
	
	public String getDubboVersion() {
		return dubboVersion;
	}
	
	public void setDubboVersion(String dubboVersion) {
		this.dubboVersion = dubboVersion;
	}
}
