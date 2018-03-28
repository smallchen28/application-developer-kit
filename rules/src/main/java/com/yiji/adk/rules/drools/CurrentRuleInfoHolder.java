package com.yiji.adk.rules.drools;

public class CurrentRuleInfoHolder {
	
	private static ThreadLocal<CurrentRelatedRuleInfo> currentRelatedRuleInfoThreadLocal = new ThreadLocal<>();
	
	public static void set(CurrentRelatedRuleInfo currentRelatedRuleInfo) {
		currentRelatedRuleInfoThreadLocal.set(currentRelatedRuleInfo);
	}
	
	public static CurrentRelatedRuleInfo get() {
		return currentRelatedRuleInfoThreadLocal.get();
	}
	
	public static void clear() {
		currentRelatedRuleInfoThreadLocal.remove();
	}
	
}
