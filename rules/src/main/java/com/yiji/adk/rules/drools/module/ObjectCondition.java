package com.yiji.adk.rules.drools.module;

import com.yiji.adk.rules.drools.DynamicConditionExecutor;
import com.yjf.common.util.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectCondition extends Condition {
	
	private String variableName;
	
	private String typeSimpleName;
	
	private List<CompareElement> compareElements = new ArrayList<>();
	
	@Override
	public void refresh(Map<String, DynamicConditionExecutor> executors) {
		//nothing ...
	}
	
	@Override
	public String toData() {
		
		StringBuilder sb = new StringBuilder();
		
		if (variableName != null && !variableName.equals("")) {
			sb.append("$").append(variableName).append(" : ");
		}
		
		sb.append(typeSimpleName.substring(typeSimpleName.lastIndexOf('.') + 1)).append("(");
		for (CompareElement element : compareElements) {
			String joinSymbol = element.getJoinSymbol();
			if (joinSymbol == null) {
				joinSymbol = "";
			}
			
			sb.append(joinSymbol).append(element.getLeftValue()).append(element.getSymbol())
				.append(element.getRightValue());
		}
		sb.append(")\n");
		
		return sb.toString();
	}
	
	public static class CompareElement {
		
		private String joinSymbol;
		
		private String symbol;
		
		private String leftValue;
		
		private String rightValue;
		
		private String reserved1;
		
		private String reserved2;
		
		public String getSymbol() {
			
			return symbol;
		}
		
		public void setSymbol(String symbol) {
			
			this.symbol = symbol;
		}
		
		public String getLeftValue() {
			
			return leftValue;
		}
		
		public void setLeftValue(String leftValue) {
			
			this.leftValue = leftValue;
		}
		
		public String getRightValue() {
			
			return rightValue;
		}
		
		public void setRightValue(String rightValue) {
			
			this.rightValue = rightValue;
		}
		
		public String getReserved1() {
			return reserved1;
		}
		
		public void setReserved1(String reserved1) {
			this.reserved1 = reserved1;
		}
		
		public String getReserved2() {
			return reserved2;
		}
		
		public void setReserved2(String reserved2) {
			this.reserved2 = reserved2;
		}
		
		@Override
		public String toString() {
			
			return ToString.toString(this);
		}
		
		public String getJoinSymbol() {
			
			return joinSymbol;
		}
		
		public void setJoinSymbol(String joinSymbol) {
			
			this.joinSymbol = joinSymbol;
		}
	}
	
	public String getVariableName() {
		
		return variableName;
	}
	
	public void setVariableName(String variableName) {
		
		this.variableName = variableName;
	}
	
	public String getTypeSimpleName() {
		
		return typeSimpleName;
	}
	
	public void setTypeSimpleName(String typeSimpleName) {
		
		this.typeSimpleName = typeSimpleName;
	}
	
	public List<CompareElement> getCompareElements() {
		
		return compareElements;
	}
	
	public void setCompareElements(List<CompareElement> compareElements) {
		
		this.compareElements = compareElements;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("ObjectCondition [variableName=");
		builder.append(variableName);
		builder.append(", typeSimpleName=");
		builder.append(typeSimpleName);
		builder.append(", compareElements=");
		builder.append(compareElements);
		builder.append(", getRuleIdentity()=");
		builder.append(getRuleIdentity());
		builder.append("]");
		return builder.toString();
	}
}
