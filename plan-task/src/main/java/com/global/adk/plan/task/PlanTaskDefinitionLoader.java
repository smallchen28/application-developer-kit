/**
 * www.global.com Inc. Copyright (c) 2011 All Rights Reserved.
 */
package com.global.adk.plan.task;

import com.global.adk.common.exception.PlanTaskException;
import com.global.adk.plan.task.module.PlanTaskTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 */
public class PlanTaskDefinitionLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(PlanTaskDefinitionLoader.class);
	
	private PlanTaskAdmin admin;
	
	public PlanTaskDefinitionLoader(PlanTaskAdmin admin) {
		this.admin = admin;
	}
	
	public void load(ClassPathResource resource) {
		
		if (logger.isInfoEnabled()) {
			logger.info("加在计划任务配置信息:{}", resource.getPath());
		}
		
		PlanTaskTable planTaskTable;
		
		try {
			
			JAXBContext context = JAXBContext.newInstance(PlanTaskTable.class);
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Marshaller marshaller = context.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf8");
			
			planTaskTable = (PlanTaskTable) unmarshaller.unmarshal(resource.getInputStream());
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			marshaller.marshal(planTaskTable, out);
			
			if (logger.isInfoEnabled()) {
				logger.info("加载PlanTaskTable\n{}\n配置xml\n{}", planTaskTable.toString(), new String(out.toByteArray(),
					"utf8"));
			}
			
		} catch (IOException | JAXBException ex) {
			throw new PlanTaskException("加载计划任务出错", ex);
		}
		
		admin.initializer(planTaskTable);
		
	}
}
