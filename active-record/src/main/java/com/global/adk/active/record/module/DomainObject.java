/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record.module;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.annotation.SqlBinder;
import com.global.adk.common.exception.DomainException;
import com.global.common.util.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 实体对象基础定义
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月29日 下午6:18:32<br>
 * @see
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public abstract class DomainObject extends AbstractDomain implements ActiveRecord {
	
	private static final long serialVersionUID = 2723530289879644298L;

	private DomainFactory domainFactory ;

	private SqlSessionTemplate sqlSessionTemplate;

	private String insert;
	
	private String delete;
	
	private String update;
	
	private boolean isSqlBinderSupport = false;
	
	@SuppressWarnings("unchecked")
	public DomainObject() {
		Class<DomainObject> entityType = ((Class<DomainObject>) getClass());
		if (entityType.isAnnotationPresent(SqlBinder.class)) {
			SqlBinder sqlBinder = entityType.getAnnotation(SqlBinder.class);
			this.insert = sqlBinder.insert();
			this.delete = sqlBinder.delete();
			this.update = sqlBinder.update();
		} else {
			//不使用SqlBinder
			//throw new DomainException(String.format("领域模型构建失败，该模型定义时未进行sqlbinder绑定,annotations = %s", entityType.getAnnotations().toString()));
			return;
		}
		
		if (StringUtils.isBlank(delete)) {
			throw new DomainException("执行delete操作过程中，sql 映射语句为空");
		}
		
		if (StringUtils.isBlank(insert)) {
			throw new DomainException("执行insert操作过程中，sql 映射语句为空");
		}
		
		if (StringUtils.isBlank(update)) {
			throw new DomainException("执行update操作过程中，sql 映射语句为空");
		}
		
		isSqlBinderSupport = true;
	}
	
	@Override
	public int delete() {
		checkSupport();
		return sqlSessionTemplate.delete(delete, this);
	}
	
	private void checkSupport() {
		if (!isSqlBinderSupport) {
			throw new DomainException("域对象没有绑定SqlBinder,不支持此操作");
		}

		//检查一个就可以了
		if(sqlSessionTemplate == null){
			throw new DomainException("DomainObject未初始化完成，不支持此操作");
		}
	}
	
	@Override
	public DomainObject insert() {
		checkSupport();
		sqlSessionTemplate.insert(insert, this);
		return this;
	}
	
	@Override
	public int update() {
		checkSupport();
		return sqlSessionTemplate.update(update, this);
	}
	
	@Override
	public <T> boolean load(T uniqueKey, String queryID) {
		checkSupport();
		boolean exists = false;
		DomainObject domainObject = sqlSessionTemplate.selectOne(queryID, uniqueKey);
		if (domainObject != null) {
			this.convertFrom(domainObject);
			exists = true;
		}
		return exists;
	}

	@Override
	public DomainFactory domainFactory() {
		return domainFactory;
	}

	public SqlSessionTemplate sqlSessionTemplate(){
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setDomainFactory(DomainFactory domainFactory){
		this.domainFactory = domainFactory;
	}
	
	protected abstract String generateBizNo(String sequenceName, boolean isOverrideIdentity, String bizPrefix);
	
	protected abstract void generateIdentity(String sequenceName);
}
