/*
 * www.global.com Inc.
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

package com.global.adk.rules.drools.test;


import com.global.adk.rules.drools.DroolsEngine;
import com.global.adk.rules.drools.module.Description;
import com.global.adk.rules.drools.module.ModuleView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class DroolsTemplateTest extends DroolsStandardTestBase {
	
	@Test
	public void testObjectCondition() {
		
		DroolsEngine.DroolsElement element = newDroolsElement();
		
		ModuleView moduleView = newModuleView();
		
		droolsTemplate.render(element, moduleView);
	}

	@Test
	public void testDefaultRule(){
		DroolsEngine.DroolsElement element = new DroolsEngine.DroolsElement();
		element.setDescription(new Description("XXXX", Description.EventRuleType.DEFAULT));
		ModuleView moduleView = newModuleView();
		droolsTemplate.render(element, moduleView);
	}
	
}
