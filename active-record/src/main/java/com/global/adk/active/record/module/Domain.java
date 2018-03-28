/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record.module;

import java.io.Serializable;

/**
 * 领域模型顶层接口定义
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年7月23日
 * @version 1.0.0
 * @see
 */
public interface Domain extends Serializable {
	
	/**
	 * DTO --> Domain拷贝
	 * @param from
	 */
	<DTO> void convertFrom(DTO from);
	
	/**
	 * DTO --> domain拷贝，并提供属性过滤。
	 * @param dto
	 * @param ignore
	 */
	<DTO> void convertFrom(DTO dto, String... ignore);
	
	/**
	 * domain --> DTO 拷贝
	 * @param dto
	 */
	<DTO> void convertTo(DTO dto);
	
	/**
	 * domain --> DTO 拷贝 ,通过properties进行复制过滤。
	 * @param dto
	 * @param ignore
	 */
	<DTO> void convertTo(DTO dto, String... ignore);
	
	/**
	 * 发布领域事件
	 * @param events
	 */
	void publish(Object... events);
}
