package com.yiji.adk.common.jdbc;

import com.yiji.adk.common.exception.KitNestException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Properties;

public abstract class AbstractRepositoryFactoryBean<T> implements FactoryBean<T>, InitializingBean {
	
	private String tableNamePre = "app_kit_";
	
	private TransactionProxyFactoryBean proxyFactoryBean = new TransactionProxyFactoryBean();
	
	private PlatformTransactionManager transactionManager;

	private Class<T> targetType;
	
	public void setDataSource(DataSource ds) {
		if (ds == null) {
			throw new KitNestException("ds == null 初始化事务管理器失败.");
		}
		this.transactionManager = new DataSourceTransactionManager(ds);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.isTrue(transactionManager != null, "事务管理器未初始化，需注入数据源(DataSources).");
		
		initGenericType();
		
		Properties transactionAttributes = new Properties();
		
		try {
			transactionAttributes.load(new StringReader("store=PROPAGATION_REQUIRED\n"
														+ "resotre=PROPAGATION_REQUIRED\n"
														+ "destroy=PROPAGATION_REQUIRED\n"
														+ "active=PROPAGATION_NOT_SUPPORTED,readOnly\n"
														+ "load*=PROPAGATION_NOT_SUPPORTED,readOnly\n"
														+ "find*=PROPAGATION_NOT_SUPPORTED,readOnly\n"
														+ "query*=PROPAGATION_NOT_SUPPORTED,readOnly\n"
														+ "tx*=PROPAGATION_REQUIRED\n"
														+ "*=PROPAGATION_NOT_SUPPORTED,readOnly\n"));
		} catch (IOException e) {
			throw new KitNestException("遇得到哟，这个来io异常,可能吗？", e);
		}
		//- 根据配置进行切面处理，无需独立pointcut
		proxyFactoryBean.setTransactionAttributes(transactionAttributes);
//		proxyFactoryBean.setProxyInterfaces(new Class<?>[] { targetType });
		proxyFactoryBean.setTarget(newInstance());
		proxyFactoryBean.setProxyTargetClass(true);

		
		proxyFactoryBean.setTransactionManager(transactionManager);
		
		proxyFactoryBean.afterPropertiesSet();
	}
	
	@SuppressWarnings("unchecked")
	private void initGenericType() {
		
		Type factoryBeanType = getClass().getGenericSuperclass();
		
		if (!(factoryBeanType instanceof ParameterizedType)) {
			throw new KitNestException("RepositoryFactoryBean 实现过程不可忽略范型");
		}
		
		targetType = (Class<T>) ((ParameterizedType) factoryBeanType).getActualTypeArguments()[0];
	}
	
	public abstract T newInstance();
	
	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		
		return (T) proxyFactoryBean.getObject();
	}
	
	@Override
	public Class<T> getObjectType() {
		
		return targetType;
		
	}
	
	public boolean isSingleton() {
		
		return true;
	}
	
	public String getTableNamePre() {
		
		return tableNamePre;
	}
	
	public void setTableNamePre(String tableNamePre) {
		
		this.tableNamePre = tableNamePre;
	}

}
