/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.biz.executor;

import com.yjf.common.lang.context.OperationContext;
import com.yjf.common.lang.result.StandardResultInfo;

/**
 * 服务执行容器
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月16日 上午10:56:04<br>
 */
public interface ExecutorContainer {
	
	<Param, R extends StandardResultInfo> R accept(Param parameter, String serviceName, OperationContext operationContext);
	
}
