/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.common;

/**
 * 系统常量定义
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月22日 下午2:46:01<br>
 */
public interface Constants {
	
	/** Drools默认延迟执行时间(秒) */
	public static final int DROOLS_DEFAULT_DELAY_SENCD = 0;
	
	//----------------------------
	//- AppKit错误源定义,错误码定义
	//----------------------------
	public static final String ERROR_SALF_SOURCE = "APP_KIT";
	
	public static final String UNKOWN_ERROR_SALF_SOURCE = "OWNER_SYSTEM";
	
	/** 内部系统异常错误 */
	public static final String ERROR_CODE_NEST = "comn_03_0000";
	
	/** 未知错误 */
	public static final String ERROR_CODE_UNKOWN = "comn_04_0000";
	
	/** 系统挂起 */
	public static final String ERROR_CODE_SUPPEND = "S064_02_0012";
	
	/** 请求参数检查出错 */
	public static final String ERROR_CODE_ILLEGA_PARAMETER = "comn_04_0003";
	
	/** 执行成功 */
	public static final String SUCCESS_CODE = "comn_04_0001";
	
	/** 重复请求 */
	public static final String REQUEST_REPEATED = "comn_04_0004";
	
}
