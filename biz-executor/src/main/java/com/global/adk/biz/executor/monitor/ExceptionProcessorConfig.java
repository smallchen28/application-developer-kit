package com.yiji.adk.biz.executor.monitor;

import com.yiji.adk.common.exception.ExecutorException;
import org.mvel2.MVEL;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

public class ExceptionProcessorConfig implements Comparable<ExceptionProcessorConfig>, InitializingBean {
	
	private int ordered;
	
	private String mvelScript;
	
	private ExceptionProcessor processor;
	
	public ExceptionProcessorConfig() {
		
	}
	
	public ExceptionProcessorConfig(int ordered, String mvelScript, ExceptionProcessor processor) {
		this.ordered = ordered;
		this.mvelScript = mvelScript;
		this.processor = processor;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if (mvelScript == null || mvelScript.equals("")) {
			throw new ExecutorException(String.format("异常处理器{}配置出现错误，mvelScript不能为空", getClass().getName()));
		}
		
		if (processor == null) {
			throw new ExecutorException(String.format("异常处理器{}配置出现错误，exceptionProcessor不能为空", getClass().getName()));
		}
		
		processor.init(this);
	}
	
	public String getMvelScript() {
		return mvelScript;
	}
	
	public void setMvelScript(String mvelScript) {
		
		this.mvelScript = mvelScript;
	}
	
	public void setProcessor(ExceptionProcessor processor) {
		
		this.processor = processor;
	}
	
	@Override
	public int compareTo(ExceptionProcessorConfig o) {
		return ordered == o.ordered ? 0 : ordered > o.ordered ? 1 : -1;
	}
	
	public int getOrdered() {
		return ordered;
	}
	
	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}
	
	protected boolean eval(Throwable throwable) {
		
		Map<String, Object> variables = new HashMap<>();
		
		variables.put("throwable", throwable);
		variables.put("processor", processor);
		
		return (boolean) MVEL.eval(mvelScript, variables);
		
	}
	
	protected ExceptionProcessor getProcessor() {
		
		return processor;
	}
}
