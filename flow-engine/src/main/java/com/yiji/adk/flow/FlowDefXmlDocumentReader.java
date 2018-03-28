/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow;

import com.yiji.adk.common.exception.FlowException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;

/**
 * 活动定义分析器
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-1-20 下午7:11<br>
 * @see
 * @since 1.0.0
 */
public class FlowDefXmlDocumentReader {
	public static final String ACTIVITY_XSD_PATH = "/META-INF/kit/flow_def.xsd";
	
	private DocumentBuilder domBuilder;
	
	private Validator validator;
	
	private FlowContext flowContext;
	
	private FlowDefParser parser = new FlowDefParser();
	
	public FlowDefXmlDocumentReader(FlowContext flowContext) {
		try {
			this.flowContext = flowContext;
			
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			this.domBuilder = builderFactory.newDocumentBuilder();
			
			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			ClassPathResource resource = new ClassPathResource(ACTIVITY_XSD_PATH);
			Schema schema = schemaFactory.newSchema(resource.getURL());
			this.validator = schema.newValidator();
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new FlowException("构建XML解析对象过程中出现错误", e);
		}
	}
	
	public void analyze(Resource resource) {
		
		//-1. 根据xsd检查xml合法性
		validate(resource);
		
		//-2. 创建Domcument对象
		Document document = createDocument(resource);
		
		//-3. 处理流程定义
		parse(document);
	}
	
	private void parse(Document document) {
		flowContext.registry(parser.parse(document.getDocumentElement()));
	}
	
	private Document createDocument(Resource resource) {
		Document document ;
		try {
			document = domBuilder.parse(resource.getInputStream());
		} catch (SAXException e) {
			throw new FlowException("分析xml文件出错...", e);
		} catch (IOException e) {
			throw new FlowException("加载xml文件失败...", e);
		}
		return document;
	}
	
	private void validate(Resource resource) {
		try {
			Source source = new StreamSource(resource.getURL().openStream());
			this.validator.validate(source);
		} catch (SAXException e) {
			throw new FlowException("xml验证失败...", e);
		} catch (IOException e) {
			throw new FlowException("加载xml文件失败...", e);
		}
	}
	
}
