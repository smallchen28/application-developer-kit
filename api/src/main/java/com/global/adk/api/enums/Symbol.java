package com.global.adk.api.enums;

public enum Symbol {
	GREATER_THEN(">"),
	
	LESS_THEN("<"),
	
	GREATER_EQUAL(">="),
	
	LESS_EQUAL("<="),
	
	EQUAL("=="),
	
	NO_EQUAL("!="),
	
	NOT_IN("!in"),
	
	IS_IN("in");
	
	private String code;
	
	private Symbol(String code) {
		
		this.code = code;
	}
	
	public String getCode() {
		
		return code;
	}
	
	public static Symbol code(String code) {
		
		Symbol symbol = null;
		for (Symbol sb : values()) {
			if (sb.code.equals(code)) {
				symbol = sb;
				break;
			}
		}
		return symbol;
	}
	
}
