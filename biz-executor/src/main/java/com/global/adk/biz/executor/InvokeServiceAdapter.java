/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor;


import com.yjf.common.lang.result.StandardResultInfo;

/**
 * 适配，默认实现。
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月19日 下午3:47:30<br>
 */

public abstract class InvokeServiceAdapter<Param, RESULT extends StandardResultInfo> extends
																				AbstractInvokeService<Param, RESULT> {
	
	@Override
	public void after(ServiceContext<Param, RESULT> serviceContext) {
		
	}
	
	@Override
	public void before(ServiceContext<Param, RESULT> serviceContext) {
		
	}
	
	@Override
	public void end(ServiceContext<Param, RESULT> serviceContext) {
		
	}
	
}
