package com.global.adk.rules.drools.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class RelatedRule implements Comparable<RelatedRule> {
	
	private long identity;

	private long policyIdentity;

	private Timestamp effectiveTime;
	
	private Timestamp expireTime;

	private Timestamp rawAddTime;
	
	private Timestamp rawUpdateTime;
	
	private boolean enable;
	
	private boolean async;
	
	private boolean loop;
	
	private int salicence;
	
	private String ruleName;
	
	private String description;
	
	private String script;
	
	private List<Condition> conditions = new ArrayList<>();
	
	private List<String> imports = new ArrayList<>();
	
	private List<String> globals = new ArrayList<>();
	
	private Map<String, String> relatedContext = new HashMap<>();



	public long getIdentity() {
		
		return identity;
	}
	
	public String relateContextToJson() {
		return JSON.toJSONString(relatedContext, SerializerFeature.UseSingleQuotes);
	}
	
	public Map<String, String> getRelatedContext() {
		return relatedContext;
	}
	
	public void setRelatedContext(Map<String, String> relatedContext) {
		this.relatedContext = relatedContext;
	}


	public void setIdentity(long identity) {
		
		this.identity = identity;
	}

	public long getPolicyIdentity() {
		return policyIdentity;
	}

	public Timestamp getEffectiveTime() {
		
		return effectiveTime;
	}
	
	public String getFormatEffectiveTime() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
		
		return sdf.format(getEffectiveTime());
	}
	
	public void setEffectiveTime(Timestamp effectiveTime) {
		
		this.effectiveTime = effectiveTime;
	}
	
	public Timestamp getExpireTime() {
		
		return expireTime;
	}
	
	public String getFormatExpireTime() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
		
		return sdf.format(getExpireTime());
	}
	
	public void setExpireTime(Timestamp expireTime) {
		
		this.expireTime = expireTime;
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
	
	public boolean isEnable() {
		
		return enable;
	}
	
	public void setEnable(boolean enable) {
		
		this.enable = enable;
	}
	
	public int getSalicence() {
		
		return salicence;
	}
	
	public void setSalicence(int salicence) {
		
		this.salicence = salicence;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public String getScript() {
		
		return script;
	}
	
	public void setScript(String script) {
		
		this.script = script;
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
		RelatedRule other = (RelatedRule) obj;
		if (identity != other.identity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RelatedRule{");
		sb.append("identity=").append(identity);
		sb.append(", policyIdentity=").append(policyIdentity);
		sb.append(", effectiveTime=").append(effectiveTime);
		sb.append(", expireTime=").append(expireTime);
		sb.append(", rawAddTime=").append(rawAddTime);
		sb.append(", rawUpdateTime=").append(rawUpdateTime);
		sb.append(", enable=").append(enable);
		sb.append(", async=").append(async);
		sb.append(", loop=").append(loop);
		sb.append(", salicence=").append(salicence);
		sb.append(", ruleName='").append(ruleName).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", script='").append(script).append('\'');
		sb.append(", conditions=").append(conditions);
		sb.append(", imports=").append(imports);
		sb.append(", globals=").append(globals);
		sb.append(", relatedContext=").append(relatedContext);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public int compareTo(RelatedRule o) {
		
		return this.salicence == o.salicence ? 0 : this.salicence > o.salicence ? 1 : -1;
	}
	
	public List<Condition> getConditions() {
		
		return conditions;
	}
	
	public List<String> getImports() {
		
		return imports;
	}
	
	public List<String> getGlobals() {
		
		return globals;
	}
	
	public String getRuleName() {
		
		return ruleName;
	}
	
	public void setRuleName(String ruleName) {
		
		this.ruleName = ruleName;
	}
	
	public boolean isAsync() {
		
		return async;
	}
	
	public void setAsync(boolean async) {
		
		this.async = async;
	}
	
	public boolean isLoop() {
		
		return loop;
	}
	
	public void setLoop(boolean loop) {
		
		this.loop = loop;
	}
	
	public String toSystemScript() {
		StringBuilder script = new StringBuilder();
		
		script
			.append("com.global.adk.rules.drools.CurrentRelatedRuleInfo  currentRelatedRuleInfo = ")
			.append(
				"new com.global.adk.rules.drools.CurrentRelatedRuleInfo($rule.getIdentity(),$rule.getRuleName(),$rule.getDescription(),com.alibaba.fastjson.JSON.toJSONString($rule.getRelatedContext())); ")
			.append("com.global.adk.rules.drools.CurrentRuleInfoHolder.set(currentRelatedRuleInfo);");
		
		return script.toString();
	}

	public void setPolicyIdentity(long identity) {
		this.policyIdentity = identity;
	}
}
