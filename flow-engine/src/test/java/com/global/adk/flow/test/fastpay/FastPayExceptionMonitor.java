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
package com.global.adk.flow.test.fastpay;

import com.global.adk.flow.engine.ExceptionMonitor;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.Flow;
import com.global.adk.flow.module.FlowNode;
import org.springframework.stereotype.Component;

import com.global.common.log.Logger;
import com.global.common.log.LoggerFactory;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-6-12 下午3:52<br>
 * @see
 * @since 1.0.0
 */
@Component
public class FastPayExceptionMonitor implements ExceptionMonitor {
	
	private static final transient Logger logger = LoggerFactory.getLogger(FastPayExceptionMonitor.class);
	
	@Override
	public void catcher(Flow flow, FlowNode node, Execution execution, Throwable throwable) {
		logger.error("状态机StateMachine={},Version={},Node={}出现意外错误", flow.getName(), flow.getVersion(),
			node.getName(), throwable);
	}
}
