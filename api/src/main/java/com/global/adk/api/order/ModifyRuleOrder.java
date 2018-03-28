/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.api.order;

import com.google.common.collect.Maps;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-11 上午11:57<br>
 * @see
 * @since 1.0.0
 */
public class ModifyRuleOrder extends OrderBase {
	
	private static final long serialVersionUID = 2718608942701600441L;
	
	@Min(value = 1)
	private long ruleIdentity;
	
	@NotNull
	private Map<Long, IdentityOptype> deleteIdentity = Maps.newHashMap();
	
	@Valid
	@NotNull
	private RegisterRuleOrder registerRuleOrder;
	
	public RegisterRuleOrder getRegisterRuleOrder() {
		return registerRuleOrder;
	}
	
	public static enum IdentityOptype {
		objId,
		evalId
	}
	
	public void setRegisterRuleOrder(RegisterRuleOrder registerRuleOrder) {
		this.registerRuleOrder = registerRuleOrder;
	}
	
	public long getRuleIdentity() {
		return ruleIdentity;
	}
	
	public void setRuleIdentity(long ruleIdentity) {
		this.ruleIdentity = ruleIdentity;
	}
	
	public Map<Long, IdentityOptype> getDeleteIdentity() {
		return deleteIdentity;
	}
	
	public void setDeleteIdentity(Map<Long, IdentityOptype> deleteIdentity) {
		this.deleteIdentity = deleteIdentity;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		ModifyRuleOrder that = (ModifyRuleOrder) o;
		
		if (ruleIdentity != that.ruleIdentity) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) (ruleIdentity ^ (ruleIdentity >>> 32));
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ModifyRuleOrder [ruleIdentity=");
		sb.append(ruleIdentity);
		sb.append(", deleteIdentity=");
		sb.append(deleteIdentity);
		sb.append(", registerRuleOrder=");
		sb.append(registerRuleOrder);
		sb.append("]");
		return sb.toString();
	}
}
