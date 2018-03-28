package com.yiji.adk.rules.drools;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasulee on 15-1-12.
 */
public class CurrentRelatedRuleInfo {
	
	private long identity;

	private long policyIdentity;

	private String ruleName;
	
	private String description;
	
	private Map<String, String> relatedContext = new HashMap<>();
	
	public CurrentRelatedRuleInfo(String policyIdentity ,String identity, String ruleName, String description, String relatedContext) {
		this.identity = Long.parseLong(identity);

		this.policyIdentity = Long.parseLong(policyIdentity);

		this.ruleName = ruleName;
		
		this.description = description;
		
		this.relatedContext = (Map) JSON.parseObject(relatedContext);
	}

	public long getPolicyIdentity(){
		return policyIdentity;
	}

	public long getIdentity() {
		return identity;
	}
	
	public String getRuleName() {
		return ruleName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Map<String, String> getRelatedContext() {
		return relatedContext;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CurrentRelatedRuleInfo{");
		sb.append("identity=").append(identity);
		sb.append("policyIdentity=").append(policyIdentity);
		sb.append(", ruleName='").append(ruleName).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", relatedContext=").append(relatedContext);
		sb.append('}');
		return sb.toString();
	}
}
