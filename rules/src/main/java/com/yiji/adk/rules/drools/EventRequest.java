package com.yiji.adk.rules.drools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventRequest implements Serializable {
	
	private static final long serialVersionUID = 2032912328931797952L;
	
	private String eventName;
	
	private String policyFrom;

	private Map<String, String> eventContext = new HashMap<>();

	public EventRequest() {
		
	}
	
	public EventRequest(String eventName) {
		
		this.eventName = eventName;
	}
	
	public void addAttribute(String key, String value) {
		
		eventContext.put(key, value);
	}
	
	public void removeAttribute(String key) {
		
		eventContext.remove(key);
	}
	
	public String getEventName() {
		
		return eventName;
	}
	
	public void setEventName(String eventName) {
		
		this.eventName = eventName;
	}

	public String getPolicyFrom() {
		return policyFrom;
	}

	public void setPolicyFrom(String policyFrom) {
		this.policyFrom = policyFrom;
	}

	public Map<String, String> getEventContext() {
		
		return eventContext;
	}
	
	public void setEventContext(Map<String, String> eventContext) {
		
		this.eventContext = eventContext;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("EventRequest [eventName=");
		builder.append(eventName);
		builder.append(", policyFrom=");
		builder.append(policyFrom);
		builder.append(", eventContext=");
		builder.append(eventContext);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EventRequest request = (EventRequest) o;
		return Objects.equals(eventName, request.eventName) &&
				Objects.equals(policyFrom, request.policyFrom) &&
				Objects.equals(eventContext, request.eventContext);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventName, policyFrom, eventContext);
	}
}
