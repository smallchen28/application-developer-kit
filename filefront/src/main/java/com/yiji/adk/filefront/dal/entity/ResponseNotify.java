/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-03 14:40 创建
 *
 */
package com.yiji.adk.filefront.dal.entity;

import com.yiji.adk.filefront.support.consts.StatusConsts;

/**
 * @author karott
 */
public class ResponseNotify extends FileNotify {
	
	public void upload() {
		setState(StatusConsts.FILE_UPLOADED);
	}
	
	public void success(String stateCode) {
		setStatus(StatusConsts.SUCCESS);
		setState(stateCode);
	}
	
}
