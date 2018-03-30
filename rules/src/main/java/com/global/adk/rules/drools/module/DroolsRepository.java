package com.global.adk.rules.drools.module;

import com.global.adk.api.order.*;
import com.global.adk.rules.drools.DroolsEngine;
import com.global.adk.rules.drools.EventRequest;

import java.util.List;
import java.util.Map;

public interface DroolsRepository {
	
	void store(Rule rule);
	
	void store(InternalRuleEvent internalRuleEvent, List<RelatedPolicyAttribute> attributes);
	
	void store(RulePolicy policy, List<RelatedRuleAttribute> attributes);
	
	void restore(long internalEventIdentity, List<ModifyEventRelatedOrder.EventRelatedElement> elements, boolean enable , String description);
	
	void restroe(long policyIdentity, String description, String riskType,String policyFrom, List<ModifyPolicyRelatedOrder.PolicyRelatedElement> elements,
					boolean enable);
	
	void destroy(UnregisterRuleOrder order);

	InternalRuleEvent active(EventRequest request);

	List<DroolsEngine.BogusEvent> loadAllWithEnable();

	List<DroolsEngine.BogusEvent> loadAll();

	void restore(ModifyRuleOrder order);
}
