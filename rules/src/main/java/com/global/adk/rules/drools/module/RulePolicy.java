package com.global.adk.rules.drools.module;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RulePolicy {
	
	private long identity;
	
	private long relatedEvent;
	
	private String policyName;
	
	private String description;

	private String policyFrom;

	private String riskType;
	
	private Timestamp rawAddTime;
	
	private Timestamp rawUpdateTime;
	
	private List<RelatedRule> relatedRules = new ArrayList<>();
	
	private boolean enable = true;
	
	public long getIdentity() {
		
		return identity;
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
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
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}

	public String getPolicyFrom() {
		return policyFrom;
	}

	public void setPolicyFrom(String policyFrom) {
		this.policyFrom = policyFrom;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (identity ^ (identity >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RulePolicy other = (RulePolicy) obj;
		if (identity != other.identity)
			return false;
		return true;
	}
	
	public List<RelatedRule> getRelatedRules() {
		
		return relatedRules;
	}
	
	public void setRelatedRules(List<RelatedRule> rules) {
		
		this.relatedRules = rules;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("RulePolicy [identity=");
		builder.append(identity);
		builder.append(", relatedEvent=");
		builder.append(relatedEvent);
		builder.append(", policyName=");
		builder.append(policyName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", rawAddTime=");
		builder.append(rawAddTime);
		builder.append(", rawUpdateTime=");
		builder.append(rawUpdateTime);
		builder.append(", relatedRules=");
		builder.append(relatedRules);
		builder.append(", enable=");
		builder.append(enable);
		builder.append("]");
		return builder.toString();
	}
	
	public long getRelatedEvent() {
		
		return relatedEvent;
	}
	
	public void setRelatedEvent(long relatedEvent) {
		
		this.relatedEvent = relatedEvent;
	}
	
	public String getPolicyName() {
		
		return policyName;
	}
	
	public void setPolicyName(String policyName) {
		
		this.policyName = policyName;
	}
	
	public boolean isEnable() {
		
		return enable;
	}
	
	public void setEnable(boolean enable) {
		
		this.enable = enable;
	}
	
}
