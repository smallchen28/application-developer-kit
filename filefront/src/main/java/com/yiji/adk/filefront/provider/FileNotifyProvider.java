/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-08-31 20:10 创建
 *
 */
package com.yiji.adk.filefront.provider;

import com.yiji.adk.filefront.support.consts.InvokeConsts;
import com.yiji.adk.api.order.FileNotifyOrder;
import com.yiji.adk.api.result.FileNotifyResult;
import com.yiji.adk.api.service.FileNotifyService;
import com.yjf.common.lang.context.OperationContext;

/**
 * @author karott
 */
public class FileNotifyProvider extends ProviderSupport implements FileNotifyService {
	
	@Override
	public FileNotifyResult requestNotify(FileNotifyOrder order) {
		
		return getContainer().accept(order, InvokeConsts.SERVICE_REQ_FILE_NOTIFY, new OperationContext());
	}
}
