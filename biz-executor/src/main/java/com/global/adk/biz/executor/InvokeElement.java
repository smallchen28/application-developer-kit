package com.global.adk.biz.executor;

import com.global.adk.active.record.module.EntityObject;
import com.global.adk.biz.executor.annotation.Invoke;
import com.yjf.common.lang.result.StandardResultInfo;

/**
 * 执行元素
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月18日 上午1:26:45<br>
 * @see
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public abstract class InvokeElement {
	
	private String serviceName;
	
	private String logName;
	
	private Class<?> entityClass;
	
	private String resultClass;
	
	private boolean isAsync;
	
	private boolean isEntityInjectSpringBeans;
	
	private Invoke.SerialLock serialLock;
	
	private Invoke.TransactionAttribute transactionAttribute;
	
	private InvokeService invokeService;
	
	private Class[] validateGroups;
	
	public InvokeElement(String serviceName, Class[] validateGroups, String logName, InvokeService invokeService,
							Class<?> entityClass, String resultClass, boolean isAsync, Invoke.SerialLock serialLock,
							Invoke.TransactionAttribute transactionAttribute, boolean isEntityInjectSpringBeans) {
		
		this.serviceName = serviceName;
		this.validateGroups = validateGroups;
		this.logName = logName;
		this.entityClass = entityClass;
		this.resultClass = resultClass;
		this.invokeService = invokeService;
		this.isAsync = isAsync;
		this.transactionAttribute = transactionAttribute;
		this.serialLock = serialLock;
		this.isEntityInjectSpringBeans = isEntityInjectSpringBeans;
	}
	
	public abstract EntityObject newEntityObject();
	
	public abstract StandardResultInfo newResult();
	
	public String logName() {
		
		return logName;
	}
	
	public InvokeService getInvokeService() {
		
		return invokeService;
	}
	
	public String getServiceName() {
		
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		
		this.serviceName = serviceName;
	}
	
	public String getLogName() {
		
		return logName;
	}
	
	public void setLogName(String logName) {
		
		this.logName = logName;
	}
	
	public Class<?> getEntityClass() {
		
		return entityClass;
	}
	
	public void setEntityClass(Class<?> entityClass) {
		
		this.entityClass = entityClass;
	}
	
	public String getResultClass() {
		
		return resultClass;
	}
	
	public void setResultClass(String resultClass) {
		
		this.resultClass = resultClass;
	}
	
	public boolean isAsync() {
		
		return isAsync;
	}
	
	public void setAsync(boolean isAsync) {
		
		this.isAsync = isAsync;
	}
	
	public void setInvokeService(InvokeService invokeService) {
		
		this.invokeService = invokeService;
	}
	
	public Invoke.SerialLock getSerialLock() {
		
		return serialLock;
	}
	
	public void setSerialLock(Invoke.SerialLock serialLock) {
		
		this.serialLock = serialLock;
	}
	
	public Invoke.TransactionAttribute getTransactionAttribute() {
		
		return transactionAttribute;
	}
	
	public void setTransactionAttribute(Invoke.TransactionAttribute transactionAttribute) {
		
		this.transactionAttribute = transactionAttribute;
	}
	
	public boolean isEntityInjectSpringBeans() {
		return isEntityInjectSpringBeans;
	}
	
	public void setEntityInjectSpringBeans(boolean isEntityInjectSpringBeans) {
		this.isEntityInjectSpringBeans = isEntityInjectSpringBeans;
	}
	
	public Class[] getValidateGroups() {
		return validateGroups;
	}
}
