package com.global.adk.rules.drools;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.global.adk.common.exception.RuleException;
import com.global.adk.rules.drools.module.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.drools.KnowledgeBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 外围规则引擎
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年6月25日
 * @version 1.0.0
 * @see
 */
public class DroolsEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(DroolsEngine.class);
	
	private DroolsProvider droolsProvider;
	
	private Map<String, DynamicConditionExecutor> executors = new ConcurrentHashMap<String, DynamicConditionExecutor>();
	
	private volatile Map<EventRequest, DroolsElement> cache = Maps.newConcurrentMap();
	
	private Monitor monitor;
	
	public DroolsEngine(DroolsProvider droolsProvider) {
		
		if (droolsProvider == null) {
			throw new RuleException("初始化RuleEngine过程中出现错误,RuleContainer为空.....");
		}
		
		this.droolsProvider = droolsProvider;
		
		applyExecutor();
		
		preProcess();
		
		applyMonitor();
	}
	
	public DroolsElement loadDroolsElement(EventRequest request, String policyFrom) {
		
		//- 直接赋值，后续再sql进行特殊处理。
		request.setPolicyFrom(Strings.isNullOrEmpty(policyFrom) ? "ALL" : policyFrom);
		
		DroolsElement element = cache.get(request);
		
		if (element == null) {
			/*到这一步说明未配置该行业线或者具体的事件，查看是否配置了通用策略，如果配置了，就使用通用策略运行*/
			request.setPolicyFrom("ALL");
			element = cache.get(request);
			
			if (element == null) {
				throw new RuleException(String.format("规则事件[%s]不存在或尚未加载", request));
			}
		}
		
		return element;
		
	}
	
	public Map<String, DynamicConditionExecutor> getExecutors() {
		
		return executors;
	}
	
	private DroolsElement createRuleElement(EventRequest request) {
		
		InternalRuleEvent internalRuleEvent = null;
		DroolsElement element = null;
		Description description;
		Set<RelatedRule> rules = new HashSet<>();
		
		try {
			//-1. 归集规则、便于渲染 & 将条件中的EvalCondition进行动态编译。
			internalRuleEvent = droolsProvider.getRuleRepository().active(request);
			List<RulePolicy> policies = internalRuleEvent.getPolicy();
			if (policies != null && policies.size() >= 0) {
				for (RulePolicy policy : policies) {
					List<RelatedRule> relatedRules = policy.getRelatedRules();
					if (relatedRules != null && relatedRules.size() > 0) {
						for (RelatedRule rule : relatedRules) {
							List<Condition> conditions = rule.getConditions();
							for (Condition condition : conditions) {
								condition.refresh(executors);
							}
							rules.add(rule);
						}
					}
				}
			}
			//-2. 对DroolsElement进行赋值，准备渲染工作。
			if (rules.size() == 0) {
				throw new EmptyResultDataAccessException("未找到相关规则定义……", 1);
			}
			
			description = new Description(request + "->" + internalRuleEvent.getDescription(),
				Description.EventRuleType.STANDARD);
			description.setRules(rules);
			
			element = new DroolsElement(request, description, internalRuleEvent.getVersion());
			
		} catch (EmptyResultDataAccessException e) {
			//- 这里并不是为了捕获找不到事件的错误，而是针对没有配置规则的特殊处理，并且需要对开关进行判断。
			//- 关闭时不再进行任何处理，此时element=null。
			if (internalRuleEvent == null) {
				return element;
			}
			description = new Description(request + "(无对应规则，将使用默认规则……)",
				Description.EventRuleType.DEFAULT);
			element = new DroolsElement(request, description, internalRuleEvent.getVersion());
			
			if (logger.isInfoEnabled()) {
				logger.info("根据request=[{}、{}、version={}]加载事件规则失败,使用默认事件,错误描述:{}。",
					request.getEventName(), request.getEventContext(),
					internalRuleEvent.getVersion(), e.getMessage());
			}
		}
		//-4. 开始渲染(默认规则不做单独处理)
		droolsProvider.getRuleTemplate().render(element,
			new ModuleView(internalRuleEvent, executors, droolsProvider.getActionDelay()));
			
		return element;
		
	}
	
	//- 初始化动态执行器groovy、compiler，使用DefaultListableBeanFactory#registerSingleton存在无法实现感知ware接口，使用AutowireCapableBeanFactory#autowire存在无法被其他类注入。
	private void applyExecutor() {
		ApplicationContext applicationContext = droolsProvider.getSpringContext();
		
		if (applicationContext == null) {
			throw new RuleException("初始化RuleEngine过程中出现错误,ApplictionContext为空.....");
		}
		
		AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(
			BeanConfig.class);
			
		configApplicationContext.setParent(applicationContext);
		Map<String, DynamicConditionExecutor> execs = configApplicationContext
			.getBeansOfType(DynamicConditionExecutor.class);
		for (Iterator<Map.Entry<String, DynamicConditionExecutor>> it = execs.entrySet()
			.iterator(); it.hasNext();) {
			Map.Entry<String, DynamicConditionExecutor> entry = it.next();
			entry.getValue().addObserver(droolsProvider.getStatisticRuleObserver());
		}
		
		executors.putAll(execs);
		
	}
	
	//- 加载所有相关事件，并初始化。
	private void preProcess() {
		List<BogusEvent> bogusEvents = droolsProvider.getRuleRepository().loadAllWithEnable();
		
		for (BogusEvent event : bogusEvents) {
			EventRequest req = new EventRequest(event.getEventName());
			Map<String, String> eventContext = JSON.parseObject(event.getEventContext(), Map.class);
			req.setPolicyFrom(event.getPolicyFrom());
			req.setEventContext(eventContext);
			applyKnowledge(req);
		}
		
	}
	
	//- 初始化监听
	private void applyMonitor() {
		
		this.monitor = new Monitor();
		
		droolsProvider.getMessageZKAccessor().getZk().getConnectionStateListenable()
			.addListener(monitor);
	}
	
	private void applyKnowledge(EventRequest req) {
		long startTime = System.currentTimeMillis();
		
		//- 根据请求创建DroolsElement。
		DroolsElement element;
		
		if ((element = createRuleElement(req)) != null) {
			//- 初始化SessionWrapperFactory对应DroolsElement中KnowledgeBase分区
			droolsProvider.getSessionWrapperFactory().applyPartition(element);
			//- put缓存
			cache.put(req, element);
			
			if (logger.isInfoEnabled()) {
				logger.info(
					"创建规则事件完成[eventName:{},policyFrom:{},eventContext:{},version:{}],cost={}",
					req.getEventName(), req.getPolicyFrom(), req.getEventContext(),
					element.getVersion(), System.currentTimeMillis() - startTime);
			}
		}
		
	}
	
	private class Monitor implements Runnable, ConnectionStateListener {
		
		private Semaphore watcher = new Semaphore(1);
		
		@Override
		public void run() {
			while (true) {
				try {
					//-1. 获取资源
					watcher.acquire();
					
					//-2. 避免创建过多的zk节点监听连，对父节点(/config/cache/watcher/validator/${spring.active.profiles})进行监听，使用新增临时节点通知配置模型变更。
					droolsProvider.getMessageZKAccessor().createNodeIfNecessary(() -> {
						NodeCache nodeCache = droolsProvider.getMessageZKAccessor()
							.getCurrentNodeCache();
							
						// 获取zk通知更新cache
						String data = new String(nodeCache.getCurrentData().getData());
						if (MessageZKAccessor.REFRESH_DATA.equals(data)) {
							refresh();
						}
						
						if (logger.isInfoEnabled()) {
							logger.info("监听节点:({})，变更信息:({}),更新缓存……",
								nodeCache.getCurrentData().getPath(), data);
						}
					});
					
					//-3. 执行完成，进入等待唤醒……
					if (logger.isInfoEnabled()) {
						logger.info("创建ZK监听完成，节点：{}", MessageZKAccessor.PATH);
					}
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error("WatchWorker执行出错，准备重试……", e);
					}
					try {
						//-4. 本来可以不用睡的，有点担心代码扯疯、zk出毛病，还是睡3秒。
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException ex) {
						//ignore……
					} finally {
						//-5. 释放资源，重新执行。
						watcher.release();
					}
				}
			}
		}
		
		private void refresh() {
			
			synchronized (monitor) {
				
				//这里暂时先用这种方式处理，后续等笨熊zk组件版本稳定了，在使用zk进行监听变换后再扫描。
				List<BogusEvent> bogusEvents = droolsProvider.getRuleRepository().loadAll();
				
				//正常的遍历检查流程。
				for (BogusEvent event : bogusEvents) {
					EventRequest req = event.toEventRequest();
					DroolsElement element = cache.get(req);
					
					//-1. 从缓存中去除已经关闭的内部事件,不可多次移除，判断用于解决默认规则。
					if (!event.enable && element != null) {
						droolsProvider.getSessionWrapperFactory()
							.removePartition(cache.remove(req));
						if (logger.isInfoEnabled()) {
							logger.info(
								"规则事件eventName={},policyFrom={},eventContext={},version={}失效，系统已移除……",
								event.getEventName(), event.getPolicyFrom(),
								event.getEventContext(), event.getVersion());
						}
						continue;
					}
					
					//-2. 处理不存在缓存对象的情况。
					// 存在事件定义，但是没有对应规则，在跑默认规则的情况，此时内存中的版本与数据库一致,在没有变更版本的情况下(事件没有增加规则),则不应当重新加载。
					// 没有事件定义已经在跑默认规则的情况，此时默认内存中的版本为Integer.MIN_VALUE，在没加载到数据库中的事件定义下无需关注，加载到对应的定义则版本的不一致将执行加载过程。
					if (event.enable
						&& (element == null || (element.getVersion() != event.getVersion()))) {
						applyKnowledge(req);
					}
				}
				
				//-3. 移除数据库中根本不存在的缓存(理论上不存在这种情况，但是测试用例会有这种情况发生。)
				for (Iterator<Map.Entry<EventRequest, DroolsElement>> it = cache.entrySet()
					.iterator(); it.hasNext();) {
					Map.Entry<EventRequest, DroolsElement> each = it.next();
					EventRequest cacheKey = each.getKey();
					DroolsElement element = each.getValue();
					
					boolean defineIsExists = false;
					for (BogusEvent event : bogusEvents) {
						if (cacheKey.equals(event.toEventRequest())) {
							defineIsExists = true;
							break;
						}
					}
					
					if (!defineIsExists) {
						it.remove();
						droolsProvider.getSessionWrapperFactory().removePartition(element);
						if (logger.isInfoEnabled()) {
							logger.info(
								"不存在的规则事件定义eventName={},policyFrom={},eventContext={}，系统已移除……",
								cacheKey.getEventName(), cacheKey.getPolicyFrom(),
								cacheKey.getEventContext());
						}
					}
				}
				
			}
		}
		
		@Override
		public void stateChanged(CuratorFramework client, ConnectionState newState) {
			
			String conn = client.getZookeeperClient().getCurrentConnectionString();
			
			switch (newState) {
				case LOST:
					
					if (logger.isInfoEnabled()) { //本业务场景无需监听lost事件
						logger.info("ZK({})连接丢失(session expire[LOST]),开始阻塞循环等待连接。", conn);
					}
					
					while (true) {
						try {
							if (!client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
								if (logger.isInfoEnabled()) {
									logger.info("zk({})断线重连成功,发布ZKLostConnectedEvent事件……", conn);
								}
								break;
							}
						} catch (InterruptedException e) {
							if (logger.isErrorEnabled()) {
								logger.warn("等待zk({})重连过程出现意外错误，继续重试……", conn, e);
							}
						}
					}
					break;
				case CONNECTED:
					
					if (logger.isInfoEnabled()) {
						logger.info("ZK({})连接成功(第一次连接[Connected])。", conn);
					}
					
					Thread thread = new Thread(monitor);
					thread.setName("ADK-RULE-MONITOR");
					thread.setDaemon(true);
					thread.start();
					
					break;
				case RECONNECTED:
					
					if (logger.isInfoEnabled()) {
						logger.info("ZK({})断线重连成功(Reconnected)。", conn);
					}
					
					//释放资源
					watcher.release();
					//刷新缓存
					refresh();
					
					break;
			}
		}
		
	}
	
	public static class BogusEvent {
		private String eventName;
		private String eventContext;
		private String policyFrom;
		private boolean enable;
		private int version;
		
		public BogusEvent(	String policyFrom, String eventName, String eventContext, int version,
							boolean enable) {
			this.eventContext = eventContext;
			this.eventName = eventName;
			this.policyFrom = policyFrom;
			this.version = version;
			this.enable = enable;
		}
		
		public EventRequest toEventRequest() {
			EventRequest req = new EventRequest(getEventName());
			Map<String, String> eventContext = JSON.parseObject(getEventContext(), Map.class);
			req.setPolicyFrom(getPolicyFrom());
			req.setEventContext(eventContext);
			return req;
		}
		
		public String getEventName() {
			return eventName;
		}
		
		public void setEventName(String eventName) {
			this.eventName = eventName;
		}
		
		public boolean isEnable() {
			return enable;
		}
		
		public void setEnable(boolean enable) {
			this.enable = enable;
		}
		
		public String getEventContext() {
			return eventContext;
		}
		
		public void setEventContext(String eventContext) {
			this.eventContext = eventContext;
		}
		
		public String getPolicyFrom() {
			return policyFrom;
		}
		
		public void setPolicyFrom(String policyFrom) {
			this.policyFrom = policyFrom;
		}
		
		public int getVersion() {
			return version;
		}
		
		public void setVersion(int version) {
			this.version = version;
		}
	}
	
	public static class DroolsElement {
		
		private EventRequest request;
		
		private Description description;
		
		private long version;
		
		private KnowledgeBase knowledgeBase;
		
		public DroolsElement() {
		
		}
		
		public DroolsElement(EventRequest request, Description description, long version) {
			
			this.request = request;
			this.description = description;
			this.version = version;
		}
		
		public long getVersion() {
			
			return version;
		}
		
		public void setVersion(long version) {
			
			this.version = version;
		}
		
		public KnowledgeBase getKnowledgeBase() {
			
			return knowledgeBase;
		}
		
		public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
			
			this.knowledgeBase = knowledgeBase;
		}
		
		public Description getDescription() {
			
			return description;
		}
		
		public void setDescription(Description description) {
			
			this.description = description;
		}
		
		public EventRequest getRequest() {
			
			return request;
		}
		
		public void setRequest(EventRequest request) {
			
			this.request = request;
		}
		
		@Override
		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			builder.append("DroolsElement [request=");
			builder.append(request);
			builder.append(", description=");
			builder.append(description);
			builder.append(", version=");
			builder.append(version);
			builder.append(", knowledgeBase=");
			builder.append(knowledgeBase);
			builder.append("]");
			return builder.toString();
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof DroolsElement))
				return false;
			DroolsElement that = (DroolsElement) o;
			return version == that.version&& Objects.equals(request, that.request)
					&& Objects.equals(knowledgeBase, that.knowledgeBase);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(request, version, knowledgeBase);
		}
	}
}
