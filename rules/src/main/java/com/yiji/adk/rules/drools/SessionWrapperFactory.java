package com.yiji.adk.rules.drools;

import com.google.common.collect.Maps;
import com.yiji.adk.common.exception.RuleException;
import com.yiji.adk.rules.drools.module.Description;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.drools.KnowledgeBase;
import org.drools.impl.KnowledgeBaseImpl;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.impl.StatelessKnowledgeSessionImpl;

import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * 由于drools存在基于KnowledgeBase的全局锁。 1. 创建session、session、insert、session.execute都将进行读写锁抢占。 2. 其中session.execute为写锁。
 *
 * 这里根据将根据配置，为每个事件创建一个或多个knowledgebase，进行分区(partition)，线程通过String#hashCode%partitionSize进行命中，起到隔离的作用。
 * @author hasulee<ligen@yiji.com>
 * @version 1.0.0
 * @see
 * @since 17/1/9
 */
public class SessionWrapperFactory {

	private static final Logger logger = LoggerFactory.getLogger(SessionWrapperFactory.class) ;

	private Map<ElementKey,KnowledgeBase[]> partitions = Maps.newConcurrentMap();

	private int partitionSize ;

	public SessionWrapperFactory(int partitionSize ){
		if (partitionSize < 1) {
			throw new RuleException("SessionWrapperFactory#patitionSize必须大于等于1");
		}

		this.partitionSize = partitionSize;
	}

	protected StatefulKnowledgeSessionWrapper newStatefulKnowledgeSessionWrapper(DroolsEngine.DroolsElement ruleElement) {
		long start = System.currentTimeMillis() ;

		int hash = 0 ;
		//特殊处理使用默认规则的事件。
		if(!ruleElement.getDescription().getEventRuleType().equals(Description.EventRuleType.DEFAULT)){
			hash = hash(Thread.currentThread().getName());
		}

		KnowledgeBase knowledgeBase = getKnowledgeBase(ruleElement,hash) ;

		StatefulKnowledgeSessionImpl session = (StatefulKnowledgeSessionImpl) knowledgeBase
			.newStatefulKnowledgeSession();

		if(logger.isInfoEnabled()){
			logger.info("线程({})根据knowledgebase({})创建session成功，耗时：{}",hash,((KnowledgeBaseImpl)session.getKnowledgeBase()).getRuleBase(),System.currentTimeMillis()- start);
		}

		return new StatefulKnowledgeSessionWrapper(session, ruleElement, ruleElement.getDescription().toMessageStream());
	}

	protected StatelessKnowledgeSessionWrapper newStatelessKnowledgeSessionWrapper(DroolsEngine.DroolsElement ruleElement) {
		long start = System.currentTimeMillis() ;

		int hash = 0 ;
		//特殊处理使用默认规则的事件。
		if(!ruleElement.getDescription().getEventRuleType().equals(Description.EventRuleType.DEFAULT)){
			hash = hash(Thread.currentThread().getName());
		}

		KnowledgeBase knowledgeBase = getKnowledgeBase(ruleElement,hash);

		StatelessKnowledgeSessionImpl session = (StatelessKnowledgeSessionImpl) knowledgeBase
				.newStatelessKnowledgeSession();

		if(logger.isInfoEnabled()){
			logger.info("线程({})根据knowledgebase({})创建session成功，耗时：{}",hash,((KnowledgeBaseImpl)knowledgeBase).getRuleBase(),System.currentTimeMillis()- start);
		}

		return new StatelessKnowledgeSessionWrapper(session,ruleElement,ruleElement.getDescription().toMessageStream());
	}

	protected void applyPartition(DroolsEngine.DroolsElement element){
		//-根据事件、上下文、版本查找对应分区，并提前创建。
		SessionWrapperFactory.ElementKey key = new SessionWrapperFactory.ElementKey(element.getRequest().getEventName(), element.getRequest().getPolicyFrom(),
				element.getRequest().getEventContext());

		int size = 1 ;
		if(!element.getDescription().getEventRuleType().equals(Description.EventRuleType.DEFAULT)){
			size = partitionSize;
		}
		partitions.put(key,createKnowledgeBases(size,element.getKnowledgeBase()));
	}

	protected void removePartition(DroolsEngine.DroolsElement element){
		if(element != null){
			partitions.remove(new SessionWrapperFactory.ElementKey(element.getRequest().getEventName(),element.getRequest().getPolicyFrom(),
					element.getRequest().getEventContext()));
		}
	}


	private KnowledgeBase[] createKnowledgeBases(int partitionSize, KnowledgeBase prototype) {

		KnowledgeBase[] partition = new KnowledgeBase[partitionSize] ;

		for(int i = 0 , j = partition.length ; i < j ; i++){
			ObjectOutputStream out = null;
			ObjectInputStream in = null;

			try {
				ByteArrayOutputStream objectData = new ByteArrayOutputStream();
				out = new ObjectOutputStream(objectData);

				out.writeObject(prototype);

				in = new ObjectInputStream(new ByteArrayInputStream(objectData.toByteArray()));

				partition[i]= (KnowledgeBase) in.readObject();

			} catch (IOException |ClassNotFoundException e) {
				throw new RuleException(e.getMessage());
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					//ignore
				} finally {
					if(in != null){
						try {
							in.close();
						} catch (IOException e) {
							//ignore
						}
					}
				}
			}
		}
		return partition;
	}
	
	private KnowledgeBase getKnowledgeBase(DroolsEngine.DroolsElement ruleElement , int hash) {
		ElementKey key = new ElementKey(ruleElement.getRequest().getEventName(), ruleElement.getRequest().getPolicyFrom(),
				ruleElement.getRequest().getEventContext());
		return partitions.get(key)[hash];
	}

	//简单测试了一下对于类似DubboHandler-Thread-X、RocketMQ-Thread-X类似的线程名称，采用String#hashCode分布基本均匀。
	private int hash(String key) {
		return Math.abs(key.hashCode()) % partitionSize;
	}


	public static class ElementKey {
		private String eventName;
		private String policyFrom;
		private Map<String, String> eventContext;

		public ElementKey(String eventName, String policyFrom, Map<String, String> eventContext) {
			this.eventName = eventName;
			this.policyFrom = policyFrom;
			this.eventContext = eventContext;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ElementKey that = (ElementKey) o;
			return Objects.equals(eventName, that.eventName) &&
					Objects.equals(policyFrom, that.policyFrom) &&
					Objects.equals(eventContext, that.eventContext);
		}

		@Override
		public int hashCode() {
			return Objects.hash(eventName, policyFrom, eventContext);
		}

		@Override
		public String toString() {
			return "ElementKey{" +
					"eventName='" + eventName + '\'' +
					", policyFrom='" + policyFrom + '\'' +
					", eventContext=" + eventContext +
					'}';
		}
	}
	
}