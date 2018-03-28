package com.global.adk.active.record;

import com.global.adk.active.record.module.DomainObject;
import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.exception.DomainException;
import com.global.adk.event.NotifierBus;
import javassist.CtClass;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 领域模型工厂
 *
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于2014年9月15日 下午2:21:50<br>
 * @see
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public class DomainFactory implements ApplicationContextAware {
	
	protected static final Logger logger = LoggerFactory.getLogger(DomainFactory.class);
	
	private AutowireCapableBeanFactory autowireCapableBeanFactory;
	
	private ValidatorSupport validatorSupport;
	
	private SqlSessionTemplate sqlSessionTemplate;
	
	private NotifierBus notifierBus;
	
	private InternalSeqCreator internalSeqCreator;
	
	private Map<Class<DomainObject>, DomainObjectCreator> domainObjectCreatorMapper = new ConcurrentHashMap<>();
	
	public DomainFactory(SqlSessionTemplate sqlSessionTemplate, InternalSeqCreator internalSeqCreator) {
		this(sqlSessionTemplate, null, internalSeqCreator);
	}

	public DomainFactory(SqlSessionTemplate sqlSessionTemplate,NotifierBus notifierBus, InternalSeqCreator internalSeqCreator) {
		this(sqlSessionTemplate, notifierBus, internalSeqCreator,null);
	}
	
	public DomainFactory(SqlSessionTemplate sqlSessionTemplate, NotifierBus notifierBus,
							InternalSeqCreator internalSeqCreator,ValidatorSupport validatorSupport) {
		if (internalSeqCreator == null) {
			throw new DomainException("初始化DomainFactory出错InternalSeqCreator不可为空");
		}
		
		if (sqlSessionTemplate == null) {
			throw new DomainException("初始化DomainFactory出错SqlMapClientTemplate不可为空");
		}
		
		this.sqlSessionTemplate = sqlSessionTemplate;
		
		this.internalSeqCreator = internalSeqCreator;

		this.notifierBus = notifierBus == null ? new NotifierBus(null) : notifierBus; //不提供时默认为关闭异步事件

		this.validatorSupport = validatorSupport;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
	}
	
	public <T> void registerListener(T target) {
		notifierBus.register(target);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DomainObject> T newInstance(Class<T> domainObjectType, boolean isAutowire) {
		
		DomainObject domainObject = obtainCreator(domainObjectType).create();
		if (isAutowire) {
			autowireCapableBeanFactory.autowireBeanProperties(domainObject, AutowireCapableBeanFactory.AUTOWIRE_NO,
				false);
			autowireCapableBeanFactory.initializeBean(domainObject, "domainObject");
		}
		return (T) domainObject;
	}
	
	public <T extends DomainObject> T newInstance(Class<T> domainObjectType) {
		
		return newInstance(domainObjectType, false);
	}
	
	public void refresh(DomainObject domainObject, boolean isAutowire) {
		obtainCreator(domainObject.getClass()).refresh(domainObject);
		if (isAutowire) {
			autowireCapableBeanFactory.autowireBeanProperties(domainObject, AutowireCapableBeanFactory.AUTOWIRE_NO,
				false);
			autowireCapableBeanFactory.initializeBean(domainObject, "domainObject");
		}
	}
	
	@SuppressWarnings("unchecked")
	private DomainObjectCreator obtainCreator(Class<? extends DomainObject> domainObjectType) {
		
		DomainObjectCreator domainObjectCreator = domainObjectCreatorMapper.get(domainObjectType);
		
		if (domainObjectCreator == null) {
			domainObjectCreator = newCreator(domainObjectType);
			domainObjectCreatorMapper.put((Class<DomainObject>) domainObjectType, domainObjectCreator);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("DomainObject{}->DomainObjectCreator{}", domainObjectType, domainObjectCreator);
		}
		
		return domainObjectCreator;
	}
	
	private DomainObjectCreator newCreator(Class<? extends DomainObject> domainObjectType) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("开始构建DomainObjectCreator==>{}", domainObjectType);
		}
		
		String createScript = null;
		String refreshScript = null;
		try {
			//- 构建javassist
			CtClass ctClass = com.yiji.adk.common.compiler.Compiler.getInstance().newCtClass(DomainObjectCreator.class);
			
			String className = ctClass.getSimpleName();
			String targetClassName = domainObjectType.getName();
			
			//- 构造器强化
			String constructorScript = generateConstructor(className);
			//- 强化方法
			createScript = generateCreate(targetClassName);
			refreshScript = generateRefresh();
			
			if (logger.isDebugEnabled()) {
				logger.debug("{}->script \nconstruct :\n{}\ncreate:\n{}\nrefresh:\n{}", className, constructorScript,
					createScript, refreshScript);
			}
			
			Compiler.getInstance().constructImplement(ctClass, constructorScript.toString());
			Compiler.getInstance().methodWeave(ctClass, DomainObjectCreator.class, createScript.toString())
				.methodWeave(ctClass, DomainObjectCreator.class, refreshScript.toString());
			
			Class<?>[] parameterTypes = new Class<?>[] {DomainFactory.class, ValidatorSupport.class, SqlSessionTemplate.class, Class.class,
														InternalSeqCreator.class, NotifierBus.class };
			return Compiler.getInstance()
				.newInstance(
					ctClass,
					parameterTypes,
					new Object[] { this,validatorSupport, sqlSessionTemplate, domainObjectType, internalSeqCreator,
									notifierBus });
		} catch (SecurityException | IllegalArgumentException e) {
			throw new DomainException(String.format("编译/生存实例过程中出现错误createScript=%s refreshScript=%s", createScript,
				refreshScript));
		}
		
	}
	
	private String generateRefresh() {
		
		StringBuilder refreshScript = new StringBuilder();
		refreshScript
			.append("public void refresh(com.yiji.adk.active.record.module.DomainObject domainObject){\n")
			.append("\t")
			.append("domainObject.setSqlSessionTemplate(this.sqlSessionTemplate);\n")
			.append("\t")
			.append("domainObject.setDomainFactory(this.domainFactory);\n")
			.append("\t")
			.append("if(domainObject instanceof com.yiji.adk.active.record.module.AbstractDomain){\n\t\t")
			.append("((com.yiji.adk.active.record.module.AbstractDomain)domainObject).setNotifierBus(notifierBus);\n\t")
			.append("}\n\t")
			.append("if(domainObject instanceof com.yiji.adk.active.record.module.EntityObject){\n\t\t")
			.append(
				"((com.yiji.adk.active.record.module.EntityObject)domainObject).setInternalSeqCreator(internalSeqCreator);\n\t")
			.append("}\n\t")
			.append("if(domainObject instanceof com.yiji.adk.active.record.module.DomainObjectValidator){\n")
			.append("\t\t")
			.append(
				"((com.yiji.adk.active.record.module.DomainObjectValidator)domainObject).setValidatorSupport(this.validatorSupport);")
			.append("\n\t}").append("\n}");
		
		return refreshScript.toString();
	}
	
	private String generateCreate(String targetClassName) {
		
		StringBuilder createScript = new StringBuilder();
		createScript
			.append("public com.yiji.adk.active.record.module.DomainObject create(){\n")
			.append("\t")
			.append(targetClassName)
			.append(" domainObject = new ")
			.append(targetClassName)
			.append("();\n")
			.append("\t")
			.append("domainObject.setSqlSessionTemplate(this.sqlSessionTemplate);\n")
			.append("\t")
			.append("domainObject.setDomainFactory(this.domainFactory);\n")
			.append("\t")
			.append("if(domainObject instanceof com.yiji.adk.active.record.module.AbstractDomain){\n\t\t")
			.append("((com.yiji.adk.active.record.module.AbstractDomain)domainObject).setNotifierBus(notifierBus);\n\t")
			.append("}\n\t")
			.append("if(domainObject instanceof com.yiji.adk.active.record.module.EntityObject){\n\t\t")
			.append(
				"((com.yiji.adk.active.record.module.EntityObject)domainObject).setInternalSeqCreator(internalSeqCreator);\n\t")
			.append("}\n\t")
			.append("if(domainObject instanceof com.yiji.adk.active.record.module.DomainObjectValidator){\n")
			.append("\t\t")
			.append(
				"((com.yiji.adk.active.record.module.DomainObjectValidator)domainObject).setValidatorSupport(this.validatorSupport);")
			.append("\n\t}").append("\treturn domainObject;\n").append("\n}");
		
		return createScript.toString();
	}
	
	private String generateConstructor(String className) {
		
		StringBuilder constructorScript = new StringBuilder();
		constructorScript
			.append("public ")
			.append(className)
			.append("(\n\t")
			.append("com.yiji.adk.active.record.DomainFactory domainFactory,\n\t")
			.append("com.yiji.adk.active.record.ValidatorSupport validatorSupport,\n\t")
			.append("org.mybatis.spring.SqlSessionTemplate sqlSessionTemplate, \n\t")
			.append("Class domainObjectType, \n\t")
			.append("com.yiji.adk.active.record.InternalSeqCreator internalSeqCreator,\n\t")
			.append("com.yiji.adk.event.NotifierBus notifierBus\n")
			.append(") {\n\t")
			.append(
				"super(domainFactory,validatorSupport, sqlSessionTemplate, domainObjectType, internalSeqCreator,notifierBus);\n")
			.append("}");
		
		return constructorScript.toString();
	}
	
	public ValidatorSupport getValidatorSupport() {
		
		return validatorSupport;
	}
	
	public void setValidatorSupport(ValidatorSupport validatorSupport) {
		
		this.validatorSupport = validatorSupport;
	}
}
