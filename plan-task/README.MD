### 目录
* [10. 计划任务](#10.)  
	* [10.1 使用计划任务](#10.1)  
	* [10.2 配置计划任务](#10.2)  
	* [10.3 计划任务数据结构](#10.3)

<h3 id="10."> 10. 计划任务（测试阶段） </h3>
> 计划任务是用于解决复杂定时任务中的有效性关联性问题的一个组件，举个例子来说，当有一个定时任务需要在00:30-05:30执行，但只允许执行一次，在执行失败的情况下需要就重试，并且这个任务分成了多个子任务进行执行，每个一个节点执行完成后不应当重复执行，另外，每个任务之间是有一定关联关系的，必须在一个子任务完成之后才可以执行，也许还会有些其他的要求，像……@#￥%。   
> 试想一下如果我们使用scheduler系统提供的功能来完成这项任务，会有哪些步骤。

> * 创建一个定时任务，像这样"0 30/1 0-5 * * ? "每天凌晨00：30-05：30每个一分钟执行一次。
> * 在数据库中创建任务主体以及子任务，按照一定的维度建立唯一约束来防止重复创建任务。
> * 通过唯一约束来发现今天创建的任务存在时，开始执行子任务，根据执行结果，更新主体及子任务状态。
> * 每次任务执行过程中，都需要出错任务进行检查，并进行补偿。

> 除开这种方式，当然，你也可以采取一个子任务一个定时配置然后通过其他的方式来检查。但无论采用什么方式，远没有像说这么简单，实现阶段通常都会相当繁琐。


> _***Maven依赖***_

>```
<dependency>
	<groupId>application-developer-kit</groupId>
	<artifactId>plan-task</artifactId>
	<version>${adk.version}</version>
</dependency>	
```

<h4 id="10.1"> 10.1 使用计划任务 </h4>
> 模拟日终过程的计划任务

>```
<tasks>
    <task name="daily-endprocess-inter">
        <task-type>cron</task-type>
        <!-- task-type>interval</task-type -->
        <task-expiration>-1</task-expiration>
        <repat-flag>true</repat-flag>
        <exec-max-count>600</exec-max-count>
        <task-exp>* * * 1 10 *</task-exp>
        <exec-context>{'id':'10','name':'dep-settle'}</exec-context>
        <exec-memo>初创</exec-memo>
        <crt-jobd>创建描述</crt-jobd>
        <statements>
            <statement task-state-name="DAILY_CUT_ACCTRANS_SERVICE"
                       state-class-type="com.yiji.adk.plan.task.test.ActionStatement1" crt-jobd="账务日切"
                       exec-priority="1"/>
            <statement task-state-name="DAILY_FINAL_ACCTRANS_NOTIFER"
                       state-class-type="com.yiji.adk.plan.task.test.ActionStatement2" crt-jobd="日终通知"
                       exec-priority="2"/>
            <statement task-state-name="DAILY_FINAL_ACCTRANS_QUERY"
                       state-class-type="com.yiji.adk.plan.task.test.ActionStatement3" crt-jobd="日终账务轮询"
                       exec-priority="3"/>
            <statement task-state-name="DAILY_JOURNALS_CHANGE"
                       state-class-type="com.yiji.adk.plan.task.test.ActionStatement4" crt-jobd="日终日记账"
                       exec-priority="4"/>
            <statement task-state-name="DAILY_FINAL_ACCOUNT_REPORT"
                       state-class-type="com.yiji.adk.plan.task.test.ActionStatement5" crt-jobd="日终报表生成"
                       exec-priority="5"/>
        </statements>
    </task>
</tasks>
```

> _***元素说明***_


> * _***task:***_ 计划任务定义
>	* _***name:***_ 任务名称，必填参数。
> * _***task-expiration:***_ 任务过期时长，单位毫秒，默认：0表示永不过期。
> * _***repat-flag:***_ 出错后是否允许重复执行，如果不允许整个任务将被置为失败，默认：false。
> * _***exec-max-count:***_ 最大执行次数，默认：0表示无限次数
> * _***task-exp:***_ 任务cron表达式，不支持day－of－weak（周字段），L（当月最后一天），W（最接近指定值得工作日），＃（第几个周几）等，必选参数。
> 		* "0 0 * * * *" = 每天、每小时0分0秒时执行。
> 		* "* 10 * * * *" = 每隔10分钟执行一次。
> 		* "0 0 8-10 * * *" = 8，9，10点整执行
>		* "0 0/30 8-10 * *" = 每天8:00,8:30, 9:00, 9:30 、10 执行.
>		* "0 0 0 25 12 ＊" = 每年12月25日 00:00:00执行
>		* "*" 表示所有值；
> 		* "?" 表示未说明的值，即不关心它为何值；
> 		* "-" 表示一个指定的范围；
>		* "," 表示附加一个可能值；
>		* "/" 符号前表示开始时间，符号后表示每次递增的值
> * _***exec-context:***_ 会注入到ActionContext的上下文参数。
> * _***exec-memo:***_ 执行时摘要信息。
> * _***crt-jobd:***_ 创建时描述
> * _***statement:***_ 任务执行条目。
> 		* _***task-state-name:***_ 子任务名称。
> 		* _***state-class-type:***_ 子任务执行器完整类路径。
> 		* _***exec-priority:***_ 执行优先级。

> 说明一下,这里并不是在启动线程池来造一个轮子，而是通过外部定时调度系统（scheduler）来进行驱动的，它仅仅是对当前时间进行计算匹配，另外，cron表达式支持完整有多难，而是觉得没有太多必要，这样就可以了，基本和cron规范差不多。

> _***子任务定义***_

>```
@Component
public class ActionStatement implements ActionStatement {	
	@Override
	public ExecutorResult execute(ActionContext actionContext) {
		ExecutorResult result = new ExecutorResult();
		result.setExecInfo("action statement 1");
		result.setExecutorStatus(ExecutorStatus.FAILD);
		return result;
	}	
}
```

> * 通过实现com.yiji.adk.plan.task.statement.ActionStatement接口对子任务进行定义。

> * 在execute方法中通过使用ActionContext可以获得部分信息
> 		* taskName: 子任务名称
> 		* startTime：子任务开始时间
> 		* exeCount：子任务执行次数
> 		* execContext：配置时注入的配置的exec-context属性，并且可以对该属性进行属性的添加，它是一个HashMap实现。
> 		* elapsed：主体任务执行到目前的时间消耗。

> * ExecutorResult返回时系统将根据返回的ExecutorStatus进行状态变更，可选值有以下几个。
> 		* success: 子任务执行成功，系统会根据整体任务状态判断最终主体任务是否成功，如果cun。
> 		* fail：子任务执行失败，此时系统会将主体任务置为失败。
> 		* processing:子任务执行状态不明确，主体任务状态为处理中。
> 		* error：子任务执行出错，此时的主体任务也将出错。
> 		* init：为初始状态。


<h4 id="10.2"> 10.2 配置计划任务 </h4>
> _***Spring3.x配置***_

>```
<bean id="planTaskAdmin" class="com.yiji.adk.plan.task.PlanTaskAdminFactoryBean"
	c:incrementer-ref="planTaskIncrementer"
	c:threadPool-ref="threadPool"
  	c:dataSource-ref="dataSource"
  	p:tableNamePre="app_kit_"
	p:location="/plantask-test.xml"/>
```

> * _***incrementer:***_ spring提供的自增序列器，具体为Oracle实现，
org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer该参数为必填参数。


> * _***threadPool:***_ 线程池支持，计划任务是建立在异步处理基础之上的，所以线程池是必填参数。

> * _***dataSource:***_ 计划任务涉及与数据库交互，需要提供数据源支持，必填参数。

> * _***location:***_ 计划任务定义路径，该路径必须在classpath之下，不支持多文件加载。

> _***yiji-boot配置***_

> \#jdbc依赖   
> yiji.ds.enable=true    
> yiji.ds.url=jdbc:oracle   
> yiji.ds.username=xxoo   
> yiji.ds.password=ooxx   
> \#计划任务开启，默认：关闭   
> yiji.adk.plantask.enable=true   
> \#指定计划任务配置路径   
> yiji.adk.plantask.location=/plantask-test.xml   
> \#以下为可选参数(当不存在线程池配置时生效)   
> yiji.adk.executor.corePoolSize		#核心线程数(默认:2)  
> yiji.adk.executor.maxPoolSize		#最大线程数(默认:10)  
> yiji.adk.executor.keepAliveSeconds	#限制线程保持时间(默认:300秒)  
> yiji.adk.executor.queueCapacity		#线程队列容量(默认:50)    


<h4 id="10.3"> 10.3 计划任务数据结构 </h4>
> 计划任务的支持是建立在Oracle之上的，目前还不支持其他数据库。

>```
DROP TABLE app_kit_plan_task;
DROP SEQUENCE seq_app_kit_plan_task;
DROP TABLE app_kit_task_statement;
>   
> -------------计划任务-------------
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
>   
> -------------主体任务序列序列-------------
create sequence seq_app_kit_plan_task
 increment by 1 
 start with 1 
 nocycle
 cache 100;
>   
> -------------子任务-------------
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
```