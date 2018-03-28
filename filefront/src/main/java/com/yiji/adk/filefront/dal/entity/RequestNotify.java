/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 14:39 创建
 *
 */
package com.yiji.adk.filefront.dal.entity;

import com.yiji.adk.filefront.support.consts.StatusConsts;

/**
 * @author karott
 */
public class RequestNotify extends FileNotify {
	
	public boolean isInit() {
		return getState().equals(StatusConsts.INIT);
	}
	
	public void fail(String stateCode) {
		setStatus(StatusConsts.FAIL);
		setState(stateCode);
		setErrorCode(stateCode);
	}
	
	public void download() {
		setState(StatusConsts.FILE_DOWNLOADED);
	}
	
	public void success(String stateCode) {
		setStatus(StatusConsts.SUCCESS);
		setState(stateCode);
	}
	
}
