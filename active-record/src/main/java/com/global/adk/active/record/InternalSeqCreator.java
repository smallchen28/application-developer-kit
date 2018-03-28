/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15/8/18 下午1:53<br>
 * @see
 * @since 1.0.0
 */
public interface InternalSeqCreator {
	
	/**
	 * 生成业务流水号
	 * @param genSeq 范围内唯一序列，比如20121201时间范围内唯一序列
	 * @param bizPrefix 填充
	 * @return
	 */
	String generateBizNo(long genSeq, String bizPrefix);
	
	/**
	 * 根据序列名称进行生成和genSeq用法一致
	 * @param sequenceName 序列名称
	 * @param bizPrefix 填充
	 * @return
	 */
	String generateBizNo(String sequenceName, String bizPrefix);
	
	/**
	 * 生存唯一序号
	 * @param sequenceName 序列名称
	 * @return
	 */
	long generateIdentity(String sequenceName);
	
}
