/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 22:09 创建
 *
 */
package com.global.adk.filefront.listeners;

import com.global.adk.filefront.dal.mapper.FileDbOperator;
import com.global.adk.filefront.schedule.task.FileTaskExecutor;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author karott
 */
public class ListenerSupport {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected FileEventBus fileEventBus;
	@Autowired
	protected FileTaskExecutor fileTaskExecutor;
	@Autowired
	protected FileDbOperator fileDbOperator;
	
}
