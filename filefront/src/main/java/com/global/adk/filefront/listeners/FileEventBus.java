/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 21:52 创建
 *
 */
package com.global.adk.filefront.listeners;

import com.global.adk.event.NotifierBus;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;

/**
 * @author karott
 */
public class FileEventBus {
	
	private final static Logger logger = LoggerFactory.getLogger(FileEventBus.class);
	
	private NotifierBus notifierBus;
	
	public FileEventBus(NotifierBus notifierBus) {
		this.notifierBus = notifierBus;
	}
	
	public void dispatchEvent(Object event) {
		logger.info("FileFront发出事件:[{}]", event);
		
		notifierBus.dispatcher(event);
	}
	
	public void dispatchEventNoError(Object event) {
		try {
			dispatchEvent(event);
		} catch (Exception e) {
			logger.error("FileFront发送事件失败拉.{}", event, e);
		}
	}
	
	public void register(Object listener) {
		notifierBus.register(listener);
	}
}
