/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-08 15:35 创建
 *
 */
package com.yiji.adk.filefront.test;

import com.yiji.adk.api.order.FileNotifyOrder;
import com.yiji.adk.filefront.listeners.FileEventBus;
import com.yiji.adk.filefront.listeners.events.ResponseFileNotifyPreparedEvent;
import com.yiji.adk.filefront.provider.FileNotifyProvider;
import com.yiji.adk.filefront.schedule.RequestFileNotifySchedule;
import com.yiji.adk.filefront.schedule.ResponseFileNotifySchedule;
import com.yiji.adk.filefront.support.function.FunctionFactory;
import com.yiji.boot.core.Apps;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author karott
 */
public class FilefrontTestBase {
	
	@Autowired
	protected FileEventBus fileEventBus;
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected FileNotifyProvider fileNotifyProvider;
	@Autowired
	protected RequestFileNotifySchedule requestFileNotifySchedule;
	@Autowired
	protected ResponseFileNotifySchedule responseFileNotifySchedule;
	@Autowired
	protected FunctionFactory functionFactory;
	
	@BeforeClass
	public static void initEnv() {
		Apps.setProfileIfNotExists("stest");
	}
	
	protected void clear() {
		jdbcTemplate.update("delete from FILE_REQ_NOTIFY_LOG");
		jdbcTemplate.update("delete from FILE_RSP_NOTIFY_LOG");
	}
	
	protected void block() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void blockTime(int timeSeconds) {
		try {
			Thread.sleep(timeSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected FileNotifyOrder buildRequestNotifyOrder() {
		FileNotifyOrder order = new FileNotifyOrder();
		
		order.setGid("00000000000000000000000000000000000");
		order.setPartnerId("00000000000000000000");
		order.setMerchOrderNo("00000000000000000000");
		order.setReqId("00000000000000000000");
		order.setBizType("gydRepayment");
		order.setTenant("gyd");
		order.setDubboGroup("test-dubbo");
		order.setDubboVersion("1.0");
		order.setFilePath("/test/download");
		order.setFileName("testparse.txt");
		order.getConfirmData().put("a", "123");
		
		return order;
	}
	
	protected ResponseFileNotifyPreparedEvent buildResponseNotifyEvent() {
		ResponseFileNotifyPreparedEvent event = new ResponseFileNotifyPreparedEvent();
		
		event.setGid("00000000000000000000000000000000000");
		event.setPartnerId("00000000000000000000");
		event.setMerchantOrderNo("00000000000000000000");
		event.setRspId("00000000000000000000");
		event.setReqId("00000000000000000000");
		event.setTenant("gyd");
		event.setBizType("gydRepayment");
		//event.setDubboGroup("test-dubbo");
		//event.setDubboVersion("1.0");
		event.setLocalFilePath("/home/karott/appdata/ftp");
		event.setLocalFileName("testparse.txt");
		
		return event;
	}
}
