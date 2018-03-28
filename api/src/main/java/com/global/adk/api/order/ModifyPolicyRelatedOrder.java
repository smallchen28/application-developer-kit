/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.api.order;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-11 下午6:11<br>
 * @see
 * @since 1.0.0
 */
public class ModifyPolicyRelatedOrder extends OrderBase{
	
	private static final long serialVersionUID = -4600008346361878121L;
	
	@Min(value = 1)
	private long policyIdentity;

	@Size(min = 1 , max = 256)
	private String description;

	@NotNull
	@Size(min=1,max=128)
	private String policyFrom;

	@NotNull
	@Size(min=1,max=128)
	private String riskType;
	
	private boolean plicyEnable = true;
	
	@Size(min = 1)
	private ArrayList<PolicyRelatedElement> elements = new ArrayList<>();
	
	public enum PolicyOptype {
		valid,
		invalid,
		delete,
		add,
		update
	}
	
	public static class PolicyRelatedElement implements Serializable {
		
		private static final long serialVersionUID = 4320428984200861589L;
		
		private RelatedRuleAttribute relatedRuleAttribute;
		
		private PolicyOptype policyOptype = PolicyOptype.invalid;
		
		public RelatedRuleAttribute getRelatedRuleAttribute() {
			return relatedRuleAttribute;
		}
		
		public void setRelatedRuleAttribute(RelatedRuleAttribute relatedRuleAttribute) {
			this.relatedRuleAttribute = relatedRuleAttribute;
		}
		
		public PolicyOptype getPolicyOptype() {
			return policyOptype;
		}
		
		public void setPolicyOptype(PolicyOptype policyOptype) {
			this.policyOptype = policyOptype;
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("PolicyRelatedElement{");
			sb.append("relatedRuleAttribute=").append(relatedRuleAttribute);
			sb.append(", policyOptype=").append(policyOptype);
			sb.append('}');
			return sb.toString();
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			
			PolicyRelatedElement that = (PolicyRelatedElement) o;
			
			if (relatedRuleAttribute.getRuleIdentity() != that.relatedRuleAttribute.getRuleIdentity()) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public int hashCode() {
			return (int) (relatedRuleAttribute.getRuleIdentity() ^ (relatedRuleAttribute.getRuleIdentity() >>> 32));
		}
	}
	
	public long getPolicyIdentity() {
		return policyIdentity;
	}
	
	public void setPolicyIdentity(long policyIdentity) {
		this.policyIdentity = policyIdentity;
	}
	
	public boolean isPlicyEnable() {
		return plicyEnable;
	}
	
	public void setPlicyEnable(boolean plicyEnable) {
		this.plicyEnable = plicyEnable;
	}
	
	public ArrayList<PolicyRelatedElement> getElements() {
		return elements;
	}
	
	public void setElements(ArrayList<PolicyRelatedElement> elements) {
		this.elements = elements;
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
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
