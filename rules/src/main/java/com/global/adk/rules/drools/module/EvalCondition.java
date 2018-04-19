package com.global.adk.rules.drools.module;

import com.global.adk.rules.drools.DynamicConditionExecutor;
import com.global.common.util.ToString;

import java.util.Map;

public class EvalCondition extends Condition {

	private int scriptIdentity;
	
	private String conditionScript;
	
	private Symbol symbol;
	
	private String requestName;
	
	private String compareValue;

	private String executorName;

	private String reserved1;

	private String reserved2;
	
	@Override
	public void refresh(Map<String, DynamicConditionExecutor> executors) {
		executors.get(executorName).generateExecutorWrapper(this);
	}
	
	@Override
	public String toData() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("eval(").append(executorName).append(".execute( ").append(getIdentity()).append(",")
			.append(compareValue).append(",\"").append(symbol.getCode()).append("\",").append(requestName).append("))");
		
		return sb.toString();
	}
	
	public int getScriptIdentity() {
		return scriptIdentity;
	}
	
	public void setScriptIdentity(int scriptIdentity) {
		this.scriptIdentity = scriptIdentity;
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

	public boolean eq(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		EvalCondition that = (EvalCondition) o;
		if (getIdentity() != that.getIdentity()) return false;
		if (getRuleIdentity() != that.getRuleIdentity()) return false;
		if (compareValue != null ? !compareValue.equals(that.compareValue) : that.compareValue != null) return false;
		if (conditionScript != null ? !conditionScript.equals(that.conditionScript) : that.conditionScript != null)
			return false;
		if (executorName != null ? !executorName.equals(that.executorName) : that.executorName != null) return false;
		if (requestName != null ? !requestName.equals(that.requestName) : that.requestName != null) return false;
		if (symbol != that.symbol) return false;

		return true;
	}

}
