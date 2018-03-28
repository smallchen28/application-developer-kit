/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record.module;

/**
 * 聚合根定义
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月28日
 * @version 1.0.0
 * @see
 */
public abstract class AggregateRoot extends EntityObject {
	
	private static final long serialVersionUID = 5921176121600366917L;
	
	protected void relation(EntityObject entityObject) {
		entityObject.reference(this);
	}
	
	@Override
	public void reference(AggregateRoot ref) {
	}
}
