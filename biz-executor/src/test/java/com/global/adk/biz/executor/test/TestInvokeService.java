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

package com.global.adk.biz.executor.test;/*
* www.yiji.com Inc.
* Copyright (c) 2014 All Rights Reserved
*/

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-12-01 22:05 创建
 *
 */

import com.global.adk.biz.executor.InvokeServiceAdapter;
import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.biz.executor.annotation.Invoke;
import com.yjf.common.lang.beans.Copier;

/**
 * @author qzhanbo@yiji.com
 */
@Invoke(entityType = Trade.class, serviceName = "trade")
public class TestInvokeService extends InvokeServiceAdapter<Order, TradeResult> {
	
	@Override
	public void invoke(ServiceContext<Order, TradeResult> serviceContext) {
		Order order = serviceContext.getParameter();
		Trade trade = (Trade) serviceContext.getEntityObject();
		trade.convertFrom(order);
		trade.generateIdentity("seq_app_kit_plan_task");
		trade.insert();
		serviceContext.result().setPrice(trade.getPrice());
	}
}
