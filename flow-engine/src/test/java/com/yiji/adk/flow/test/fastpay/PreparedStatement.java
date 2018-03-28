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

/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.test.fastpay;

import com.yiji.adk.flow.annotation.Before;
import com.yiji.adk.flow.annotation.Executor;
import com.yiji.adk.flow.engine.Execution;
import org.springframework.stereotype.Component;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-21 下午2:58<br>
 * @see
 * @since 1.0.0
 */
@Component
public class PreparedStatement {
	
	private Logger logger = LoggerFactory.getLogger(PreparedStatement.class);
	
	@Before
	public void prepareMetadata(Execution execution) {
        logger.info("\n***************测试用例**************************\n* 1. price<100将在选择支付方式部分出错……          *\n* 2. price=>100业务正常进行，等待支付通知……        *\n***************测试用例**************************");
	}

	@Executor
	public void execute(Execution execution) {

		Customer customer = execution.getTarget();
		if (customer.getConsumer().getPrice() >= 100) {
			logger.info("预处理结果：业务判断price >= 100等待支付通知");
		} else {
			logger.info("预处理结果：业务判断price < 100要报错");
		}
	}


}
