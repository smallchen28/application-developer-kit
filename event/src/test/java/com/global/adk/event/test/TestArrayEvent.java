/*
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

package com.global.adk.event.test;

import com.global.adk.common.compiler.*;
import com.global.adk.common.compiler.Compiler;
import javassist.CtClass;

import org.junit.Test;

public class TestArrayEvent {
	
	@Test
	public void test() {
		
		StringBuilder src = new StringBuilder();
		
		src.append("public void invocation(Object[] events){\n\t")
			.append("for(int i = 0 ,j = events.length ; i < j ; i++){System.out.println(events[i]);}").append("}");
		
		Compiler compiler = com.global.adk.common.compiler.Compiler.getInstance();
		
		CtClass ctClass = compiler.newCtClass(AbstractDefinition.class);
		
		compiler.methodWeave(ctClass, AbstractDefinition.class, src.toString());
		
		AbstractDefinition definition = compiler.newInstance(ctClass, null, null);
		
		Object[] events = new Object[] { "ASDf", 123, 345L };
		definition.invocation(events);
		
	}
	
}
