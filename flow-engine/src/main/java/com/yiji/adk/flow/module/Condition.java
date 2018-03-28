/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.google.common.collect.Maps;
import com.yiji.adk.common.exception.FlowException;
import com.yiji.adk.flow.delegate.InvokeDelegateContext;
import com.yiji.adk.flow.engine.Execution;
import com.yiji.adk.flow.engine.NodeExecution;
import com.yjf.common.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-13 下午3:03<br>
 * @see
 * @since 1.0.0
 */
public class Condition extends AbstractNode {
	
	private ActivityNode activityNode;
	
	@Size(min = 1)
	@Valid
	private Map<String, Transition> transitions = Maps.newHashMap();
	
	private String mvelScript;
	
	public void addTransition(Transition transition) {
		transitions.put(transition.getEvent(), transition);
	}
	
	public Map<String, Transition> getTransitions() {
		return transitions;
	}
	
	public String getMvelScript() {
		return mvelScript;
	}
	
	public void setMvelScript(String mvelScript) {
		this.mvelScript = mvelScript;
	}
	
	public void setActivityNode(ActivityNode activityNode) {
		this.activityNode = activityNode;
	}
	
	@Override
	public void initialize(Flow flow) {
		for (Iterator<Map.Entry<String, Transition>> it = transitions.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Transition> each = it.next();
			each.getValue().initialize(flow);
		}
	}
	
	@Override
	public void execute(Execution execution) {
		NodeExecution nodeExecution = execution.currentNodeExecution();
		Flow flow = execution.getCurrentFlow();
		
		//-1. StartNode、EndNode
		FlowNode currentFlowNode = nodeExecution.currentNode();
		if (currentFlowNode instanceof StartNode) {
			Iterator<Transition> ts = getTransitions().values().iterator();
			if (!ts.hasNext()) {
				throw new FlowException("流程定义Flow={},Version={}开始节点没有配置正常的Transition流转……");
			}
			Transition transition = ts.next();
			transition.execute(execution);
			return;
		}
		
		if (currentFlowNode instanceof EndNode) {
			return;
		}

		//-2. 根据优先级获取decision最终值,优先级：ExecutorResult > ConditionResult > MvelScriptResult
		if (isTerminate(nodeExecution.decision(), execution)) {
			return;
		}
		
		//-3. 查看Condition执行结果
		InvokeDelegateContext invokeDelegateContext = execution.getEngine().getInvokeDelegateContext();
		Flow.Key flowKey = new Flow.Key(flow.getName(), flow.getVersion());
		Object conditionResult = invokeDelegateContext
			.invoke(new Object[] { flowKey, nodeExecution.currentNode().getName(),
									com.yiji.adk.flow.annotation.Condition.class, execution });
		
		if (conditionResult != null && isTerminate(conditionResult.toString(), execution)) {
			return;
		}
		
		//-4. 都木有，就查看一下mvelScript执行结果
		if (StringUtils.isNotBlank(mvelScript)) {
			Object mvelResult = execution.getEngine().getMvelScriptContext()
				.calculate(execution, flowKey, nodeExecution.currentNode().getName());
			//仅仅对toString进行判断
			if (mvelResult == null) {
				return;
			}
			
			if (isTerminate(mvelResult.toString(), execution)) {
				return;
			}
		}

		//-5. 特殊处理只有一个流转事件定义的节点,在所有条件都不满足时，自动流转。
		if(getTransitions().values().size() == 1){
			//-支持只有一个流转事件不存在流转条件时，根据扭转事件发布事件
			Transition transition = getTransitions().values().iterator().next();
			execution.getEngine().getListenerDelegateContext().action(execution, transition.getEvent());//发布事件
			transition.execute(execution);
			return ;
		}


		//此处应当一定存在路由条件，不应当出现无结果的情况，如果一定要停下流程可以根据DecisionCode进行返回
		throw new FlowException(String.format("流程流转出错，不能找到匹配的流转节点Decision=null,Flow=%s,Version=%s,Node=%s没法匹配的流转条件.",
			flow.getName(), flow.getVersion(), execution.currentNodeExecution().currentNode()));
		
	}
	
	private boolean isTerminate(String decision, Execution execution) {
		
		if (StringUtils.isNotBlank(decision)) {
			//DecisionCode.SKIP_DECISION跳开决策使用其他模式，DecisionCode.STOP_DECISION停止决策
			if (decision.equals(DecisionCode.STOP_DECISION)) {
				return Boolean.TRUE;
			}
			
			if (!decision.equals(DecisionCode.SKIP_DECISION)) {
				Transition transition = transitions.get(decision);
				if (transition == null) {
					throw new FlowException(String.format(
						"抉择条件出错Decision=%s不存在对应的Transition定义，流程Flow=%s,Version=%s,NodeName=%s", decision, execution
							.getCurrentFlow().getName(), execution.getCurrentFlow().getVersion(), activityNode
							.getName()));
				}
				execution.getEngine().getListenerDelegateContext().action(execution, decision);//发布事件
				transition.execute(execution);//流转
				//决策完成即退出
				return Boolean.TRUE;
			}
			
		}
		return Boolean.FALSE;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.module.Condition{");
		sb.append("activityStateNode=").append(activityNode.getName());
		sb.append(", mvelScript='").append(mvelScript).append('\'');
		sb.append(", transitions=").append(transitions);
		sb.append('}');
		return sb.toString();
	}

}
