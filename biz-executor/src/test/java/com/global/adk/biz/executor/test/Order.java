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

import com.global.common.service.OrderBase;

import javax.validation.constraints.NotNull;

public class Order extends OrderBase {
	
	private static final long serialVersionUID = -580190241387790865L;
	
	@NotNull
	private String productName;
	
	private long price;
	
	public Order(String productName, long price) {
		this.productName = productName;
		this.price = price;

		this.setMerchOrderNo("merchOrderNo");
		this.setPartnerId("partnerId:0123456789");
		this.setGid("gid(012345678901234567890123456789)");
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
	
	public boolean isCheckGid() {
		
		return false;
	}
	
}
