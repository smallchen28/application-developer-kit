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
package com.yiji.adk.flow.test.fastpay;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-7-21 下午3:38<br>
 * @see
 * @since 1.0.0
 */
public class Customer {
	
	private String userName;
	
	private long balance;
	
	private Consumer consumer;
	
	public Customer(String userName, Consumer consumer) {
		this.userName = userName;
		this.consumer = consumer;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public long getBalance() {
		return balance;
	}
	
	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	public Consumer getConsumer() {
		return consumer;
	}
	
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.test.fastpay.Customer{");
		sb.append("balance=").append(balance);
		sb.append(", consumer=").append(consumer);
		sb.append(", userName='").append(userName).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
