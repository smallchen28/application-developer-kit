package com.global.adk.api.order;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UnregisterRuleOrder extends OrderBase {
	
	private static final long serialVersionUID = 2878032330658845509L;
	
	@NotNull
	@Size(min = 1)
	private ArrayList<Long> ruleIds = new ArrayList<>();
	
	@NotNull
	@Size(min=1 , max=512)
	private String deleteReason;
	
	@NotNull
	@Size(min=1 , max = 30)
	private String opertorIP;
	
	@NotNull
	@Size(min=1 , max = 30)
	private String opertorID;
	
	public ArrayList<Long> getRuleIds() {
		
		return ruleIds;
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
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("DeleteRuleOrder [ruleIds=");
		builder.append(ruleIds);
		builder.append(", deleteReason=");
		builder.append(deleteReason);
		builder.append(", opertorIP=");
		builder.append(opertorIP);
		builder.append(", opertorID=");
		builder.append(opertorID);
		builder.append("]");
		return builder.toString();
	}
	
}
