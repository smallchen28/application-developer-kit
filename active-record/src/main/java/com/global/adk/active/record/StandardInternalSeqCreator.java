/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record;

import com.global.adk.active.record.module.DBPlugin;
import com.global.adk.common.exception.DomainException;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 内部标准流水号生存器
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-8-17 下午6:29<br>
 * @see
 * @since 1.0.0
 */
public class StandardInternalSeqCreator implements InternalSeqCreator {
	
	private static final String SEQ_PLACEHOLDER = "00000000";
	/**
	 * 上次生成时间
	 */
	private static long LAST_GEN_TIME = 0;
	
	/**
	 * 上次生成日期字符串
	 */
	private static String LAST_GEN_STR = "";
	
	/**
	 * 1小时的毫秒数
	 */
	private static final long MS_IN_hours = 60 * 60 * 1000L;
	
	private DBPlugin dbPlugin;
	
	public StandardInternalSeqCreator(DBPlugin dbPlugin) {
		if (dbPlugin == null) {
			throw new DomainException("初始化InternalSeqCreator出错，DBPlugin不能为空");
		}
		
		this.dbPlugin = dbPlugin;
	}
	
	/**
	 * 按照易极付体系内20位长度 4bizPrefix + yymmddhh + 8seq
	 */
	public String generateBizNo(long genSeq, String bizPrefix) {
		
		StringBuilder sb = new StringBuilder();
		//- 截取业务扩展编号
		String _bizPrefix = "0000";
		
		if (bizPrefix != null && bizPrefix.length() == 4) {
			_bizPrefix = bizPrefix;
		}
		
		sb.append(_bizPrefix);
		sb.append(getDate(dbPlugin));
		StringBuilder seq = new StringBuilder(SEQ_PLACEHOLDER);
		
		seq.append(genSeq);
		
		//截取最后八位
		sb.append(seq.substring(seq.length() - 8));
		
		return sb.toString();
	}
	
	@Override
	public String generateBizNo(String sequenceName, String bizPrefix) {
		return generateBizNo(generateIdentity(sequenceName), bizPrefix);
	}
	
	private static String getDate(DBPlugin dbPlugin) {
		
		long current = System.currentTimeMillis();
		if (current - LAST_GEN_TIME > MS_IN_hours) {
			StringBuilder sb = new StringBuilder();
			Calendar now = new GregorianCalendar();
			now.setTime(dbPlugin.currentTimestamp());
			//取出yyyy/MM/dd/hh
			String year = String.valueOf(now.get(Calendar.YEAR));
			String month = String.valueOf(now.get(Calendar.MONTH) + 1);
			String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
			String hours = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
			//拼接日期
			year = year.substring(2);
			sb.append(year);
			if (month.length() != 2) {
				sb.append("0");
			}
			sb.append(month);
			if (day.length() != 2) {
				sb.append("0");
			}
			sb.append(day);
			if (hours.length() != 2) {
				sb.append("0");
			}
			sb.append(hours);
			String str = sb.toString();
			
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			LAST_GEN_TIME = now.getTimeInMillis();
			LAST_GEN_STR = str;
			return str;
		} else {
			return LAST_GEN_STR;
		}
	}
	
	public long generateIdentity(String sequenceName) {
		return dbPlugin.nextVar(sequenceName);
	}
}
