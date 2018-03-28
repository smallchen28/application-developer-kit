/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.active.record;

import com.global.adk.active.record.module.DomainObject;
import com.global.adk.event.NotifierBus;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 实体对象生存器，动态编译生存newConsturct setter模式全部继承于他。
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0
 * @version 1.0.0
 * @see
 *
 * @history hasuelee创建于2014年9月29日 下午6:18:02<br>
 */
@SuppressWarnings({ "rawtypes", "deprecation" })
public abstract class DomainObjectCreator {
	
	protected ValidatorSupport validatorSupport;
	
	protected SqlSessionTemplate sqlSessionTemplate;
	
	protected Class domainObjectType;
	
	protected InternalSeqCreator internalSeqCreator;
	
	protected NotifierBus notifierBus;

	protected DomainFactory domainFactory;
	
	public DomainObjectCreator(DomainFactory domainFactory , ValidatorSupport validatorSupport, SqlSessionTemplate sqlSessionTemplate,
								Class domainObjectType, InternalSeqCreator internalSeqCreator, NotifierBus notifierBus) {
		this.domainFactory = domainFactory;
		this.validatorSupport = validatorSupport;
		this.sqlSessionTemplate = sqlSessionTemplate;
		this.domainObjectType = domainObjectType;
		this.internalSeqCreator = internalSeqCreator;
		this.notifierBus = notifierBus;
	}
	
	protected abstract DomainObject create();
	
	protected abstract void refresh(DomainObject domainObject);
	
}
