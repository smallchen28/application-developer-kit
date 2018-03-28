/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-09 13:17 创建
 *
 */
package com.yiji.adk.filefront.schedule;

import com.yiji.adk.filefront.dal.entity.IdentifyTerm;
import com.yiji.adk.filefront.dal.mapper.FileDbOperator;
import com.yiji.adk.filefront.listeners.FileEventBus;
import com.yiji.adk.filefront.listeners.events.inner.FileNotifyEvent;
import com.yiji.adk.filefront.schedule.task.FileTaskExecutor;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author karott
 */
public abstract class RetryServiceSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected FileDbOperator fileDbOperator;
	@Autowired
	protected FileEventBus fileEventBus;
	@Autowired
	protected FileTaskExecutor taskExecutor;
	
	public void justDoIT() {
		taskExecutor.executeTask(true, new FileTaskExecutor.ExecuteCallback("FileFront执行任务调度", null) {
			
			@Override
			public void doExecute() {
				logger.info("FileFront任务执行开始.......................");
				
				List<IdentifyTerm> terms = forTerms();
				logger.info("FileFront任务执行数:{},{}", terms.size(), terms);
				terms.stream().forEach(term -> {
					try {
						theJust(term);
					} catch (Exception e) {
						logger.info("FileFront执行任务条目失败：{}", term, e);
					}
				});
				
				logger.info("FileFront任务执行结束.......................");
			}
		});
	}
	
	protected abstract List<IdentifyTerm> forTerms();
	
	protected abstract void theJust(IdentifyTerm term);
	
	protected abstract void copyValue(IdentifyTerm term, FileNotifyEvent event);
	
}
