package com.global.adk.active.record.test;

import com.global.adk.active.record.DomainObjectCreator;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/11/2
 */
public class AppKitGenDomainObjectCreator22736502217113600 extends DomainObjectCreator {

    public AppKitGenDomainObjectCreator22736502217113600(
            com.global.adk.active.record.DomainFactory domainFactory,
            com.global.adk.active.record.ValidatorSupport validatorSupport,
            org.mybatis.spring.SqlSessionTemplate sqlSessionTemplate,
            Class domainObjectType,
            com.global.adk.active.record.InternalSeqCreator internalSeqCreator,
            com.global.adk.event.NotifierBus notifierBus
    ) {
        super(domainFactory,validatorSupport, sqlSessionTemplate, domainObjectType, internalSeqCreator,notifierBus);
    }

    public com.global.adk.active.record.module.DomainObject create(){
        com.global.adk.active.record.test.Order domainObject = new com.global.adk.active.record.test.Order();
        domainObject.setSqlSessionTemplate(this.sqlSessionTemplate);
        domainObject.setDomainFactory(this.domainFactory);
        if(domainObject instanceof com.global.adk.active.record.module.AbstractDomain){
            ((com.global.adk.active.record.module.AbstractDomain)domainObject).setNotifierBus(notifierBus);
        }
        if(domainObject instanceof com.global.adk.active.record.module.EntityObject){
            ((com.global.adk.active.record.module.EntityObject)domainObject).setInternalSeqCreator(internalSeqCreator);
        }
        if(domainObject instanceof com.global.adk.active.record.module.DomainObjectValidator){
            ((com.global.adk.active.record.module.DomainObjectValidator)domainObject).setValidatorSupport(this.validatorSupport);
        }	return domainObject;

    }

    public void refresh(com.global.adk.active.record.module.DomainObject domainObject){
        domainObject.setSqlSessionTemplate(this.sqlSessionTemplate);
        domainObject.setDomainFactory(this.domainFactory);
        if(domainObject instanceof com.global.adk.active.record.module.AbstractDomain){
            ((com.global.adk.active.record.module.AbstractDomain)domainObject).setNotifierBus(notifierBus);
        }
        if(domainObject instanceof com.global.adk.active.record.module.EntityObject){
            ((com.global.adk.active.record.module.EntityObject)domainObject).setInternalSeqCreator(internalSeqCreator);
        }
        if(domainObject instanceof com.global.adk.active.record.module.DomainObjectValidator){
            ((com.global.adk.active.record.module.DomainObjectValidator)domainObject).setValidatorSupport(this.validatorSupport);
        }
    }


}
