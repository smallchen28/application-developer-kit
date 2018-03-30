
DROP TABLE app_kit_plan_task;
DROP SEQUENCE seq_app_kit_plan_task;
DROP TABLE app_kit_task_statement;


--计划任务
create table app_kit_plan_task(
       task_identity number(*,0) primary key,
       task_name varchar2(256) not null,
       task_type varchar2(20) default 'cron' not null,
       task_expiration number(*,0),
       repeat_flag varchar2(5) default 'TRUE' not null,
       task_exp varchar(50) not null,
       exec_status varchar2(10) default 'INIT' not null ,
       exec_count int default 0 not null ,
       exec_max_count int default -1 not null ,
       exec_start_time timestamp ,
       exec_last_time timestamp ,
       exec_next_time timestamp ,
       exec_context varchar2(1024),
       exec_memo varchar2(256) ,
       current_stat_name varchar2(50) ,
       crt_jobd varchar2(256) , 
       raw_add_time timestamp not null
);

comment on table app_kit_plan_task is '计划任务表' ;
comment on column app_kit_plan_task.task_identity is '无意义自增主键';
comment on column app_kit_plan_task.task_name is '任务名称';
comment on column app_kit_plan_task.task_type is '任务类别，默认cron';
comment on column app_kit_plan_task.task_expiration is '任务过期时间,单位为秒,为空时则永不过期';
comment on column app_kit_plan_task.repeat_flag is '任务是否允许重复执行标记TRUE：允许、FALSE：不允许 默认为TRUE';
comment on column app_kit_plan_task.task_exp is '任务表达式,如 * * * 1 0 0';
comment on column app_kit_plan_task.exec_status is '任务执行状态 INITIAL-初始 SUCCESS-成功 FAILURE-失败 PROCESSING-处理中';
comment on column app_kit_plan_task.exec_count is '执行次数';
comment on column app_kit_plan_task.exec_max_count is '最大执行次数，当为 -1 时表示可以任意执行';
comment on column app_kit_plan_task.exec_start_time is '任务开始执行时间（全局）';
comment on column app_kit_plan_task.exec_last_time is '最后一次执行时间（不能成功执行时会）';
comment on column app_kit_plan_task.exec_context is '执行上下文，全局参数保存在此处，通过json格式进行保存';
comment on column app_kit_plan_task.exec_memo is '任务执行摘要描述';
comment on column app_kit_plan_task.current_stat_name is '任务执行的当前条目';
comment on column app_kit_plan_task.crt_jobd is '任务创建描述';
comment on column app_kit_plan_task.raw_add_time is '任务添加时间,下次执行时间';

create sequence seq_app_kit_plan_task
 increment by 1 
 start with 1 
 nocycle
 cache 100;
 
create table app_kit_task_statement(
       task_identity number(*,0) ,
       task_stat_name varchar2(256) not null,
       stat_class_type varchar2(256) not null ,
       exec_priority int not null,
       exec_status varchar2(10) DEFAULT 'INIT' not null ,
       exec_info varchar2(1024) ,
       crt_jobd varchar2(256),
       start_time timestamp ,
       end_time timestamp,
       exec_count int
);

comment on table app_kit_task_statement is '计划任务条目表' ;
comment on column app_kit_task_statement.task_identity is '关联计划任务主键';
comment on column app_kit_task_statement.task_stat_name is '任务条目名称';
comment on column app_kit_task_statement.stat_class_type is '任务条目完整类路径';
comment on column app_kit_task_statement.exec_priority is '任务条目执行优先级';
comment on column app_kit_task_statement.exec_status is '任务条目执行状态';
comment on column app_kit_task_statement.exec_info is '执行信息，包括错误详情。';
comment on column app_kit_task_statement.crt_jobd is '创建任务条目描述';
comment on column app_kit_task_statement.start_time is '任务条目开始执行时间（单条任务条目）';
comment on column app_kit_task_statement.end_time is '最后一次任务条目执行时间（单条任务条目）';
comment on column app_kit_task_statement.exec_count is '执行次数';

