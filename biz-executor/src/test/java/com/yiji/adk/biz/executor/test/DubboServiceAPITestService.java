/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-19 17:49 创建
 *
 */
package com.yiji.adk.biz.executor.test;

import com.yiji.adk.api.annotation.DubboServiceAPI;
import com.yjf.common.lang.context.OperationContext;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
@DubboServiceAPI(group = "test", version = "1.0")
public interface DubboServiceAPITestService {
	
	SubResult testService(TestApiOrder order, OperationContext context);
	
	SubResult testServiceNoContext(TestApiOrder order);
	
}
