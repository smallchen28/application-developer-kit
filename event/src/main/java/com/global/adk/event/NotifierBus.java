package com.global.adk.event;

import com.global.adk.common.exception.EventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 事件通知总线
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 * @history hasuelee创建于2014年9月30日 下午1:54:07<br>
 */
public class NotifierBus implements Notifier {
	
	private static final Logger logger = LoggerFactory.getLogger(NotifierBus.class);
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private Dispatcher dispacher;
	
	private volatile Map<String, List<SubscriberWrapper>> cache = new ConcurrentHashMap<>();
	
	public NotifierBus() {
		this(null);
	}
	
	public NotifierBus(ThreadPoolTaskExecutor threadPool) {
		Executor executor = threadPool;
		if (executor == null) {
			executor = new Executor() {
				@Override
				public void execute(Runnable command) {
					command.run();
				}
			};
			logger.info("通知总线线程池初始化为空，忽略异步执行..");
		} else {
			logger.info("通知总线初始化完成threadPool[core_size={},keep_alive={},max_size={}]", threadPool.getCorePoolSize(),
				threadPool.getKeepAliveSeconds(), threadPool.getMaxPoolSize());
		}
		dispacher = new Dispatcher(executor);
	}
	
	@Override
	public boolean dispatcher(Object... events) {
		if (!checkEvent(events)) {
			return false;
		}
		try {
			lock.readLock().lock();
			
			String key = getKey(events);
			List<SubscriberWrapper> subscriberWrappers = cache.get(key);
			
			if (subscriberWrappers == null) {
				logger.warn("不存在的事件注册eventTypes = {}", key);
				return false;
			}
			
			for (final SubscriberWrapper subscriberWrapper : subscriberWrappers) {
				dispacher.execute(new Dispatcher.DispatcherTask(subscriberWrapper, events));
			}
			return true;
		} finally {
			lock.readLock().unlock();
		}
		
	}
	
	private boolean checkEvent(Object[] events) {
		boolean flag = true;
		if (events == null || events.length == 0) {
			flag = false;
			logger.warn("请求事件不能为null，如果存在无参事件监听，请使用NoneEvent发布事件.方法调用位置:{}", getMethodLocationInfo());
		} else {
			for (Object event : events) {
				if (event == null || event.getClass().isArray()) {
					flag = false;
					logger.warn("请求事件中不能有为null/数组的.请求对象列表为:{}.方法调用位置:{}", Arrays.toString(events),
						getMethodLocationInfo());
					break;
				}
			}
		}
		return flag;
	}
	
	private String getMethodLocationInfo() {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
		return stackTraceElement.getClassName()+ "#" + stackTraceElement.getMethodName() + ":"
				+ stackTraceElement.getLineNumber();
	}
	
	@Override
	public <L> void register(final L listener) {
		if (listener == null) {
			return;
		}
		proceed(listener, new ProceedCallback() {
			
			@Override
			void doProceed(Class<?>[] eventTypes, Method method) {
				
				SubscriberWrapper subscriber = SubscriberWrapper.newInstance(listener, method);
				
				Subscribe subscribe = method.getAnnotation(Subscribe.class);
				
				subscriber.setAsync(subscribe.isAsync());
				subscriber.setListener(listener);
				subscriber.setPriority(subscribe.priority());
				
				List<SubscriberWrapper> _subscribers = new ArrayList<>();
				_subscribers.add(subscriber);
				
				String key = getKey(eventTypes);
				List<SubscriberWrapper> subscribers = cache.get(key);
				
				if (subscribers != null) {
					subscribers.addAll(_subscribers);
				} else {
					cache.put(key, _subscribers);
				}
				
				Collections.sort(cache.get(key));
			}
		});
	}
	
	@Override
	public <L> void unregister(@Nonnull final L listener) {
		if (listener == null) {
			return;
		}
		proceed(listener, new ProceedCallback() {
			@Override
			void doProceed(Class<?>[] eventTypes, Method method) {
				String key = getKey(eventTypes);
				List<SubscriberWrapper> subscriberWrappers = cache.get(key);
				Iterator<SubscriberWrapper> iterator = subscriberWrappers.iterator();
				while (iterator.hasNext()) {
					SubscriberWrapper subscriberWrapper = iterator.next();
					if (subscriberWrapper.equals(listener)) {
						iterator.remove();
					}
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private <L> void proceed(L listener, ProceedCallback callback) {
		
		try {
			lock.writeLock().lock();
			Class<L> listenerType = (Class<L>) listener.getClass();
			//获取参数类型（事件类型）
			Method[] methods = listenerType.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Subscribe.class)) {
					checkMethod(method);
					Class<?>[] eventTypes = extraEventType(method);
					callback.doProceed(eventTypes, method);
				}
			}
			
		} catch (Exception e) {
			if (e instanceof EventException) {
				throw e;
			}
			throw new EventException(String.format("注册监听器过程中出现错误，target=%s.", listener), e);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	private Class<?>[] extraEventType(Method method) {
		Class<?>[] paramTypes = method.getParameterTypes();
		Class<?>[] eventTypes;
		if (paramTypes.length == 0) {
			eventTypes = new Class[] { NoneEvent.class };
		} else {
			eventTypes = paramTypes;
		}
		eventTypes = typeConvert(eventTypes);
		return eventTypes;
	}
	
	private Class<?>[] checkMethod(Method method) {
		Class<?>[] paramTypes = method.getParameterTypes();
		for (Class<?> paramType : paramTypes) {
			if (paramType.isArray()) {
				throw new EventException(String.format("监听事件不支持数组类型,请使用集合,method=%s",
					method.getDeclaringClass().getName() + "#" + method.getName()));
			}
		}
		Class<?> returnType = method.getReturnType();
		if (returnType != void.class) {
			throw new EventException(String.format("监听事件非法返回值returnType=%s", returnType.getName()));
		}
		return paramTypes;
	}
	
	private Class<?>[] typeConvert(Class<?>[] eventTypes) {
		
		Class<?>[] types = new Class<?>[eventTypes.length];
		
		for (int i = 0, j = types.length; i < j; i++) {
			Class<?> eventType = eventTypes[i];
			if (eventType.isPrimitive()) {
				if (eventType == int.class) {
					types[i] = Integer.class;
				} else if (eventType == byte.class) {
					types[i] = Byte.class;
				} else if (eventType == short.class) {
					types[i] = Short.class;
				} else if (eventType == long.class) {
					types[i] = Long.class;
				} else if (eventType == char.class) {
					types[i] = Character.class;
				} else if (eventType == float.class) {
					types[i] = Float.class;
				} else if (eventType == double.class) {
					types[i] = Double.class;
				} else if (eventType == boolean.class) {
					types[i] = Boolean.class;
				}
			} else {
				types[i] = eventType;
			}
		}
		
		return types;
	}
	
	private String getKey(Class<?>[] eventTypes) {
		
		StringBuilder key = new StringBuilder();
		for (int i = 0, j = eventTypes.length; i < j; i++) {
			if (i != 0) {
				key.append(",");
			}
			key.append(eventTypes[i].getName());
		}
		return key.toString();
	}
	
	private String getKey(Object[] events) {
		StringBuilder key = new StringBuilder();
		for (int i = 0, j = events.length; i < j; i++) {
			if (i != 0) {
				key.append(",");
			}
			key.append(events[i].getClass().getName());
		}
		return key.toString();
	}
	
	private static abstract class ProceedCallback {
		
		abstract void doProceed(Class<?>[] eventTypes, Method method);
	}
	
}
