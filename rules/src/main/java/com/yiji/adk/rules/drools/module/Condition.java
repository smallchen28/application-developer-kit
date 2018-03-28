package com.yiji.adk.rules.drools.module;

import com.yiji.adk.rules.drools.DynamicConditionExecutor;

import java.util.Map;

public abstract class Condition implements Comparable<Condition> {
	
	private long ruleIdentity;
	
	private long identity;
	
	protected abstract String toData();
	
	public abstract void refresh(Map<String, DynamicConditionExecutor> executors);
	
	protected long getRuleIdentity() {
		
		return ruleIdentity;
	}
	
	protected void setRuleIdentity(long ruleIdentity) {
		
		this.ruleIdentity = ruleIdentity;
	}
	
	@Override
	public int compareTo(Condition o) {
		
		return o.getClass() == ObjectCondition.class ? 1 : -1;
	}
	
	public long getIdentity() {
		
		return identity;
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
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
		Condition other = (Condition) obj;
		if (identity != other.identity)
			return false;
		return true;
	}
	
}
