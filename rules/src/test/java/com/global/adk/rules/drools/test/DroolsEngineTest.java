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


import com.google.common.collect.Lists;
import com.global.adk.api.order.ModifyEventRelatedOrder;
import com.global.adk.common.exception.RuleException;
import com.global.adk.rules.drools.DroolsEngine;
import com.global.adk.rules.drools.EventRequest;
import com.global.adk.rules.drools.module.Description;
import com.global.adk.rules.drools.module.InternalRuleEvent;
import com.global.adk.rules.drools.module.RelatedRule;
import com.yjf.common.lang.util.money.Money;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DroolsEngineTest extends DroolsStandardTestBase {


	@Test
	public void standardTest(){
		try {
			//- 此时清除的数据已经被引擎预先加载，需要等待60S才能清除。
			jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
			jdbcTemplate.update("delete from app_kit_rule");
			jdbcTemplate.update("delete from app_kit_rule_policy");
			jdbcTemplate.update("delete from app_kit_related_rule");
			jdbcTemplate.update("delete from app_kit_rule_history");
			jdbcTemplate.update("delete from app_kit_rule_obj_condition");
			jdbcTemplate.update("delete from app_kit_rule_eval_condition");
			TimeUnit.MINUTES.sleep(1);

			String eventName = "deposit" ;
			HashMap<String, String> eventContext = new HashMap<String, String>();
			eventContext.put("k1", "v1");
			eventContext.put("k2", "v2");
			//创建一个内部事件，并拥有全套规则
			newStandardRuleEvent(eventName, "充值", eventContext);
			TimeUnit.MINUTES.sleep(1);


			eventContext = new HashMap<String, String>();
			eventContext.put("k1", "v1");
			eventContext.put("k2", "v2");
			EventRequest request = new EventRequest(eventName);
			request.setEventContext(eventContext);

			request = new EventRequest(eventName);
			request.setEventContext(eventContext);

			DroolsEngine.DroolsElement droolsElement = droolsProvider.getDroolsEngine().loadDroolsElement(request, "ALL");
			Assert.assertTrue(droolsElement != null &&
					droolsElement.getDescription().getEventRuleType() == Description.EventRuleType.STANDARD &&
					droolsElement.getVersion() == 1);

			//跑一圈
			eventContext = new HashMap<>();
			eventContext.put("k1", "v1");
			eventContext.put("k2", "v2");

			request = new EventRequest(eventName);
			request.setEventContext(eventContext);

			StatelessKnowledgeSession session = droolsProvider.newStatelessSession(request, "ALL");
			Order order = new Order("jixianenv", 3D);
			order.getExtendsProperties().put("BIZ_IDENTITY", "EASY_TRADE");
			order.setMoney(new Money(500));
			order.setEventName(EventNameEnum.DEPOSIT);
			System.out.println(order.getMoney().getCent());
			session.execute(order);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}



	@Test
	public void noneRuleAndVersionTest() {

		try {
			//- 此时清除的数据已经被引擎预先加载，需要等待60S才能清除。
			jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
			jdbcTemplate.update("delete from app_kit_rule");
			jdbcTemplate.update("delete from app_kit_rule_policy");
			jdbcTemplate.update("delete from app_kit_related_rule");
			jdbcTemplate.update("delete from app_kit_rule_history");
			jdbcTemplate.update("delete from app_kit_rule_obj_condition");
			jdbcTemplate.update("delete from app_kit_rule_eval_condition");

			TimeUnit.MINUTES.sleep(1);

			String eventName = "deposit";
			HashMap<String, String> eventContext = new HashMap<String, String>();
			eventContext.put("k1", "v1");
			eventContext.put("k2", "v2");
			EventRequest request = new EventRequest(eventName);
			request.setEventContext(eventContext);

			InternalRuleEvent event = newStandardRuleEvent(eventName, "充值", eventContext);

			TimeUnit.MINUTES.sleep(1);
			// init -> event.version = element.version
			DroolsEngine.DroolsElement droolsElement = droolsProvider.getDroolsEngine().loadDroolsElement(request, "ALL");
			Assert.assertTrue(event.getVersion() == droolsElement.getVersion());

			//失效对应策略
			List<Long> identitys = jdbcTemplate.query("select identity from app_kit_rule_policy where related_event = ?",
					new RowMapper<Long>() {

						@Override
						public Long mapRow(ResultSet rs, int rowNum) throws SQLException {

							return rs.getLong(1);
						}
					}, new Object[]{event.getIdentity()});
			ModifyEventRelatedOrder eventRelatedOrder = new ModifyEventRelatedOrder();
			eventRelatedOrder.setInternalEventEnable(true);
			eventRelatedOrder.setInternalEventIdentity(event.getIdentity());

			boolean flag = true;
			ArrayList<ModifyEventRelatedOrder.EventRelatedElement> elements = Lists.newArrayList();
			for (Long identity : identitys) {
				ModifyEventRelatedOrder.EventRelatedElement element = new ModifyEventRelatedOrder.EventRelatedElement();
				element.setOptype(flag ? ModifyEventRelatedOrder.Optype.invalid : ModifyEventRelatedOrder.Optype.delete);
				element.setPolicyIdentity(identity);
				elements.add(element);
				flag = !flag;
			}
			eventRelatedOrder.setElements(elements);
			eventRelatedOrder.setDescription("hello");
			droolsAdmin.modifyEventRelated(eventRelatedOrder);

			TimeUnit.MINUTES.sleep(1);

			request = new EventRequest(eventName);
			try {
				droolsProvider.getDroolsEngine().loadDroolsElement(request, "ALL");
			}catch (RuleException e){
				Assert.assertTrue(e.getMessage().indexOf("不存在或尚未加载") > 0);
			}


			eventContext = new HashMap<>();
			eventContext.put("k1", "v1");
			eventContext.put("k2", "v2");
			request = new EventRequest(eventName);
			request.setEventContext(eventContext);

			droolsElement = droolsProvider.getDroolsEngine().loadDroolsElement(request, "ALL");

			Set<RelatedRule> rules = droolsElement.getDescription().getRules();
			Assert.assertTrue(rules == null || rules.size() == 0);
			Assert.assertTrue(droolsElement.getDescription().getEventRuleType() == Description.EventRuleType.DEFAULT);
			Assert.assertTrue(droolsElement.getVersion() == event.getVersion() + 1);

			StatelessKnowledgeSession session = droolsProvider.newStatelessSession(request, "ALL");
			session.execute(new Order("jixianenv", 11D));


		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

}
