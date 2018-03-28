package com.yiji.adk.rules.drools.module;

import java.sql.Timestamp;
import java.util.List;

public class DeleteRuleDetail {
	
	private long identity;
	
	private List<Rule> rules;
	
	private String deleteReason;
	
	private String opertorIP;
	
	private String opertorID;
	
	private Timestamp rawAddTime;
	
	private Timestamp rawUpdateTime;
	
	public List<Rule> getRules() {
		
		return rules;
	}
	
	public void setRules(List<Rule> rules) {
		
		this.rules = rules;
	}
	
	public String getDeleteReason() {
		
		return deleteReason;
	}
	
	public void setDeleteReason(String deleteReason) {
		
		this.deleteReason = deleteReason;
	}
	
	public String getOpertorIP() {
		
		return opertorIP;
	}
	
	public void setOpertorIP(String opertorIP) {
		
		this.opertorIP = opertorIP;
	}
	
	public String getOpertorID() {
		
		return opertorID;
	}
	
	public void setOpertorID(String opertorID) {
		
		this.opertorID = opertorID;
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
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("DeleteRuleDetail [identity=");
		builder.append(identity);
		builder.append(", rules=");
		builder.append(rules);
		builder.append(", deleteReason=");
		builder.append(deleteReason);
		builder.append(", opertorIP=");
		builder.append(opertorIP);
		builder.append(", opertorID=");
		builder.append(opertorID);
		builder.append(", rawAddTime=");
		builder.append(rawAddTime);
		builder.append(", rawUpdateTime=");
		builder.append(rawUpdateTime);
		builder.append("]");
		return builder.toString();
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
		DeleteRuleDetail other = (DeleteRuleDetail) obj;
		if (identity != other.identity)
			return false;
		return true;
	}
	
}
