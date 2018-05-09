/**
 *
 *                             _ooOoo_
 *                            o8888888o
 *                            88"   .  "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.biz.executor.monitor;

import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.common.exception.InitializerException;
import com.global.adk.common.exception.KitNestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.List;

/**
 * 标准异常监控实现
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * 
 * @history hasuelee创建于2014年9月25日 下午12:33:47<br>
 */
public class StandardExceptionMonitor implements ExceptionMonitor, InitializingBean {
	
	private final Logger logger;
	
	private String systemName;
	
	private final ExceptionProcessorChain exceptionProcessorChain;
	
	private List<ExceptionProcessorConfig> configs;
	
	public StandardExceptionMonitor(String logName, String systemName) {
		
		if (logName == null || logName.equals("")) {
			throw new InitializerException("logName不能为空.");
		}
		if (systemName == null || systemName.equals("")) {
			throw new InitializerException("systemName不能为空");
		}
		
		this.logger = LoggerFactory.getLogger(logName);
		this.systemName = systemName;
		this.exceptionProcessorChain = new ExceptionProcessorChain();
	}
	
	public String getSystemName() {
		return systemName;
	}
	
	public void setConfigs(List<ExceptionProcessorConfig> configs) {
		this.configs = configs;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//- 无自定义异常情况下，使用系统默认处理器。
		if (configs != null && configs.size() > 0) {
			Collections.sort(configs);
			for (ExceptionProcessorConfig config : configs) {
				ExceptionProcessor processor = config.getProcessor();
				if (processor.logger() == null) {
					processor.setLogger(this.logger);
				}
				if (processor.systemName() == null || processor.systemName().equals("")) {
					processor.setSystemName(this.systemName);
				}
				exceptionProcessorChain.add(config);
			}
		} else {
			//- 1. KitNestException 业务内部异常
			String mvelScript = "throwable instanceof com.global.adk.common.exception.KitNestException";
			ExceptionProcessor processor = new KitNestExceptionProcessor();
			processor.setLogger(logger);
			processor.setSystemName(systemName);
			
			ExceptionProcessorConfig config = new ExceptionProcessorConfig(1, mvelScript, processor);
			
			//- 2. Exception 未知异常
			String mvelScript2 = "1==1";
			ExceptionProcessor processor2 = new UnkownExceptionProcessor();
			processor2.setLogger(logger);
			processor2.setSystemName(systemName);
			
			ExceptionProcessorConfig config2 = new ExceptionProcessorConfig(2, mvelScript2, processor2);
			
			config.afterPropertiesSet();
			config2.afterPropertiesSet();
			exceptionProcessorChain.add(config);
			exceptionProcessorChain.add(config2);
		}
		
	}
	
	@Override
	public void catcher(Throwable e, ServiceContext<?, ?> serviceContext) {
		
		if (e instanceof Error) {
			logger.error("error错误{}", e.getMessage());
			throw (Error) e;
		}
		
		if (serviceContext == null) {
			logger.error("系统bug有点深厚，异常监听器中serviceContext为空。", e);
			return;
		}


		try {
			if (!((ExceptionProcessorChain) exceptionProcessorChain.clone()).run(e, serviceContext)) {
				logger.error("异常处理失败，没有对应的处理器对应该异常,ExceptionProcessorChain={}",exceptionProcessorChain.toString(),e);
			}
		} catch (Throwable e1) {
			logger.error("处理异常出错，忽略异常处理", e1);
			logger.error("######原始异常######", e);
		}
	}
	
}
