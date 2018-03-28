package com.yiji.adk.rules.drools.module;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yiji.adk.api.order.*;
import com.yiji.adk.common.exception.RuleException;
import com.yiji.adk.common.jdbc.AbstractRepositoryFactoryBean;
import com.yiji.adk.rules.drools.DroolsEngine;
import com.yiji.adk.rules.drools.EventRequest;
import com.yiji.adk.rules.drools.ProcessorStatuspr;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DroolsRepositoryFactoryBean extends AbstractRepositoryFactoryBean<DroolsRepository> {
	
	private JdbcTemplate jdbcTemplate;
	
	private OracleSequenceMaxValueIncrementer incrementer;
	
	public static final String RULE_INSERT_EXP = "insert into #tableNamePre#rule(identity,rule_name,description,script,imports,globals,reserved1,reserved2)values(?,?,?,?,?,?,?,?)";
	
	public static final String RULE_UPDATE_EXP = "update #tableNamePre#rule set rule_name = ?, description = ?, script = ?, imports = ?, globals = ?, reserved1 = ? ,reserved2 = ? ,raw_update_time = systimestamp where identity = ?";

	public static final String EVAL_CONDITION_INSERT_EXP = "insert into #tableNamePre#rule_eval_condition(identity,rule_identity,condition_script,symbol,request_name,compare_value,executor_name,script_identity,reserved1,reserved2)values(?,?,?,?,?,?,?,?,?,?)";

	public static final String EVAL_CONDITION_UPDATE_EXP = "update #tableNamePre#rule_eval_condition set condition_script = ?,symbol = ?,request_name = ?,compare_value = ?,executor_name = ?,reserved1 = ?,reserved2 = ? where identity = ?";

	public static final String EVAL_CONDITION_DELETE_EXP = "delete from #tableNamePre#rule_eval_condition where identity = ?";

	public static final String OBJ_CONDITION_INSERT_EXP = "insert into #tableNamePre#rule_obj_condition(identity,rule_identity ,variable_name , type_simple_name ,join_symbol ,symbol,left_value , right_value,reserved1,reserved2)values(?,?,?,?,?,?,?,?,?,?)";

	public static final String OBJ_CONDITION_UPDATE_EXP = "update #tableNamePre#rule_obj_condition set variable_name = ?, type_simple_name = ?, join_symbol = ?, symbol = ?, left_value = ?, right_value = ?,reserved1 = ?,reserved2 = ? where identity = ?";

	public static final String OBJ_CONDTION_DELETE_EXP = "delete from #tableNamePre#rule_obj_condition where identity = ?";
	
	public static final String INTERNAL_RULE_EVENT_INSERT_EXP = "insert into #tableNamePre#internal_rule_event(identity,event_name,version,description,event_context,enable)values(?,?,?,?,?,?)";
	
	public static final String EVENT_RELATED_POLICY_UPDATE_EXP = "update #tableNamePre#rule_policy set related_event = ? ,enable = ? where identity = ?";
	
	public static final String EVENT_RELATED_POLICY_REFRESH_EXP = "update #tableNamePre#internal_rule_event t set t.version = t.version + 1 where t.identity in("
																	+ "  select t1.related_event from #tableNamePre#rule_policy t1 where t1.identity in("
																	+ "    select policy_identity from #tableNamePre#related_rule  where rule_identity = ?"
																	+ "  ))";
	
	public static final String RULE_POLICY_INSERT_EXP = "insert into #tableNamePre#rule_policy(identity , description,enable, risk_type, policy_from) values(?,?,?,?,?)";
	
	public static final String RELATED_RULE_INSERT_EXP = "insert into #tableNamePre#related_rule(policy_identity , rule_identity , effective_time , expire_time , enable , async , loop , salicence,related_context)values(?,?,?,?,?,?,?,?,?)";
	
	public static final String RULE_DELETE_EXP = "delete from #tableNamePre#rule where identity in (";
	
	public static final String RULE_RELATED_DELETE_EXP = "delete from #tableNamePre#related_rule where rule_identity in (";
	
	public static final String RULE_DELETE_TRANSFER_LOG_EXP = "insert into #tableNamePre#rule_history(identity,rule_name,description,script,imports, globals,reserved1,reserved2,raw_add_time,raw_update_time, delete_reason,opertor_ip,opertor_id) select t.identity,t.rule_name,t.description,t.script, t.imports,t.globals,t.reserved1,t.reserved2, t.raw_add_time,t.raw_update_time,?,?,? from #tableNamePre#rule t where t.identity =  ?";
	
	public static final String EVENT_REFRESH_EXP = "update #tableNamePre#internal_rule_event set version = version+1 , enable= ? , description = ? where identity = ? ";
	
	public static final String POLICY_RELATED_RULE_VERSION_EXP = "update #tableNamePre#internal_rule_event set version = version+1, enable = ? where identity in (select related_event from #tableNamePre#rule_policy where identity = ?)";
	
	public static final String EVENT_UPDATE_VERSION_EXP = "update #tableNamePre#internal_rule_event set version = ? where event_name = ? and  event_context = ?";
	
	public static final String INTERNAL_EVENT_ADD_POLICY_EXP = "update #tableNamePre#rule_policy set related_event = ?, enable = ? where identity = ?";
	
	public static final String INTERNAL_EVENT_INVALID_POLICY_EXP = "update #tableNamePre#rule_policy set enable=? where identity = ?";
	
	public static final String RELATED_RULE_DELETE_EXP = "delete from #tableNamePre#related_rule where policy_identity = ?";
	
	public static final String RULE_POLICY_DELETE_EXP = "delete from #tableNamePre#rule_policy where identity = ?";
	
	public static final String INTERNAL_RULE_EVENT_FETCH_POLICY_EXP_WITH_POLICYFROM = "select identity,related_event,description,raw_add_time , raw_update_time , enable,risk_type, policy_from  from #tableNamePre#rule_policy where related_event = ? and enable = ? and (policy_from = ? or policy_from = 'ALL')";

	public static final String INTERNAL_RULE_EVENT_FETCH_RULE_EXP = "select t.identity , t.rule_name , t.description , t.script , t.imports , t.globals ,"
																	+ "  t2.effective_time , t2.expire_time , t2.raw_add_time , t2.raw_update_time , t2.enable , t2.async , t2 .loop ,t2.salicence ,t2.related_context "
																	+ "from #tableNamePre#rule t inner join #tableNamePre#related_rule t2 on (t.identity = t2.rule_identity) where t2.policy_identity = ? and t2.enable = ?";
	
	public static final String INTERNAL_RULE_EVENT_FETCH_RULE_OBJCONDITION_STATISTIC_EXP = "select distinct(variable_name ||'&'|| type_simple_name) as composit_data  from #tableNamePre#rule_obj_condition where rule_identity = ?";
	
	public static final String INTERNAL_RULE_EVENT_FETCH_RULE_OBJCONDITION_EXP = "select  join_symbol , symbol , left_value , right_value from #tableNamePre#rule_obj_condition where rule_identity = ? and variable_name = ? and type_simple_name = ? order by identity";
	
	public static final String INTERNAL_RULE_EVENT_FETCH_RULE_EVALCONDITION_EXP = "select identity, condition_script , symbol,request_name , compare_value , executor_name from #tableNamePre#rule_eval_condition where rule_identity = ?";
	
	public static final String INTERNAL_RULE_EVENT_LOAD_ALL_WITH_ENABLE_EXP = "select nvl(pf,'ALL') , en ,ec ,v from (select distinct(t2.policy_from) pf , event_name en ,event_context ec ,version v from #tableNamePre#internal_rule_event t left join  #tableNamePre#rule_policy t2 on (t.identity = t2.related_event) where t.enable = '1')";

	public static final String INTERNAL_RULE_EVENT_LOAD_ALL_EXP = "select nvl(pf,'ALL') , en ,ec ,v ,e from (select distinct(t2.policy_from) pf , event_name en ,event_context ec ,version v,t.enable e from #tableNamePre#internal_rule_event t left join  #tableNamePre#rule_policy t2 on (t.identity = t2.related_event))";

	public DroolsRepositoryFactoryBean(JdbcTemplate jdbcTemplate, OracleSequenceMaxValueIncrementer incrementer) {
		
		this.jdbcTemplate = jdbcTemplate;
		this.incrementer = incrementer;
		setDataSource(jdbcTemplate.getDataSource());
	}
	
	@Override
	public DroolsRepository newInstance() {

		return new DroolsRepository() {

			@Override
			public void store(final Rule rule) {
				
				rule.setIdentity(incrementer.nextLongValue());
				//规则入库
				jdbcTemplate.update(RULE_INSERT_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new PreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps) throws SQLException {

							ps.setLong(1, rule.getIdentity());
							ps.setString(2, rule.getRuleName());
							ps.setString(3, rule.getDescription());
							ps.setString(4, rule.getScript());
							ps.setString(5, JSON.toJSONString(rule.getImports()));
							ps.setString(6, JSON.toJSONString(rule.getGlobals()));
							ps.setString(7, rule.getReserved1());
							ps.setString(8, rule.getReserved2());
						}
					});
				
				//条件入库
				for (final Condition condition : rule.getConditions()) {
					if (condition instanceof EvalCondition) {
						jdbcTemplate.update(EVAL_CONDITION_INSERT_EXP.replaceAll("#\\S*#", getTableNamePre()),
							new PreparedStatementSetter() {
								
								@Override
								public void setValues(PreparedStatement ps) throws SQLException {
									
									EvalCondition evalCondition = (EvalCondition) condition;
									ps.setLong(1, incrementer.nextLongValue());
									ps.setLong(2, rule.getIdentity());
									ps.setString(3, evalCondition.getConditionScript());
									ps.setString(4, evalCondition.getSymbol().getCode());
									ps.setString(5, evalCondition.getRequestName());
									ps.setString(6, evalCondition.getCompareValue());
									ps.setString(7, evalCondition.getExecutorName());
									ps.setInt(8, evalCondition.getScriptIdentity());
									ps.setString(9, evalCondition.getReserved1());
									ps.setString(10, evalCondition.getReserved2());
								}
							});

					} else if (condition instanceof ObjectCondition) {
						
						ObjectCondition objectCondition = (ObjectCondition) condition;
						final String typeSimpleName = objectCondition.getTypeSimpleName();
						final String variableName = objectCondition.getVariableName();
						final long identity = rule.getIdentity();
						List<ObjectCondition.CompareElement> elements = objectCondition.getCompareElements();
						
						//- 单纯赋值的时候会出现element.size ＝＝ 0 ，例如：$order : Order() 转换为 $order : Order(1==1)
						if (elements.size() == 0) {
							ObjectCondition.CompareElement element = new ObjectCondition.CompareElement();
							element.setJoinSymbol(null);
							element.setLeftValue("1");
							element.setRightValue("1");
							element.setSymbol("==");
							elements.add(element);
						}
						jdbcTemplate.batchUpdate(OBJ_CONDITION_INSERT_EXP.replaceAll("#\\S*#", getTableNamePre()),
							elements, elements.size(),
							new ParameterizedPreparedStatementSetter<ObjectCondition.CompareElement>() {
								
								@Override
								public void setValues(PreparedStatement ps, ObjectCondition.CompareElement argument)
																													throws SQLException {
									
									ps.setLong(1, incrementer.nextLongValue());
									ps.setLong(2, identity);
									ps.setString(3, variableName);
									ps.setString(4, typeSimpleName);
									ps.setString(5, argument.getJoinSymbol());
									ps.setString(6, argument.getSymbol());
									ps.setString(7, argument.getLeftValue());
									ps.setString(8, argument.getRightValue());
									ps.setString(9, argument.getReserved1());
									ps.setString(10, argument.getReserved2());
								}
							});
					} else {
						throw new RuleException(String.format("%s该条件插入尚未支持", condition));
					}
				}
				
			}
			
			public void store(final InternalRuleEvent event, List<RelatedPolicyAttribute> attributes) {
				
				event.setIdentity(incrementer.nextLongValue());
				//- 1. 插入内部事件对象
				jdbcTemplate.update(INTERNAL_RULE_EVENT_INSERT_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							
							ps.setLong(1, event.getIdentity());
							ps.setString(2, event.getEventName());
							ps.setLong(3, event.getVersion());
							ps.setString(4, event.getDescription());
							ps.setString(5, JSON.toJSONString(event.getEventContext()));
							ps.setBoolean(6, event.isEnable());
						}
					});
				
				//- 2. 批量更新
				for (final RelatedPolicyAttribute attribute : attributes) {
					jdbcTemplate.update(EVENT_RELATED_POLICY_UPDATE_EXP.replaceAll("#\\S*#", getTableNamePre()),
						new PreparedStatementSetter() {
							
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								
								ps.setLong(1, event.getIdentity());
								ps.setBoolean(2, attribute.isEnable());
								ps.setLong(3, attribute.getIdentity());
							}
						});
				}
			}
			
			@Override
			public void store(final RulePolicy policy, final List<RelatedRuleAttribute> relatedRuleAttributes) {
				
				policy.setIdentity(incrementer.nextLongValue());
				
				jdbcTemplate.update(RULE_POLICY_INSERT_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							
							ps.setLong(1, policy.getIdentity());
							ps.setString(2, policy.getDescription());
							ps.setBoolean(3, policy.isEnable());
							ps.setString(4, policy.getRiskType());
							ps.setString(5, policy.getPolicyFrom());
						}
					});
				
				//- 关联批量插入
				jdbcTemplate.batchUpdate(RELATED_RULE_INSERT_EXP.replaceAll("#\\S*#", getTableNamePre()),
					relatedRuleAttributes, relatedRuleAttributes.size(),
					new ParameterizedPreparedStatementSetter<RelatedRuleAttribute>() {
						
						@Override
						public void setValues(PreparedStatement ps, RelatedRuleAttribute ruleAttribute)
																										throws SQLException {
							
							ps.setLong(1, policy.getIdentity());
							ps.setLong(2, ruleAttribute.getRuleIdentity());
							ps.setTimestamp(3, ruleAttribute.getEffectiveTime());
							ps.setTimestamp(4, ruleAttribute.getExpireTime());
							ps.setBoolean(5, ruleAttribute.isEnable());
							ps.setBoolean(6, ruleAttribute.isAsync());
							ps.setBoolean(7, ruleAttribute.isLoop());
							ps.setInt(8, ruleAttribute.getSalicence());
							ps.setString(9, JSON.toJSONString(ruleAttribute.getRelatedContext()));
							
							RelatedRule relatedRule = new RelatedRule();
							relatedRule.setPolicyIdentity(policy.getIdentity());
							relatedRule.setIdentity(ruleAttribute.getRuleIdentity());
							relatedRule.setRelatedContext(ruleAttribute.getRelatedContext());
							relatedRule.setAsync(ruleAttribute.isAsync());
							relatedRule.setDescription(ruleAttribute.getDescription());
							relatedRule.setEffectiveTime(ruleAttribute.getEffectiveTime());
							relatedRule.setEnable(ruleAttribute.isEnable());
							relatedRule.setExpireTime(ruleAttribute.getExpireTime());
							relatedRule.setLoop(ruleAttribute.isLoop());
							
							policy.getRelatedRules().add(relatedRule);
						}
					});
				
			}
			
			@Override
			public void destroy(final UnregisterRuleOrder order) {
				
				Object[] parameter = order.getRuleIds().toArray(new Long[0]);
				final List<Long> ruleids = order.getRuleIds();
				
				//- 1. 刷新相关策略版本 
				jdbcTemplate.batchUpdate(EVENT_RELATED_POLICY_REFRESH_EXP.replaceAll("#\\S*#", getTableNamePre()),
					ruleids, ruleids.size(), new ParameterizedPreparedStatementSetter<Long>() {
						
						@Override
						public void setValues(PreparedStatement ps, Long ruleid) throws SQLException {
							
							ps.setLong(1, ruleid);
						}
					});
				
				//- 2. 清除策略关联
				StringBuilder clearSql = new StringBuilder(RULE_RELATED_DELETE_EXP);
				dynamicSql(clearSql, ruleids);
				jdbcTemplate.update(clearSql.toString().replaceAll("#\\S*#", getTableNamePre()), parameter);
				
				//- 3. 转纪录至history
				jdbcTemplate.batchUpdate(RULE_DELETE_TRANSFER_LOG_EXP.replaceAll("#\\S*#", getTableNamePre()), ruleids,
					ruleids.size(), new ParameterizedPreparedStatementSetter<Long>() {
						
						@Override
						public void setValues(PreparedStatement ps, Long ruleid) throws SQLException {
							
							ps.setString(1, order.getDeleteReason());
							ps.setString(2, order.getOpertorIP());
							ps.setString(3, order.getOpertorID());
							ps.setLong(4, ruleid);
						}
					});
				
				//- 4.删除规则
				final StringBuilder sql = new StringBuilder(RULE_DELETE_EXP);
				dynamicSql(sql, ruleids);
				jdbcTemplate.update(sql.toString().replaceAll("#\\S*#", getTableNamePre()), parameter);
			}
			
			@Override
			public void restore(long internalEventIdentity, List<ModifyEventRelatedOrder.EventRelatedElement> elements,
								boolean enable,String description) {
				
				//- 1.更新内部事件状态并增加版本
				jdbcTemplate.update(EVENT_REFRESH_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new Object[] { enable,description, internalEventIdentity });
				//- 2.根据枚举做策略处理
				if(elements != null && elements.size() > 0){
					for (ModifyEventRelatedOrder.EventRelatedElement element : elements) {
						ProcessorStrategy.getOptype(element.getOptype()).execute(internalEventIdentity, jdbcTemplate,
								element, getTableNamePre());
					}
				}
			}
			
			@Override
			public InternalRuleEvent active(EventRequest request) {

				String policyFrom = request.getPolicyFrom();
					//- 1. 抓取event
				InternalRuleEvent event = jdbcTemplate.queryForObject(
						EventExpressionSQL.sql(request.getEventName(), request.getEventContext()).replaceAll("#\\S*#",
								getTableNamePre()), new RowMapper<InternalRuleEvent>() {

							@SuppressWarnings("unchecked")
							@Override
							public InternalRuleEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
								
								InternalRuleEvent event = new InternalRuleEvent();
								event.setIdentity(rs.getLong(1));
								event.setEventName(rs.getString(2));
								event.setVersion(rs.getLong(3));
								event.setDescription(rs.getString(4));
								event.setEventContext((Map<String, String>) JSON.parse(rs.getString(5)));
								event.setEnable(rs.getBoolean(6));
								event.setRawAddTime(rs.getTimestamp(7));
								event.setRawUpdateTime(rs.getTimestamp(8));
								
								//- 2. 抓取策略
								List<RulePolicy> policys = jdbcTemplate.query(
									INTERNAL_RULE_EVENT_FETCH_POLICY_EXP_WITH_POLICYFROM.replaceAll("#\\S*#",
										getTableNamePre()), new Object[] { event.getIdentity(), true, policyFrom },
									new RowMapper<RulePolicy>() {
										
										@Override
										public RulePolicy mapRow(ResultSet rs, int rowNum) throws SQLException {

											RulePolicy rulePolicy = new RulePolicy();
											rulePolicy.setIdentity(rs.getLong(1));
											rulePolicy.setRelatedEvent(rs.getLong(2));
											rulePolicy.setDescription(rs.getString(3));
											rulePolicy.setRawAddTime(rs.getTimestamp(4));
											rulePolicy.setRawUpdateTime(rs.getTimestamp(5));
											rulePolicy.setEnable(rs.getBoolean(6));
											
											//- 3. 抓取关联规则
											List<RelatedRule> relatedRules = jdbcTemplate.query(
												INTERNAL_RULE_EVENT_FETCH_RULE_EXP.replaceAll("#\\S*#",
													getTableNamePre()),
												new Object[] { rulePolicy.getIdentity(), true },
												new RowMapper<RelatedRule>() {
													
													@Override
													public RelatedRule mapRow(ResultSet rs, int rowNum)
																										throws SQLException {
														
														final RelatedRule relatedRule = new RelatedRule();
														relatedRule.setPolicyIdentity(rulePolicy.getIdentity());
														relatedRule.setIdentity(rs.getLong(1));
														relatedRule.setRuleName(rs.getString(2));
														relatedRule.setDescription(rs.getString(3));
														relatedRule.setScript(rs.getString(4));
														relatedRule.getImports().addAll(
															(List<String>) JSON.parse(rs.getString(5)));
														relatedRule.getGlobals().addAll(
															(List<String>) JSON.parse(rs.getString(6)));
														relatedRule.setEffectiveTime(rs.getTimestamp(7));
														relatedRule.setExpireTime(rs.getTimestamp(8));
														relatedRule.setRawAddTime(rs.getTimestamp(9));
														relatedRule.setRawUpdateTime(rs.getTimestamp(10));
														relatedRule.setEnable(rs.getBoolean(11));
														relatedRule.setAsync(rs.getBoolean(12));
														relatedRule.setLoop(rs.getBoolean(13));
														relatedRule.setSalicence(rs.getInt(14));
														relatedRule.setRelatedContext((Map) JSON.parseObject(rs
															.getString(15)));
														//- 4. 抓取相应条件EvalCondition
														List<Condition> conditions = new ArrayList<>();
														conditions.addAll(jdbcTemplate.query(
															INTERNAL_RULE_EVENT_FETCH_RULE_EVALCONDITION_EXP
																.replaceAll("#\\S*#", getTableNamePre()),
																new Object[]{relatedRule.getIdentity()},
																new RowMapper<EvalCondition>() {

																	//select identity, condition_script , symbol,request_name ,compare_type , compare_value , executor_name from app_kit_rule_eval_condition where rule_identity = ?
																@Override
																public EvalCondition mapRow(ResultSet rs, int rowNum)
																														throws SQLException {
																	
																	EvalCondition condition = new EvalCondition();
																	condition.setRuleIdentity(relatedRule.getIdentity());
																	condition.setIdentity(rs.getLong(1));
																	condition.setConditionScript(rs.getString(2));
																	condition.setSymbol(Symbol.code(rs.getString(3)));
																	condition.setRequestName(rs.getString(4));
																	condition.setCompareValue(rs.getString(5));
																	condition.setExecutorName(rs.getString(6));
																	return condition;
																}
															}));
														//- 5. 抓取相应条件ObjCondition
														//- 5.1 根据id统计出具体条件名称
														List<Map<String, Object>> attributes = jdbcTemplate
															.queryForList(
																INTERNAL_RULE_EVENT_FETCH_RULE_OBJCONDITION_STATISTIC_EXP
																	.replaceAll("#\\S*#", getTableNamePre()),
																new Object[] { relatedRule.getIdentity() });
														
														for (int i = 0, j = attributes.size(); i < j; i++) {
															Map<String, Object> entry = attributes.get(i);
															String data = entry.get("composit_data").toString();
															
															int index = data.indexOf("&");
															String typeSimpleName = data.substring(index + 1);
															String variableName = data.substring(0, index);
															
															final ObjectCondition objectCondition = new ObjectCondition();
															//预先赋值
															objectCondition.setRuleIdentity(relatedRule.getIdentity());
															objectCondition.setVariableName(variableName);
															objectCondition.setTypeSimpleName(typeSimpleName);
															conditions.add(objectCondition);
															
															objectCondition.setCompareElements(jdbcTemplate.query(
																INTERNAL_RULE_EVENT_FETCH_RULE_OBJCONDITION_EXP
																	.replaceAll("#\\S*#", getTableNamePre()),
																new Object[] { relatedRule.getIdentity(), variableName,
																				typeSimpleName },
																new RowMapper<ObjectCondition.CompareElement>() {
																	
																	@Override
																	public ObjectCondition.CompareElement mapRow(	ResultSet rs,
																													int rowNum)
																																throws SQLException {
																		
																		ObjectCondition.CompareElement compareElement = new ObjectCondition.CompareElement();
																		compareElement.setJoinSymbol(rs.getString(1));
																		compareElement.setSymbol(rs.getString(2));
																		compareElement.setLeftValue(rs.getString(3));
																		compareElement.setRightValue(rs.getString(4));
																		return compareElement;
																	}
																}));
														}
														Collections.sort(conditions);
														relatedRule.getConditions().addAll(conditions);
														return relatedRule;
													}
												});
											Collections.sort(relatedRules);
											rulePolicy.setRelatedRules(relatedRules);
											return rulePolicy;
										}
									});
								event.setPolicy(policys);
								return event;
							}
						});
					return event;
			}

			@Override
			public List<DroolsEngine.BogusEvent> loadAllWithEnable() {
				//- 抓取全部开关全部打开的数据。
				return jdbcTemplate.query(
						INTERNAL_RULE_EVENT_LOAD_ALL_WITH_ENABLE_EXP.replaceAll("#\\S*#", getTableNamePre()),
						new RowMapper<DroolsEngine.BogusEvent>() {
							@Override
							public DroolsEngine.BogusEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
								return new DroolsEngine.BogusEvent(rs.getString(1), rs.getString(2), rs.getString(3),rs.getInt(4),true);
							}
						}
				);
			}

			@Override
			public List<DroolsEngine.BogusEvent> loadAll() {

				//-1. 抓取全部事件，无论是否打开开关。
				return jdbcTemplate.query(
					INTERNAL_RULE_EVENT_LOAD_ALL_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new RowMapper<DroolsEngine.BogusEvent>() {
						@Override
						public DroolsEngine.BogusEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new DroolsEngine.BogusEvent(rs.getString(1), rs.getString(2), rs.getString(3),rs.getInt(4),rs.getBoolean(5));
						}
					}
				);
			}
			
			@Override
			public void restore(final ModifyRuleOrder order) {
				//1：规则修改
				jdbcTemplate.update(RULE_UPDATE_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, order.getRegisterRuleOrder().getRuleName());
							ps.setString(2, order.getRegisterRuleOrder().getDescription());
							ps.setString(3, order.getRegisterRuleOrder().getScript());
							ps.setString(4, JSON.toJSONString(order.getRegisterRuleOrder().getImports()));
							ps.setString(5, JSON.toJSONString(order.getRegisterRuleOrder().getGlobals()));
							ps.setString(6, order.getRegisterRuleOrder().getReserved1());
							ps.setString(7, order.getRegisterRuleOrder().getReserved2());
							ps.setLong(8, order.getRuleIdentity());
						}
					});
				//2：删除指定条件
				for (Entry<Long, ModifyRuleOrder.IdentityOptype> optype : order.getDeleteIdentity().entrySet()) {
					ProcessorDeleteRule.getIdentityOptype(optype.getValue()).execute(optype.getKey(), jdbcTemplate,
						getTableNamePre());
				}
				//3：其他条件入库
				for (final ConditionOrder conditionOrder : order.getRegisterRuleOrder().getConditions()) {
					//3.1：然后判断是否属于obj
					if (conditionOrder instanceof ObjectConditionOrder) {
						ObjectConditionOrder obj = (ObjectConditionOrder) conditionOrder;
						ArrayList<ObjectConditionOrder.CompareElementOrder> compareElementOrders = obj
							.getCompareElementOrders();
						for (ObjectConditionOrder.CompareElementOrder compareElementOrder : compareElementOrders) {
							//3.1.1：判断条件是否存在ID，如果大于0执行修改条件，否则增加条件
							if (compareElementOrder.getIdentity() > 0) {
								ProcessorRulegy.getOptypeEnum(OptypeEnum.update).execute(order.getRuleIdentity(),
									jdbcTemplate, incrementer, conditionOrder, getTableNamePre(), compareElementOrder);
							} else {
								ProcessorRulegy.getOptypeEnum(OptypeEnum.add).execute(order.getRuleIdentity(),
									jdbcTemplate, incrementer, conditionOrder, getTableNamePre(), compareElementOrder);
							}
						}
					}
					//3.2：首先判断是否属于eval
					else if (conditionOrder instanceof EvalConditionOrder) {
						EvalConditionOrder eval = (EvalConditionOrder) conditionOrder;
						//3.2.1：判断条件是否存在ID，如果大于0执行修改条件，否则增加条件
						if (eval.getIdentity() > 0) {
							ProcessorRulegy.getOptypeEnum(OptypeEnum.update).execute(order.getRuleIdentity(),
								jdbcTemplate, incrementer, conditionOrder, getTableNamePre(), null);
						} else {
							ProcessorRulegy.getOptypeEnum(OptypeEnum.add).execute(order.getRuleIdentity(),
									jdbcTemplate, incrementer, conditionOrder, getTableNamePre(), null);
						}
					}
				}
				//4：更新内部事件状态并增加版本
				jdbcTemplate.update(EVENT_RELATED_POLICY_REFRESH_EXP.replaceAll("#\\S*#", getTableNamePre()),
						order.getRuleIdentity());

			}

			@Override
			public void restroe(long policyIdentity, String description, String riskType, String policyFrom,
								List<ModifyPolicyRelatedOrder.PolicyRelatedElement> elements, boolean enable) {
				//- 1.根据枚举做策略关联规则处理
				for (ModifyPolicyRelatedOrder.PolicyRelatedElement element : elements) {
					ProcessorStatuspr.getPolicyOptype(element.getPolicyOptype()).execute(policyIdentity, description,
						riskType, policyFrom, jdbcTemplate, element, getTableNamePre());
				}
				//- 2.更新内部事件状态并增加版本
				jdbcTemplate.update(POLICY_RELATED_RULE_VERSION_EXP.replaceAll("#\\S*#", getTableNamePre()),
					new Object[] { enable, policyIdentity });
			}
			
		};
	}
	
	public enum ProcessorStrategy {
		
		ADD(ModifyEventRelatedOrder.Optype.add) {
			
			@Override
			void execute(final long eventIdentity, JdbcTemplate jdbcTemplate,
							final ModifyEventRelatedOrder.EventRelatedElement element, String tableNamePre) {

				jdbcTemplate.update(INTERNAL_EVENT_ADD_POLICY_EXP.replaceAll("#\\S*#", tableNamePre),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							
							ps.setLong(1, eventIdentity);
							ps.setBoolean(2, true);
							ps.setLong(3, element.getPolicyIdentity());
						}
					});
			}
		},
		INVALID(ModifyEventRelatedOrder.Optype.invalid) {
			
			@Override
			void execute(final long eventIdentity, JdbcTemplate jdbcTemplate,
							final ModifyEventRelatedOrder.EventRelatedElement element, String tableNamePre) {

				jdbcTemplate.update(INTERNAL_EVENT_INVALID_POLICY_EXP.replaceAll("#\\S*#", tableNamePre),
						new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {

								ps.setBoolean(1, false);
								ps.setLong(2, element.getPolicyIdentity());
							}
						});
			}
		},
		VALID(ModifyEventRelatedOrder.Optype.valid) {
			@Override
			void execute(long eventIdentity, JdbcTemplate jdbcTemplate,
						 final ModifyEventRelatedOrder.EventRelatedElement element, String tableNamePre) {
				
				jdbcTemplate.update(INTERNAL_EVENT_INVALID_POLICY_EXP.replaceAll("#\\S*#", tableNamePre),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							
							ps.setBoolean(1, true);
							ps.setLong(2, element.getPolicyIdentity());
						}
					});
			}
			
		},
		DELETE(ModifyEventRelatedOrder.Optype.delete) {
			
			@Override
			void execute(long eventIdentity, JdbcTemplate jdbcTemplate,
							final ModifyEventRelatedOrder.EventRelatedElement element, String tableNamePre) {
				
				//- 1. 删除规则关联
				jdbcTemplate.update(RELATED_RULE_DELETE_EXP.replaceAll("#\\S*#", tableNamePre),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							
							ps.setLong(1, element.getPolicyIdentity());
						}
					});
				//- 2. 删除策略
				jdbcTemplate.update(RULE_POLICY_DELETE_EXP.replaceAll("#\\S*#", tableNamePre),
					new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							
							ps.setLong(1, element.getPolicyIdentity());
						}
					});
			}
		};
		
		private ModifyEventRelatedOrder.Optype optype;
		
		public ModifyEventRelatedOrder.Optype optype() {
			
			return optype;
		}
		
		public static ProcessorStrategy getOptype(ModifyEventRelatedOrder.Optype optype) {
			
			ProcessorStrategy ps = null;
			for (ProcessorStrategy processorStrategy : values()) {
				if (processorStrategy.optype == optype) {
					ps = processorStrategy;
					break;
				}
			}
			return ps;
		}
		
		private ProcessorStrategy(ModifyEventRelatedOrder.Optype optype) {
			
			this.optype = optype;
		}
		
		abstract void execute(long eventIdentity, JdbcTemplate jdbcTemplate,
								ModifyEventRelatedOrder.EventRelatedElement element, String tableNamePre);
		
	}
	
	public JdbcTemplate getJdbcTemplate() {
		
		return jdbcTemplate;
	}
	
	public OracleSequenceMaxValueIncrementer getIncrementer() {
		
		return incrementer;
	}
	
	@SuppressWarnings("rawtypes")
	private void dynamicSql(StringBuilder sql, List rules) {
		
		for (int i = 0, j = rules.size(); i < j; i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append("?");
		}
		sql.append(")");
	}
	
	enum OptypeEnum {
		update,
		add
	}
	
	public enum ProcessorRulegy {
		ADD(OptypeEnum.add) {
			
			@Override
			void execute(final long ruleIdentity, final JdbcTemplate jdbcTemplate,
							final OracleSequenceMaxValueIncrementer incrementer, final ConditionOrder conditionOrder,
							final String tableNamePre,
							final ObjectConditionOrder.CompareElementOrder compareElementOrder) {
				if (conditionOrder instanceof EvalConditionOrder) {
					jdbcTemplate.update(EVAL_CONDITION_INSERT_EXP.replaceAll("#\\S*#", tableNamePre),
						new PreparedStatementSetter() {
							
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								
								EvalConditionOrder evalCondition = (EvalConditionOrder) conditionOrder;
								ps.setLong(1, incrementer.nextLongValue());
								ps.setLong(2, ruleIdentity);
								ps.setString(3, evalCondition.getConditionScript());
								ps.setString(4, evalCondition.getSymbol().getCode());
								ps.setString(5, evalCondition.getRequestName());
								ps.setString(6, evalCondition.getCompareValue());
								ps.setString(7, evalCondition.getExecutorName());
								ps.setInt(8, evalCondition.getScriptIdentity());
								ps.setString(9, evalCondition.getReserved1());
								ps.setString(10, evalCondition.getReserved2());
							}
						});
					
				} else if (conditionOrder instanceof ObjectConditionOrder) {
					
					ObjectConditionOrder objectCondition = (ObjectConditionOrder) conditionOrder;
					final String typeSimpleName = objectCondition.getTypeSimpleName();
					final String variableName = objectCondition.getVariableName();
					
					jdbcTemplate.update(OBJ_CONDITION_INSERT_EXP.replaceAll("#\\S*#", tableNamePre),
						new PreparedStatementSetter() {
							
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setLong(1, incrementer.nextLongValue());
								ps.setLong(2, ruleIdentity);
								ps.setString(3, variableName);
								ps.setString(4, typeSimpleName);
								ps.setString(5, compareElementOrder.getJoinSymbol());
								ps.setString(6, compareElementOrder.getSymbol());
								ps.setString(7, compareElementOrder.getLeftValue());
								ps.setString(8, compareElementOrder.getRightValue());
								ps.setString(9, compareElementOrder.getReserved1());
								ps.setString(10, compareElementOrder.getReserved2());
							}
							
						});
				} else {
					throw new RuleException(String.format("%s该条件插入尚未支持", conditionOrder));
				}
			}
		},
		UPDATE(OptypeEnum.update) {
			
			@Override
			void execute(final long ruleIdentity, final JdbcTemplate jdbcTemplate,
							final OracleSequenceMaxValueIncrementer incrementer, final ConditionOrder conditionOrder,
							final String tableNamePre,
							final ObjectConditionOrder.CompareElementOrder compareElementOrder) {
				if (conditionOrder instanceof EvalConditionOrder) {
					jdbcTemplate.update(EVAL_CONDITION_UPDATE_EXP.replaceAll("#\\S*#", tableNamePre),
						new PreparedStatementSetter() {
							
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								
								EvalConditionOrder evalCondition = (EvalConditionOrder) conditionOrder;
								ps.setString(1, evalCondition.getConditionScript());
								ps.setString(2, evalCondition.getSymbol().getCode());
								ps.setString(3, evalCondition.getRequestName());
								ps.setString(4, evalCondition.getCompareValue());
								ps.setString(5, evalCondition.getExecutorName());
								ps.setString(6, evalCondition.getReserved1());
								ps.setString(7, evalCondition.getReserved2());
								ps.setLong(8, evalCondition.getIdentity());
							}
						});
					
				} else if (conditionOrder instanceof ObjectConditionOrder) {
					
					ObjectConditionOrder objectCondition = (ObjectConditionOrder) conditionOrder;
					final String typeSimpleName = objectCondition.getTypeSimpleName();
					final String variableName = objectCondition.getVariableName();
					
					jdbcTemplate.update(OBJ_CONDITION_UPDATE_EXP.replaceAll("#\\S*#", tableNamePre),
						new PreparedStatementSetter() {
							
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setString(1, variableName);
								ps.setString(2, typeSimpleName);
								ps.setString(3, compareElementOrder.getJoinSymbol());
								ps.setString(4, compareElementOrder.getSymbol());
								ps.setString(5, compareElementOrder.getLeftValue());
								ps.setString(6, compareElementOrder.getRightValue());
								ps.setString(7, compareElementOrder.getReserved1());
								ps.setString(8, compareElementOrder.getReserved2());
								ps.setLong(9, compareElementOrder.getIdentity());
							}
							
						});
				} else {
					throw new RuleException(String.format("%s该条件插入尚未支持", conditionOrder));
				}
			}

		};
		private OptypeEnum optypeEnum;
		
		public OptypeEnum optypeEnum() {
			return optypeEnum;
		}
		
		private ProcessorRulegy(OptypeEnum optypeEnum) {
			this.optypeEnum = optypeEnum;
		}
		
		public static ProcessorRulegy getOptypeEnum(OptypeEnum optypeEnum) {
			ProcessorRulegy pr = null;
			for (ProcessorRulegy processorRulegy : values()) {
				if (processorRulegy.optypeEnum == optypeEnum) {
					pr = processorRulegy;
					break;
				}
			}
			return pr;
		}
		
		abstract void execute(long ruleIdentity, JdbcTemplate jdbcTemplate,
								OracleSequenceMaxValueIncrementer incrementer, ConditionOrder conditionOrder,
								String tableNamePre, ObjectConditionOrder.CompareElementOrder compareElementOrder);
	}
	
	public enum ProcessorDeleteRule {
		DELETEOBJ(ModifyRuleOrder.IdentityOptype.objId) {
			
			@Override
			void execute(long identity, JdbcTemplate jdbcTemplate, String tableNamePre) {
				jdbcTemplate.update(OBJ_CONDTION_DELETE_EXP.replaceAll("#\\S*#", tableNamePre), identity);
			}
			
		},
		DELETEEVAL(ModifyRuleOrder.IdentityOptype.evalId) {
			
			@Override
			void execute(long identity, JdbcTemplate jdbcTemplate, String tableNamePre) {
				jdbcTemplate.update(EVAL_CONDITION_DELETE_EXP.replaceAll("#\\S*#", tableNamePre), identity);
			}
			
		};
		private ModifyRuleOrder.IdentityOptype identityOptype;
		
		public ModifyRuleOrder.IdentityOptype identityOptype() {
			return identityOptype;
		}
		
		private ProcessorDeleteRule(ModifyRuleOrder.IdentityOptype identityOptype) {
			this.identityOptype = identityOptype;
		}
		
		public static ProcessorDeleteRule getIdentityOptype(ModifyRuleOrder.IdentityOptype identityOptype) {
			ProcessorDeleteRule pdr = null;
			for (ProcessorDeleteRule processorDeleteRule : values()) {
				if (processorDeleteRule.identityOptype == identityOptype) {
					pdr = processorDeleteRule;
					break;
				}
			}
			return pdr;
		}
		
		abstract void execute(long identity, JdbcTemplate jdbcTemplate, String tableNamePre);
	}
	
	private static class EventExpressionSQL {
		
		public static final String SQL_TEMPLATE = "select identity , event_name ,version ,description , event_context , enable , raw_add_time , raw_update_time  from("
				+ "  select ire.* from("
													+ " #eventExpression# "
													+ " )tmp , #tableNamePre#internal_rule_event ire "
				+ " where tmp.event_name = ire.event_name and tmp.event_context = ire.event_context and ire.enable=1 order by tmp.priority desc  "
													+ " )where rownum < 2 ";
		
		/**
		 * @param eventName
		 * @param eventContext
		 * @return
		 */
		public static String sql(String eventName, Map<String, String> eventContext) {
			//针对表达式URL进行解析
			List<String> eventNames;
			String prefix = "";
			
			if (eventName.startsWith("event_exp://")) {
				eventNames = Lists.newArrayList(Splitter.on("/").trimResults().omitEmptyStrings()
					.split("/" + eventName.substring("event_exp://".length())));
				prefix = "/";
			} else {
				eventNames = Lists.newArrayList(eventName);
			}
			
			StringBuilder sql = new StringBuilder();
			
			String[] compositNames = new String[eventNames.size()];
			
			for (int i = 0, j = eventNames.size(); i < j; i++) {
				//-1. 每次赋值，将前一项拼接起来,特殊处理第一项。
				StringBuilder name = new StringBuilder();
				compositNames[i] = i == 0 ? name.append(prefix).append(eventNames.get(i)).toString() : name
					.append(compositNames[i - 1]).append("/").append(eventNames.get(i)).toString();
				
				//-2. 拼接sql
				sql.append("select ").append(i).append(" priority, ").append("'").append(compositNames[i])
					.append("' event_name , '").append(JSON.toJSON(eventContext)).append("' event_context ")
					.append(" from dual");
				
				//-3. 特殊处理最后一行
				if (i < j - 1) {
					sql.append(" union all ");
				}
			}
			
			return SQL_TEMPLATE.replaceAll("#eventExpression#", sql.toString());
		}
	}
}
