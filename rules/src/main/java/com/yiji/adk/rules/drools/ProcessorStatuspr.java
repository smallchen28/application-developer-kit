package com.yiji.adk.rules.drools;

import com.alibaba.fastjson.JSON;
import com.yiji.adk.api.order.ModifyPolicyRelatedOrder;
import com.yiji.adk.rules.drools.module.DroolsRepositoryFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Desecription 策略关联规则处理
 */
public enum ProcessorStatuspr {
	VALID(ModifyPolicyRelatedOrder.PolicyOptype.valid) {
		
		@Override
		public void execute(final long prolicyIdentity, String description, String riskType, String policyFrom, JdbcTemplate jdbcTemplate,
							final ModifyPolicyRelatedOrder.PolicyRelatedElement element, String tableNamePre) {
			jdbcTemplate.update(PROLICY_VALID_OR_INVALID_RELATED_RULE_EXP.replaceAll("#\\S*#", tableNamePre),
				new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setBoolean(1, true);
						ps.setLong(2, prolicyIdentity);
						ps.setLong(3, element.getRelatedRuleAttribute().getRuleIdentity());
					}
				});
		}
		
	},
	INVALID(ModifyPolicyRelatedOrder.PolicyOptype.invalid) {
		
		@Override
		public void execute(final long prolicyIdentity, String description, String riskType, String policyFrom, JdbcTemplate jdbcTemplate,
							final ModifyPolicyRelatedOrder.PolicyRelatedElement element, String tableNamePre) {
			jdbcTemplate.update(PROLICY_VALID_OR_INVALID_RELATED_RULE_EXP.replaceAll("#\\S*#", tableNamePre),
				new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setBoolean(1, false);
						ps.setLong(2, prolicyIdentity);
						ps.setLong(3, element.getRelatedRuleAttribute().getRuleIdentity());
					}
				});
		}
		
	},
	DELETE(ModifyPolicyRelatedOrder.PolicyOptype.delete) {
		
		@Override
		public void execute(final long prolicyIdentity, String description, String riskType, String policyFrom, JdbcTemplate jdbcTemplate,
							final ModifyPolicyRelatedOrder.PolicyRelatedElement element, String tableNamePre) {
			jdbcTemplate.update(PROLICY_DELETE_RELATED_RULE_EXP.replaceAll("#\\S*#", tableNamePre),
				new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, prolicyIdentity);
						ps.setLong(2, element.getRelatedRuleAttribute().getRuleIdentity());
					}
				});
		}
		
	},
	ADD(ModifyPolicyRelatedOrder.PolicyOptype.add) {
		
		@Override
		public void execute(final long prolicyIdentity, final String description, String riskType, String policyFrom, JdbcTemplate jdbcTemplate,
							final ModifyPolicyRelatedOrder.PolicyRelatedElement element, String tableNamePre) {
			jdbcTemplate.update(POLICY_UPDATE_DESCRIPTION_EXP.replaceAll("#\\S*#", tableNamePre),
				new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, description);
						ps.setString(2, riskType);
						ps.setString(3, policyFrom);
						ps.setLong(4, prolicyIdentity);
					}
				});
			jdbcTemplate.update(DroolsRepositoryFactoryBean.RELATED_RULE_INSERT_EXP.replaceAll("#\\S*#", tableNamePre),
				new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, prolicyIdentity);
						ps.setLong(2, element.getRelatedRuleAttribute().getRuleIdentity());
						ps.setTimestamp(3, element.getRelatedRuleAttribute().getEffectiveTime());
						ps.setTimestamp(4, element.getRelatedRuleAttribute().getExpireTime());
						ps.setBoolean(5, element.getRelatedRuleAttribute().isEnable());
						ps.setBoolean(6, element.getRelatedRuleAttribute().isAsync());
						ps.setBoolean(7, element.getRelatedRuleAttribute().isLoop());
						ps.setInt(8, element.getRelatedRuleAttribute().getSalicence());
						ps.setString(9, JSON.toJSONString(element.getRelatedRuleAttribute().getRelatedContext()));
					}
				});
		}
		
	},
	UPDATE(ModifyPolicyRelatedOrder.PolicyOptype.update) {
		
		@Override
		public void execute(final long prolicyIdentity, final String description, String riskType, String policyFrom, JdbcTemplate jdbcTemplate,
							final ModifyPolicyRelatedOrder.PolicyRelatedElement element, String tableNamePre) {
			
			jdbcTemplate.update(POLICY_UPDATE_RELATED_RULE_EXP.replaceAll("#\\S*#", tableNamePre),
				new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, element.getRelatedRuleAttribute().getSalicence());
						ps.setString(2, JSON.toJSONString(element.getRelatedRuleAttribute().getRelatedContext()));
						ps.setLong(3, prolicyIdentity);
						ps.setLong(4, element.getRelatedRuleAttribute().getRuleIdentity());
					}
				});
		}
		
	};
	
	public static final String PROLICY_VALID_OR_INVALID_RELATED_RULE_EXP = "update #tableNamePre#related_rule set enable = ? where policy_identity = ? and rule_identity = ?";
	
	public static final String PROLICY_DELETE_RELATED_RULE_EXP = "delete from #tableNamePre#related_rule where policy_identity = ? and rule_identity = ?";
	
	public static final String POLICY_UPDATE_DESCRIPTION_EXP = "update #tableNamePre#rule_policy set description = ?, risk_type = ?, policy_from = ? where identity = ?";
	
	public static final String POLICY_UPDATE_RELATED_RULE_EXP = "update #tableNamePre#related_rule set salicence = ?, related_context = ? where policy_identity = ? and rule_identity = ?";
	
	private ModifyPolicyRelatedOrder.PolicyOptype policyOptype;
	
	public ModifyPolicyRelatedOrder.PolicyOptype policyOptype() {
		return policyOptype;
	}
	
	private ProcessorStatuspr(ModifyPolicyRelatedOrder.PolicyOptype policyOptype) {
		this.policyOptype = policyOptype;
	}
	
	public static ProcessorStatuspr getPolicyOptype(ModifyPolicyRelatedOrder.PolicyOptype policyOptype) {
		ProcessorStatuspr pr = null;
		for (ProcessorStatuspr processorStatuspr : values()) {
			if (processorStatuspr.policyOptype == policyOptype) {
				pr = processorStatuspr;
				break;
			}
		}
		return pr;
	}
	
	public abstract void execute(long prolicyIdentity, String description, String riskType, String policyFrom, JdbcTemplate jdbcTemplate,
									ModifyPolicyRelatedOrder.PolicyRelatedElement element, String tableNamePre);
}
