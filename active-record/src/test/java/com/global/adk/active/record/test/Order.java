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

package com.global.adk.active.record.test;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.annotation.SqlBinder;
import com.global.adk.active.record.module.AggregateRoot;
import com.global.adk.active.record.module.EntityObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SqlBinder(insert = "ACTIVE_RECORD_ORDER.ORDER_INSERT", delete = "ACTIVE_RECORD_ORDER.ORDER_DELETE",
		update = "ACTIVE_RECORD_ORDER.ORDER-UPDATE")
public class Order extends EntityObject {
	
	private static final long serialVersionUID = -3699665642076551244L;
	
	@NotNull(groups = { OrderInsertGroup.class })
	private String productName;
	
	@NotNull(groups = { OrderInsertGroup.class })
	@Size(min = 1, groups = { OrderInsertGroup.class, OrderUpdateGroup.class })
	private int price;
	
	@Autowired
	public DomainFactory domainFactory;
	
	public String getProductName() {
		
		return productName;
	}
	
	public void setProductName(String productName) {
		
		this.productName = productName;
	}
	
	public int getPrice() {
		
		return price;
	}
	
	public void setPrice(int price) {
		
		this.price = price;
	}
	
	@Override
	public void reference(AggregateRoot ref) {
		
		//nothing ...
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("Order [productName=");
		builder.append(productName);
		builder.append(", price=");
		builder.append(price);
		builder.append(", getIdentity()=");
		builder.append(getIdentity());
		builder.append(", getBizNo()=");
		builder.append(getBizNo());
		builder.append("]");
		return builder.toString();
	}
	
}
