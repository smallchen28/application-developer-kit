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

package com.yiji.adk.plan.task.test;

import com.yiji.adk.plan.task.statement.ActionContext;
import com.yiji.adk.plan.task.statement.ActionStatement;
import com.yiji.adk.plan.task.statement.ExecutorResult;
import com.yiji.adk.plan.task.statement.ExecutorStatus;

public class ActionStatement5 implements ActionStatement {
	
	@Override
	public ExecutorResult execute(ActionContext actionContext) {
		
		ExecutorResult result = new ExecutorResult();
		result.setExecInfo("action statement 1");
		result.setExecutorStatus(ExecutorStatus.SUCCESS);
		return result;
	}
	
}
