package com.global.adk.rules.drools;

import com.global.adk.common.Constants;
import com.global.adk.common.exception.RuleException;
import com.global.adk.rules.drools.module.*;
import com.yjf.common.lang.beans.Copier;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.conf.KnowledgeBaseOptionsConfiguration;
import org.drools.io.impl.ReaderResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class DroolsTemplate {
	
	private Logger logger = LoggerFactory.getLogger(DroolsTemplate.class);
	
	private static final String VM_TEMPLATE_PATH = "/META-INF/kit/adk_drools_template.vm";
	
	private static final String VM_NONE_PATH = "/META-INF/kit/adk_drools_none_template.vm";
	
	private VelocityEngine velocityEngine;

	private KnowledgeBuilderConfiguration knowledgeBuilderConfiguration ;

	private KnowledgeBaseConfiguration knowledgeBaseConfiguration;

	private KnowledgeBase defaultKnowlegeBase;
	
	public DroolsTemplate(ClassPathResource velocityProps, ClassPathResource ruleProps) {

		if(velocityProps == null || ruleProps == null){
			throw new RuleException(String.format("启动初始化drools引擎过程中，ruleProps、velocityProps不可为空，ruleProps=%s,velocityProps=%s",velocityProps,ruleProps));
		}

		velocityEngine = new VelocityEngine();
		
		try {
			iniVelocity(PropertiesLoaderUtils.loadProperties(velocityProps));
			iniDroolsConf(PropertiesLoaderUtils.loadProperties(ruleProps));
		} catch (IOException e) {
			throw new RuleException(String.format("读取配置文件过程中出错,error=[%s]", e.getMessage()));
		}
		
		velocityEngine.init();

		initDefaultRule();
	}

	private void initDefaultRule(){
		Template template = velocityEngine.getTemplate(VM_NONE_PATH);
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		logger.info("init default rule\n{}", writer);
		defaultKnowlegeBase = createKnowledgeBase(writer);
	}
	
	private void iniDroolsConf(Properties ruleProps) {
//		System.setProperty("drools.dialect.java.compiler","JANINO");
//		System.setProperty("drools.dialect.java.lngLevel","1.6");
		this.knowledgeBaseConfiguration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(ruleProps);

		//暂时木有啥添加滴
		this.knowledgeBuilderConfiguration = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
	}
	
	private void iniVelocity(Properties velocityProps) {
		
		initProps(velocityProps);
		
		initRuntime();
		
	}
	
	private void initRuntime() {
		
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.setProperty("classpath.resource.loader.cache", "true");
		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new VelocitySlf4jLogChute());
	}
	
	private void initProps(Properties velocityProps) {
		
		for (Iterator<Entry<Object, Object>> it = velocityProps.entrySet().iterator(); it.hasNext();) {
			Entry<Object, Object> entry = it.next();
			velocityEngine.setProperty(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	public void render(DroolsEngine.DroolsElement element, ModuleView moduleView) {
		//如果是默认规则，将初始化缓存的KnowlegeBase直接set到element中
		if(element.getDescription().getEventRuleType() == Description.EventRuleType.DEFAULT){
			element.setKnowledgeBase(defaultKnowlegeBase);
			return;
		}

		VelocityContext context = new VelocityContext();
		context.put("moduleView", moduleView);
		context.put("timeStamp", new Date().getTime());


		Template template = velocityEngine.getTemplate(VM_TEMPLATE_PATH);
		
		StringWriter writer = new StringWriter();
		
		template.merge(context, writer);
		
		logger.info("{}->{}->rules\n{}", new Object[] { element.getRequest(), moduleView.getRuleEvent(), writer });
		
		KnowledgeBase knowledgeBase = createKnowledgeBase(writer);
		
		element.setKnowledgeBase(knowledgeBase);
	}
	
	private KnowledgeBase createKnowledgeBase(StringWriter writer) {
		
		//- 构建器，与base一致，每次渲染需要重新构建。
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder(knowledgeBuilderConfiguration);
		//- 将渲染完成得模版增加到
		builder.add(new ReaderResource(new StringReader(writer.toString())), ResourceType.DRL);
		//- 检查规则，此检查为冗余步骤，防止测试阶段脏数据。
		check(builder);
		//- 创建KnowledgeBase
		KnowledgeBase knowledge = KnowledgeBaseFactory.newKnowledgeBase(knowledgeBaseConfiguration);
		//- 
		knowledge.addKnowledgePackages(builder.getKnowledgePackages());

		return knowledge;
	}
	
	private void check(KnowledgeBuilder builder) {
		
		StringBuilder info = new StringBuilder();
		
		if (builder.hasErrors()) {
			KnowledgeBuilderErrors errors = builder.getErrors();
			for (Iterator<KnowledgeBuilderError> it = errors.iterator(); it.hasNext();) {
				info.append("加载规则库出现错误，详情：{}").append(it.next()).append("/n");
			}
		}
		
		if (!info.toString().equals("")) {
			throw new RuleException(String.format("加载规则过程中出现错误/n%s", info.toString()));
		}
	}
	
	//- 恶心地检查 fuck！
	public void checkRule(Rule rule) {
		DroolsEngine.DroolsElement element = new DroolsEngine.DroolsElement(null, new Description("规则检查",
			Description.EventRuleType.STANDARD), -2);
		
		InternalRuleEvent event = new InternalRuleEvent();
		event.setEventName("测试规则插入");
		RulePolicy policy = new RulePolicy();
		RelatedRule relatedRule = new RelatedRule();
		event.getPolicy().add(policy);
		policy.getRelatedRules().add(relatedRule);
		Copier.copy(rule, relatedRule);
		relatedRule.getConditions().addAll(rule.getConditions());
		relatedRule.getImports().addAll(rule.getImports());
		relatedRule.getGlobals().addAll(rule.getGlobals());
		relatedRule.setEnable(true);
		relatedRule.setAsync(true);
		relatedRule.setLoop(true);
		relatedRule.setSalicence(0);
		
		relatedRule.getGlobals().add("com.global.adk.rules.drools.DynamicConditionExecutor compilerExecutor");
		ModuleView moduleView = new ModuleView(event, null, Constants.DROOLS_DEFAULT_DELAY_SENCD);
		
		logger.info("规则注册验证检查...");
		render(element, moduleView);
	}
	
}
