-- Add/modify columns 
alter table APP_KIT_RULE_EVAL_CONDITION add script_identity number(10);
-- Add comments to the columns 
comment on column APP_KIT_RULE_EVAL_CONDITION.script_identity is '脚本条件属性ID';

--2017-05-15 表结构变更
-- Add/modify columns
alter table app_kit_rule_obj_condition add reserved1 varchar2(256);
alter table app_kit_rule_obj_condition add reserved2 varchar2(256);
-- Add comments to the columns
comment on column app_kit_rule_obj_condition.reserved1 is '预留域1';
comment on column app_kit_rule_obj_condition.reserved2 is '预留域2';
-- Add/modify columns
alter table app_kit_rule_eval_condition add reserved1 varchar2(256);
alter table app_kit_rule_eval_condition add reserved2 varchar2(256);
-- Add comments to the columns
comment on column app_kit_rule_eval_condition.reserved1 is '预留域1';
comment on column app_kit_rule_eval_condition.reserved2 is '预留域2';