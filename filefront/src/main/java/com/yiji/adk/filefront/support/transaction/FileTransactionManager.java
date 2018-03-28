/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-30 08:33 创建
 *
 */
package com.yiji.adk.filefront.support.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author karott
 */
public class FileTransactionManager {
	
	private TransactionTemplate transactionTemplate;
	
	public FileTransactionManager(PlatformTransactionManager platformTransactionManager) {
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		transactionTemplate.afterPropertiesSet();
	}
	
	public void executeTX(TransactionCallbackWithoutResult callback) {
		transactionTemplate.execute(callback);
	}
	
}
