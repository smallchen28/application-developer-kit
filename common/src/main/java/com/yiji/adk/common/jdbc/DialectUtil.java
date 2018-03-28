/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-13 18:16 创建
 *
 */
package com.yiji.adk.common.jdbc;

import com.yiji.boot.core.Apps;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class DialectUtil {
	
	public static String dialectByDSUrl() {
		String url = Apps.getEnvironment().getProperty("yiji.ds.url");
		if (null == url) {
			return null;
		}
		
		if (url.contains("mysql")) {
			return "mysql";
		}
		if (url.contains("oracle")) {
			return "oracle";
		}
		
		return null;
	}
}
