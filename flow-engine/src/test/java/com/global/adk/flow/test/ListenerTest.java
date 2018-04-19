package com.global.adk.flow.test;

import com.google.common.collect.Maps;
import com.global.adk.flow.delegate.ListenerDelegateContext;
import com.global.adk.flow.engine.Execution;
import com.global.adk.flow.module.EventListener;
import com.global.adk.flow.module.Flow;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/11/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/flow.xml" })
public class ListenerTest implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@BeforeClass
	public static void before() {
		System.setProperty("yiji.dubbo.enable", "false");
		System.setProperty("yiji.yedis.enable", "false");
		System.setProperty("yiji.adk.flowengine.enableRetryProvider", "false");
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Test
	public void testListenerRepeated() {
		ListenerDelegateContext listenerDelegateContext = new ListenerDelegateContext(applicationContext);
		
		EventListener eventListener = new EventListener(TestEventListener.class.getName(), "木有");
		
		Flow flow = new Flow();
		flow.setName("TestListenerFlow");
		flow.setVersion(1);
		flow.setLogName("Test");
		flow.addEvent("assemble_repay_file_success");
		flow.addEvent("receive_repay_file_result_success");
		flow.addEvent("assemble_repay_file_unknown");
		listenerDelegateContext.register(flow, eventListener);
		
		Execution execution = new Execution(null, flow, null, Maps.newHashMap());
		
		listenerDelegateContext.action(execution, "assemble_repay_file_unknown");
	}
	
	@Test
	public void testListener() {
		ListenerDelegateContext listenerDelegateContext = new ListenerDelegateContext(applicationContext);
		
		EventListener eventListener = new EventListener(TestEventListener.class.getName(), "木有");
		
		Flow flow = new Flow();
		flow.setName("TestListenerFlow");
		flow.setVersion(1);
		flow.setLogName("Test");
		flow.addEvent("yyy");
		flow.addEvent("xxx");
		flow.addEvent("zzz");
		listenerDelegateContext.register(flow, eventListener);
		
		Execution execution = new Execution(null, flow, null, Maps.newHashMap());
		
		listenerDelegateContext.action(execution, "yyy");
		
		Assert.assertEquals(execution.getAttribute("xxx,yyy,zzz"), "yyy");
		Assert.assertEquals(execution.getAttribute("!xxx,yyy,zzz"), "yyy");
		Assert.assertEquals(execution.getAttribute("yyy"), "yyy");
		Assert.assertEquals(execution.getAttribute("*+2"), "yyy");
		Assert.assertEquals(execution.getAttribute("\\S+1"), "yyy");
		Assert.assertEquals(execution.getAttribute("\\S+3"), "yyy");
		Assert.assertEquals(execution.getAttachment().size(), 6);
	}
	
	@Test
	public void testPriority() {
		ListenerDelegateContext listenerDelegateContext = new ListenerDelegateContext(applicationContext);
		
		EventListener eventListener = new EventListener(TestPatternEventListener.class.getName(), "木有");
		
		Flow flow = new Flow();
		flow.setName("TestListenerFlow");
		flow.setVersion(1);
		flow.setLogName("Test");
		flow.addEvent("yyy");
		flow.addEvent("xxx");
		flow.addEvent("zzz");
		listenerDelegateContext.register(flow, eventListener);
		
		Execution execution = new Execution(null, flow, null, Maps.newHashMap());
		
		listenerDelegateContext.action(execution, "yyy");
		
		Assert.assertEquals(execution.getAttachment().get("\\S+1"), "yyy1");
		Assert.assertEquals(execution.getAttachment().get("\\S+3"), "yyy3");
		Assert.assertEquals(execution.getAttachment().get("*+2"), "yyy2");
		Assert.assertEquals(execution.getAttachment().size(), 3);
	}
	
}
