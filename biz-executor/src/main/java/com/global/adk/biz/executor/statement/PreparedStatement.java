package com.global.adk.biz.executor.statement;

import com.global.adk.active.record.DomainFactory;
import com.global.adk.active.record.module.EntityObject;
import com.global.adk.biz.executor.ServiceContext;
import com.global.adk.biz.executor.event.InitEvent;
import com.global.adk.biz.executor.event.ServiceApplyEvent;
import com.global.adk.common.Constants;
import com.global.adk.common.exception.InitializerException;
import com.global.adk.event.Subscribe;
import com.global.common.lang.result.StandardResultInfo;
import com.global.common.lang.result.Status;

public class PreparedStatement {
	
	@Subscribe(priority = 2, isAsync = false)
	public void preparedResult(ServiceApplyEvent event) {
		
		try {
			ServiceContext<?, ?> serviceContext = event.value();

			StandardResultInfo result = serviceContext.getInvokeElement().newResult();
			
			result.setStatus(Status.SUCCESS);

			result.setCode(Constants.SUCCESS_CODE);

			result.setDescription("处理成功");
			
			serviceContext.setResult(result);
			
		} catch (Exception e) {
			throw new InitializerException(String.format("%s构建默认应答出错.", event.value().getInvokeElement()
				.getInvokeService().getClass()), e);
		}
	}
	
	@Subscribe(priority = 3, isAsync = false)
	public void preparedEntityObject(ServiceApplyEvent event) {
		
		try {
			ServiceContext<?, ?> serviceContext = event.value();
			
			EntityObject entityObject = serviceContext.getInvokeElement().newEntityObject();
			DomainFactory domainFactory = event.source().getDomainFactory();
			if (entityObject != null && domainFactory != null) {
				domainFactory.refresh(entityObject, serviceContext.getInvokeElement().isEntityInjectSpringBeans());
			}
			serviceContext.setEntityObject(entityObject);
		} catch (Exception e) {
			throw new InitializerException(String.format("%s构建领域模型时出错.", event.value().getInvokeElement()
				.getInvokeService().getClass()), e);
		}
		
	}
	
	@Subscribe(isAsync = false)
	public void initializer(InitEvent event) {
		
	}
	
}
