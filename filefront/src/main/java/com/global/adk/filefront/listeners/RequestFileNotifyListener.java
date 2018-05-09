/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-01 22:11 创建
 *
 */
package com.global.adk.filefront.listeners;

import com.global.adk.event.Subscribe;
import com.global.adk.filefront.dal.entity.RequestNotify;
import com.global.adk.filefront.exceptions.RequestFileNotifyException;
import com.global.adk.filefront.listeners.events.RequestFileNotifyDownloadedEvent;
import com.global.adk.filefront.listeners.events.RequestFileNotifyErrorEvent;
import com.global.adk.filefront.listeners.events.RequestFileNotifyParsedEvent;
import com.global.adk.filefront.listeners.events.inner.RequestFileNotifyInitedEvent;
import com.global.adk.filefront.support.client.FileClient;
import com.global.adk.filefront.support.client.FileClientFactory;
import com.global.adk.filefront.support.config.FileConfigContext;
import com.global.adk.filefront.support.config.FileConfigParser;
import com.global.adk.filefront.support.consts.FilefrontConsts;
import com.global.adk.filefront.support.consts.StatusConsts;
import com.yjf.common.lang.beans.Copier;

import java.io.File;
import java.util.Date;

/**
 * 请求文件通知监听
 *
 * @author karott
 */
public class RequestFileNotifyListener extends ListenerSupport {
	
	/**
	 * 此事件最后执行，保证业务可以先于FileFront做逻辑操作
	 * <p/>
	 * 异步任务下载文件并发送RequestFileNotifyDownloadedEvent事件
	 *
	 * @param event 请求日志已存储事件
	 */
	@Subscribe(priority = 1)
	public void fileInited(RequestFileNotifyInitedEvent event) {
		RequestNotify notify = (RequestNotify) event.getFileNotify();
		
		if (notify.isInit()) {
			event.setFileNotify(notify);
			
			RequestFileNotifyDownloadedEvent downloadedEvent = new RequestFileNotifyDownloadedEvent();
			Copier.copy(notify, downloadedEvent);
			downloadedEvent.setLocalFileTime(new Date());
			downloadedEvent.setFileNotify(notify);
			
			String isDownload = FileConfigParser.attrByNodeSystem(notify.getBizType(), FileConfigContext.DOWNLOAD,
				FileConfigContext.DOWNLOAD_OPTION);
			if (Boolean.valueOf(isDownload)) {
				//下载文件
				FileClient fileClient = FileClientFactory.client(notify.getBizType(), notify.getTenant());
				File downLoadedFile = fileClient.download(notify.getFilePath(), notify.getFileName());
				downloadedEvent.setLocalFilePath(downLoadedFile.getParent());
				downloadedEvent.setLocalFileName(downLoadedFile.getName());
				
				Copier.copy(downloadedEvent, notify);
				notify.download();
				fileDbOperator.restoreRequestNotify(notify);
				
				fileEventBus.dispatchEvent(downloadedEvent);
			} else {
				downloadedEvent.setLocalFilePath(FilefrontConsts.DEFAULT);
				downloadedEvent.setLocalFileName(FilefrontConsts.DEFAULT);
				
				Copier.copy(downloadedEvent, notify);
				notify.success(StatusConsts.DOWNLOAD_IGNORE);
				fileDbOperator.restoreRequestNotify(notify);
			}
		}
	}
	
	/**
	 * 改变日志记录状态,由业务方发出通知事件
	 * 
	 * @param event 请求文件通知已解析事件
	 * @throws RequestFileNotifyException 请求文件通知异常
	 */
	@Subscribe(priority = 10000)
	public void fileParsed(RequestFileNotifyParsedEvent event) throws RequestFileNotifyException {
		RequestNotify notify = fileDbOperator.byReqId(event.getReqId(), event.getTenant());
		notify.success(StatusConsts.FILE_PARSED);
		
		fileDbOperator.restoreRequestNotify(notify);
	}
	
	@Subscribe(priority = 10000)
	public void bizJobError(RequestFileNotifyErrorEvent event) throws RequestFileNotifyException {
		RequestNotify notify = fileDbOperator.byReqId(event.getReqId(), event.getTenant());
		notify.fail(StatusConsts.BIZ_JOB_FAIL);
		notify.setErrorCode(event.getErrorCode());
		notify.setErrorMsg(event.getErrorMessage());
		
		fileDbOperator.restoreRequestNotify(notify);
	}
	
}