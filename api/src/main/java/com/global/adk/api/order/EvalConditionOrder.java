package com.global.adk.api.order;

import com.global.adk.api.enums.Symbol;
import com.global.common.util.ToString;

public class EvalConditionOrder extends ConditionOrder {
	
	private static final long serialVersionUID = -9217342965230340398L;
	
	private long identity;
	
	private int scriptIdentity;
	
	private String conditionScript;
	
	private Symbol symbol;
	
	private String requestName;
	
	private String compareValue;
	
	private String executorName;
	
	private String reserved1;
	
	private String reserved2;
	
	public int getScriptIdentity() {
		return scriptIdentity;
	}
	
	public void setScriptIdentity(int scriptIdentity) {
		this.scriptIdentity = scriptIdentity;
	}
	
	public long getIdentity() {
		return identity;
	}
	
	public void setIdentity(long identity) {
		this.identity = identity;
	}
	
	public String getConditionScript() {
		
		return conditionScript;
	}
	
	public void setConditionScript(String conditionScript) {
		
		this.conditionScript = conditionScript;
	}
	
	public Symbol getSymbol() {
		
		return symbol;
	}
	
	public void setSymbol(Symbol symbol) {
		
		this.symbol = symbol;
	}
	
	public String getRequestName() {
		
		return requestName;
	}
	
	public void setRequestName(String requestName) {
		
		this.requestName = requestName;
	}
	
	public String getCompareValue() {
		
		return compareValue;
	}
	
	public void setCompareValue(String compareValue) {
		
		this.compareValue = compareValue;
	}
	
	public String getExecutorName() {
		
		return executorName;
	}
	
	public void setExecutorName(String executorName) {
		
		this.executorName = executorName;
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
	
}
