/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-10-08 10:59 创建
 *
 */
package com.yiji.adk.filefront.test;

import com.yiji.adk.filefront.support.function.ConvertFunction;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author karott
 */
@Component("testFunction")
public class TestFunction implements ConvertFunction {
	
	@Override
	public String convert(Map<String, Object> params) {
		return "just for test";
	}
}
