/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 21:39 创建
 *
 */
package com.global.adk.filefront.schedule.task;

import com.global.adk.filefront.exceptions.FileBizException;
import com.global.adk.filefront.support.transaction.FileTransactionManager;
import com.yjf.common.concurrent.MonitoredThreadPoolExecutor;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author karott
 */
public class FileTaskExecutor {
	
	private final static Logger logger = LoggerFactory.getLogger(FileTaskExecutor.class);
	
	private ExecutorService executorService;
	
	@Autowired
	private FileTransactionManager fileTransactionManager;
	
	public FileTaskExecutor(int core, int max, long keepAliveTime, int capacity) {
		executorService = new MonitoredThreadPoolExecutor(core, max, keepAliveTime, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(capacity), runnable -> {
				Thread thread = new Thread(runnable);
				thread.setName("FileFront");
				return thread;
			} , new ThreadPoolExecutor.AbortPolicy());
			
		((MonitoredThreadPoolExecutor) executorService).initialize();
	}
	
	public void executeTask(boolean tx, ExecuteCallback callback) {
		try {
			logger.info("FileFront-Executor开始执行.{}", callback);
			
			executorService.submit(() -> {
				try {
					if (tx) {
						fileTransactionManager.executeTX(new TransactionCallbackWithoutResult() {
							@Override
							protected void doInTransactionWithoutResult(TransactionStatus status) {
								try {
									callback.doExecute();
								} catch (Exception e) {
									logger.error("事务内执行异常", e);
									
									status.setRollbackOnly();
									throw new FileBizException(e);
								}
							}
						});
					} else {
						callback.doExecute();
					}
				} catch (Exception e) {
					logger.error("FileFront-Executor执行异常.{}", callback, e);
				}
			});
		} catch (Exception e) {
			logger.error("FileFront-Executor执行失败.{}", callback, e);
		}
	}
	
	public static abstract class ExecuteCallback {
		
		private String action;
		private Object param;
		
		public ExecuteCallback(String action, Object param) {
			this.action = action;
			this.param = param;
		}
		
		public abstract void doExecute();
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[action]:").append(action);
			builder.append(",[param]：").append(null != param ? param : "null");
			
			return builder.toString();
		}
	}
	
}
