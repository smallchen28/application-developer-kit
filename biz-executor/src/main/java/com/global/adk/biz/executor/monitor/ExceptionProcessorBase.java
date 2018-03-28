package com.yiji.adk.biz.executor.monitor;

import com.yiji.adk.biz.executor.ServiceContext;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class ExceptionProcessorBase implements ExceptionProcessor {
	
	private Logger logger;
	
	private ExceptionProcessorConfig config;
	
	private boolean isPrintStackTrace = true;
	
	private boolean isCompress = false;
	
	private String systemName;
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public Logger logger() {
		return logger;
	}
	
	public void setPrintStackTrace(boolean isPrintStackTrace) {
		this.isPrintStackTrace = isPrintStackTrace;
	}
	
	public void setCompress(boolean isCompress) {
		this.isCompress = isCompress;
	}
	
	protected void compressStackTrace(Throwable throwable) {
		
		if (logger != null && logger.isErrorEnabled() && isPrintStackTrace) {
			
			if (isCompress) {
				StringBuilder sb = new StringBuilder();
				
				StringWriter writer = new StringWriter();
				
				throwable.printStackTrace(new PrintWriter(writer));
				
				String message = writer.toString();
				
				//- 逐行读取
				int counter = 0;
				int index = 0;
				while ((index = message.indexOf(System.getProperty("line.separator"))) > 0) {
					String tmp = message.substring(0, index);
					message = message.substring(index + 1);
					if (tmp.startsWith("\tat")) {
						if (++counter < 3) {
							sb.append(tmp).append("\n");
						}
					} else {
						counter = 0;
						if (tmp.equals("\t... 5 more")) {
							sb.append("\t…… more\n");
						} else {
							sb.append(tmp).append("\n");
						}
					}
				}
				if (counter >= 3) {
					sb.append("\t…… more\n");
				}
				if (logger.isErrorEnabled()) {
					logger.error("压缩错误堆栈\n{}", sb.toString());
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error("错误堆栈\n", throwable);
				}
			}
			
		}
		
	}
	
	@Override
	public void init(ExceptionProcessorConfig config) {
		
		this.config = config;
	}
	
	public ExceptionProcessorConfig getConfig() {
		
		return config;
	}
	
	@Override
	public boolean proceed(Throwable throwable, ServiceContext<?, ?> serviceContext, ExceptionProcessorChain chain) {
		
		if (config.eval(throwable)) {
			try {
				doProceed(throwable, serviceContext, chain);
				return true;
			} finally {
				compressStackTrace(throwable);
			}
		}
		return chain.run(throwable, serviceContext);
	}
	
	public String getSystemName() {
		return systemName;
	}
	
	@Override
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	@Override
	public String systemName() {
		return getSystemName();
	}
	
	public abstract void doProceed(	Throwable throwable, ServiceContext<?, ?> serviceContext,
									ExceptionProcessorChain chain);
}
