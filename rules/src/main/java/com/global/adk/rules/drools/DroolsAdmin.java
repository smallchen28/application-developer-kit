package com.global.adk.rules.drools;

import com.global.adk.api.order.*;
import com.global.adk.common.exception.RuleException;
import com.global.adk.rules.drools.module.*;
import com.global.common.lang.beans.Copier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.groups.Default;
import java.util.List;

/**
 * drools管理接口
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014年6月13日
 * @version 1.0.0
 * @see
 */
public class DroolsAdmin {
	
	private static final Logger logger = LoggerFactory.getLogger(DroolsAdmin.class);
	
	private DroolsRepository droolsRepository;
	
	private DroolsTemplate droolsTemplate;
	
	private MessageZKAccessor messageZKAccessor;
	
	public DroolsAdmin(	DroolsRepository droolsRepository, DroolsTemplate droolsTemplate,
						MessageZKAccessor messageZKAccessor) {
						
		this.droolsRepository = droolsRepository;
		
		this.droolsTemplate = droolsTemplate;
		
		this.messageZKAccessor = messageZKAccessor;
	}
	
	//-----------------------------------------
	//- 注册类管理
	//-----------------------------------------
	public Rule register(RegisterRuleOrder order) {
		logger.info("注册请求->{},{}", order.getClass().getSimpleName(), order);
		Rule rule = null;
		try {
			order.checkWithGroup(Default.class);
			rule = convert(order);
			if (order.isCheckDroolsKonwledgeBase()) {
				//- 生存RelatedRule进行规则验证
				droolsTemplate.checkRule(rule);
			}
			droolsRepository.store(rule);
			logger.info("规则检查成功，入库完成.rule={}", rule);
		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("注册规则过程出现内部错误！", e);
		}
		return rule;
	}
	
	public InternalRuleEvent register(RegisterInternalRuleEventOrder order) {
		
		logger.info("注册请求->{},{}", order.getClass().getSimpleName(), order);
		
		InternalRuleEvent internalRuleEvent = new InternalRuleEvent();
		try {
			order.checkWithGroup(Default.class);
			
			Copier.copy(order, internalRuleEvent);
			
			droolsRepository.store(internalRuleEvent, order.getAttributes());
			
			logger.info("规则事件入库完成,event={}", internalRuleEvent);
			
			//通知各个节点更新缓存
			messageZKAccessor.notifyZk();
			
		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("注册规则事件过程出现内部错误！", e);
		}
		return internalRuleEvent;
	}
	
	public RulePolicy register(RegisterRulePolicyOrder order) {
		
		logger.info("注册请求->{},{}", order.getClass().getSimpleName(), order);
		
		RulePolicy rulePolicy = new RulePolicy();
		
		try {
			order.checkWithGroup(Default.class);
			Copier.copy(order, rulePolicy);
			
			droolsRepository.store(rulePolicy, order.getRelatedRuleAttributes());
			
			logger.info("规则策略入库完成,policy={}", rulePolicy);
		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("注册规则集合过程出现内部错误！", e);
		}
		
		return rulePolicy;
	}
	
	//-----------------------------------------
	//- 变更类管理
	//-----------------------------------------
	public void unRegisterRule(UnregisterRuleOrder order) {
		
		logger.info("规则删除请求->{},{}", order.getClass().getSimpleName(), order);
		
		try {
			order.checkWithGroup(Default.class);
			droolsRepository.destroy(order);
			
			logger.info("规则删除成功..");
			
			//通知各个节点更新缓存
			messageZKAccessor.notifyZk();
			
		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("删除规则过程出现内部错误！", e);
		}
	}
	
	public void modifyRule(ModifyRuleOrder order) {
		
		logger.info("规则修改请求->{},{}", order.getClass().getSimpleName(), order);
		
		try {
			order.checkWithGroup(Default.class);
			
			Rule rule = convert(order.getRegisterRuleOrder());
			// consider for 1==1
			for (Condition condition : rule.getConditions()) {
				if (condition instanceof ObjectCondition) {
					if (((ObjectCondition) condition).getCompareElements().size() > 0)
						((ObjectCondition) condition).getCompareElements().get(0)
							.setJoinSymbol(null);
				}
			}
			
			if (order.getRegisterRuleOrder().isCheckDroolsKonwledgeBase()) {
				droolsTemplate.checkRule(rule);
			}
			
			droolsRepository.restore(order);
			
			logger.info("规则修改成功..");

			//通知各个节点更新缓存
			messageZKAccessor.notifyZk();

		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("规则修改过程出现内部错误！", e);
		}
	}
	
	public void modifyPolicyRelated(ModifyPolicyRelatedOrder order) {
		logger.info("修改策略关联规则请求->{}, {}", order.getClass().getSimpleName(), order);
		try {
			order.checkWithGroup(Default.class);
			
			droolsRepository.restroe(order.getPolicyIdentity(), order.getDescription(),
					order.getRiskType(), order.getPolicyFrom(), order.getElements(),
					order.isPlicyEnable());
				
			logger.info("修改策略关联规则请求成功..");

			//通知各个节点更新缓存
			messageZKAccessor.notifyZk();

		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("修改策略关联规则请求过程出现内部错误！", e);
		}
	}
	
	public void modifyEventRelated(ModifyEventRelatedOrder order) {
		
		logger.info("修改关联规则请求->{},{}", order.getClass().getSimpleName(), order);
		
		try {
			order.checkWithGroup(Default.class);
			
			droolsRepository.restore(order.getInternalEventIdentity(), order.getElements(),
				order.isInternalEventEnable(), order.getDescription());
				
			logger.info("关联规则修改成功..");

			//通知各个节点更新缓存
			messageZKAccessor.notifyZk();

		} catch (Exception e) {
			if (e instanceof RuleException) {
				throw e;
			}
			throw new RuleException("修改关联规则程出现内部错误！", e);
		}
	}
	
	private Rule convert(RegisterRuleOrder order) {
		Rule rule = new Rule();
		//- 1. rule复制、2. 条件复制、3比较元素复制 (List)
		Copier.copy(order, rule, new String[] { "conditions" });
		
		List<ConditionOrder> conditionOrders = order.getConditions();
		for (ConditionOrder conditionOrder : conditionOrders) {
			
			Condition condition = null;
			
			if (conditionOrder.getClass() == EvalConditionOrder.class) {
				condition = new EvalCondition();
				
				Copier.copy(conditionOrder, condition, "symbol");
				((EvalCondition) condition).setSymbol(
					Symbol.code(((EvalConditionOrder) conditionOrder).getSymbol().getCode()));
					
			} else if (conditionOrder.getClass() == ObjectConditionOrder.class) {
				condition = new ObjectCondition();
				
				Copier.copy(conditionOrder, condition, "compareElementOrders");
				
				List<ObjectConditionOrder.CompareElementOrder> compareElementOrders = ((ObjectConditionOrder) conditionOrder)
					.getCompareElementOrders();
					
				for (ObjectConditionOrder.CompareElementOrder compareElementOrder : compareElementOrders) {
					ObjectCondition.CompareElement compareElement = new ObjectCondition.CompareElement();
					Copier.copy(compareElementOrder, compareElement);
					((ObjectCondition) condition).getCompareElements().add(compareElement);
				}
				
			} else {
				throw new RuleException(
					String.format("不支持的条件类型ConditionType = %s", conditionOrder.getClass()));
			}
			
			rule.getConditions().add(condition);
		}
		return rule;
	}
}
