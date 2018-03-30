package com.global.adk.rules.drools;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import com.global.adk.rules.drools.module.Description;
import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.impl.KnowledgeBaseImpl;
import org.drools.runtime.*;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItemManager;
import org.drools.runtime.rule.*;
import org.drools.time.SessionClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatefulKnowledgeSessionWrapper implements StatefulKnowledgeSession {
	
	private static final Logger logger = LoggerFactory.getLogger(StatefulKnowledgeSessionWrapper.class);
	
	private StatefulKnowledgeSession session;
	
	private DroolsEngine.DroolsElement ruleElement;
	
	private String description;
	
	public StatefulKnowledgeSessionWrapper(StatefulKnowledgeSession session, DroolsEngine.DroolsElement ruleElement,
											String description) {
		this.ruleElement = ruleElement;
		this.session = session;
		this.description = description;
	}
	
	public int execute(final Object object) {
		
		Integer target = new ExecuteInterceptor<Integer>() {
			
			@Override
			Integer doExecute(StatefulKnowledgeSession session) {
				
				session.insert(object);
				
				return session.fireAllRules();
			}
		}.execute(session);
		
		return target != null ? target : 0;
	}
	
	@Override
	public int fireAllRules() {
		
		Integer target = new ExecuteInterceptor<Integer>() {
			
			@Override
			Integer doExecute(StatefulKnowledgeSession session) {
				
				return session.fireAllRules();
			}
			
		}.execute(session);
		
		return target != null ? target : 0;
	}
	
	@Override
	public int fireAllRules(final int max) {
		
		Integer target = new ExecuteInterceptor<Integer>() {
			
			@Override
			Integer doExecute(StatefulKnowledgeSession session) {
				
				return session.fireAllRules(max);
			}
			
		}.execute(session);
		
		return target != null ? target : 0;
	}
	
	@Override
	public int fireAllRules(final AgendaFilter agendaFilter) {
		
		Integer target = new ExecuteInterceptor<Integer>() {
			
			@Override
			Integer doExecute(StatefulKnowledgeSession session) {
				
				return session.fireAllRules(agendaFilter);
			}
			
		}.execute(session);
		
		return target != null ? target : 0;
	}
	
	@Override
	public int fireAllRules(final AgendaFilter agendaFilter, final int max) {
		
		Integer target = new ExecuteInterceptor<Integer>() {
			
			@Override
			Integer doExecute(StatefulKnowledgeSession session) {
				
				return session.fireAllRules(agendaFilter, max);
			}
			
		}.execute(session);
		
		return target != null ? target : 0;
	}
	
	@Override
	public <T> T execute(final Command<T> command) {
		
		return new ExecuteInterceptor<T>() {
			
			@Override
			T doExecute(StatefulKnowledgeSession session) {
				
				return session.execute(command);
			}
			
		}.execute(session);
		
	}
	
	private abstract class ExecuteInterceptor<T> {
		
		T execute(StatefulKnowledgeSession session) {
			
			DroolsExecuteContext context = new DroolsExecuteContext();
			context.setDescription(description);
			context.setRequest(ruleElement.getRequest());
			
			long begin = System.currentTimeMillis();
			context.setStartTime(new Timestamp(begin));
			
			Exception ex = null;
			
			T target = null;
			try {
				session.insert(context);
				target = doExecute(session);
			} catch (Exception e) {
				ex = e;
				throw e;
			} finally {
				long end = System.currentTimeMillis();
				context.setEndTime(new Timestamp(end));
				context.setElapse(end - begin);
				context.setError(ex);
				context.setExecuted(context.getError() == null);
				logger.info("执行规则=>{},knowledgeBase({})", context.toString(),((KnowledgeBaseImpl)session.getKnowledgeBase()).getRuleBase());
			}
			return target;
		}
		
		abstract T doExecute(StatefulKnowledgeSession session);
	}
	
	//-----------------------------
	//- 以下忽略....
	//-----------------------------
	@Override
	public void fireUntilHalt() {
		
		session.fireUntilHalt();
	}
	
	@Override
	public void fireUntilHalt(AgendaFilter agendaFilter) {
		
		session.fireUntilHalt(agendaFilter);
	}
	
	@Override
	public <T extends SessionClock> T getSessionClock() {
		
		return session.getSessionClock();
	}
	
	@Override
	public void setGlobal(String identifier, Object value) {
		if(!ruleElement.getDescription().getEventRuleType().equals(Description.EventRuleType.DEFAULT)){
			session.setGlobal(identifier, value);
		}
	}
	
	@Override
	public Object getGlobal(String identifier) {
		
		return session.getGlobal(identifier);
	}
	
	@Override
	public Globals getGlobals() {
		return session.getGlobals();
	}
	
	@Override
	public Calendars getCalendars() {
		
		return session.getCalendars();
	}
	
	@Override
	public Environment getEnvironment() {
		
		return session.getEnvironment();
	}
	
	@Override
	public KnowledgeBase getKnowledgeBase() {
		
		return session.getKnowledgeBase();
	}
	
	@Override
	public void registerExitPoint(String name, ExitPoint exitPoint) {
		
		session.registerExitPoint(name, exitPoint);
	}
	
	@Override
	public void unregisterExitPoint(String name) {
		
		session.unregisterExitPoint(name);
	}
	
	@Override
	public void registerChannel(String name, Channel channel) {
		
		session.registerChannel(name, channel);
	}
	
	@Override
	public void unregisterChannel(String name) {
		
		session.unregisterChannel(name);
	}
	
	@Override
	public Map<String, Channel> getChannels() {
		
		return session.getChannels();
	}
	
	@Override
	public KnowledgeSessionConfiguration getSessionConfiguration() {
		
		return session.getSessionConfiguration();
	}
	
	@Override
	public void halt() {
		
		session.halt();
	}
	
	@Override
	public Agenda getAgenda() {
		
		return session.getAgenda();
	}
	
	@Override
	public WorkingMemoryEntryPoint getWorkingMemoryEntryPoint(String name) {
		
		return session.getWorkingMemoryEntryPoint(name);
	}
	
	@Override
	public Collection<? extends WorkingMemoryEntryPoint> getWorkingMemoryEntryPoints() {
		
		return session.getWorkingMemoryEntryPoints();
	}
	
	@Override
	public QueryResults getQueryResults(String query, Object... arguments) {
		
		return session.getQueryResults(query, arguments);
	}
	
	@Override
	public LiveQuery openLiveQuery(String query, Object[] arguments, ViewChangedEventListener listener) {
		
		return session.openLiveQuery(query, arguments, listener);
	}
	
	@Override
	public String getEntryPointId() {
		
		return session.getEntryPointId();
	}
	
	@Override
	public FactHandle insert(Object object) {
		
		return session.insert(object);
	}
	
	@Override
	public void retract(FactHandle handle) {
		
		session.retract(handle);
	}
	
	@Override
	public void update(FactHandle handle, Object object) {
		
		session.update(handle, object);
	}
	
	@Override
	public FactHandle getFactHandle(Object object) {
		
		return session.getFactHandle(object);
	}
	
	@Override
	public Object getObject(FactHandle factHandle) {
		
		return session.getObject(factHandle);
	}
	
	@Override
	public Collection<Object> getObjects() {
		
		return session.getObjects();
	}
	
	@Override
	public Collection<Object> getObjects(ObjectFilter filter) {
		
		return session.getObjects(filter);
	}
	
	@Override
	public <T extends FactHandle> Collection<T> getFactHandles() {
		
		return session.getFactHandles();
	}
	
	@Override
	public <T extends FactHandle> Collection<T> getFactHandles(ObjectFilter filter) {
		
		return session.getFactHandles(filter);
	}
	
	@Override
	public long getFactCount() {
		
		return session.getFactCount();
	}
	
	@Override
	public ProcessInstance startProcess(String processId) {
		
		return session.startProcess(processId);
	}
	
	@Override
	public ProcessInstance startProcess(String processId, Map<String, Object> parameters) {
		
		return session.startProcess(processId, parameters);
	}
	
	@Override
	public ProcessInstance createProcessInstance(String processId, Map<String, Object> parameters) {
		
		return session.createProcessInstance(processId, parameters);
	}
	
	@Override
	public ProcessInstance startProcessInstance(long processInstanceId) {
		
		return session.startProcessInstance(processInstanceId);
	}
	
	@Override
	public void signalEvent(String type, Object event) {
		
		session.signalEvent(type, event);
	}
	
	@Override
	public void signalEvent(String type, Object event, long processInstanceId) {
		
		session.signalEvent(type, event, processInstanceId);
	}
	
	@Override
	public Collection<ProcessInstance> getProcessInstances() {
		
		return session.getProcessInstances();
	}
	
	@Override
	public ProcessInstance getProcessInstance(long processInstanceId) {
		
		return session.getProcessInstance(processInstanceId);
	}
	
	@Override
	public void abortProcessInstance(long processInstanceId) {
		
		session.abortProcessInstance(processInstanceId);
	}
	
	@Override
	public WorkItemManager getWorkItemManager() {
		
		return session.getWorkItemManager();
	}
	
	@Override
	public void addEventListener(WorkingMemoryEventListener listener) {
		
		session.addEventListener(listener);
	}
	
	@Override
	public void removeEventListener(WorkingMemoryEventListener listener) {
		
		session.removeEventListener(listener);
	}
	
	@Override
	public Collection<WorkingMemoryEventListener> getWorkingMemoryEventListeners() {
		
		return session.getWorkingMemoryEventListeners();
	}
	
	@Override
	public void addEventListener(AgendaEventListener listener) {
		
		session.addEventListener(listener);
	}
	
	@Override
	public void removeEventListener(AgendaEventListener listener) {
		
		session.removeEventListener(listener);
	}
	
	@Override
	public Collection<AgendaEventListener> getAgendaEventListeners() {
		
		return session.getAgendaEventListeners();
	}
	
	@Override
	public void addEventListener(ProcessEventListener listener) {
		
		session.addEventListener(listener);
	}
	
	@Override
	public void removeEventListener(ProcessEventListener listener) {
		
		session.removeEventListener(listener);
	}
	
	@Override
	public Collection<ProcessEventListener> getProcessEventListeners() {
		
		return session.getProcessEventListeners();
	}
	
	@Override
	public int getId() {
		
		return session.getId();
	}
	
	@Override
	public void dispose() {
		
		session.dispose();
	}
	
}
