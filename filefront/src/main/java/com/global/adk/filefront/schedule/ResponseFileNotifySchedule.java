/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-09 10:39 创建
 *
 */
package com.global.adk.filefront.schedule;

import com.global.adk.filefront.dal.entity.IdentifyTerm;
import com.global.adk.filefront.dal.entity.ResponseNotify;
import com.global.adk.filefront.listeners.events.inner.FileNotifyEvent;
import com.global.adk.filefront.listeners.events.inner.ResponseFileNotifyInitedEvent;
import com.global.adk.filefront.listeners.events.inner.ResponseFileNotifyUploadedEvent;
import com.global.adk.filefront.support.consts.StatusConsts;
import com.global.common.lang.beans.Copier;
import com.yjf.scheduler.service.api.ScheduleCallBackService;

import java.util.List;

/**
 * @author karott
 */
public class ResponseFileNotifySchedule extends RetryServiceSupport implements ScheduleCallBackService {
	
	@Override
	protected void theJust(IdentifyTerm term) {
		logger.info("执行响应通知任务:{}", term);
		
		switch (term.getState()) {
			case StatusConsts.INIT:
				ResponseFileNotifyInitedEvent initedEvent = new ResponseFileNotifyInitedEvent();
				copyValue(term, initedEvent);
				
				fileEventBus.dispatchEvent(initedEvent);
				
				break;
			case StatusConsts.FILE_UPLOADED:
				ResponseFileNotifyUploadedEvent uploadedEvent = new ResponseFileNotifyUploadedEvent();
				copyValue(term, uploadedEvent);
				
				fileEventBus.dispatchEvent(uploadedEvent);
				
				break;
				
		}
	}
	
	@Override
	protected List<IdentifyTerm> forTerms() {
		return fileDbOperator.queryResponseRetryIds();
	}
	
	@Override
	protected void copyValue(IdentifyTerm term, FileNotifyEvent event) {
		ResponseNotify notify = fileDbOperator.byRspIdLocked(term.getIdempotency(), term.getTenant());
		Copier.copy(notify, event);
		event.setFileNotify(notify);
	}
}
