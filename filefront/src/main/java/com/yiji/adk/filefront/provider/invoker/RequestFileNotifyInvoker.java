/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 20:22 创建
 *
 */
package com.yiji.adk.filefront.provider.invoker;

import com.yiji.adk.active.record.DomainFactory;
import com.yiji.adk.biz.executor.ServiceContext;
import com.yiji.adk.biz.executor.annotation.Invoke;
import com.yiji.adk.common.Constants;
import com.yiji.adk.filefront.dal.entity.RequestNotify;
import com.yiji.adk.filefront.exceptions.RequestFileNotifyException;
import com.yiji.adk.api.order.FileNotifyOrder;
import com.yiji.adk.api.result.FileNotifyResult;
import com.yiji.adk.filefront.listeners.events.RequestFileNotifyIdempotencedEvent;
import com.yiji.adk.filefront.listeners.events.inner.RequestFileNotifyInitedEvent;
import com.yiji.adk.filefront.listeners.events.inner.RequestFileNotifyReceivedEvent;
import com.yiji.adk.filefront.schedule.task.FileTaskExecutor;
import com.yiji.adk.filefront.support.client.FileClient;
import com.yiji.adk.filefront.support.client.FileClientFactory;
import com.yiji.adk.filefront.support.consts.InvokeConsts;
import com.yjf.common.lang.beans.Copier;
import com.yjf.common.lang.result.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

/**
 * @author karott
 */
@Invoke(serviceName = InvokeConsts.SERVICE_REQ_FILE_NOTIFY)
public class RequestFileNotifyInvoker extends FileInvokerSupport<FileNotifyOrder, FileNotifyResult> {
	
	@Autowired
	private DomainFactory domainFactory;
	
	@Override
	public void before(ServiceContext<FileNotifyOrder, FileNotifyResult> serviceContext) {
		processBefore(serviceContext);
		
		RequestNotify notify = domainFactory.newInstance(RequestNotify.class, false);
		notify.setId(fileDbOperator.nextSeq());
		notify.setMerchantOrderNo(serviceContext.getParameter().getMerchOrderNo());
		serviceContext.setEntityObject(notify);
	}
	
	@Override
	public void invoke(ServiceContext<FileNotifyOrder, FileNotifyResult> serviceContext) {
		RequestNotify notify = (RequestNotify) serviceContext.getEntityObject();
		
		try {
			Copier.copy(serviceContext.getParameter(), notify, "fileService");
			notify.setFileService(serviceContext.getParameter().getFileService());
			
			fileDbOperator.storeRequestNotify(notify);
		} catch (DuplicateKeyException e) {
			postIdempotency(notify, e.getMessage());
		}
	}
	
	@Override
	public void after(ServiceContext<FileNotifyOrder, FileNotifyResult> serviceContext) {
		throw new RequestFileNotifyException("处理中", Constants.SUCCESS_CODE, Status.PROCESSING);
	}
	
	@Override
	public void end(ServiceContext<FileNotifyOrder, FileNotifyResult> serviceContext) {
		if (!serviceContext.result().isFail()) {
			
			fileTaskExecutor.executeTask(true,
				new FileTaskExecutor.ExecuteCallback("request notify 初始化事件发出", serviceContext.getEntityObject()) {
					@Override
					public void doExecute() {
						RequestFileNotifyInitedEvent initedEvent = new RequestFileNotifyInitedEvent();
						Copier.copy(serviceContext.getParameter(), initedEvent);
						initedEvent.setMerchantOrderNo(serviceContext.getParameter().getMerchOrderNo());
						
						RequestNotify notify = fileDbOperator.byReqIdLocked(serviceContext.getParameter().getReqId(),
							serviceContext.getParameter().getTenant());
						initedEvent.setFileNotify(notify);
						
						fileEventBus.dispatchEvent(initedEvent);
					}
				});
				
		}
	}
	
	private void processBefore(ServiceContext<FileNotifyOrder, FileNotifyResult> serviceContext) {
		RequestFileNotifyReceivedEvent recvEvent = new RequestFileNotifyReceivedEvent();
		Copier.copy(serviceContext.getParameter(), recvEvent);
		recvEvent.setMerchantOrderNo(serviceContext.getParameter().getMerchOrderNo());
		fileEventBus.dispatchEvent(recvEvent);
		
		FileClient fileClient = FileClientFactory.client(serviceContext.getParameter().getBizType(),
			serviceContext.getParameter().getTenant());
		fileClient.checkFileFromFTP(serviceContext.getParameter().getFilePath(),
			serviceContext.getParameter().getFileName());
	}
	
	private boolean postIdempotency(RequestNotify notify, String description) {
		try {
			logger.info("FileFront Request Notify幂等操作.notify:[{}]", notify);
			
			RequestFileNotifyIdempotencedEvent idempotenceEvent = new RequestFileNotifyIdempotencedEvent();
			Copier.copy(notify, idempotenceEvent);
			fileEventBus.dispatchEvent(idempotenceEvent);
		} catch (Exception e1) {
			logger.error("业务错误,也嘿,你们干了啥子", e1);
		}
		
		RequestNotify requestNotifyFromStore = fileDbOperator.byReqId(notify.getReqId(), notify.getTenant());
		Copier.copy(requestNotifyFromStore, notify);
		
		Status status = Status.findStatus(notify.getStatus());
		throw new RequestFileNotifyException(description, Constants.REQUEST_REPEATED, status);
	}
}
