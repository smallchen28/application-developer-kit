/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.biz.executor;

import com.yjf.common.lang.result.StandardResultInfo;
import org.springframework.beans.factory.BeanNameAware;

/**
 * 服务处理器基类实现
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月17日 下午1:18:25<br>
 */
public interface InvokeService<PARAM, RESULT extends StandardResultInfo> extends BeanNameAware {
	
	void invoke(ServiceContext<PARAM, RESULT> serviceContext);
	
	void before(ServiceContext<PARAM, RESULT> serviceContext);
	
	void after(ServiceContext<PARAM, RESULT> serviceContext);
	
	void end(ServiceContext<PARAM, RESULT> serviceContext);
	
	String getInvockServiceName();
}
