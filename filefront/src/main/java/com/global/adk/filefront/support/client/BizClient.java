/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-02-15 10:10 创建
 *
 */
package com.global.adk.filefront.support.client;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public abstract class BizClient implements FileClient {
	
	protected String bizType;
	
	public BizClient(String bizType) {
		this.bizType = bizType;
	}
	
	public String getBizType() {
		return bizType;
	}
	
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
}
