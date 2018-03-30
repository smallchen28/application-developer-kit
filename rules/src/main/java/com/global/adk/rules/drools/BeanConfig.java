package com.global.adk.rules.drools;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class BeanConfig {
	
	@Bean(autowire = Autowire.NO, name = { "compilerExecutor" })
	public DynamicConditionExecutor createCompilerConditionExecutor() {
		CompilerDynamicExecutor compilerDynamicExecutor = new CompilerDynamicExecutor();
		return compilerDynamicExecutor;
	}
	
	@Bean(autowire = Autowire.NO, name = { "groovyExecutor" })
	public DynamicConditionExecutor createGroovyConditionExecutor() {
		GroovyDynamicExecutor groovyDynamicExecutor = new GroovyDynamicExecutor();
		return groovyDynamicExecutor;
	}

}
