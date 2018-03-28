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

import com.yiji.adk.biz.executor.InvokeServiceAdapter;
import com.yiji.adk.biz.executor.ServiceContext;
import com.yiji.adk.biz.executor.annotation.Invoke;

/**
 * Created by hasulee on 15-1-13.
 */
@Invoke(serviceName = "test_result_invoke_service", logName = "XXX",
		transactionAttribute = @Invoke.TransactionAttribute(isTx = false))
public class TestResultInvokeService extends InvokeServiceAdapter<Void, TestResultBase<Order>> {
	
	@Override
	public void invoke(ServiceContext serviceContext) {
		
	}
	
}
