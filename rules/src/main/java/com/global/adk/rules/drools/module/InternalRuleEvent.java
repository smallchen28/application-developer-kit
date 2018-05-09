package com.global.adk.rules.drools.module;

import com.yjf.common.lang.security.MD5Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalRuleEvent {
	
	private long identity;
	
	private String eventName;
	
	private long version = 1L;
	
	private List<RulePolicy> policy = new ArrayList<>();
	
	private String description;
	
	private Map<String, String> eventContext = new HashMap<String, String>();
	
	private boolean enable = true;
	
	private Timestamp rawAddTime;
	
	private Timestamp rawUpdateTime;
	
	public void addAttribute(String key, String value) {
		
		this.eventContext.put(key, value);
	}
	
	public void removeAttribute(String key) {
		
		this.eventContext.remove(key);
	}
	
	public long getVersion() {
		
		return version;
	}
	
	public void setVersion(long version) {
		
		this.version = version;
	}
	
	public String getEventName() {
		
		return eventName;
	}
	
	public void setEventName(String eventName) {
		
		this.eventName = eventName;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public List<RulePolicy> getPolicy() {
		
		return policy;
	}
	
	public void setPolicy(List<RulePolicy> policy) {
		
		this.policy = policy;
	}
	
	public Timestamp getRawAddTime() {
		
		return rawAddTime;
	}
	
	public void setRawAddTime(Timestamp rawAddTime) {
		
		this.rawAddTime = rawAddTime;
	}
	
	public Timestamp getRawUpdateTime() {
		
		return rawUpdateTime;
	}
	
	public void setRawUpdateTime(Timestamp rawUpdateTime) {
		
		this.rawUpdateTime = rawUpdateTime;
	}
	
	public long getIdentity() {
		
		return identity;
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("InternalRuleEvent [identity=");
		builder.append(identity);
		builder.append(", eventName=");
		builder.append(eventName);
		builder.append(", version=");
		builder.append(version);
		builder.append(", policy=");
		builder.append(policy);
		builder.append(", description=");
		builder.append(description);
		builder.append(", eventContext=");
		builder.append(eventContext);
		builder.append(", enable=");
		builder.append(enable);
		builder.append(", rawAddTime=");
		builder.append(rawAddTime);
		builder.append(", rawUpdateTime=");
		builder.append(rawUpdateTime);
		builder.append("]");
		return builder.toString();
	}
	
	public Map<String, String> getEventContext() {
		
		return eventContext;
	}
	
	public boolean isEnable() {
		
		return enable;
	}
	
	public void setEnable(boolean enable) {
		
		this.enable = enable;
	}
	
	public void setEventContext(Map<String, String> eventContext) {
		
		this.eventContext = eventContext;
	}
	
	public String toDRLPackage(){
		StringBuilder pack = new StringBuilder("com.global.adk.rules.drools.generate");
		pack.append(MD5Util.encodeByMD5(eventName));
		return pack.toString();
	}

}
