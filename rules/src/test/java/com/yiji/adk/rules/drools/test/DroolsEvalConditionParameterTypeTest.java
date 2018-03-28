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

package com.yiji.adk.rules.drools.test;


import com.yiji.adk.rules.drools.EventRequest;
import com.yiji.adk.rules.drools.module.InternalRuleEvent;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DroolsEvalConditionParameterTypeTest extends DroolsStandardTestBase {
	
	@Test
	public void allTypeTest() {

		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		jdbcTemplate.update("delete from app_kit_rule_obj_condition");
		jdbcTemplate.update("delete from app_kit_rule_eval_condition");


		HashMap<String, String> eventContext = new HashMap<String, String>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		
		InternalRuleEvent depositInt = newBaiscTypeInernalRuleEvent("deposit-int", "充值", eventContext);
		InternalRuleEvent depositLong = newBaiscTypeLongRuleEvent("deposit-long", "充值", eventContext);
		InternalRuleEvent depositDouble = newBaiscTypeDoubleRuleEvent("deposit-double", "充值", eventContext);
		InternalRuleEvent depositFloat = newBaiscTypeFloatRuleEvent("deposit-float", "充值", eventContext);
		InternalRuleEvent depositString = newBaiscTypeStringRuleEvent("deposit-string", "充值", eventContext);
		InternalRuleEvent depositBoolean = newBaiscTypeBooleanRuleEvent("deposit-boolean", "充值", eventContext);
		InternalRuleEvent depositChar = newBaiscTypeCharRuleEvent("deposit-char", "充值", eventContext);

		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		//int
		eventContext = new HashMap<String, String>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		EventRequest request = new EventRequest(depositInt.getEventName());
		request.setEventContext(eventContext);
		StatelessKnowledgeSession session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));

		//long
		eventContext = new HashMap<>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		request = new EventRequest(depositLong.getEventName());
		request.setEventContext(eventContext);
		session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));

		//double
		eventContext = new HashMap<>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		request = new EventRequest(depositDouble.getEventName());
		request.setEventContext(eventContext);
		session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));

		//float
		eventContext = new HashMap<>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		request = new EventRequest(depositFloat.getEventName());
		request.setEventContext(eventContext);
		session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));

		//string
		eventContext = new HashMap<>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		request = new EventRequest(depositString.getEventName());
		request.setEventContext(eventContext);
		session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));

		//boolean
		eventContext = new HashMap<>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		request = new EventRequest(depositBoolean.getEventName());
		request.setEventContext(eventContext);
		session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));

		//char
		eventContext = new HashMap<>();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		request = new EventRequest(depositChar.getEventName());
		request.setEventContext(eventContext);
		session = droolsProvider.newStatelessSession(request, "ALL");
		session.execute(new Order("jixianenv", 11D));
	}

}
