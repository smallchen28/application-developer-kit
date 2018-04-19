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

package com.global.adk.plan.task.test;

import com.global.adk.plan.task.statement.ActionContext;
import com.global.adk.plan.task.statement.ExecutorResult;
import com.global.adk.plan.task.statement.ExecutorStatus;

public class ActionStatement2 extends ActionStatement1 {
	
	public int count = 1;
	
	@Override
	public ExecutorResult execute(ActionContext actionContext) {
		
		ExecutorResult result = new ExecutorResult();
		result.setExecInfo("action statement 2");
		
		if (count++ == 1) {//重试一次
			result.setExecutorStatus(ExecutorStatus.ERROR);
		} else {
			result.setExecutorStatus(ExecutorStatus.SUCCESS);
		}
		
		return result;
	}
	
}
