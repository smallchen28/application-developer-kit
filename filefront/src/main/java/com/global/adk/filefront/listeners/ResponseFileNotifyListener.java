/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 20:35 创建
 *
 */
package com.global.adk.filefront.listeners;

import com.global.adk.common.Constants;
import com.global.adk.event.Subscribe;
import com.global.adk.filefront.dal.entity.ResponseNotify;
import com.global.adk.filefront.exceptions.ResponseFileNotifyException;
import com.global.adk.filefront.exceptions.ResponseFileNotifyIdemException;
import com.global.adk.api.order.FileNotifyOrder;
import com.global.adk.api.result.FileNotifyResult;
import com.global.adk.api.service.FileNotifyService;
import com.global.adk.filefront.listeners.events.ResponseFileNotifyFinishedEvent;
import com.global.adk.filefront.listeners.events.ResponseFileNotifyPreparedEvent;
import com.global.adk.filefront.listeners.events.inner.ResponseFileNotifyInitedEvent;
import com.global.adk.filefront.listeners.events.inner.ResponseFileNotifyUploadedEvent;
import com.global.adk.filefront.support.client.FileClient;
import com.global.adk.filefront.support.client.FileClientFactory;
import com.global.adk.filefront.support.config.FileConfigContext;
import com.global.adk.filefront.support.config.FileConfigParser;
import com.global.adk.filefront.support.consts.FilefrontConsts;
import com.global.adk.filefront.support.consts.StatusConsts;
import com.yjf.common.dubbo.DubboRemoteProxyFacotry;
import com.yjf.common.lang.beans.Copier;
import com.yjf.common.lang.result.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.Date;

/**
 * 响应文件通知监听.接收业务组件监听事件，并做后续处理
 *
 * @author karott
 */
public class ResponseFileNotifyListener extends ListenerSupport {
	
	@Autowired
	private DubboRemoteProxyFacotry dubboRemoteProxyFacotry;
	
	/**
	 * 监听响应文件已准备事件
	 * <p/>
	 * 1.本地文件校验<br/>
	 * 2.持久化通知日志<br>
	 * 3.异步任务上传文件并通知出去
	 *
	 * @param event 响应文件已准备事件
	 * @throws ResponseFileNotifyException 响应文件通知异常
	 */
	@Subscribe(isAsync = false, priority = 10000)
	public void responseNotify(ResponseFileNotifyPreparedEvent event) throws ResponseFileNotifyException {
		ResponseNotify notify = new ResponseNotify();
		
		try {
			notify.setId(fileDbOperator.nextSeq());
			
			if (!FileClientFactory.localFileExists(event.getLocalFilePath(), event.getLocalFileName())) {
				throw new ResponseFileNotifyException("本地文件不存在", StatusConsts.LOCAL_FILE_NOT_FOUND, Status.FAIL);
			}
			
			Copier.copy(event, notify, "fileService");
			fileDbOperator.storeResponseNotify(notify);
		} catch (ResponseFileNotifyException re) {
			throw re;
		} catch (DuplicateKeyException de) {
			postIdempotency(notify, de.getMessage());
		} catch (Exception e) {
			logger.error("尼玛，响应出什么问题了,{}", event, e);
			throw new ResponseFileNotifyException("unknown", "unknown", Status.FAIL);
		}
		
		uploadFile(notify);
	}
	
	@Subscribe(isAsync = false)
	public void responseNotifyInited(ResponseFileNotifyInitedEvent event) {
		ResponseNotify notify = (ResponseNotify) event.getFileNotify();
		Copier.copy(event, notify);
		
		uploadFile(notify);
	}
	
	@Subscribe(isAsync = false)
	public void responseNotifyUploaded(ResponseFileNotifyUploadedEvent event) {
		ResponseNotify notify = (ResponseNotify) event.getFileNotify();
		
		dubboNotify(notify);
	}
	
	private void postIdempotency(ResponseNotify notify, String description) {
		logger.info("FileFront Response Notify幂等操作.notify:[{}]", notify);
		
		ResponseNotify notifyFromStore = fileDbOperator.byRspId(notify.getRspId(), notify.getTenant());
		throw new ResponseFileNotifyIdemException(description, Constants.REQUEST_REPEATED,
			Status.findStatus(notifyFromStore.getStatus()));
	}
	
	private void uploadFile(ResponseNotify notify) {
		upload(notify);
	}
	
	private void upload(ResponseNotify notify) {
		String isUpload = FileConfigParser.attrByNodeSystem(notify.getBizType(), FileConfigContext.UPLOAD,
			FileConfigContext.UPLOAD_OPTION);
			
		notify.setFileTime(new Date());
		if (Boolean.valueOf(isUpload)) {
			FileClient fileClient = FileClientFactory.client(notify.getBizType(), notify.getTenant());
			fileClient.upload(notify.getLocalFilePath(), notify.getLocalFileName());
			
			String configDir = FileConfigParser.textByNodeSystem(notify.getBizType(), FileConfigContext.UPLOAD);
			String uploadDirectory = FileClientFactory.createDirecotryByBizAndTime(notify.getBizType(), configDir);
			notify.setFilePath(uploadDirectory);
			notify.setFileName(notify.getLocalFileName());
			
			notify.upload();
			fileDbOperator.restoreResponseNotify(notify);
			
			dubboNotify(notify);
		} else {
			notify.setFilePath(FilefrontConsts.DEFAULT);
			notify.setFileName(FilefrontConsts.DEFAULT);
			
			notify.success(StatusConsts.UPLOAD_IGNORE);
			fileDbOperator.restoreResponseNotify(notify);
		}
		
		//发出响应文件完结事件
		ResponseFileNotifyFinishedEvent fileNotifyFinishedEvent = new ResponseFileNotifyFinishedEvent();
		fileNotifyFinishedEvent.setReqId(notify.getReqId());
		fileNotifyFinishedEvent.setRspId(notify.getRspId());
		fileNotifyFinishedEvent.setTenant(notify.getTenant());
		fileEventBus.dispatchEventNoError(fileNotifyFinishedEvent);
	}
	
	private void dubboNotify(ResponseNotify notify) {
		logger.info("FileFront发出文件通知.{}", notify);
		
		if (notify.getDubboGroup() != null) {
			FileNotifyService remoteService = dubboRemoteProxyFacotry.getProxy(FileNotifyService.class,
				notify.getDubboGroup(), notify.getDubboVersion());
				
			FileNotifyOrder order = new FileNotifyOrder();
			Copier.copy(notify, order);
			order.setMerchOrderNo(notify.getMerchantOrderNo());
			
			FileNotifyResult result = remoteService.requestNotify(order);
			
			if (result.isSuccess()) {
				notify.success(StatusConsts.FILE_NOTIFIED);
				fileDbOperator.restoreResponseNotify(notify);
			} else {
				logger.warn("文件响应通知失败.result={}", result);
			}
		} else {
			notify.success(StatusConsts.NOTIFY_IGNORE);
			fileDbOperator.restoreResponseNotify(notify);
		}
		
	}
}
