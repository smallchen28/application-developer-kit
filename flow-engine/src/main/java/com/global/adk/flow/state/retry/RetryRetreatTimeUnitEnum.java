/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/8-16:48 创建
 *
 */

package com.global.adk.flow.state.retry;

import com.global.adk.flow.state.FlowTrace;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 重试时间单元
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public enum RetryRetreatTimeUnitEnum {
	
	HOUR("hour", "时") {
		@Override
		Date doCalUnitTime(DateTime dateTime, int skipUnit) {
			return dateTime.plusHours(skipUnit).toDate();
		}
	},
	
	MINUTE("minute", "分") {
		@Override
		Date doCalUnitTime(DateTime dateTime, int skipUnit) {
			return dateTime.plusMinutes(skipUnit).toDate();
		}
	},
	
	DAY("day", "天") {
		@Override
		Date doCalUnitTime(DateTime dateTime, int skipUnit) {
			return dateTime.plusDays(skipUnit).toDate();
		}
	},;
	
	/**
	 * 枚举值
	 */
	private final String code;
	
	/**
	 * 枚举描述
	 */
	private final String message;
	
	public void calUnitTime(FlowTrace flowTrace, int skipUnit) {
		DateTime dateTime = new DateTime(flowTrace.getNextRetryTime());
		flowTrace.setNextRetryTime(doCalUnitTime(dateTime, skipUnit));
	}
	
	abstract Date doCalUnitTime(DateTime dateTime, int skipUnit);
	
	/**
	 * @param code 枚举值
	 * @param message 枚举描述
	 */
	private RetryRetreatTimeUnitEnum(String code, String message) {
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
	 * 通过枚举<code>code</code>获得枚举
	 *
	 * @param code
	 * @return RetryStrategyEnum
	 */
	public static RetryRetreatTimeUnitEnum getByCode(String code) {
		for (RetryRetreatTimeUnitEnum _enum : values()) {
			if (_enum.getCode().equals(code)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 *
	 * @return List<RetryStrategyEnum>
	 */
	public static java.util.List<RetryRetreatTimeUnitEnum> getAllEnum() {
		java.util.List<RetryRetreatTimeUnitEnum> list = new java.util.ArrayList<RetryRetreatTimeUnitEnum>(
			values().length);
		for (RetryRetreatTimeUnitEnum _enum : values()) {
			list.add(_enum);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举值
	 *
	 * @return List<String>
	 */
	public static java.util.List<String> getAllEnumCode() {
		java.util.List<String> list = new java.util.ArrayList<String>(values().length);
		for (RetryRetreatTimeUnitEnum _enum : values()) {
			list.add(_enum.getCode());
		}
		return list;
	}
	
	/**
	 * 通过code获取msg
	 *
	 * @param code 枚举值
	 * @return
	 */
	public static String getMsgByCode(String code) {
		if (code == null) {
			return null;
		}
		RetryRetreatTimeUnitEnum _enum = getByCode(code);
		if (_enum == null) {
			return null;
		}
		return _enum.getMessage();
	}
	
	/**
	 * 获取枚举code
	 *
	 * @param _enum
	 * @return
	 */
	public static String getCode(RetryRetreatTimeUnitEnum _enum) {
		if (_enum == null) {
			return null;
		}
		return _enum.getCode();
	}
}
