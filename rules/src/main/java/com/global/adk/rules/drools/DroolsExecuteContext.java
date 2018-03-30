package com.global.adk.rules.drools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DroolsExecuteContext {
	
	private static final Logger logger = LoggerFactory.getLogger(DroolsExecuteContext.class);
	
	private EventRequest request;
	
	private Object targetObject;
	
	private String description;
	
	private boolean executed;
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private Map<String, Object> attributes = new HashMap<>();
	
	private Exception error;
	
	private long elapse;
	
	public Logger log() {
		
		return logger;
	}
	
	public void addAttribute(String key, Object value) {
		
		this.attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public long getElapse() {
		
		return elapse;
	}
	
	public void setElapse(long elapse) {
		
		this.elapse = elapse;
	}
	
	public boolean isExecuted() {
		
		return executed;
	}
	
	public void setExecuted(boolean executed) {
		
		this.executed = executed;
	}
	
	public Timestamp getStartTime() {
		
		return startTime;
	}
	
	public void setStartTime(Timestamp startTime) {
		
		this.startTime = startTime;
	}
	
	public Timestamp getEndTime() {
		
		return endTime;
	}
	
	public void setEndTime(Timestamp endTime) {
		
		this.endTime = endTime;
	}
	
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}
	
	public Exception getError() {
		
		return error;
	}
	
	public void setError(Exception error) {
		
		this.error = error;
	}
	
	public Object getTargetObject() {
		
		return targetObject;
	}
	
	public void setTargetObject(Object targetObject) {
		
		this.targetObject = targetObject;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("DroolsExecuteContext [request=");
		builder.append(request);
		builder.append(", targetObject=");
		builder.append(targetObject);
		builder.append(", description=");
		builder.append(description);
		builder.append(", executed=");
		builder.append(executed);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append(", error=");
		builder.append(error);
		builder.append(", elapse=");
		builder.append(elapse);
		builder.append("]");
		return builder.toString();
	}
	
	public EventRequest getRequest() {
		
		return request;
	}
	
	public void setRequest(EventRequest request) {
		
		this.request = request;
	}
}
