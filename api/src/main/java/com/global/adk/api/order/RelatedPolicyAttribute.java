package com.global.adk.api.order;

import javax.validation.constraints.Min;
import java.io.Serializable;

public class RelatedPolicyAttribute implements Serializable {
	
	private static final long serialVersionUID = -1831664226843648283L;
	
	@Min(value = 1)
	private long identity;
	
	private boolean enable = Boolean.TRUE;
	
	public RelatedPolicyAttribute(long identity, boolean enable) {
		
		this.identity = identity;
		this.enable = enable;
	}
	
	public long getIdentity() {
		
		return identity;
	}
	
	public boolean isEnable() {
		
		return enable;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("RelatedPolicyAttribute [identity=");
		builder.append(identity);
		builder.append(", enable=");
		builder.append(enable);
		builder.append("]");
		return builder.toString();
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
	}
	
	public void setEnable(boolean enable) {
		
		this.enable = enable;
	}
	
}