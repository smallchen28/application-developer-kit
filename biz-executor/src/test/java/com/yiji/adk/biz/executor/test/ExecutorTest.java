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

package com.yiji.adk.biz.executor.test;

import com.yiji.adk.biz.executor.ActivityExecutorContainer;
import com.yiji.adk.common.exception.DomainException;
import com.yjf.common.lang.context.OperationContext;
import com.yjf.common.lang.context.OperationContext.BusinessTypeEnum;
import com.yjf.common.lang.context.OperationContext.OperationTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * 同步、异步、串行、事务
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月24日 下午6:25:21<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/biz-executor.xml" })
public class ExecutorTest {
	
	@Autowired
	private ActivityExecutorContainer activityExecutorContainer;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void txFailTest() {

		try{
			jdbcTemplate.update("delete from app_kit_order_test");

			Order order = new Order("jixianenv", 11L);

			activityExecutorContainer.accept(order, "order_test",
					OperationContext.build("app_kit", "app_kit", OperationTypeEnum.test, BusinessTypeEnum.TEST));

			//这里应该要出现异常
		}catch(DomainException e){
			Assert.assertEquals(e.getMessage(),"序列号生存器InternalSeqCreator尚未初始化，不支持的操作...");
		}
	}

	@Test
	public void asynFailTest() throws InterruptedException {

		try {
			jdbcTemplate.update("delete from app_kit_order_test");

			Order order = new Order("jixianenv", 11L);

			activityExecutorContainer.accept(order, "order_asyn_test",
                OperationContext.build("app_kit", "app_kit", OperationTypeEnum.test, BusinessTypeEnum.TEST));
		} catch (DomainException e) {
			Assert.assertEquals(e.getMessage(),"序列号生存器InternalSeqCreator尚未初始化，不支持的操作...");
		}


	}
	
	@Test
	public void noneEntityTest() {
		Order order = new Order("jixianenv", 11L);
		OrderResult result = activityExecutorContainer.accept(order, "none_entity_invoke_service",
			OperationContext.build("app_kit", "app_kit", OperationTypeEnum.test, BusinessTypeEnum.TEST));
		Assert.assertTrue(result != null);
	}
	

	@Test
	public void innerGenericTest() {
		activityExecutorContainer.accept(null, "test_result_invoke_service",
			OperationContext.build("app_kit", "app_kit", OperationTypeEnum.test, BusinessTypeEnum.TEST));
	}
}
