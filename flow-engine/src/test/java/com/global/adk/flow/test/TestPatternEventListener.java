package com.global.adk.flow.test;

import com.global.adk.flow.annotation.Listen;
import com.global.adk.flow.engine.Execution;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/11/25
 */
public class TestPatternEventListener {
	
	private int counter = 1;
	
	@Listen(eventExpression = "*", priority = 2)
	public void onChange5(Execution execution, String eventName) {
		execution.addAttribute("*+2",eventName+counter++);
	}
	
	@Listen(eventExpression = "\\S+", priority = 1)
	public void onChange6(Execution execution, String eventName) {
        execution.addAttribute("\\S+1",eventName+counter++);
	}
	
	@Listen(eventExpression = "\\S+", priority = 3)
	public void onChange7(Execution execution, String eventName) {
        execution.addAttribute("\\S+3", eventName+counter++);
	}
	
}