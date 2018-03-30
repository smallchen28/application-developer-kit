package com.global.adk.plan.task.statement;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ActionContext {
	
	private String taskName;
	
	private Timestamp startTime;
	
	private long elapsed;
	
	private int exeCount;
	
	private Map<String, String> exeContext = new HashMap<>();
	
	public void addAttribute(String key, String value) {
		
		this.exeContext.put(key, value);
	}
	
	public boolean removeAttribute(String key) {
		
		return exeContext.remove(key) != null;
	}
	
	public String getTaskName() {
		
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		
		this.taskName = taskName;
	}
	
	public Timestamp getStartTime() {
		
		return startTime;
	}
	
	public void setStartTime(Timestamp startTime) {
		
		this.startTime = startTime;
	}
	
	public long getElapsed() {
		
		return elapsed;
	}
	
	public void setElapsed(long elapsed) {
		
		this.elapsed = elapsed;
	}

	public Map<String, String> getExeContext() {
		return exeContext;
	}

	public void setExeContext(Map<String, String> exeContext) {
		this.exeContext = exeContext;
	}

	@Override
	public String toString() {
		
		return "ActionContext [taskName=" + taskName + ", startTime=" + startTime + ", elapsed=" + elapsed
				+ ", exeContext=" + exeContext + "]";
	}
	
	public int getExeCount() {
		
		return exeCount;
	}
	
	public void setExeCount(int exeCount) {
		
		this.exeCount = exeCount;
	}
}
