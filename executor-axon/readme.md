## 依赖
```
<dependency>
    <groupId>com.yiji.adk</groupId>
    <artifactId>executor-axon</artifactId>
</dependency>
```

## 1启用
```
yiji.adk.executor.enable=true(依赖adk-executor)
yiji.adk.executor.axon.enable=true
```
## 2实现

### 2.1聚合
```
@org.axonframework.commandhandling.model.AggregateRoot
public class XXSubAggregate {

    @org.axonframework.commandhandling.model.AggregateIdentifier
    /**
    * 实体标识
    * 可以使对象，里面做好toString()方法即可
    */
    private String identifier;
}
```
可以继承adk原有AggregateRoot不受影响,或者暂时什么都不继承


### 2.2仓储
**基础类型**

仓储统一实现```com.yiji.adk.executor.axon.repository.AbstractAggregateRepository<T>```
其中T为对应聚合对象类型

**构造函数**

```
@bean
public AbstractAggregateRepository xxSubRepository(AbstractAggregateDBLockFactory xxSubLockFactory){
  return new XXSubRepository(XXSub.class,xxSubLockFactory);
}
```
AbstractAggregateDBLockFactory用于实现聚合对象锁处理，可以针对每一个聚合对象实现一个lockFactory子对象并进行注入。也可以在构造函数上默认实现，如下：

```
public XXSubAggregateRepository(Class<T> aggregateType) {
    super(aggregateType, () -> new AbstractAggregateDBLockFactory() {
        @Override
        protected void lockDB(String identifier) {
            //通过AbstractAggregateDBLockFactory#lockMapper方法获取相关mapper，执行锁动作
            //lockMapper(XXMapper.class).lock(identifier);
        }
    });
}
```

+ 如果实现了lockFactory，则仓储的load和newInstance方法会自动加锁
+ 仓储load方法自带缓存，所以可以多次load
+ 仓储只提供根据identifier标识标准的load方法，原则上只需要这个即可，如果需要灵活通过其它条件load,需要注入子仓储类型并添加自定义方法
+ 批量及强事务保证类功能，可以灵活使用newInstanceX,loadX,saveX,deleteX方法，默认情况不需要使用这些方法


### 2.3统一命令invoke

统一继承```com.yiji.adk.executor.axon.container.AbstractCommandInvoke<PARAM, RESULT extends StandardResultInfo>```

可实现如下方法：
```
/**
 * 做些前置处理，不包括创建插入存储操作，无事务
 */
protected void doBefore(ServiceContext<PARAM, RESULT> serviceContext) {

}

/**
 * 做幂等处理,实现可直接调用 {@link #createNewRootWithIdempotence(Repository, Callable, ServiceContext, Function)}实现或者自行实现
 * <p/>
 * 处于invoke事务中
 */
protected abstract void doIdempotence(ServiceContext<PARAM, RESULT> serviceContext);


/**
 * 应用处理，处于invoke事务中
 */
protected abstract void doInvoke(ServiceContext<PARAM, RESULT> serviceContext);

/**
 * 后置处理，做些转换之类的，无事务
 */
protected void doAfter(ServiceContext<PARAM, RESULT> serviceContext) {

}

/**
 * 结尾处理，无论是否异常都会执行,无事务
 */
protected void doEnd(ServiceContext<PARAM, RESULT> serviceContext) {

}
```

**invoke示例及说明**
```
public class XXSubInvoke extends AbstractCommandInvoke<OrderBase, StandardResultInfo> {

    @Autowired
    private AbstractAggregateRepository xxSubRepository;


    @Override
    protected void doIdempotence(ServiceContext<OrderBase, StandardResultInfo> serviceContext) {

        createNewRootWithIdempotence(xxSubRepository, () -> {
            XXSubAggregate xxSubAggregate = new XXSubAggregate();

            //初始化等处理

            return xxSubAggregate;
        }, serviceContext, context -> {
            logger.warn("[重复幂等].................");
            //幂等后相关处理

            //返回true会跳过后续doInvoke,doAfter，也可以通过抛出业务异常中断后续doInvoke,doAfter处理
            return false;
        });

    }

    @Override
    protected void doInvoke(ServiceContext<OrderBase, StandardResultInfo> serviceContext) {
        //前提：使用createNewRootWithIdempotence做幂等创建处理，否则需要自己往serviceContext塞aggregate
        Object aggregate = serviceContext.getAggregate();

        //如果仓储实现了lockFactory,则后续不用再次锁住业务对象，因为每一次load和newInstance默认实现了悲观锁(防止部分同学经常忘记加锁)
        //aggregate.xxDomainMethod();

        //如果需要删除聚合对象，则需要调用AbstractAggregateRepository.markCurrentRootDeleted();

        //注：后续不需要执行仓储更新、删除等方法,会自动处理
    }
}
```
按照以上示例，期间不需要调用仓储更新及删除等方法，会自动处理


## 说明

+ 使用axonframework对DDD及Axon完整特性
+ 目前屏蔽事件命令、溯源、工作单元等事情，提供原有开发使用模式
+ 后续再实践其它特性
