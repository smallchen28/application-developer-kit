/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.rules.drools;

import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.exception.RuleException;
import com.global.adk.rules.drools.module.Condition;
import com.global.adk.rules.drools.module.EvalCondition;
import com.global.adk.rules.drools.module.Symbol;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态条件JAVA版实现，编译器：JANINO
 *
 * @author hasulee
 * @since 2014年7月10日
 * @version 1.0.0
 *
 * @see com.global.adk.rules.drools.DynamicConditionExecutor
 * @see com.global.adk.rules.drools.GroovyDynamicExecutor
 */
public class CompilerDynamicExecutor extends DynamicConditionExecutor {
	
	public static final Pattern classPattern = Pattern.compile("\\w*\\s*\\w+\\s+extends\\s+ExecutorWrapper");
	
	public static final Pattern packageName = Pattern.compile("package\\s+[a-zA-Z]+(\\.[a-zA-Z]+)+;");
	
	public static final String PACKAGE_NAME = "package com.global.adk.rules.drools.compiler;";
	
	public static final String CLASS_NAME_PRE = "com.global.adk.rules.drools.compiler";
	
	/** Wrapper缓存 <code>cache</code> */
	private Map<EvalCondition, ExecutorWrapper> cache = new ConcurrentHashMap<>();

	@Override
	public ExecutorWrapper generateExecutorWrapper(Condition evalCondition) {

		ExecutorWrapper wrapper = cache.get(evalCondition);

		if(wrapper != null){
			//根据所有内容进行判断，是否发生变化，避免重复加载，导致permgen溢出。
			for(Iterator<EvalCondition> it = cache.keySet().iterator() ; it.hasNext();){
				EvalCondition target = it.next();
				//只要出现完全重合则表示已加载，直接返回即可，无需刷新缓存。
				if(target.eq(evalCondition)){
					return wrapper;
				}
			}
		}

		wrapper = newInstance(((EvalCondition) evalCondition).getConditionScript()) ;
		cache.put((EvalCondition) evalCondition,wrapper) ;

		return wrapper;

	}

	@Override
	public boolean execute(long identity, Object compareValue, String symbol, Object... parameters) {

		EvalCondition condition = new EvalCondition();
		condition.setIdentity(identity);

		ExecutorWrapper wrapper = cache.get(condition);

		long startTime = System.currentTimeMillis();
		Object result = wrapper.execute(parameters);

		boolean rs = eval(result, Symbol.code(symbol), compareValue);

		certEvalCaptrueInfo(identity, result, compareValue, symbol.toString(), rs, System.currentTimeMillis() - startTime, parameters);
		return rs;

	}
	
	private void certEvalCaptrueInfo(long evalId, Object leftValue, Object rightValue, String symbol, boolean result,
										long executorTime, Object[] parameter) {
		EvalCaptrueInfo evalGrabInfo = new EvalCaptrueInfo();
		evalGrabInfo.setEvalId(evalId);
		String left = String.format("%s=(类型=%s)", leftValue, null != leftValue ? leftValue.getClass() : null);
		evalGrabInfo.setLeftValue(left);
		String right = String.format("%s=(类型=%s)", rightValue, null != rightValue ? rightValue.getClass() : null);
		evalGrabInfo.setRightValue(right);
		evalGrabInfo.setSymbol(symbol);
		evalGrabInfo.setResult(String.valueOf(result));
		evalGrabInfo.setExecutorTime(Long.valueOf(executorTime).intValue());
		evalGrabInfo.setParameter(parameter);
		setChanged();
		notifyObservers(evalGrabInfo);
	}
	
	private boolean eval(Object result, Symbol code, Object compareValue) {
		return code.caculate(result, compareValue);
	}
	
	private ExecutorWrapper newInstance(String conditionScript) {
		
		try {
			
			ClassInfo info = replaceClassName(conditionScript);
			
			if (logger.isInfoEnabled()) {
				logger.info("规则条件编译{}", info.toString());
			}
			
			ExecutorWrapper wrapper = (ExecutorWrapper) com.global.adk.common.compiler.Compiler.getInstance()
				.compilerJavaCode(info.className, info.javaScript).newInstance();
			
			AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
			
			factory.autowireBeanProperties(wrapper, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
			
			factory.initializeBean(wrapper, info.className.toLowerCase());
			
			return wrapper;
			
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(String.format("动态编译出错 script=%s", conditionScript), e);
		}
		
	}
	
	private ClassInfo replaceClassName(String conditionScript) {
		
		ClassInfo info = new ClassInfo();
		
		Matcher packageNameMatcher = packageName.matcher(conditionScript);
		
		if (packageNameMatcher.find()) {
			//有package直接替换掉,没有就出错。
			conditionScript = packageNameMatcher.replaceFirst(PACKAGE_NAME);
			
			Matcher classMatcher = classPattern.matcher(conditionScript);
			
			if (classMatcher.find()) {
				
				String classSimpleName = getClassName();
				
				StringBuilder className = new StringBuilder();
				
				className.append(CLASS_NAME_PRE).append(".").append(classSimpleName);
				info.className = className.toString();
				
				info.javaScript = classMatcher.replaceFirst("public class " + classSimpleName
															+ " extends ExecutorWrapper");
				return info;
			}
		}
		
		throw new RuleException(String.format("非法的类名称....script=%s", conditionScript));
	}
	
	private String getClassName() {
		
		StringBuilder classSimpleName = new StringBuilder();
		
		classSimpleName.append(Compiler.PROXY_PREFIX).append(Compiler.PID).append("ExecutorWrapper").append(counter());
		
		return classSimpleName.toString();
		
	}
	
	private static String counter() {
		
		return String.valueOf(Compiler.counter());
	}
	
	public class ClassInfo {
		
		String className;
		
		String javaScript;
		
		@Override
		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			
			builder.append(javaScript).append("\n");
			
			return builder.toString();
		}
		
	}
	
}
