/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-30 18:35 创建
 *
 */
package com.yiji.adk.filefront.support.function;

import com.google.common.collect.Maps;
import org.mvel2.MVEL;

import java.util.Map;

/**
 * @author karott
 */
public class FunctionParser {
	
	public static String parse(ConvertFunction function, Map<String, Object> params) {
		Map<String, Object> finalParams = Maps.newHashMap();
		finalParams.put("func", function);
		finalParams.put("params", params);
		
		return (String) MVEL.eval("func.convert(params)", finalParams);
	}
	
	public static void main(String[] args) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("from", "all right");
		params.put("splitter", "_");
		
		System.out.println(parse(params1 -> "that's" + params1.get("splitter") + params1.get("from"), params));
	}
}
