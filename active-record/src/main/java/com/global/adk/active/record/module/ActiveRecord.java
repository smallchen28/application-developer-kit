/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record.module;

import com.global.adk.active.record.DomainFactory;

/**
 * 领域模型层基础动作定义
 * @author hasulee
 * @since 1.0.0 2014年7月23日
 * @version 1.0.0
 * @see
 */
public interface ActiveRecord {
	
	/**
	 * 持久化操作
	 */
	Object insert();
	
	/**
	 * 更新操作
	 */
	int update();
	
	/**
	 * 删除操作
	 */
	int delete();
	
	/**
	 * 根据唯一标示加载领域模型
	 * @param uniqueKey 唯一约束对象，对多建唯一约束进行支持
	 * @param queryID ibatis映射SQL语句ID
	 * @return
	 */
	<T> boolean load(T uniqueKey, String queryID);

	/**
	 * 获取领域工厂用于构建
	 * @return
	 */
	DomainFactory domainFactory();
}
