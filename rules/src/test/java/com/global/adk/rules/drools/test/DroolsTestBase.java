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
import com.google.common.collect.Maps;
import com.global.adk.api.enums.Symbol;
import com.global.adk.api.order.*;
import com.global.adk.rules.drools.*;
import com.global.adk.rules.drools.module.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DroolsTestBase {
	
	@Autowired
	protected DroolsAdmin droolsAdmin;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	protected DroolsProvider droolsProvider;
	
	@Autowired
	protected DroolsTemplate droolsTemplate;
	
	protected ArrayList<String> builderImports() {
		
//		ArrayList<String> imports = Lists.newArrayList("java.lang.String", "java.util.ArrayList",
//				"java.util.Map");
//
		ArrayList<String> imports = Lists.newArrayList();

		return imports;
	}
	
	protected ModifyEventRelatedOrder newModifyEventRelatedOrder4(RulePolicy rulePolicy, long internalEventIdentity) {
		
		ModifyEventRelatedOrder order = new ModifyEventRelatedOrder();
		
		ModifyEventRelatedOrder.EventRelatedElement element = new ModifyEventRelatedOrder.EventRelatedElement();
		element.setOptype(ModifyEventRelatedOrder.Optype.valid);
		element.setPolicyIdentity(rulePolicy.getIdentity());
		
		order.setElements(Lists.newArrayList(element));
		order.setInternalEventEnable(true);
		order.setInternalEventIdentity(internalEventIdentity);
		order.setDescription("卧了个槽");
		return order;
	}
	
	protected ModifyEventRelatedOrder newModifyEventRelatedOrder3(RulePolicy rulePolicy, long internalEventIdentity) {
		
		ModifyEventRelatedOrder order = new ModifyEventRelatedOrder();
		
		ModifyEventRelatedOrder.EventRelatedElement element = new ModifyEventRelatedOrder.EventRelatedElement();
		element.setOptype(ModifyEventRelatedOrder.Optype.invalid);
		element.setPolicyIdentity(rulePolicy.getIdentity());
		
		order.setElements(Lists.newArrayList(element));
		order.setInternalEventEnable(false);
		order.setInternalEventIdentity(internalEventIdentity);

		return order;
	}
	
	protected ModifyEventRelatedOrder newModifyEventRelatedOrder(RulePolicy rulePolicy, long internalEventIdentity) {
		
		ModifyEventRelatedOrder order = new ModifyEventRelatedOrder();
		
		ModifyEventRelatedOrder.EventRelatedElement element = new ModifyEventRelatedOrder.EventRelatedElement();
		element.setOptype(ModifyEventRelatedOrder.Optype.delete);
		element.setPolicyIdentity(rulePolicy.getIdentity());
		
		order.setElements(Lists.newArrayList(element));
		order.setInternalEventEnable(false);
		order.setInternalEventIdentity(internalEventIdentity);
		order.setDescription("我勒个去。。。。。");
		return order;
	}
	
	protected ModifyEventRelatedOrder newModifyEventRelatedOrder2(RulePolicy rulePolicy, long internalEventIdentity) {
		
		ModifyEventRelatedOrder order = new ModifyEventRelatedOrder();
		
		ModifyEventRelatedOrder.EventRelatedElement element = new ModifyEventRelatedOrder.EventRelatedElement();
		element.setOptype(ModifyEventRelatedOrder.Optype.add);
		element.setPolicyIdentity(rulePolicy.getIdentity());
		
		order.setElements(Lists.newArrayList(element));
		order.setInternalEventEnable(true);
		order.setInternalEventIdentity(internalEventIdentity);
		order.setDescription("卧了个槽");

		return order;
	}
	
	protected ArrayList<String> builderGlobals() {
		
//		ArrayList<String> globals = Lists.newArrayList("java.lang.String", "java.util.ArrayList",
//				"java.util.Map");
		ArrayList<String> globals = Lists.newArrayList();

		return globals;
	}
	
	protected UnregisterRuleOrder newRuleDeleteOrder() {
		
		UnregisterRuleOrder deleteRuleOrder = new UnregisterRuleOrder();
		deleteRuleOrder.setDeleteReason("测试删除");
		deleteRuleOrder.setOpertorID("ligen");
		deleteRuleOrder.setOpertorIP("192.168.0.1");

		return deleteRuleOrder;
	}
	
	protected ArrayList<ConditionOrder> builderConditions() {
		
		ObjectConditionOrder condition = new ObjectConditionOrder();
		EvalConditionOrder condition2 = new EvalConditionOrder();
		
		ObjectConditionOrder.CompareElementOrder compareElement = new ObjectConditionOrder.CompareElementOrder();
		ObjectConditionOrder.CompareElementOrder compareElement2 = new ObjectConditionOrder.CompareElementOrder();
		compareElement.setJoinSymbol("");
		compareElement.setLeftValue("1");
		compareElement.setSymbol("==");
		compareElement.setRightValue("1");

		compareElement2.setJoinSymbol("&&");
		compareElement2.setLeftValue("1");
		compareElement2.setSymbol("==");
		compareElement2.setRightValue("1");
		ArrayList<ObjectConditionOrder.CompareElementOrder> compareElements = Lists.newArrayList(compareElement, compareElement2);
		condition.setCompareElementOrders(compareElements);
		condition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		condition.setVariableName("order");

		condition2.setCompareValue("\"ligen\"");
		condition2.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \"ligen\";}  }");
		condition2.setExecutorName("compilerExecutor");
		condition2.setRequestName("$order");
		condition2.setSymbol(Symbol.EQUAL);

		ArrayList<ConditionOrder> conditions = Lists.newArrayList(condition, condition2);
		
		return conditions;
	}
	
	protected RegisterRuleOrder newRegisterRuleOrder(String ruleName, String description) {
		
		RegisterRuleOrder order = new RegisterRuleOrder();
		order.getConditions().addAll(builderConditions());
		order.setDescription(description);
		order.setGlobals(builderGlobals());
		order.setImports(builderImports());
		order.setRuleName(ruleName);
		order.setScript("System.out.println(\"Hello world ...\");");

		return order;
	}
	
	protected RegisterInternalRuleEventOrder newInternalRuleEventOrder(String eventName, String description) {
		
		RegisterInternalRuleEventOrder order = new RegisterInternalRuleEventOrder();
		order.setEventName(eventName);
		order.setDescription(description);
		HashMap<String, String> eventContext = Maps.newHashMap();
		eventContext.put("k1", "v1");
		eventContext.put("k2", "v2");
		order.setEventContext(eventContext);
		return order;
	}
	
	protected RegisterRulePolicyOrder newRulePolicyOrder(String rulePolicyName, String description) {
		
		RegisterRulePolicyOrder order = new RegisterRulePolicyOrder();
		
		order.setDescription(description);
		order.setRiskType("测试风险");
		order.setPolicyFrom("ALL");
		
		return order;
		
	}
	
	protected Rule storeRule() {
		
		Rule rule = droolsAdmin.register(newRegisterRuleOrder("测试规则1", "testRuleInsert"));
		rule = jdbcTemplate.queryForObject("select * from app_kit_rule where identity=?",
			new Object[] { rule.getIdentity() }, new BeanPropertyRowMapper<Rule>(Rule.class));
		return rule;
	}
	
	protected RulePolicy storeRulePolicy() {
		
		Rule rule1 = droolsAdmin.register(newRegisterRuleOrder("测试规则1", "testRuleInsert"));
		Rule rule2 = droolsAdmin.register(newRegisterRuleOrder("测试规则2", "testRuleInsert"));
		
		RelatedRuleAttribute relatedRuleAttribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		relatedRuleAttribute1.setAsync(true);
		relatedRuleAttribute1.setSalicence(100);
		relatedRuleAttribute1.setDescription("sdf");
		RelatedRuleAttribute relatedRuleAttribute2 = new RelatedRuleAttribute(rule2.getIdentity());
		relatedRuleAttribute2.setLoop(true);
		relatedRuleAttribute2.setDescription("sdf");
		relatedRuleAttribute2.setSalicence(88);
		ArrayList<RelatedRuleAttribute> relatedRulesAttributes = Lists.newArrayList(relatedRuleAttribute1,
			relatedRuleAttribute2);
		
		RegisterRulePolicyOrder order = new RegisterRulePolicyOrder();
		order.setDescription("测试");
		order.setRiskType("测试盗刷");
		order.setPolicyFrom("ALL");
		order.getRelatedRuleAttributes().addAll(relatedRulesAttributes);
		RulePolicy policy = droolsAdmin.register(order);
		
		RelatedRule relatedRule1 = new RelatedRule();
		relatedRule1.setRuleName(rule1.getRuleName());
		RelatedRule relatedRule2 = new RelatedRule();
		relatedRule1.setRuleName(rule2.getRuleName());
		
		ArrayList<RelatedRule> relatedRules = Lists.newArrayList(relatedRule1, relatedRule2);
		policy.setRelatedRules(relatedRules);
		return policy;
	}
	
	protected InternalRuleEvent storeRuleEvent() {
		
		RegisterInternalRuleEventOrder ruleEventOrder = newInternalRuleEventOrder("测试事件1",
			"testInternalRuleEventInsert");
		
		RulePolicy rulePolicy = storeRulePolicy();
		ruleEventOrder.getAttributes().addAll(
			Lists.newArrayList(new RelatedPolicyAttribute(rulePolicy.getIdentity(), Boolean.TRUE)));
		
		InternalRuleEvent internalRuleEvent = droolsAdmin.register(ruleEventOrder);
		
		internalRuleEvent.getPolicy().add(rulePolicy);
		internalRuleEvent.setDescription("卧了个槽……");
		
		return internalRuleEvent;
	}
	
	protected ModuleView newModuleView() {
        InternalRuleEvent event = new InternalRuleEvent();
        event.setDescription("渲染模版测试");
        event.setEnable(true);
        event.setEventName("render_test");
        event.setIdentity(12);
        event.setVersion(23);
        ArrayList<RulePolicy> policies = Lists.newArrayList();
        event.setPolicy(policies);
        //- 策略
        RulePolicy policy = new RulePolicy();
        RulePolicy policy2 = new RulePolicy();
        policies.add(policy2);
        policies.add(policy);
        //- 规则
        RelatedRule related1 = new RelatedRule();
        RelatedRule related2 = new RelatedRule();
        
        policy.setRelatedRules(Lists.newArrayList(related1));
        policy2.setRelatedRules(Lists.newArrayList(related1,related2));
        
        //- 条件
        ObjectCondition objectCondition = new ObjectCondition();
        EvalCondition evalCondition = new EvalCondition();

        ArrayList<Condition> conditions1 = new ArrayList<>();
        conditions1.add(objectCondition);

        ArrayList<Condition> conditions2 = new ArrayList<>();
        conditions2.add(objectCondition);
        conditions2.add(evalCondition);
        
        //条件赋值
        objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
        objectCondition.setVariableName("order");
        ObjectCondition.CompareElement element1 = new ObjectCondition.CompareElement();
        element1.setLeftValue("getProductName()");
        element1.setSymbol("==");
        element1.setRightValue("\"jixianenv\"");
        objectCondition.setCompareElements(Lists.newArrayList(element1));
        
        evalCondition.setCompareValue("\"jixianenv\"");
        evalCondition.setConditionScript("package com.global;  Calcu extends ExecutorWrapper{  public Object execute(Object ... parameter){return \"jixianenv\" ;}  }");
        evalCondition.setExecutorName("compilerExecutor");
        evalCondition.setRequestName("$order");
        evalCondition.setSymbol(com.global.adk.rules.drools.module.Symbol.EQUAL);
        
        //规则赋值
        related1.setIdentity(1);
        related1.setAsync(true);
        related1.getConditions().addAll(conditions1);
        related1.setDescription("测试规则");
        related1.setEnable(true);
        related1.setRuleName("testRuleName");
        related1.setScript("System.out.println(\"hello ......\");");
        related1.getImports().addAll(Lists.newArrayList("java.lang.Object","java.util.ArrayList"));
        related1.getGlobals().addAll(Lists.newArrayList("java.lang.Object obj","java.util.ArrayList list"));
        related1.setExpireTime(new Timestamp(new Date().getTime()+100000000));
        
        related2.setIdentity(2);
        related2.setAsync(true);
        related2.getConditions().addAll(conditions2);
        related2.setDescription("测试规则2");
        related2.setEnable(true);
        related2.setRuleName("testRuleName2");
        related2.setScript("System.out.println(\"hello2 ......\");");
        related2.setEffectiveTime(new Timestamp(new Date().getTime()+100000000));
        
        //策略赋值
        policy.setDescription("渲染测试策略");
        policy.setEnable(true);
        policy.setPolicyName("p1");
        
        policy2.setDescription("渲染测试策略2");
        policy2.setEnable(true);
        policy2.setPolicyName("p2");
        
        
        HashMap<String, DynamicConditionExecutor> executors = Maps.newHashMap();
        executors.put("compilerExecutor", new CompilerDynamicExecutor());
        executors.put("groovyExecutor", new GroovyDynamicExecutor());
        
        ModuleView moduleView = new ModuleView(event,executors,10000);
        
        return moduleView;
    }
	
	protected DroolsEngine.DroolsElement newDroolsElement() {
		DroolsEngine.DroolsElement element = new DroolsEngine.DroolsElement();
		element.setDescription(new Description("XXXX", Description.EventRuleType.STANDARD));
		return element;
	}
	
	protected InternalRuleEvent newStandardRuleEvent(String eventName, String description,
														HashMap<String, String> eventContext) {
		
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("standardRule1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		RegisterRuleOrder rule2Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions2 = Lists.newArrayList();
		rule2Order.getConditions().addAll(conditions2);
		rule2Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule2Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule2Order.setDescription(description);
		rule2Order.setRuleName("standardRule2");
		rule2Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 conditions1 , conditions2
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		EvalConditionOrder evalCondition = new EvalConditionOrder();
		
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		ObjectConditionOrder.CompareElementOrder element1 = new ObjectConditionOrder.CompareElementOrder();
		element1.setLeftValue("getProductName()");
		element1.setSymbol("==");
		element1.setRightValue("\"jixianenv\"");
		ObjectConditionOrder.CompareElementOrder element2 = new ObjectConditionOrder.CompareElementOrder();
		element2.setJoinSymbol(" && ");
		element2.setLeftValue("extendsProperties['BIZ_IDENTITY']");
		element2.setSymbol("==");
		element2.setRightValue("\"EASY_TRADE\"");
		ObjectConditionOrder.CompareElementOrder element3 = new ObjectConditionOrder.CompareElementOrder();
		element3.setJoinSymbol(" && ");
		element3.setLeftValue("money.getCent()");
		element3.setSymbol("==");
		element3.setRightValue("50000");
		ObjectConditionOrder.CompareElementOrder element4 = new ObjectConditionOrder.CompareElementOrder();
		element4.setJoinSymbol(" && ");
		element4.setLeftValue("eventName.code()");
		element4.setSymbol("==");
		element4.setRightValue("\"DEPOSIT\"");
		ObjectConditionOrder.CompareElementOrder element5 = new ObjectConditionOrder.CompareElementOrder();
		element5.setJoinSymbol(" && ");
		element5.setLeftValue("getPrice()");
		/*测试in*/
		element5.setSymbol(" in ");
		element5.setRightValue("(\"3\", \"5\")");
		objectCondition.setCompareElementOrders(Lists.newArrayList(element1, element2, element3, element4, element5));
		
		evalCondition.setCompareValue("\"jixianenv\"");
		evalCondition
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \"jixianenv\" ;}  }");
		evalCondition.setExecutorName("compilerExecutor");
		evalCondition.setRequestName("$order");
		evalCondition.setSymbol(Symbol.NOT_IN);
		evalCondition.setScriptIdentity(1);
		
		conditions1.add(objectCondition);
		conditions2.add(evalCondition);
		conditions2.add(objectCondition);
		
		//- rule1Order ,rule2Order赋值conditions1，conditions2
		rule1Order.getConditions().addAll(conditions1);
		rule2Order.getConditions().addAll(conditions2);
		
		//- 注册rule1Order , rule2Order
		Rule rule1 = droolsAdmin.register(rule1Order);
		Rule rule2 = droolsAdmin.register(rule2Order);
		
		//- 创建策略p1 ，p2
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		RegisterRulePolicyOrder p2Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		p1Order.setDescription("description");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		p2Order.setDescription("standard_policy2");
		p2Order.setRiskType("测试风险");
		p2Order.setPolicyFrom("ALL");
		p2Order.setDescription("description");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes2 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2); relatedRuleAttributes1.add(attribute1);
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("description");
		relatedRuleAttributes2.add(attribute1);
		relatedRuleAttributes1.add(attribute1);

		
		RelatedRuleAttribute attribute2 = new RelatedRuleAttribute(rule2.getIdentity());
		attribute2.setAsync(false);
		attribute2.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute2.setLoop(true);
		attribute2.setSalicence(1);
		attribute2.setDescription("sdf");
		relatedRuleAttributes2.add(attribute2);
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		p2Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes2);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		RulePolicy policy2 = droolsAdmin.register(p2Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);

		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy2.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
		
	}
	
	protected InternalRuleEvent newBaiscTypeInernalRuleEvent(String eventName, String description,
																HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("int1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");

		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");

		//int  >
		EvalConditionOrder intOrder = new EvalConditionOrder();
		intOrder.setCompareValue("8");
		intOrder
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 9 ;}  }");
		intOrder.setExecutorName("compilerExecutor");
		intOrder.setRequestName("$order");
		intOrder.setSymbol(Symbol.GREATER_THEN);
		//int >=
		EvalConditionOrder int2Order = new EvalConditionOrder();
		int2Order.setCompareValue("8");
		int2Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8 ;}  }");
		int2Order.setExecutorName("compilerExecutor");
		int2Order.setRequestName("$order");
		int2Order.setSymbol(Symbol.GREATER_EQUAL);
		//int <
		EvalConditionOrder int3Order = new EvalConditionOrder();
		int3Order.setCompareValue("8");
		int3Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7 ;}  }");
		int3Order.setExecutorName("compilerExecutor");
		int3Order.setRequestName("$order");
		int3Order.setSymbol(Symbol.LESS_THEN);
		//int ==
		EvalConditionOrder int4Order = new EvalConditionOrder();
		int4Order.setCompareValue("8");
		int4Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8 ;}  }");
		int4Order.setExecutorName("compilerExecutor");
		int4Order.setRequestName("$order");
		int4Order.setSymbol(Symbol.EQUAL);
		//int  <=
		EvalConditionOrder int5Order = new EvalConditionOrder();
		int5Order.setCompareValue("8");
		int5Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7 ;}  }");
		int5Order.setExecutorName("compilerExecutor");
		int5Order.setRequestName("$order");
		int5Order.setSymbol(Symbol.LESS_EQUAL);
		//int  ==
		EvalConditionOrder int6Order = new EvalConditionOrder();
		int6Order.setCompareValue("8");
		int6Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8;}  }");
		int6Order.setExecutorName("compilerExecutor");
		int6Order.setRequestName("$order");
		int6Order.setSymbol(Symbol.EQUAL);
		//int equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("8");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8;}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int5Order);
		conditions1.add(int6Order);
		conditions1.add(intOrder);
		conditions1.add(int2Order);
		conditions1.add(int3Order);
		conditions1.add(int4Order);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		p1Order.setDescription("description");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("ff");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
	}
	
	protected InternalRuleEvent newBaiscTypeLongRuleEvent(String eventName, String description,
															HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("long1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		
		//long  >
		EvalConditionOrder intOrder = new EvalConditionOrder();
		intOrder.setCompareValue("8L");
		intOrder
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 9L ;}  }");
		intOrder.setExecutorName("compilerExecutor");
		intOrder.setRequestName("$order");
		intOrder.setSymbol(Symbol.GREATER_THEN);
		//long >=
		EvalConditionOrder int2Order = new EvalConditionOrder();
		int2Order.setCompareValue("8L");
		int2Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8L ;}  }");
		int2Order.setExecutorName("compilerExecutor");
		int2Order.setRequestName("$order");
		int2Order.setSymbol(Symbol.GREATER_EQUAL);
		//long <
		EvalConditionOrder int3Order = new EvalConditionOrder();
		int3Order.setCompareValue("8L");
		int3Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7L ;}  }");
		int3Order.setExecutorName("compilerExecutor");
		int3Order.setRequestName("$order");
		int3Order.setSymbol(Symbol.LESS_THEN);
		//long ==
		EvalConditionOrder int4Order = new EvalConditionOrder();
		int4Order.setCompareValue("8L");
		int4Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8L ;}  }");
		int4Order.setExecutorName("compilerExecutor");
		int4Order.setRequestName("$order");
		int4Order.setSymbol(Symbol.EQUAL);
		//long  <=
		EvalConditionOrder int5Order = new EvalConditionOrder();
		int5Order.setCompareValue("8L");
		int5Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7L ;}  }");
		int5Order.setExecutorName("compilerExecutor");
		int5Order.setRequestName("$order");
		int5Order.setSymbol(Symbol.LESS_EQUAL);
		//long  ==
		EvalConditionOrder int6Order = new EvalConditionOrder();
		int6Order.setCompareValue("8L");
		int6Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8L;}  }");
		int6Order.setExecutorName("compilerExecutor");
		int6Order.setRequestName("$order");
		int6Order.setSymbol(Symbol.EQUAL);
		//long equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("8L");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8L;}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int5Order);
		conditions1.add(int6Order);
		conditions1.add(intOrder);
		conditions1.add(int2Order);
		conditions1.add(int3Order);
		conditions1.add(int4Order);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("dd");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
	}
	
	protected InternalRuleEvent newBaiscTypeFloatRuleEvent(String eventName, String description,
															HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("float1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		
		//float  >
		EvalConditionOrder intOrder = new EvalConditionOrder();
		intOrder.setCompareValue("8F");
		intOrder
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 9F ;}  }");
		intOrder.setExecutorName("compilerExecutor");
		intOrder.setRequestName("$order");
		intOrder.setSymbol(Symbol.GREATER_THEN);
		//float >=
		EvalConditionOrder int2Order = new EvalConditionOrder();
		int2Order.setCompareValue("8F");
		int2Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8F ;}  }");
		int2Order.setExecutorName("compilerExecutor");
		int2Order.setRequestName("$order");
		int2Order.setSymbol(Symbol.GREATER_EQUAL);
		//float <
		EvalConditionOrder int3Order = new EvalConditionOrder();
		int3Order.setCompareValue("8F");
		int3Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7F ;}  }");
		int3Order.setExecutorName("compilerExecutor");
		int3Order.setRequestName("$order");
		int3Order.setSymbol(Symbol.LESS_THEN);
		//float ==
		EvalConditionOrder int4Order = new EvalConditionOrder();
		int4Order.setCompareValue("8F");
		int4Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8F ;}  }");
		int4Order.setExecutorName("compilerExecutor");
		int4Order.setRequestName("$order");
		int4Order.setSymbol(Symbol.EQUAL);
		//float  <=
		EvalConditionOrder int5Order = new EvalConditionOrder();
		int5Order.setCompareValue("8F");
		int5Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7F ;}  }");
		int5Order.setExecutorName("compilerExecutor");
		int5Order.setRequestName("$order");
		int5Order.setSymbol(Symbol.LESS_EQUAL);
		//float  ==
		EvalConditionOrder int6Order = new EvalConditionOrder();
		int6Order.setCompareValue("8F");
		int6Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8F;}  }");
		int6Order.setExecutorName("compilerExecutor");
		int6Order.setRequestName("$order");
		int6Order.setSymbol(Symbol.EQUAL);
		//float equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("8F");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8F;}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int5Order);
		conditions1.add(int6Order);
		conditions1.add(intOrder);
		conditions1.add(int2Order);
		conditions1.add(int3Order);
		conditions1.add(int4Order);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("sff");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
	}
	
	protected InternalRuleEvent newBaiscTypeDoubleRuleEvent(String eventName, String description,
															HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("double1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		
		//double  >
		EvalConditionOrder intOrder = new EvalConditionOrder();
		intOrder.setCompareValue("8D");
		intOrder
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 9D ;}  }");
		intOrder.setExecutorName("compilerExecutor");
		intOrder.setRequestName("$order");
		intOrder.setSymbol(Symbol.GREATER_THEN);
		//double >=
		EvalConditionOrder int2Order = new EvalConditionOrder();
		int2Order.setCompareValue("8D");
		int2Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8D ;}  }");
		int2Order.setExecutorName("compilerExecutor");
		int2Order.setRequestName("$order");
		int2Order.setSymbol(Symbol.GREATER_EQUAL);
		//double <
		EvalConditionOrder int3Order = new EvalConditionOrder();
		int3Order.setCompareValue("8D");
		int3Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7D ;}  }");
		int3Order.setExecutorName("compilerExecutor");
		int3Order.setRequestName("$order");
		int3Order.setSymbol(Symbol.LESS_THEN);
		//double ==
		EvalConditionOrder int4Order = new EvalConditionOrder();
		int4Order.setCompareValue("8D");
		int4Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8D ;}  }");
		int4Order.setExecutorName("compilerExecutor");
		int4Order.setRequestName("$order");
		int4Order.setSymbol(Symbol.EQUAL);
		//double  <=
		EvalConditionOrder int5Order = new EvalConditionOrder();
		int5Order.setCompareValue("8D");
		int5Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 7D ;}  }");
		int5Order.setExecutorName("compilerExecutor");
		int5Order.setRequestName("$order");
		int5Order.setSymbol(Symbol.LESS_EQUAL);
		//double  ==
		EvalConditionOrder int6Order = new EvalConditionOrder();
		int6Order.setCompareValue("8D");
		int6Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8D;}  }");
		int6Order.setExecutorName("compilerExecutor");
		int6Order.setRequestName("$order");
		int6Order.setSymbol(Symbol.EQUAL);
		//double equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("8D");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return 8D;}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int5Order);
		conditions1.add(int6Order);
		conditions1.add(intOrder);
		conditions1.add(int2Order);
		conditions1.add(int3Order);
		conditions1.add(int4Order);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("sf");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
	}
	
	protected InternalRuleEvent newBaiscTypeCharRuleEvent(String eventName, String description,
															HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("char1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		
		//char  >
		EvalConditionOrder intOrder = new EvalConditionOrder();
		intOrder.setCompareValue("\'D\'");
		intOrder
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'E\' ;}  }");
		intOrder.setExecutorName("compilerExecutor");
		intOrder.setRequestName("$order");
		intOrder.setSymbol(Symbol.GREATER_THEN);
		//char >=
		EvalConditionOrder int2Order = new EvalConditionOrder();
		int2Order.setCompareValue("\'D\'");
		int2Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'D\' ;}  }");
		int2Order.setExecutorName("compilerExecutor");
		int2Order.setRequestName("$order");
		int2Order.setSymbol(Symbol.GREATER_EQUAL);
		//char <
		EvalConditionOrder int3Order = new EvalConditionOrder();
		int3Order.setCompareValue("\'D\'");
		int3Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'C\' ;}  }");
		int3Order.setExecutorName("compilerExecutor");
		int3Order.setRequestName("$order");
		int3Order.setSymbol(Symbol.LESS_THEN);
		//char ==
		EvalConditionOrder int4Order = new EvalConditionOrder();
		int4Order.setCompareValue("\'D\'");
		int4Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'D\' ;}  }");
		int4Order.setExecutorName("compilerExecutor");
		int4Order.setRequestName("$order");
		int4Order.setSymbol(Symbol.EQUAL);
		//char  <=
		EvalConditionOrder int5Order = new EvalConditionOrder();
		int5Order.setCompareValue("\'D\'");
		int5Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'B\' ;}  }");
		int5Order.setExecutorName("compilerExecutor");
		int5Order.setRequestName("$order");
		int5Order.setSymbol(Symbol.LESS_EQUAL);
		//char  ==
		EvalConditionOrder int6Order = new EvalConditionOrder();
		int6Order.setCompareValue("\'D\'");
		int6Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'D\';}  }");
		int6Order.setExecutorName("compilerExecutor");
		int6Order.setRequestName("$order");
		int6Order.setSymbol(Symbol.EQUAL);
		//char equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("\'D\'");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \'D\';}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int5Order);
		conditions1.add(int6Order);
		conditions1.add(intOrder);
		conditions1.add(int2Order);
		conditions1.add(int3Order);
		conditions1.add(int4Order);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("fff");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
	}
	
	protected InternalRuleEvent newBaiscTypeStringRuleEvent(String eventName, String description,
															HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("string2");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		
		//char equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("\"ligen\"");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \"ligen\";}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("sdf");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);

		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));

		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);

		return event;
	}

	protected RulePolicy newAddPolicyFromPolicy(String description) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription("行业线策略");
		rule1Order.setRuleName("行业线策略规则");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");

		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");

		//char equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("true");
		int7Order
				.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return true;}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);

		conditions1.add(objectCondition);
		conditions1.add(int7Order);

		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);

		//- 注册rule1Order
		Rule rule1 = droolsAdmin.register(rule1Order);

		//- 创建策略p1
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();

		p1Order.setDescription("procuctType_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("productType");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();

		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(16);
		attribute1.setDescription("fff");
		relatedRuleAttributes1.add(attribute1);

		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);

		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);

		return policy1;
	}

	protected InternalRuleEvent newBaiscTypeBooleanRuleEvent(String eventName, String description,
																HashMap<String, String> eventContext) {
		//- 创建规则
		RegisterRuleOrder rule1Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions1 = Lists.newArrayList();
		rule1Order.getConditions().addAll(conditions1);
		rule1Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule1Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule1Order.setDescription(description);
		rule1Order.setRuleName("boolean1");
		rule1Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 $order:Order() byte , short ,int ,long , char , double , float ,boolean
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		
		//char equlas
		EvalConditionOrder int7Order = new EvalConditionOrder();
		int7Order.setCompareValue("true");
		int7Order
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return true;}  }");
		int7Order.setExecutorName("compilerExecutor");
		int7Order.setRequestName("$order");
		int7Order.setSymbol(Symbol.EQUAL);
		
		conditions1.add(objectCondition);
		conditions1.add(int7Order);
		
		//- rule1Order 赋值conditions1
		rule1Order.getConditions().addAll(conditions1);
		
		//- 注册rule1Order 
		Rule rule1 = droolsAdmin.register(rule1Order);
		
		//- 创建策略p1 
		RegisterRulePolicyOrder p1Order = new RegisterRulePolicyOrder();
		
		p1Order.setDescription("standard_policy1");
		p1Order.setRiskType("测试风险");
		p1Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes1 = Lists.newArrayList();
		
		//- 绑定规则属性赋值 relatedRuleAttributes2.add(attribute2)
		RelatedRuleAttribute attribute1 = new RelatedRuleAttribute(rule1.getIdentity());
		attribute1.setAsync(true);
		attribute1.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute1.setExpireTime(new Timestamp(new Date().getTime() + 100000000));
		attribute1.setLoop(false);
		attribute1.setSalicence(10);
		attribute1.setDescription("ffff");
		relatedRuleAttributes1.add(attribute1);
		
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p1Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes1);
		
		//- 注册p1，p2
		RulePolicy policy1 = droolsAdmin.register(p1Order);
		RulePolicy policy2 = newAddPolicyFromPolicy("行业线策略");
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy1.getIdentity(), true));
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy2.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
	}
	
	protected InternalRuleEvent newOneRuleEvent(String eventName, String description,
												HashMap<String, String> eventContext) {
		
		//- 创建规则
		RegisterRuleOrder rule2Order = new RegisterRuleOrder();
		ArrayList<ConditionOrder> conditions2 = Lists.newArrayList();
		rule2Order.getConditions().addAll(conditions2);
		rule2Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule2Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule2Order.setDescription(description);
		rule2Order.setRuleName("onerule1");
		rule2Order.setScript("System.out.println(\"Hello world ...\");");
		
		//- 创建条件表达式 conditions1 , conditions2
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		EvalConditionOrder evalCondition = new EvalConditionOrder();
		
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		ObjectConditionOrder.CompareElementOrder element1 = new ObjectConditionOrder.CompareElementOrder();
		element1.setLeftValue("getProductName()");
		element1.setSymbol("==");
		element1.setRightValue("\"jixianenv\"");
		objectCondition.setCompareElementOrders(Lists.newArrayList(element1));
		
		evalCondition.setCompareValue("\"jixianenv111\"");
		evalCondition
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \"jixianenv\" ;}  }");
		evalCondition.setExecutorName("compilerExecutor");
		evalCondition.setRequestName("$order");
		evalCondition.setSymbol(Symbol.NO_EQUAL);
		
		conditions2.add(evalCondition);
		conditions2.add(objectCondition);
		
		//- rule1Order ,rule2Order赋值conditions1，conditions2
		rule2Order.getConditions().addAll(conditions2);
		
		//- 注册rule1Order , rule2Order
		Rule rule2 = droolsAdmin.register(rule2Order);
		
		//- 创建策略p1 ，p2
		RegisterRulePolicyOrder p2Order = new RegisterRulePolicyOrder();
		
		p2Order.setDescription("standard_policy2");
		p2Order.setRiskType("测试风险");
		p2Order.setPolicyFrom("ALL");
		ArrayList<RelatedRuleAttribute> relatedRuleAttributes2 = Lists.newArrayList();
		
		RelatedRuleAttribute attribute2 = new RelatedRuleAttribute(rule2.getIdentity());
		attribute2.setAsync(false);
		attribute2.setEffectiveTime(new Timestamp(new Date().getTime() + 100000));
		attribute2.setLoop(true);
		attribute2.setSalicence(1);
		attribute2.setDescription("fs");
		relatedRuleAttributes2.add(attribute2);
		//- 绑定策略p1-> relatedRuleAttributes1 p2->relatedRuleAttributes2
		p2Order.getRelatedRuleAttributes().addAll(relatedRuleAttributes2);
		
		//- 注册p1，p2
		RulePolicy policy2 = droolsAdmin.register(p2Order);
		
		//- 创建一个标准事件
		RegisterInternalRuleEventOrder internalRuelEventOrder = new RegisterInternalRuleEventOrder();
		internalRuelEventOrder.setEventName(eventName);
		internalRuelEventOrder.setDescription(description);
		internalRuelEventOrder.setEventContext(eventContext);
		internalRuelEventOrder.setEnable(true);
		
		//- 关联策略
		internalRuelEventOrder.getAttributes().add(new RelatedPolicyAttribute(policy2.getIdentity(), true));
		
		InternalRuleEvent event = droolsAdmin.register(internalRuelEventOrder);
		
		return event;
		
	}
	
	protected ModifyRuleOrder modifyRule(long ruleIdentity, long objIdentity, long evalIdentity, String ruleName,
											String description, Map<Long, ModifyRuleOrder.IdentityOptype> deleteIdentity) {
		RegisterRuleOrder rule2Order = new RegisterRuleOrder();

		rule2Order.setGlobals(Lists.newArrayList("java.lang.Object obj", "java.util.ArrayList list"));
		rule2Order.setImports(Lists.newArrayList("com.global.adk.rules.drools.test.Order", "java.util.ArrayList"));
		rule2Order.setDescription(description);
		rule2Order.setRuleName(ruleName);
		rule2Order.setScript("System.out.println(\"Hello world ...\");");
		
		ObjectConditionOrder objectCondition = new ObjectConditionOrder();
		EvalConditionOrder evalCondition = new EvalConditionOrder();
		
		objectCondition.setTypeSimpleName("com.global.adk.rules.drools.test.Order");
		objectCondition.setVariableName("order");
		ObjectConditionOrder.CompareElementOrder element1 = new ObjectConditionOrder.CompareElementOrder();
		element1.setIdentity(2266);
		element1.setLeftValue("getProductName()");
		element1.setSymbol("==");
		element1.setRightValue("\"jixianenv111\"");
		ObjectConditionOrder.CompareElementOrder element2 = new ObjectConditionOrder.CompareElementOrder();
		element2.setLeftValue("price");
		element2.setSymbol("==");
		element2.setRightValue("5000");
		objectCondition.setCompareElementOrders(Lists.newArrayList(element1, element2));
		
		evalCondition.setIdentity(2265);
		evalCondition.setCompareValue("\"jixianenv111\"");
		evalCondition
			.setConditionScript("package com.global; import com.global.adk.rules.drools.ExecutorWrapper;  Calcu extends ExecutorWrapper{  public Object execute(Object[] parameter){return \"jixianenv\" ;}  }");
		evalCondition.setExecutorName("compilerExecutor");
		evalCondition.setRequestName("$order");
		evalCondition.setSymbol(Symbol.EQUAL);

		rule2Order.getConditions().add(objectCondition);
		rule2Order.getConditions().add(evalCondition);


		ModifyRuleOrder modifyRuleOrder = new ModifyRuleOrder();
		modifyRuleOrder.setRuleIdentity(ruleIdentity);
		modifyRuleOrder.setRegisterRuleOrder(rule2Order);
		modifyRuleOrder.setDeleteIdentity(deleteIdentity);
		return modifyRuleOrder;
	}
}
