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

import com.yiji.adk.common.exception.DefaultBizException;
import com.yiji.adk.flow.annotation.*;
import com.yiji.adk.flow.engine.Execution;
import org.springframework.stereotype.Component;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-21 下午8:35<br>
 * @see
 * @since 1.0.0
 */
@Component
public class BizError {
	
	private static Logger logger = LoggerFactory.getLogger(BizError.class);
	
	@Executor
	public String result(Execution execution) {
		
		//这里execution.currentNodeExecution().getError().getMessage()  getError会出现NPE【没有值】
		//		logger.info("及时到账业务检查过程出错Status={},Node={},Error={}", "Fail",
		//			execution.currentNodeExecution().currentNode().getName(),
		//			execution.currentNodeExecution().getError().getMessage());
		
		logger.info("及时到账业务检查过程出错Status={},Node={},Error={}", execution.currentNodeExecution().currentNode().getName());
		
		//测试重试用
		String retry = System.getProperty("retry");
		if (null != retry) {
			throw new DefaultBizException("for retry test executor");
		}
		
		return "biz_over";
	}
}
