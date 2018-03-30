package com.global.adk.rules.drools;

import com.global.adk.rules.drools.module.DroolsRepository;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 规则容器
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年6月25日
 * @version 1.0.0
 * @see
 */
public class DroolsProvider implements ApplicationContextAware, InitializingBean {
	
	private long actionDelay = 0L;
	
	private DroolsEngine droolsEngine;
	
	private DroolsRepository ruleRepository;
	
	private DroolsTemplate ruleTemplate;
	
	private SessionWrapperFactory sessionWrapperFactory;
	
	private ApplicationContext applicationContext;
	
	private StatisticRuleObserver statisticRuleObserver;
	
	private MessageZKAccessor messageZKAccessor;
	
	public DroolsProvider(	DroolsRepository ruleRepository, DroolsTemplate droolsTemplate,
							SessionWrapperFactory sessionWrapperFactory) {
							
		this.ruleRepository = ruleRepository;
		
		this.ruleTemplate = droolsTemplate;
		
		this.sessionWrapperFactory = sessionWrapperFactory;

	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	protected ApplicationContext getSpringContext() {
		return applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		droolsEngine = new DroolsEngine(this);
		
	}
	
	public StatefulKnowledgeSession newStatefulSession(EventRequest request, String policyFrom) {
		
		DroolsEngine.DroolsElement ruleElement = droolsEngine.loadDroolsElement(request,
			policyFrom);
			
		StatefulKnowledgeSessionWrapper wrapper = sessionWrapperFactory
			.newStatefulKnowledgeSessionWrapper(ruleElement);
			
		for (Iterator<Entry<String, DynamicConditionExecutor>> it = droolsEngine.getExecutors()
			.entrySet().iterator(); it.hasNext();) {
			Entry<String, DynamicConditionExecutor> entry = it.next();
			wrapper.setGlobal(entry.getKey(), entry.getValue());
		}
		
		return wrapper;
		
	}
	
	public StatelessKnowledgeSession newStatelessSession(EventRequest request, String policyFrom) {
		
		DroolsEngine.DroolsElement ruleElement = droolsEngine.loadDroolsElement(request,
			policyFrom);
			
		StatelessKnowledgeSessionWrapper wrapper = sessionWrapperFactory
			.newStatelessKnowledgeSessionWrapper(ruleElement);
			
		for (Iterator<Entry<String, DynamicConditionExecutor>> it = droolsEngine.getExecutors()
			.entrySet().iterator(); it.hasNext();) {
			Entry<String, DynamicConditionExecutor> entry = it.next();
			wrapper.setGlobal(entry.getKey(), entry.getValue());
		}
		
		return wrapper;
	}
	
	public DroolsEngine getDroolsEngine() {
		
		return droolsEngine;
	}
	
	public long getActionDelay() {
		
		return actionDelay;
	}
	
	public void setActionDelay(long actionDelay) {
		
		this.actionDelay = actionDelay;
	}
	
	public DroolsRepository getRuleRepository() {
		
		return ruleRepository;
	}
	
	public DroolsTemplate getRuleTemplate() {
		
		return ruleTemplate;
	}
	
	public StatisticRuleObserver getStatisticRuleObserver() {
		
		return statisticRuleObserver;
	}
	
	public void setStatisticRuleObserver(StatisticRuleObserver statisticRuleObserver) {
		this.statisticRuleObserver = statisticRuleObserver;
	}
	
	public SessionWrapperFactory getSessionWrapperFactory() {
		return sessionWrapperFactory;
	}

	public MessageZKAccessor getMessageZKAccessor() {
		return messageZKAccessor;
	}

	public void setMessageZKAccessor(MessageZKAccessor messageZKAccessor) {
		this.messageZKAccessor = messageZKAccessor;
	}
}
