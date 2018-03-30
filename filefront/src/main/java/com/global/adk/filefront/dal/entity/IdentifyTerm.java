/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-30 09:48 创建
 *
 */
package com.global.adk.filefront.dal.entity;

import com.yjf.common.util.ToString;

/**
 * @author karott
 */
public class IdentifyTerm {
	
	private String idempotency;
	private String tenant;
	private String state;
	
	public String getIdempotency() {
		return idempotency;
	}
	
	public void setIdempotency(String idempotency) {
		this.idempotency = idempotency;
	}
	
	public String getTenant() {
		return tenant;
	}
	
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
