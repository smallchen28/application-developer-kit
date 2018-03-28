/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.engine;

/**
 * 流程执行大状态： 挂起、失败、成功.
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-5-19 下午2:41<br>
 * @see
 * @since 1.0.0
 */
public enum ExecutionStatus {
	SUSPEND,
	FAIL,
	SUCCESS;
}
