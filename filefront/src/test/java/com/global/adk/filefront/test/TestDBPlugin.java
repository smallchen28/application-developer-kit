/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-05 21:11 创建
 *
 */
package com.global.adk.filefront.test;

import com.global.adk.active.record.module.DBPlugin;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author karott
 */
@Component
public class TestDBPlugin implements DBPlugin {
	@Override
	public Timestamp currentTimestamp() {
		return new Timestamp(10000);
	}
	
	@Override
	public Long nextVar(String sequenceName) {
		return (long) 1;
	}
	
	@Override
	public void lock(String policy, String module, String lockName) {
	
	}
	
	@Override
	public void lockNoWaite(String policy, String module, String lockName) {
	
	}
}
