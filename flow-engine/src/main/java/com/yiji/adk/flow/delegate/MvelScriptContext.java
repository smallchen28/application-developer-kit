/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.delegate;

import com.google.common.collect.Maps;
import com.yiji.adk.common.exception.FlowException;
import com.yiji.adk.common.log.TraceLogFactory;
import com.yiji.adk.flow.engine.Execution;
import com.yiji.adk.flow.module.Flow;
import com.yjf.common.log.Logger;
import com.yjf.common.util.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-19 下午5:57<br>
 * @see
 * @since 1.0.0
 */
public class MvelScriptContext implements MvelScript, InvokeSupport {
	
	private final ApplicationContext applicationContext;
	
	private Map<Key, String> scripts = Maps.newHashMap();
	
	public MvelScriptContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public String calculate(Execution execution, Flow.Key key, String nodeName) {
		Object result = null;
		
		Key mKey = new Key(key.getFlowName(), key.getVersion(), nodeName);
		
		String script = scripts.get(mKey);
		
		if (script != null) {
			
			try {
				CompiledExpression compiled = (CompiledExpression) MVEL.compileExpression(script);
				
				Map vars = new HashMap();
				vars.put("execution", execution);
				
				result = MVEL.executeExpression(compiled, vars);
				
				if (result instanceof String) {
					return result.toString();
				}
				
				throw new FlowException(String.format("状态机【StateMachine=%s,Version=%s,Node=%s】执行脚本【script=%s】无返回值",
					key.getFlowName(), key.getVersion(), nodeName, script));
			} finally {
				Logger logger = TraceLogFactory.getLogger(execution.getCurrentFlow().getLogName());
				if (logger.isDebugEnabled()) {
					logger.debug("节点：{}进行脚本决策\nMVEL->{}\n入参->execution={}\n决策结果->{}", execution.currentNodeExecution()
						.currentNode().getName(), script, execution, result);
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void proceed(Flow.Key flowKey, String nodeName, String target) {
		if (StringUtils.isNotBlank(target)) {
			String stateMachine = flowKey.getFlowName();
			int version = flowKey.getVersion();
			
			Key mKey = new Key(stateMachine, version, nodeName);
			
			scripts.put(mKey, target);
		}
	}
	
	public static class Key {
		
		private String stateMachineName;
		
		private int version;
		
		private String nodeName;
		
		public Key(String stateMachineName, int version, String nodeName) {
			this.stateMachineName = stateMachineName;
			this.version = version;
			this.nodeName = nodeName;
		}
		
		public String getStateMachineName() {
			return stateMachineName;
		}
		
		public int getVersion() {
			return version;
		}
		
		public String getNodeName() {
			return nodeName;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			
			Key key = (Key) o;
			
			if (version != key.version) {
				return false;
			}
			
			if (nodeName != null ? !nodeName.equals(key.nodeName) : key.nodeName != null) {
				return false;
			}
			
			if (stateMachineName != null ? !stateMachineName.equals(key.stateMachineName)
				: key.stateMachineName != null) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public int hashCode() {
			int result = stateMachineName != null ? stateMachineName.hashCode() : 0;
			result = 31 * result + version;
			result = 31 * result + (nodeName != null ? nodeName.hashCode() : 0);
			return result;
		}
	}
}
