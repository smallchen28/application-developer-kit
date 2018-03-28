# 1 基础能力
从adk总的readme.md里面copy过来

# 2  重试能力
为flowengine增加重试特性！

## 2.1 基础配置
yiji.adk.flowengine.enable=true  启动流程引擎
yiji.adk.flowengine.enableRetry=true  启动流程引擎重试特性

## 2.2 数据表创建
+ mysql：flow-engine.jar!/resources/db/mysql/flowengine.ddl.sql
+ oracle：flow-engine.jar!/resources/db/oracle/flowengine.ddl.sql

## 2.3 特性支持

### 2.3.1 重试类型

+ 立即重试：当前线程立即重试

---

配置示例：
```
  <auto_task name="BizError" trigger_class="com.yiji.adk.flow.test.fastpay.BizError">
        <condition>
            <transition event="biz_over" description="构建完成错误结果结束流程" to="Finished"/>
        </condition>
    </auto_task>

    <retry_task target="BizError" name="retryValidate" retryFail="failFast"    retryInfo="2,1000"       trigger_class="com.yiji.adk.flow.test.fastpay.FastPayRetryTrigger"/>
```

为BizError创建重试任务retryValidate。retryInfo="2,1000" 即 最大重试两次,且间隔1000毫秒执行，
如果只配置2,则可以由应用自己控制时间
PS：此类型重试如果重试次数太大，可能导致应用资源占用过多，所以flowengine内部
限制retryMax<=3

+ 重试到爆：无止境重试，特定场景使用

---

配置示例：
```
  <auto_task name="BizError" trigger_class="com.yiji.adk.flow.test.fastpay.BizError">
        <condition>
            <transition event="biz_over" description="构建完成错误结果结束流程" to="Finished"/>
        </condition>
    </auto_task>
    <!-- 重试节点 -->
    <retry_task target="BizError" name="retryValidate" retryFail="failBomb" retryInfo="2"
                trigger_class="com.yiji.adk.flow.test.fastpay.FastPayRetryTrigger"/>
```
同上，retryInfo="2"，表示最大次数，但是会被忽略

+ 退避重试：以一定频率衰退递减重试，常用于通知商户场景

---
配置示例：
```
  <auto_task name="BizError" trigger_class="com.yiji.adk.flow.test.fastpay.BizError">
        <condition>
            <transition event="biz_over" description="构建完成错误结果结束流程" to="Finished"/>
        </condition>
    </auto_task>
    <retry_task target="BizError" name="retryValidate" retryFail="failRetreat" retryInfo="6,1,hour,byDouble"
                trigger_class="com.yiji.adk.flow.test.fastpay.FastPayRetryTrigger"/>
```
retryInfo="6,1,hour,byDouble"，即最大次数=6，间隔单元1，小时，成倍递减
PS:成倍递减：1,2,4,8,16...  flowEngine内部控制最大次数为8次


### 2.3.2 重试调度个性化粒度

+ 应用手动配置schedule(目前)

  根据节点及频率分组配置：

<pre>
  failType:失败重试类型，必须.failRetreat/failBomb
  orderBy:排序字段，必须.startTime/updateTime
  sortBy:排序方式，可选，asc/desc.默认asc
  batch:批量大小，可选，默认50
  retryNodes:重试节点(retry_task节点的name属性值)，可选，比如 settlementRetry`chargeRetry`accountRetry`notifyRetry
</pre>

+ 自动注册schedule
暂不支持

### 2.3.3 重试任务隔离

重试到爆类型任务比较特殊，为了不对其它任务产生影响，对这类任务进行了隔离

### 2.3.4 重试超限流转指定节点
为retry_task配置retryMaxLimitNode属性，意味着重试达到最大次数后会自动流转到该节点

### 2.3.5 扩展回调

以上配置在重试节点的FastPayRetryTrigger触发类，需要继承com.yiji.adk.flow.state.retry.FlowRetryTrigger类.

```
重试前回调：
protected  void beforeRetry(String orderId, String flowName, int version, String retryNode,
										String targetNode, RetryFailTypeEnum retryFail, int retryMax, int retryTimes,
										Date startTime, Date lastRetryTime);
```

```
重试后回调：
protected  void afterRetry(	String orderId, String flowName, int version, String retryNode,
										String targetNode, RetryFailTypeEnum retryFail, int retryMax, int retryTimes,
										Date startTime, Date lastRetryTime);
```

```
获取聚合对象回调：
protected  Object target(String orderId);
提供覆盖业务对象的机会
```

```
获取扩展信息回调：
protected  Map<String, Object> attachment(String orderId, Object target);
提供覆盖业务信息的机会
```

### 2.3.6 hera配置支持
暂不支持
