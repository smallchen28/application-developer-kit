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
package com.global.adk.rules.drools.test;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Filename EventNameEnum.java
 * 
 * @Description 事件名称
 * 
 * @Version 1.0
 * 
 * @Author tangwei
 * 
 * @Email tangwei@yiji.com
 * 
 * @History <li>Author: tangwei</li> <li>Date: 2013-5-16</li> <li>Version: 1.0</li>
 * <li>Content: create</li>
 * 
 */
public enum EventNameEnum {
	
	LOGIN("LOGIN", "会员登陆"),
	
	LOGIN_PARTNER("LOGINPARTNER", "信任登陆"),
	
	REGISTER("REGISTER", "用户注册"),
	
	USER_ACTIVE("USER_ACTIVE", "用户激活"),
	
	BINDING_MOBILE("BINDINGMOBILE", "用户绑定手机"),
	
	UNBINDING_MOBILE("UNBINDINGMOBILE", "用户解除绑定手机"),
	
	MODIFY_BANK_CARD("MODIFYBANKCARD", "修改银行卡信息"),
	
	RESET_PASSWORD("RESETPASSWORD", "密码找回"),
	
	DEPOSIT("DEPOSIT", "充值"),
	
	DEPOSIT_APPLY("DEPOSIT_APPLY", "充值申请"),
	
	CREATE_TRADE("CREATETRADE", "创建交易"),
	
	BUYER_PAY("PAYBYACCOUNT", "买家付款"),
	
	MODIFY_PASSWORD("MODIFYPASSWORD", "修改密码"),
	
	TRANSFER_BILL("TRANSFERBILL", "转账到卡"),
	
	WITHDRAW("WITHDRAW", "提现"),
	
	WITHDRAW_APPLY("WITHDRAW_APPLY", "提现申请"),
	
	CBPAYMENT("CBPAYMENT", "外卡支付"),
	
	PAY_REMIT("PAYREMIT", "付汇"),
	
	FAST_PAY("FAST_PAY", "及时到账"),
	
	EQUITY_TRANSFER("EQUITY_TRANSFER", " 权益转账"),
	
	POSPAY_CONSUME("POSPAY_CONSUME", "pos收单-消费"),
	
	POSPAY_CANCEL("POSPAY_CANCEL", "pos收单-撤销"),
	
	POSPAY_DRAWBACK("POSPAY_DRAWBACK", "pos收单-退货"),
	
	POSPAY_CONSUME_FLUSHES("POSPAY_CONSUME_FLUSHES", "pos收单-消费冲正"),
	
	POSPAY_CANCEL_FLUSHES("POSPAY_CANCEL_FLUSHES", "pos收单-撤销冲正"),
	
	STATION_TRANSFER("STATION_TRANSFER", "站内转账"), ;
	
	/** 枚举值 */
	private final String code;
	
	/** 枚举描述 */
	private final String message;
	
	/**
	 * 构造一个<code>EventNameEnum</code>枚举对象
	 * 
	 * @param code
	 * @param message
	 */
	private EventNameEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return Returns the code.
	 */
	public String code() {
		return code;
	}
	
	/**
	 * @return Returns the message.
	 */
	public String message() {
		return message;
	}
	
	/**
	 * 通过枚举<code>code</code>获得枚举
	 * 
	 * @param code
	 * @return EventNameEnum
	 */
	public static EventNameEnum getByCode(String code) {
		for (EventNameEnum _enum : values()) {
			if (_enum.getCode().equals(code)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<EventNameEnum>
	 */
	public static List<EventNameEnum> getAllEnum() {
		List<EventNameEnum> list = new ArrayList<EventNameEnum>();
		for (EventNameEnum _enum : values()) {
			list.add(_enum);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public static List<String> getAllEnumCode() {
		List<String> list = new ArrayList<String>();
		for (EventNameEnum _enum : values()) {
			list.add(_enum.code());
		}
		return list;
	}
}
