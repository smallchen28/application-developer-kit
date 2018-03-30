package com.global.adk.rules.drools.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Objects;

public class Description {
	
	private EventRuleType eventRuleType;
	
	private String eventInfo;
	
	private Set<RelatedRule> rules;
	
	public enum EventRuleType {
		STANDARD,
		DEFAULT
	}
	
	public Description(String eventInfo, EventRuleType eventRuleType) {
		
		this.eventInfo = eventInfo;
		this.eventRuleType = eventRuleType;
	}
	
	public String toMessageStream() {
		
		List<RelatedRule> ruleArrayList = new ArrayList<>();
		
		if (rules != null && rules.size() > 0) {
			ruleArrayList.addAll(rules);
		}
		Collections.sort(ruleArrayList);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("事件=>[").append(eventInfo).append("]{");
		
		for (int i = 0, j = ruleArrayList.size(); i < j; i++) {
			RelatedRule rule = ruleArrayList.get(i);
			sb.append(i+1).append(".规则描述：").append(rule.getDescription()).append("；");
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
	public Set<RelatedRule> getRules() {
		
		return rules;
	}
	
	public void setRules(Set<RelatedRule> rules) {
		
		this.rules = rules;
	}
	
	public EventRuleType getEventRuleType() {
		
		return eventRuleType;
	}
	
	public void setEventRuleType(EventRuleType eventRuleType) {
		
		this.eventRuleType = eventRuleType;
	}

	public String getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Description)) return false;
		Description that = (Description) o;
		return eventRuleType == that.eventRuleType &&
				Objects.equals(eventInfo, that.eventInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventRuleType, eventInfo);
	}
}
