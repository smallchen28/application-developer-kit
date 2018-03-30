package com.global.adk.plan.task.statement;

import java.util.Map;

public class ExecutorResult {

	private ExecutorStatus executorStatus;

	private String execInfo;

	private Map<String,String> exeContext;

	public void setExeContext(Map<String, String> exeContext){
		this.exeContext = exeContext;
	}

	public Map<String,String> getExeContext(){
		return exeContext;
	}

	public ExecutorStatus getExecutorStatus() {
		
		return executorStatus;
	}
	
	public void setExecutorStatus(ExecutorStatus executorStatus) {
		
		this.executorStatus = executorStatus;
	}
	
	public String getExecInfo() {
		
		return execInfo;
	}
	
	public void setExecInfo(String execInfo) {
		
		this.execInfo = execInfo;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ExecutorResult{");
		sb.append("executorStatus=").append(executorStatus);
		sb.append(", execInfo='").append(execInfo).append('\'');
		sb.append(", exeContext=").append(exeContext);
		sb.append('}');
		return sb.toString();
	}
}
