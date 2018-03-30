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

import java.util.HashMap;

import com.global.adk.api.order.*;
import com.global.adk.rules.drools.module.InternalRuleEvent;
import com.global.adk.rules.drools.module.Rule;
import com.global.adk.rules.drools.module.RulePolicy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/rule.xml"})
public class DroolsAdminTest extends DroolsStandardTestBase {
	
	@Test
	public void testRuleInsert() {
		
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		
		Rule rule = storeRule();
		
		Assert.assertNotNull(rule);
		Assert.assertTrue(rule.getRuleName().equals("测试规则1"));
		Assert.assertTrue(rule.getDescription().equals("testRuleInsert"));
	}
	
	@Test
	public void testRulePolicyInsert() {
		
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		
		RulePolicy rulePolicy = storeRulePolicy();
		
		Assert.assertNotNull(rulePolicy);
		Assert.assertTrue(rulePolicy.getDescription().equals("测试"));
		Assert.assertTrue(rulePolicy.getRelatedRules().size() == 2);
	}
	
	@Test
	public void testInternalRuleEventInsert() {
		
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		
		InternalRuleEvent internalRuleEvent = storeRuleEvent();
		
		internalRuleEvent = jdbcTemplate.queryForObject(
			"select identity,event_name,description from APP_KIT_INTERNAL_RULE_EVENT where identity = ?",
			new Object[] { internalRuleEvent.getIdentity() }, new BeanPropertyRowMapper<InternalRuleEvent>(
				InternalRuleEvent.class));
		
		Assert.assertNotNull(internalRuleEvent);
		Assert.assertTrue(internalRuleEvent.getEventName().equals("测试事件1"));
		Assert.assertTrue(internalRuleEvent.getDescription().equals("testInternalRuleEventInsert"));
	}
	
	@Test
	public void testRuleDelete() {
		
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		
		InternalRuleEvent event = storeRuleEvent();
		
		String ruleName = event.getPolicy().get(0).getRelatedRules().get(0).getRuleName();
		UnregisterRuleOrder order = newRuleDeleteOrder();
		
		long ruleIdentity = jdbcTemplate.queryForObject("select identity from app_kit_rule where rule_name = ?",
			new Object[] { ruleName }, Long.class);
		order.getRuleIds().add(ruleIdentity);
		
		droolsAdmin.unRegisterRule(order);
		
		//验证删除
		long counter = jdbcTemplate.queryForObject("select count(*) from app_kit_rule where identity=?",
			new Object[] { ruleIdentity }, Long.class);
		Assert.assertTrue(counter == 0L);
		
		//验证关联关系
		counter = jdbcTemplate.queryForObject("select count(*) from app_kit_related_rule where rule_identity = ? ",
			new Object[] { ruleIdentity }, Long.class);
		Assert.assertTrue(counter == 0);
		
		//验证策略版本
		long version = jdbcTemplate.queryForObject(
			"select version from app_kit_internal_rule_event where identity = ? ",
			new Object[] { event.getIdentity() }, Long.class);
		Assert.assertTrue(version - event.getVersion() == 1);
		
		//验证历史库
		counter = jdbcTemplate.queryForObject("select count(*) from app_kit_rule_history where identity = ?",
			new Object[] { ruleIdentity }, Long.class);
		Assert.assertTrue(counter == 1);
		
	}
	
	@Test
	public void testModifyEventRelated() {
		
		// add
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		
		InternalRuleEvent internalRuleEvent2 = storeRuleEvent();
		
		droolsAdmin.modifyEventRelated(newModifyEventRelatedOrder2(internalRuleEvent2.getPolicy().get(0),
			internalRuleEvent2.getIdentity()));
		
		//验证事件是否存在
		long counter = jdbcTemplate.queryForObject(
			"select count(*) from app_kit_internal_rule_event where identity = ?",
			new Object[] { internalRuleEvent2.getIdentity() }, Long.class);
		Assert.assertTrue(counter == 1);
		
		//验证版本
		long version = jdbcTemplate.queryForObject(
			"select version from app_kit_internal_rule_event where identity = ?",
			new Object[] { internalRuleEvent2.getIdentity() }, Long.class);
		Assert.assertTrue(version - internalRuleEvent2.getVersion() == 1);
		
		//验证策略add
		counter = jdbcTemplate.queryForObject("select count(*) from app_kit_rule_policy where related_event = ? ",
			new Object[] { internalRuleEvent2.getIdentity() }, Long.class);
		Assert.assertTrue(counter == 1);
		
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		//delete
		InternalRuleEvent internalRuleEvent = storeRuleEvent();
		
		RulePolicy rulePolicy = internalRuleEvent.getPolicy().get(0);
		
		droolsAdmin.modifyEventRelated(newModifyEventRelatedOrder(rulePolicy, internalRuleEvent.getIdentity()));
		
		//验证版本
		version = jdbcTemplate.queryForObject("select version from app_kit_internal_rule_event where identity = ?",
			new Object[] { internalRuleEvent.getIdentity() }, Long.class);
		Assert.assertTrue(version - internalRuleEvent2.getVersion() == 1);
		
		//验证策略delete
		counter = jdbcTemplate.queryForObject("select count(*) from app_kit_rule_policy where related_event = ? ",
			new Object[] { internalRuleEvent.getIdentity() }, Long.class);
		Assert.assertTrue(counter == 0);
		
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		//invalid
		InternalRuleEvent internalRuleEvent3 = storeRuleEvent();
		
		RulePolicy rulePolicy3 = internalRuleEvent.getPolicy().get(0);
		
		droolsAdmin.modifyEventRelated(newModifyEventRelatedOrder4(rulePolicy3, internalRuleEvent3.getIdentity()));
		
		//验证版本
		version = jdbcTemplate.queryForObject("select version from app_kit_internal_rule_event where identity = ?",
			new Object[] { internalRuleEvent3.getIdentity() }, Long.class);
		Assert.assertTrue(version - internalRuleEvent2.getVersion() == 1);
		
		//验证策略invalid
		counter = jdbcTemplate.queryForObject(
			"select count(*) from app_kit_rule_policy where related_event = ? and enable = ?",
			new Object[] { internalRuleEvent3.getIdentity(), "1" }, Long.class);
		Assert.assertTrue(counter == 1);
		
	}
	
	@Test
	public void testModifyRule() {
		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
		jdbcTemplate.update("delete from app_kit_rule");
		jdbcTemplate.update("delete from app_kit_rule_policy");
		jdbcTemplate.update("delete from app_kit_related_rule");
		jdbcTemplate.update("delete from app_kit_rule_history");
		jdbcTemplate.update("delete from app_kit_rule_obj_condition");
		jdbcTemplate.update("delete from app_kit_rule_eval_condition");
		
		String ruleName = "A22222222222222";
		String eventName = "deposit";
		
		HashMap<String, String> eventContext = new HashMap<String, String>();
		
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		newOneRuleEvent(eventName, "充值", eventContext);
		
		long rule_id = jdbcTemplate
			.queryForObject(
				"select IDENTITY from app_kit_rule where identity in ("
						+ "select DISTINCT rule_identity "
						+ "from app_kit_related_rule "
						+ "where policy_identity in"
						+ " ( select DISTINCT identity"
						+ " from app_kit_rule_policy where related_event in (select identity from app_kit_internal_rule_event where event_name = ? and event_context = ?)))   ",
				new Object[] { eventName, JSON.toJSONString(eventContext) }, Long.class);
		
		long object_id = jdbcTemplate.queryForObject(
			"select IDENTITY from app_kit_rule_obj_condition where RULE_IDENTITY = ? ", new Object[] { rule_id },
			Long.class);
		
		long eval_id = jdbcTemplate.queryForObject(
			"select IDENTITY from app_kit_rule_eval_condition where RULE_IDENTITY = ? ", new Object[] { rule_id },
			Long.class);
		
		String description = "充值";
		
		ModifyRuleOrder order = modifyRule(rule_id, object_id, eval_id, ruleName, description,
			new HashMap<Long, ModifyRuleOrder.IdentityOptype>());
		droolsAdmin.modifyRule(order);
		
	}
	
	@Test
	public void testModifyInternalPolicy() {
		ModifyEventRelatedOrder order = new ModifyEventRelatedOrder();
		order.setDescription("哈哈哈");
		order.setInternalEventIdentity(2552);
		order.setInternalEventEnable(true);
		ModifyEventRelatedOrder.EventRelatedElement element = new ModifyEventRelatedOrder.EventRelatedElement();
		element.setOptype(ModifyEventRelatedOrder.Optype.valid);
		element.setPolicyIdentity(2551);
		order.setElements(Lists.newArrayList(element));
		droolsAdmin.modifyEventRelated(order);
	}
	
	@Test
	public void testModifyPolicyRelatedRule() {
		ModifyPolicyRelatedOrder order = new ModifyPolicyRelatedOrder();
		order.setDescription("我去……");
		order.setRiskType("巴拉巴拉");
		order.setPolicyFrom("ALL");
		order.setPolicyIdentity(2558);
		order.setPlicyEnable(true);
		ModifyPolicyRelatedOrder.PolicyRelatedElement element = new ModifyPolicyRelatedOrder.PolicyRelatedElement();
		element.setPolicyOptype(ModifyPolicyRelatedOrder.PolicyOptype.invalid);
		RelatedRuleAttribute relatedRuleAttribute = new RelatedRuleAttribute(2553);
		element.setRelatedRuleAttribute(relatedRuleAttribute);
		order.setElements(Lists.newArrayList(element));
		droolsAdmin.modifyPolicyRelated(order);
	}
}
