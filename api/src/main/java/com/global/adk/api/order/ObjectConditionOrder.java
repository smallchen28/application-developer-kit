package com.global.adk.api.order;

import com.yjf.common.util.ToString;

import java.io.Serializable;
import java.util.ArrayList;

public class ObjectConditionOrder extends ConditionOrder {
	
	private static final long serialVersionUID = 8152741010412811722L;
	
	private String variableName;
	
	private String typeSimpleName;
	
	private ArrayList<CompareElementOrder> compareElementOrders = new ArrayList<>();
	
	public static class CompareElementOrder implements Serializable {
		
		private static final long serialVersionUID = -4378563527342413678L;
		
		private long identity;
		
		private String joinSymbol;
		
		private String symbol;
		
		private String leftValue;
		
		private String rightValue;
		
		private String reserved1;
		
		private String reserved2;
		
		public long getIdentity() {
			return identity;
		}
		
		public void setIdentity(long identity) {
			this.identity = identity;
		}
		
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
		
		public String getReserved2() {
			return reserved2;
		}
		
		public void setReserved2(String reserved2) {
			this.reserved2 = reserved2;
		}
		
		public String getReserved1() {
			return reserved1;
		}
		
		public void setReserved1(String reserved1) {
			this.reserved1 = reserved1;
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
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("ObjectCondition [variableName=");
		builder.append(variableName);
		builder.append(", typeSimpleName=");
		builder.append(typeSimpleName);
		builder.append(", compareElements=");
		builder.append(compareElementOrders);
		builder.append("]");
		return builder.toString();
	}
	
	public ArrayList<CompareElementOrder> getCompareElementOrders() {
		
		return compareElementOrders;
	}
	
	public void setCompareElementOrders(ArrayList<CompareElementOrder> compareElementOrders) {
		
		this.compareElementOrders = compareElementOrders;
	}
}
