package com.global.adk.api.order;

import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterRulePolicyOrder extends OrderBase {
	
	private static final long serialVersionUID = -5828315376555132156L;
	
	@Size(min = 1)
	@Valid
	private ArrayList<RelatedRuleAttribute> relatedRuleAttributes = new ArrayList<>();
	
	@NotNull
	@Size(min=1 , max = 256)
	private String description;

	@NotNull
	@Size(min=1 , max = 128)
	private String policyFrom;

	@NotNull
	@Size(min=1 , max = 128)
	private String riskType;
	
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

	public ArrayList<RelatedRuleAttribute> getRelatedRuleAttributes() {
		
		return relatedRuleAttributes;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterRulePolicyOrder [relatedRuleAttributes=");
		builder.append(relatedRuleAttributes);
		builder.append(", description=");
		builder.append(description);
		builder.append(", riskType=");
		builder.append(riskType);
		builder.append(", policyFrom=");
		builder.append(policyFrom);
		builder.append("]");
		return builder.toString();
	}
	
}
