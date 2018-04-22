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
package com.global.adk.biz.executor.test;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.biz.executor.InvokeServiceAdapter;
import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.biz.executor.annotation.Invoke;
import com.global.common.lang.beans.Copier;
import org.springframework.beans.factory.annotation.Autowired;

@Invoke(entityType = CustomerOrder.class, serviceName = "order_asyn_test", logName = "XXX", isAsync = true)
public class AsyncInvokeService extends InvokeServiceAdapter<Order, OrderResult> {
	
	@Autowired
	private DomainFactory domainFactory;
	
	public AsyncInvokeService() {
		
	}
	
	@Override
	public void invoke(ServiceContext<Order, OrderResult> serviceContext) {
		
		Order order = serviceContext.getParameter();
		CustomerOrder customerOrder = (CustomerOrder) serviceContext.getEntityObject();
		Copier.copy(order, customerOrder);
		customerOrder.generateIdentity("seq_app_kit_plan_task");
		customerOrder.insert();
		serviceContext.result().setIdentity(customerOrder.getIdentity());
	}
	
}
