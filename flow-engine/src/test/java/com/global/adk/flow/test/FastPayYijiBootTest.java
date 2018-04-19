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
import com.global.adk.flow.state.retry.RetryFlowProvider;
import com.global.boot.core.Apps;
import com.global.boot.core.YijiBootApplication;
import com.global.common.lang.result.Status;
import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author hasulee
 * @email ligen@yiji.com
 * @history hasuelee创建于15/9/24 下午下午2:02<br>
 * @see
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { FastPayYijiBootTest.class })
@YijiBootApplication(sysName = "yiji-adk-test", heraEnable = false, httpPort = 0)
@Configuration
public class FastPayYijiBootTest {
	private static final Logger logger = LoggerFactory.getLogger(FastPayYijiBootTest.class);
	
	private static final String FAST_PAY_STATE_MACHINE_NAME = "fast_pay";
	private static final int FAST_PAY_STATE_MACHINE_VERSION = 1;
	
	private static final String PROFILE = "stest";
	
	@Autowired
	protected FlowContext flowContext;
	@Autowired(required = false)
	protected RetryFlowProvider retryFlowProvider;
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void initEnv() {
		Apps.setProfileIfNotExists(PROFILE);
		System.setProperty("yiji.adk.flowengine.enableRetry", "false");
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
