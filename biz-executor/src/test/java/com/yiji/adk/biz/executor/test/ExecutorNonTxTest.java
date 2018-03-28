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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yiji.adk.biz.executor.ActivityExecutorContainer;
import com.yiji.adk.common.exception.DomainException;
import com.yjf.common.lang.context.OperationContext;
import com.yjf.common.lang.context.OperationContext.BusinessTypeEnum;
import com.yjf.common.lang.context.OperationContext.OperationTypeEnum;

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
@ContextConfiguration({ "classpath*:/spring/biz-executor-non-tx.xml" })
public class ExecutorNonTxTest {
	
	@Autowired
	private ActivityExecutorContainer activityExecutorContainer;
	

	@Test
	public void noneEntityTest() {
		Order order = new Order("jixianenv", 11L);
		OrderResult result = activityExecutorContainer.accept(order, "non-tx-invoke",
			OperationContext.build("app_kit", "app_kit", OperationTypeEnum.test, BusinessTypeEnum.TEST));
		Assert.assertTrue(result != null);
		Assert.assertTrue(result.getIdentity() == 0);//Oracle下事物木有生效。
	}

}
