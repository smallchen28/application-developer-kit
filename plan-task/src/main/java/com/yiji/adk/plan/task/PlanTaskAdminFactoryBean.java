package com.yiji.adk.plan.task;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import com.yiji.adk.common.exception.PlanTaskException;
import com.yiji.adk.common.jdbc.AbstractRepositoryFactoryBean;
import com.yiji.adk.plan.task.statement.ActionStatement;
import com.yjf.common.concurrent.MonitoredThreadPool;

import javax.sql.DataSource;

public class PlanTaskAdminFactoryBean extends AbstractRepositoryFactoryBean<PlanTaskAdmin> implements FactoryBean<PlanTaskAdmin>, ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private PlanTaskAdmin admin;

	private ClassPathResource location;
	
	public PlanTaskAdminFactoryBean(DataSource dataSource , OracleSequenceMaxValueIncrementer incrementer , MonitoredThreadPool threadPool) {
		setDataSource(dataSource);
		this.admin = new PlanTaskAdmin(new JdbcTemplate(dataSource),incrementer,getTableNamePre(),threadPool);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

		super.afterPropertiesSet();

		if (location == null) {
			throw new PlanTaskException("初始化计划任务管理器出错，location为空。");
		}

		Map<String, ActionStatement> statements = applicationContext.getBeansOfType(ActionStatement.class);

		for (Iterator<Entry<String, ActionStatement>> it = statements.entrySet().iterator(); it.hasNext();) {
			this.admin.getTaskEngine().addStatementCache(it.next().getValue());
		}

		PlanTaskDefinitionLoader definitionLoader = new PlanTaskDefinitionLoader(this.admin);

		definitionLoader.load(location);
	}

	@Override
	public PlanTaskAdmin newInstance() {
		return admin;
	}

	@Override
	public PlanTaskAdmin getObject() throws Exception {
		
		return this.admin;
	}
	
	@Override
	public Class<PlanTaskAdmin> getObjectType() {
		
		return PlanTaskAdmin.class;
	}
	
	@Override
	public boolean isSingleton() {
		
		return true;
	}

	public void setLocation(ClassPathResource location) {
		
		this.location = location;
	}

}
