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

package com.global.adk.biz.executor.test;

import com.global.adk.active.record.annotation.SqlBinder;
import com.global.adk.active.record.module.AggregateRoot;
import com.global.adk.active.record.module.EntityObject;

@SqlBinder(insert = "ORDER_INSERT", delete = "X", update = "X")
public class CustomerOrder extends EntityObject {
	
	private static final long serialVersionUID = -2014268639534931811L;
	
	private String productName;
	
	private long price;
	
	public CustomerOrder() {
		
	}
	
	@Override
	public void reference(AggregateRoot ref) {
		
	}
	
	public String getProductName() {
		
		return productName;
	}
	
	public void setProductName(String productName) {
		
		this.productName = productName;
	}
	
	public long getPrice() {
		
		return price;
	}
	
	public void setPrice(long price) {
		
		this.price = price;
	}
	
}
