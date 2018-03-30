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

package com.global.adk.rules.drools.test;

import com.google.common.collect.Maps;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.yjf.common.lang.util.money.Money;

import java.util.Map;

public class Order {
	
	private String productName;
	
	private double price;
	
	private EventNameEnum eventName;
	
	private Money money;
	
	private Map<String, String> extendsProperties = Maps.newHashMap();
	
	public Order(String productName, double price) {
		this.productName = productName;
		this.price = price;
	}
	
	public Order(String productName, double price, Map<String, String> extendsProperties, EventNameEnum eventName,
					Money money) {
		this.eventName = eventName;
		this.money = money;
		this.extendsProperties = extendsProperties;
		this.productName = productName;
		this.price = price;
	}
	
	public String getProductName() {
		
		return productName;
	}
	
	public void setProductName(String productName) {
		
		this.productName = productName;
	}
	
	public double getPrice() {
		
		return price;
	}
	
	public void setPrice(double price) {
		
		this.price = price;
	}
	
	public Map<String, String> getExtendsProperties() {
		return extendsProperties;
	}
	
	public void setExtendsProperties(Map<String, String> extendsProperties) {
		this.extendsProperties = extendsProperties;
	}
	
	public EventNameEnum getEventName() {
		return eventName;
	}
	
	public void setEventName(EventNameEnum eventName) {
		this.eventName = eventName;
	}
	
	public Money getMoney() {
		return money;
	}
	
	public void setMoney(Money money) {
		this.money = money;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
