--规则引擎
drop table app_kit_internal_rule_event;
drop table app_kit_related_rule;
drop table app_kit_rule;
drop table app_kit_rule_eval_condition;
drop table app_kit_rule_obj_condition;
drop table app_kit_rule_policy;
drop table app_kit_rule_history;

drop sequence seq_app_kit_rule;


create table app_kit_rule(
  identity number(*,0) primary key ,
  rule_name varchar2(64) not null unique,
  description varchar2(256) not null,
  script varchar2(4000) not null,
  imports varchar2(512),
  globals varchar2(512),
  reserved1 varchar2(256),
  reserved2 varchar2(256),
  raw_add_time timestamp default systimestamp,
  raw_update_time timestamp default systimestamp
);


comment on table app_kit_rule is 'drools规则定义';
comment on column app_kit_rule.identity is '无意义序列';
comment on column app_kit_rule.description is '规则描述';
comment on column app_kit_rule.script is '脚本定义';
comment on column app_kit_rule.imports is '倒入包定义';
comment on column app_kit_rule.globals is '全局倒入定义' ;
comment on column app_kit_rule.raw_add_time is '创建时间' ;
comment on column app_kit_rule.raw_update_time is '最后更新时间' ;
comment on column app_kit_rule.reserved1 is '预留域1' ;
comment on column app_kit_rule.reserved2 is '预留域2' ;

create table app_kit_rule_history(
	identity number(*,0) primary key ,
	rule_name varchar2(64) not null,
	description varchar2(256) not null,
	script varchar2(4000) not null,
	imports varchar2(512),
	globals varchar2(512),
	raw_add_time timestamp not null,
	raw_update_time timestamp not null,
	delete_reason varchar2(512) not null ,
	opertor_ip varchar2(30) not null,
	opertor_id varchar2(30) not null,
  reserved1 varchar2(256),
  reserved2 varchar2(256),
  history_add_time timestamp default systimestamp,
	history_update_time timestamp default systimestamp
);

comment on table app_kit_rule_history is '删除规则历史定义';
comment on column app_kit_rule_history.identity is '删除相应规则标示';
comment on column app_kit_rule_history.description is '规则描述';
comment on column app_kit_rule_history.script is '脚本定义';
comment on column app_kit_rule_history.imports is '倒入包定义';
comment on column app_kit_rule_history.globals is '全局倒入定义' ;
comment on column app_kit_rule_history.raw_add_time is '原始规则创建时间' ;
comment on column app_kit_rule_history.raw_update_time is '原始规则最后更新时间' ;
comment on column app_kit_rule_history.delete_reason is '删除原因' ;
comment on column app_kit_rule_history.opertor_ip is '操作员ip' ;
comment on column app_kit_rule_history.opertor_id is '操作员id' ;
comment on column app_kit_rule_history.history_add_time is '最后更新时间' ;
comment on column app_kit_rule_history.history_update_time is '最后更新时间' ;
comment on column app_kit_rule.reserved1 is '预留域1' ;
comment on column app_kit_rule.reserved2 is '预留域2' ;

create sequence seq_app_kit_rule
 increment by 1 
 start with 1 
 nocycle
 cache 100;

create table app_kit_rule_eval_condition(
  identity number(*,0),
  rule_identity number(*,0) ,
  condition_script varchar2(512) not null,
  symbol varchar2(6) not null,
  request_name varchar2(64) not null,
  compare_value varchar2(64) not null,
  executor_name varchar2(64) not null
);

comment on table app_kit_rule_eval_condition is '规则条件定义（eval）' ;
comment on column app_kit_rule_eval_condition.identity is '无意义主键' ;
comment on column app_kit_rule_eval_condition.rule_identity is '规则主键' ;
comment on column app_kit_rule_eval_condition.condition_script is '条件脚本' ;
comment on column app_kit_rule_eval_condition.symbol is '条件比较符号' ;
comment on column app_kit_rule_eval_condition.request_name is '请求参数名称' ;
comment on column app_kit_rule_eval_condition.compare_value is '比较值' ;
comment on column app_kit_rule_eval_condition.executor_name is '执行器名称' ;

create table app_kit_rule_obj_condition(
  identity number(*,0),
  rule_identity number(*,0) ,
  variable_name varchar2(32) not null , 
  type_simple_name varchar2(64) not null ,
  join_symbol varchar2(8) ,
  symbol varchar2(8) not null,
  left_value varchar2(128) not null,
  right_value varchar2(64) not null
);


comment on table app_kit_rule_obj_condition is '规则条件定义(object)';
comment on column app_kit_rule_obj_condition.identity is '无意义主键' ;
comment on column app_kit_rule_obj_condition.rule_identity is '规则主键' ;
comment on column app_kit_rule_obj_condition.variable_name is '变量名称（冗余字段）';
comment on column app_kit_rule_obj_condition.type_simple_name is '变量简单类名（冗余字段）';
comment on column app_kit_rule_obj_condition.join_symbol is '连接符号(||,&&)';
comment on column app_kit_rule_obj_condition.symbol is '比较符号';
comment on column app_kit_rule_obj_condition.left_value is '左值';
comment on column app_kit_rule_obj_condition.right_value is '右值';
  
  

create table app_kit_internal_rule_event(
	identity number(*,0) primary key ,
	event_name varchar2(256) not null,
	version number(*,0) default 1 not null ,
	description varchar2(256),
	event_context varchar2(512) default '{}' not null,
  	enable varchar2(1) default '1' not null,
	raw_add_time timestamp default systimestamp,
	raw_update_time timestamp default systimestamp,
	constraint internal_rule_event_uq unique(event_name,event_context)
);

comment on table app_kit_internal_rule_event is '内部规则事件';
comment on column app_kit_internal_rule_event.identity is '无意义主键';
comment on column app_kit_internal_rule_event.enable is '开关';
comment on column app_kit_internal_rule_event.event_name is '事件名称';
comment on column app_kit_internal_rule_event.version is '版本';
comment on column app_kit_internal_rule_event.description is '描述';
comment on column app_kit_internal_rule_event.event_context is '事件上下文';
comment on column app_kit_internal_rule_event.raw_add_time is '增加时间';
comment on column app_kit_internal_rule_event.raw_update_time is '最后修改时间';

  
create table app_kit_rule_policy(
  identity number(*,0) primary key ,
  related_event number(*,0),
  description varchar2(256) not null ,
  raw_add_time timestamp default systimestamp,
  raw_update_time timestamp default systimestamp,
  enable varchar2(1) default '1' not null,
  risk_type varchar2(128) not null ,
  policy_from varchar2(128) not null
);

comment on table app_kit_rule_policy is '规则策略' ;
comment on column app_kit_rule_policy.identity is '无意义主键' ;
comment on column app_kit_rule_policy.related_event is '关联事件' ;
comment on column app_kit_rule_policy.description is '规则策略描述' ;
comment on column app_kit_rule_policy.raw_add_time is '增加时间' ;
comment on column app_kit_rule_policy.raw_update_time is '最后更新时间' ;
comment on column app_kit_rule_policy.enable is '开关';
comment on column app_kit_rule_policy.risk_type is '风险类型' ;
comment on column app_kit_rule_policy.policy_from is '策略来源' ;

--关联表
create table app_kit_related_rule(
	policy_identity number(*,0) , 
	rule_identity number(*,0) ,
	effective_time timestamp default null,
	expire_time timestamp default null,
	raw_add_time timestamp default systimestamp ,
	raw_update_time timestamp default systimestamp ,
	enable varchar2(1) default '1' not null ,
	async varchar2(1) default '0' not null ,
	loop varchar2(1) default '1' not null ,
	salicence int not null,
	related_context varchar2(256) default '{}' not null
);

comment on table app_kit_related_rule is '规则关联关系';
comment on column app_kit_related_rule.rule_identity is '关联策略';
comment on column app_kit_related_rule.effective_time is '生效时间';
comment on column app_kit_related_rule.expire_time is '失效时间';
comment on column app_kit_related_rule.raw_add_time is '增加时间';
comment on column app_kit_related_rule.raw_update_time is '最后更新时间';
comment on column app_kit_related_rule.enable is '开关；0：否、1：是，默认1';
comment on column app_kit_related_rule.async is '是否异步执行；0：否、1：是，默认0';
comment on column app_kit_related_rule.loop is '是否允许循环执行；0：否、1：是，默认1';
comment on column app_kit_related_rule.salicence is '执行优先级';
comment on column app_kit_related_rule.related_context is '关联上下文，允许开发者将业务信息存入以Map方式展现';

