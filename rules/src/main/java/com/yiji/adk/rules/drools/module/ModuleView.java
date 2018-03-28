package com.yiji.adk.rules.drools.module;

import com.yiji.adk.common.Constants;
import com.yiji.adk.rules.drools.DynamicConditionExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleView {
	
	private InternalRuleEvent ruleEvent;
	
	private Map<String, DynamicConditionExecutor> executorMapper;
	
	private Set<RelatedRule> rules = new HashSet<>();
	
	private Set<String> imports = new HashSet<>();
	
	private Set<String> globals = new HashSet<>();
	
	private long delay = Constants.DROOLS_DEFAULT_DELAY_SENCD;
	
	public ModuleView(InternalRuleEvent ruleEvent, Map<String, DynamicConditionExecutor> executorMapper, long delay) {
		
		this.ruleEvent = ruleEvent;
		
		this.executorMapper = executorMapper;
		
		if (this.delay >= delay) {
			this.delay = delay;
		}
		//使用默认规则时
		if (this.ruleEvent != null) {
			initializer();
		}
		
	}
	
	private void initializer() {
		List<RulePolicy> rulePolicies = ruleEvent.getPolicy();
		if (rulePolicies != null) {
			for (RulePolicy policy : ruleEvent.getPolicy()) {
				List<RelatedRule> relatedRules = policy.getRelatedRules();
				if (relatedRules != null) {
					for (RelatedRule relatedRule : relatedRules) {
						rules.add(relatedRule);
						//- ObjectCondition 中类型需要特殊处理
						specialProceedConditions(relatedRule);
						List<String> imports = relatedRule.getImports();
						List<String> globals = relatedRule.getGlobals();
						if (imports != null && imports.size() > 0) {
							this.imports.addAll(imports);
						}
						if (globals != null && globals.size() > 0) {
							this.globals.addAll(relatedRule.getGlobals());
						}
					}
				}
			}
		}
	}
	
	private void specialProceedConditions(RelatedRule relatedRule) {
		List<Condition> conditions = relatedRule.getConditions();
		if (conditions != null) {
			for (Condition condition : conditions) {
				if (condition.getClass() == ObjectCondition.class) {
					String typeSimpleName = ((ObjectCondition) condition).getTypeSimpleName();
					relatedRule.getImports().add(typeSimpleName);
				}
			}
		}
	}
	
	public Set<String> getImports() {
		
		return imports;
	}
	
	public InternalRuleEvent getRuleEvent() {
		
		return ruleEvent;
	}
	
	public void setRuleEvent(InternalRuleEvent ruleEvent) {
		
		this.ruleEvent = ruleEvent;
	}
	
	public Map<String, DynamicConditionExecutor> getExecutorMapper() {
		
		return executorMapper;
	}
	
	public Set<RelatedRule> getRules() {
		
		return rules;
	}
	
	public void setRules(Set<RelatedRule> rules) {
		
		this.rules = rules;
	}
	
	public Set<String> getGlobals() {
		
		return globals;
	}
	
	public void setGlobals(Set<String> globals) {
		
		this.globals = globals;
	}
	
	public void setImports(Set<String> imports) {
		
		this.imports = imports;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("ModuleView [ruleEvent=");
		builder.append(ruleEvent);
		builder.append(", executorMapper=");
		builder.append(executorMapper);
		builder.append(", rules=");
		builder.append(rules);
		builder.append(", imports=");
		builder.append(imports);
		builder.append(", globals=");
		builder.append(globals);
		builder.append("]");
		return builder.toString();
	}
	
	public long getDelay() {
		
		return delay;
	}
}
