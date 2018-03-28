package com.yiji.adk.rules.drools;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Observable;

/**
 * @Desecription 脚本抓取信息
 */
public class EvalCaptrueInfo extends Observable implements Serializable {
	
	private static final long serialVersionUID = 6582190193306650110L;
	
	/** 脚本ID */
	private long evalId;
	
	/** 左值（且类型） */
	private String leftValue;
	
	/** 右值（且类型） */
	private String rightValue;
	
	/** 比较值 */
	private String symbol;
	
	/** 比较返回结果 */
	private String result;
	
	/** 脚本执行时间 */
	private int executorTime;
	
	/** 入参 */
	private Object[] parameter;
	
	public long getEvalId() {
		return evalId;
	}
	
	public void setEvalId(long evalId) {
		this.evalId = evalId;
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
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public int getExecutorTime() {
		return executorTime;
	}
	
	public void setExecutorTime(int executorTime) {
		this.executorTime = executorTime;
	}
	
	public Object[] getParameter() {
		return parameter;
	}
	
	public void setParameter(Object[] parameter) {
		this.parameter = parameter;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("EvalCaptrueInfo{");
		sb.append("evalId=").append(evalId);
		sb.append(", leftValue='").append(leftValue).append('\'');
		sb.append(", rightValue='").append(rightValue).append('\'');
		sb.append(", symbol='").append(symbol).append('\'');
		sb.append(", result='").append(result).append('\'');
		sb.append(", executorTime=").append(executorTime);
		sb.append(", parameter=").append(Arrays.toString(parameter));
		sb.append('}');
		return sb.toString();
	}
}