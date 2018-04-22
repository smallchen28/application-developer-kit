/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-09 10:36 创建
 *
 */
package com.global.adk.filefront.schedule;

import com.global.adk.filefront.dal.entity.IdentifyTerm;
import com.global.adk.filefront.dal.entity.RequestNotify;
import com.global.adk.filefront.listeners.events.RequestFileNotifyDownloadedEvent;
import com.global.adk.filefront.listeners.events.inner.FileNotifyEvent;
import com.global.adk.filefront.listeners.events.inner.RequestFileNotifyInitedEvent;
import com.global.adk.filefront.support.consts.StatusConsts;
import com.global.common.lang.beans.Copier;
import com.yjf.scheduler.service.api.ScheduleCallBackService;

import java.util.List;

/**
 * @author karott
 */
public class RequestFileNotifySchedule extends RetryServiceSupport implements ScheduleCallBackService {
	
	@Override
	public void theJust(IdentifyTerm term) {
		logger.info("执行请求通知任务:{}", term);

		switch (term.getState()) {
			case StatusConsts.INIT:
				RequestFileNotifyInitedEvent initedEvent = new RequestFileNotifyInitedEvent();
				copyValue(term, initedEvent);
				
				fileEventBus.dispatchEvent(initedEvent);
				
				break;
			case StatusConsts.FILE_DOWNLOADED:
				RequestFileNotifyDownloadedEvent downloadedEvent = new RequestFileNotifyDownloadedEvent();
				copyValue(term, downloadedEvent);
				
				fileEventBus.dispatchEvent(downloadedEvent);
				
				break;
		}
	}
	
	@Override
	protected List<IdentifyTerm> forTerms() {
		return fileDbOperator.queryRequestRetryIds();
	}
	
	@Override
	protected void copyValue(IdentifyTerm term, FileNotifyEvent event) {
		RequestNotify notify = fileDbOperator.byReqIdLocked(term.getIdempotency(), term.getTenant());
		Copier.copy(notify, event);
		event.setFileNotify(notify);
	}
}
