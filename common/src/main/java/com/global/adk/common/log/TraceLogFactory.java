/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.common.log;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 跟踪日志工厂
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-8-10 下午3:49<br>
 * @see
 * @since 1.0.0
 */
public class TraceLogFactory {
	private static final Map<String, Logger> loggers = new ConcurrentHashMap<>();
	
	public static Logger getLogger(String logName) {

		Logger logger = loggers.get(logName);

		if(logger == null){
			Logger newLogger = LoggerFactory.getLogger(logName) ;

			logger = loggers.putIfAbsent(logName,newLogger);

			if(logger == null){
				logger = newLogger;
			}
		}

		return logger;
	}
}
