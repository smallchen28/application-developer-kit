/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-03-15 17:52 创建
 *
 */
package com.global.adk.flow.state.retry;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class TxWrapper {
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void withNewTx(TxCallback callback) {
		callback.doWithNewTx();
	}
	
	public static interface TxCallback {
		void doWithNewTx();
	}
}
