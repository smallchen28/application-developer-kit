/*
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

package com.global.adk.flow.test;

import com.global.adk.flow.test.fastpay.Consumer;
import com.global.adk.flow.test.fastpay.Customer;
import com.global.adk.flow.FlowContext;
import com.global.common.lang.result.Status;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/flow.xml" })
public class FastPayTest {
	
	private static final Logger logger = LoggerFactory.getLogger(FastPayTest.class);
	
	private static final String FAST_PAY_STATE_MACHINE_NAME = "fast_pay";
	
	private static final int FAST_PAY_STATE_MACHINE_VERSION = 1;
	
	@Autowired
	public FlowContext flowContext;
	
	@BeforeClass
	public static void before() {
		System.setProperty("yiji.dubbo.enable", "false");
		System.setProperty("yiji.yedis.enable", "false");
		System.setProperty("yiji.adk.flowengine.enableRetry", "false");
		System.setProperty("yiji.adk.flowengine.enableRetryProvider", "false");
	}
	
	@Test
	public void testFastPay() {
		Consumer consumer = new Consumer("极限恶女剑", 160);
		consumer.setStatus(Status.PROCESSING);
		consumer.setNode("initialize");
		
		Customer customer = new Customer("李根", consumer);
		
		logger.info("创建并提交消费订单Consumer={}", consumer);
		
		flowContext.start(FAST_PAY_STATE_MACHINE_NAME, FAST_PAY_STATE_MACHINE_VERSION, customer);
	}
	
	@Test
	public void testNotify() {
		logger.info("接受到消费确认通知");
		
		Consumer consumer = new Consumer("极限恶女剑", 160);
		consumer.setStatus(Status.PROCESSING);
		consumer.setNode("wait_notify");
		Customer customer = new Customer("李根", consumer);
		
		flowContext.execute(FAST_PAY_STATE_MACHINE_NAME, "WaitNotify", FAST_PAY_STATE_MACHINE_VERSION, customer);
	}
	
	@Test
	public void testRefund() {
		logger.info("收到交易确认通知（退货）");
		Consumer consumer = new Consumer("极限恶女剑", 160);
		consumer.setStatus(Status.PROCESSING);
		consumer.setNode("transfer_success");
		Customer customer = new Customer("李根", consumer);
		
		flowContext.execute(FAST_PAY_STATE_MACHINE_NAME, "WaitTradeResultConfirm", FAST_PAY_STATE_MACHINE_VERSION,
			customer);
	}
	
}
