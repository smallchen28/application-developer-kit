package com.global.adk.api.order;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

public class RelatedRuleAttribute implements Serializable {
	
	private static final long serialVersionUID = -2260666571048551676L;
	
	@Min(value = 1)
	private long ruleIdentity;

	private Timestamp effectiveTime;

	private Timestamp expireTime;
	
	private boolean enable = true;
	
	private boolean async;
	
	private boolean loop;
	
	private int salicence;

	@NotNull
	@Size(min=0,max = 256)
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	private HashMap<String, String> relatedContext = new HashMap<>();
	
	public RelatedRuleAttribute(long ruleIdentity) {
		
		this.ruleIdentity = ruleIdentity;
	}
	
	public void addContextAttribute(String key, String value) {
		relatedContext.put(key, value);
	}
	
	public void removeContextAttribute(String key) {
		relatedContext.remove(key);
	}
	
	public HashMap<String, String> getRelatedContext() {
		return relatedContext;
	}
	
	public long getRuleIdentity() {
		return ruleIdentity;
	}
	
	public void setRuleIdentity(long ruleIdentity) {
		
		this.ruleIdentity = ruleIdentity;
	}
	
	public Timestamp getEffectiveTime() {
		
		return effectiveTime;
	}
	
	public void setEffectiveTime(Timestamp effectiveTime) {
		
		this.effectiveTime = effectiveTime;
	}
	
	public Timestamp getExpireTime() {
		
		return expireTime;
	}
	
	public void setExpireTime(Timestamp expireTime) {
		
		this.expireTime = expireTime;
	}
	
	public boolean isEnable() {
		
		return enable;
	}
	
	public void setEnable(boolean enable) {
		
		this.enable = enable;
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
	
	public int getSalicence() {
		
		return salicence;
	}
	
	public void setSalicence(int salicence) {
		
		this.salicence = salicence;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RelatedRuleAttribute{");
		sb.append("ruleIdentity=").append(ruleIdentity);
		sb.append(", effectiveTime=").append(effectiveTime);
		sb.append(", expireTime=").append(expireTime);
		sb.append(", enable=").append(enable);
		sb.append(", async=").append(async);
		sb.append(", loop=").append(loop);
		sb.append(", salicence=").append(salicence);
		sb.append(", relatedContext=").append(relatedContext);
		sb.append('}');
		return sb.toString();
	}
}
