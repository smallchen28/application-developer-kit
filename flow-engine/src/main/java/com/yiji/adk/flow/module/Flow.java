/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiji.adk.common.exception.FlowException;
import com.yiji.adk.flow.engine.Execution;
import com.yjf.common.lang.validator.YJFValidatorFactory;
import com.yjf.common.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程定义，利用Execution来驱动实例，但不独立定义实例……
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-13 下午3:00<br>
 * @see
 * @since 1.0.0
 */
public class Flow extends FlowNode {
	
	private String description;
	
	private String logName;
	
	@Min(value = 1)
	@Max(Integer.MAX_VALUE)
	private int version;
	
	@NotNull
	@Valid
	private StartNode startNode;
	
	@NotNull
	@Valid
	private EndNode endNode;
	
	@Valid
	private List<ActivityNode> nodes = Lists.newArrayList();
	private Map<String, ActivityNode> nodeMap = Maps.newHashMap();
	
	@Valid
	private ErrorMonitor errorMonitor;
	
	private EventListeners eventListeners;
	
	@Size(min = 1)
	private List<String> events = Lists.newArrayList();
	
	@Override
	public void initialize(Flow flow) {
		startNode.initialize(this);
	}
	
	@Override
	public void execute(Execution execution) {
		this.startNode.execute(execution);
	}
	
	@Override
	public void validate() {
		Set<ConstraintViolation<AbstractNode>> constraintViolations = YJFValidatorFactory.INSTANCE.getValidator()
			.validate(this);
		//- jsr303检查
		if (constraintViolations != null && !constraintViolations.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("Flow定义检查出错，name=").append(getName()).append(",version=").append(version).append(",");
			
			for (ConstraintViolation<AbstractNode> constraintViolation : constraintViolations) {
				errorMessage.append("错误信息：").append(constraintViolation.getPropertyPath().toString()).append("->")
					.append(constraintViolation.getMessage()).append(",节点信息：")
					.append(constraintViolation.getLeafBean().toString()).append(",");
			}
			
			throw new FlowException(errorMessage.substring(0, errorMessage.length() - 1));
		}
	}
	
	/**
	 * 通过名字获取节点
	 * @param name 名字
	 * @return 节点
	 */
	public ActivityNode nodeByName(String name) {
		return nodeMap.get(name);
	}
	
	/**
	 * 获取最新当前添加节点
	 * @return 最新当前添加节点
	 */
	public ActivityNode lastAddedNode() {
		return getNodes().get(getNodes().size() - 1);
	}
	
	public boolean notExistNode(String node) {
		return null == node || null == nodeByName(node) && notStartAndEndNode(node);
	}
	
	private boolean notStartAndEndNode(String node) {
		return null == node
				|| StringUtils.notEquals(node, startNode.getName()) && StringUtils.notEquals(node, endNode.getName());
				
	}
	
	public List<String> getEvents() {
		return events;
	}
	
	public EndNode getEndNode() {
		return endNode;
	}
	
	public void setEndNode(EndNode endNode) {
		this.endNode = endNode;
	}
	
	public StartNode getStartNode() {
		return startNode;
	}
	
	public void setStartNode(StartNode startNode) {
		this.startNode = startNode;
	}
	
	public void addNode(ActivityNode node) {
		this.nodes.add(node);
		nodeMap.put(node.getName(), node);
	}
	
	public List<ActivityNode> getNodes() {
		return nodes;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getVersion() {
		return version;
	}
	
	public ErrorMonitor getErrorMonitor() {
		return errorMonitor;
	}
	
	public void setErrorMonitor(ErrorMonitor errorMonitor) {
		this.errorMonitor = errorMonitor;
	}
	
	public EventListeners getEventListeners() {
		return eventListeners;
	}
	
	public void setEventListeners(EventListeners eventListeners) {
		this.eventListeners = eventListeners;
	}
	
	public void addEvent(String event) {
		//		if(events.contains(event)){
		//			throw new FlowException(String.format("flow=%s，version=%s流程定义重复的事件名称event=%s",getName(),getVersion(),event));
		//		}//暂时允许重复定义，先看看效果再说。
		this.events.add(event);
	}
	
	public String getLogName() {
		return logName;
	}
	
	public void setLogName(String logName) {
		this.logName = logName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static class Key {
		
		private String flowName;
		
		private int version;
		
		public Key(String flowName, int version) {
			this.flowName = flowName;
			this.version = version;
		}
		
		public String getFlowName() {
			return flowName;
		}
		
		public int getVersion() {
			return version;
		}
		
		public void setVersion(int version) {
			this.version = version;
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
			if (flowName != null ? !flowName.equals(key.flowName) : key.flowName != null) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public int hashCode() {
			int result = flowName != null ? flowName.hashCode() : 0;
			result = 31 * result + version;
			return result;
		}
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("com.yiji.adk.flow.module.FLow{");
		sb.append("name=").append(getName());
		sb.append(", version=").append(version);
		sb.append(", description =").append(description);
		sb.append(",triggerClass=").append(getTriggerClass());
		sb.append(",endNode=").append(endNode);
		sb.append(", eventListeners=").append(eventListeners);
		sb.append(", errorMonitor=").append(errorMonitor);
		sb.append(", nodes=").append(nodes);
		sb.append(", startNode=").append(startNode);
		sb.append(", logName=").append(logName);
		sb.append('}');
		return sb.toString();
	}
}
