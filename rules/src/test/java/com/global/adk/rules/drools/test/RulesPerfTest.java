//package com.yiji.adk.rules.drools.test;
///*
// * www.cutebear.com Inc.
// * Copyright (c) 2016 All Rights Reserved
// */
//
///*
// * 修订记录:
// * @author lingxu (e-mail:wjiayin@yiji.com) 2017-01-20 14:10 创建
// *
// */
//
//import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
//import com.carrotsearch.junitbenchmarks.BenchmarkRule;
//import com.yiji.adk.rules.drools.EventRequest;
//import com.yiji.adk.rules.drools.StatefulKnowledgeSessionWrapper;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TestRule;
//
//import java.util.HashMap;
//
///**
// * @author lingxu (e-mail:wjiayin@yiji.com)
// */
//public class RulesPerfTest extends DroolsStandardTestBase {
//
//	@Rule
//	public TestRule benchmarkRun = new BenchmarkRule();
//
////	@Before
//	public void before() {
//
//		jdbcTemplate.update("delete from APP_KIT_INTERNAL_RULE_EVENT");
//		jdbcTemplate.update("delete from app_kit_rule");
//		jdbcTemplate.update("delete from app_kit_rule_policy");
//		jdbcTemplate.update("delete from app_kit_related_rule");
//		jdbcTemplate.update("delete from app_kit_rule_history");
//
//		HashMap<String, String> eventContext = new HashMap<>();
//
//		eventContext.put("k1", "v1");
//		eventContext.put("k2", "v2");
//
//		newBaiscTypeStringRuleEvent("deposit", "充值", eventContext);
//
//	}
//
//	@Test
//	@BenchmarkOptions(benchmarkRounds = 6000, warmupRounds = 1000, concurrency = 64)
//	public void stringTest() {
//
//		EventRequest request = new EventRequest("deposit");
//
//		HashMap<String, String> eventContext = new HashMap<>();
//		eventContext.put("k1", "v1");
//		eventContext.put("k2", "v2");
//		request.setEventContext(eventContext);
//
//		StatefulKnowledgeSessionWrapper session = (StatefulKnowledgeSessionWrapper) droolsProvider
//			.newStatefulSession(request, "ALL");
//
//		session.execute(new Order("jixianenv", 11D));
//	}
//
//}
