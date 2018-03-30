/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-04 20:11 创建
 *
 */
package com.global.adk.filefront.support.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.global.adk.filefront.exceptions.FileFrontException;
import com.global.boot.core.Apps;
import com.yjf.common.lang.result.Status;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文件配置对象解析器,组件或业务方可注入使用(或静态工具使用),解析配置
 *
 * <p/>
 *
 * 使用前提：1、启用filefront 或 2、应用自己调用{@link #initConfigs}初始化
 *
 * @author karott
 */
public class FileConfigParser extends PathMatchingResourcePatternResolver implements FileConfigContext {
	private static final Logger logger = LoggerFactory.getLogger(FileConfigParser.class);
	
	private final static String LINE_SEPERATOR = System.getProperty("line.separator", "\n");
	private final static Map<String, Document> configDocs = Maps.newHashMap();
	private final static String SUFFIX = "-" + System.getProperty(Apps.SPRING_PROFILE_ACTIVE);
	
	/**
	 * 初始化配置
	 * @param location 配置路径
	 */
	public synchronized void initConfigs(String location) {
		// 加载所有规则
		try {
			List<Resource> realConfigs = forRealConfigs(getResources(location));
			
			SAXReader saxReader = new SAXReader();
			Document newDoc;
			String bizType;
			for (Resource config : realConfigs) {
				newDoc = saxReader.read(config.getURL());
				
				// TODO 配置项检查
				// TODO 公共配置项抽取覆盖
				
				bizType = newDoc.selectSingleNode(FILE_CONFIG).valueOf(BIZ_TYPE);
				if (configDocs.containsKey(bizType)) {
					logger.warn("FileFront存在重复配置,忽略.{},{}", bizType, newDoc.getName());
					continue;
				}
				configDocs.put(bizType, newDoc);
			}
		} catch (FileFrontException fe) {
			throw fe;
		} catch (Exception e) {
			throw new FileFrontException("文件配置解析异常", e);
		}
	}
	
	private List<Resource> forRealConfigs(Resource[] resources) {
		List<Resource> realConfigs = Lists.newArrayList();
		
		List<Resource> realBizConfigs = Lists.newArrayList();
		List<Resource> realSysConfigs = Lists.newArrayList();
		
		String resName;
		Set<String> repeatConfigs = Sets.newHashSet();
		for (Resource res : resources) {
			resName = res.getFilename();
			
			if (resName.startsWith("biz-")) {
				logger.info("找到ftp业务配置:{}", resName);
				realBizConfigs.add(res);
			} else if (resName.startsWith("sys-")) {
				if (resName.endsWith(SUFFIX + ".xml")) {
					logger.info("找到环境感知ftp系统配置:{}", resName);
					realSysConfigs.add(res);
					repeatConfigs.add(resName.replace(SUFFIX, "-default"));
				}
			} else {
				throw new FileFrontException(String.format("出现非法ftp命名配置:%s,必须以biz-或者sys-开头", resName));
			}
		}
		
		//添加默认系统配置
		for (Resource res : resources) {
			resName = res.getFilename();
			if (resName.startsWith("sys-") && resName.endsWith("-default.xml")) {
				if (!repeatConfigs.contains(resName)) {
					logger.info("加入默认ftp系统配置:{}", resName);
					realSysConfigs.add(res);
				}
			}
		}
		
		if (realBizConfigs.size() == 0) {
			throw new FileFrontException("真的没有一个业务配置文件？哪怕一个", null);
		}
		if (realSysConfigs.size() == 0) {
			throw new FileFrontException("真的没有一个系统配置文件？哪怕一个", null);
		}
		
		realConfigs.addAll(realBizConfigs);
		realConfigs.addAll(realSysConfigs);
		
		return realConfigs;
	}
	
	private static String trimedStr(String str) {
		if (null == str) {
			return null;
		}
		return str.replaceAll(LINE_SEPERATOR, "").trim();
	}
	
	/**
	 * 通过bizType+nodePath获取节点内容
	 * @param bizType 业务类型
	 * @param nodePath 节点路径
	 * @return 节点内容
	 *
	 * @throws com.yiji.adk.filefront.exceptions.FileFrontException 文件配置异常
	 */
	public static String textByNode(String bizType, String nodePath) throws FileFrontException {
		Document doc = configDocs.get(bizType);
		if (null == doc) {
			throw new FileFrontException(String.format("文件配置不存在,%s", bizType), "", Status.FAIL);
		}
		
		Node node = doc.selectSingleNode(nodePath);
		if (null == node) {
			throw new FileFrontException(String.format("配置节点不存在,%s,%s", bizType, nodePath), "", Status.FAIL);
		}
		
		return trimedStr(node.getText());
	}
	
	/**
	 * 通过bizType+nodePath获取节点属性
	 * @param bizType 业务类型
	 * @param nodePath 节点路径
	 * @return 节点内容
	 *
	 * @throws com.yiji.adk.filefront.exceptions.FileFrontException 文件配置异常
	 */
	public static String attrByNode(String bizType, String nodePath, String attr) throws FileFrontException {
		Document doc = configDocs.get(bizType);
		if (null == doc) {
			throw new FileFrontException(String.format("文件配置不存在,%s", bizType), "", Status.FAIL);
		}
		
		Node node = doc.selectSingleNode(nodePath);
		if (null == node) {
			throw new FileFrontException(String.format("配置节点不存在,%s,%s,%s", bizType, nodePath, attr), "", Status.FAIL);
		}
		
		return trimedStr(node.valueOf(attr));
	}
	
	/**
	 * 通过bizType+nodePath获取节点内容
	 * @param bizType 业务类型
	 * @param nodePath 节点路径
	 * @return 节点内容
	 *
	 * @throws com.yiji.adk.filefront.exceptions.FileFrontException 文件配置异常
	 */
	public static String textByNodeSystem(String bizType, String nodePath) throws FileFrontException {
		Document doc = configDocs.get(bizType);
		if (null == doc) {
			throw new FileFrontException(String.format("文件配置不存在,%s", bizType), "", Status.FAIL);
		}
		
		String extendsOption = attrByNode(bizType, SYSTEM, EXTENDS);
		if (null != extendsOption && Boolean.valueOf(extendsOption)) {
			String fileService = attrByNode(bizType, SYSTEM, FILE_SERVICE);
			String tenant = attrByNode(bizType, FILE_CONFIG, TENANT);
			doc = configDocs.get(tenant + fileService);
			if (null == doc) {
				throw new FileFrontException(String.format("文件配置不存在,%s,%s", bizType, fileService), "", Status.FAIL);
			}
		}
		
		Node node = doc.selectSingleNode(nodePath);
		if (null == node) {
			throw new FileFrontException(String.format("System配置节点不存在,%s,%s", bizType, nodePath), "", Status.FAIL);
		}
		
		return trimedStr(node.getText());
	}
	
	/**
	 * 通过bizType+nodePath获取节点属性
	 * @param bizType 业务类型
	 * @param nodePath 节点路径
	 * @return 节点内容
	 *
	 * @throws com.yiji.adk.filefront.exceptions.FileFrontException 文件配置异常
	 */
	public static String attrByNodeSystem(String bizType, String nodePath, String attr) throws FileFrontException {
		Document doc = configDocs.get(bizType);
		if (null == doc) {
			throw new FileFrontException(String.format("文件配置不存在,%s", bizType), "", Status.FAIL);
		}
		
		String extendsOption = attrByNode(bizType, SYSTEM, EXTENDS);
		if (null != extendsOption && Boolean.valueOf(extendsOption)) {
			String fileService = attrByNode(bizType, SYSTEM, FILE_SERVICE);
			String tenant = attrByNode(bizType, FILE_CONFIG, TENANT);
			doc = configDocs.get(tenant + fileService);
			if (null == doc) {
				throw new FileFrontException(String.format("文件配置不存在,%s,%s", bizType, fileService), "", Status.FAIL);
			}
		}
		
		Node node = doc.selectSingleNode(nodePath);
		if (null == node) {
			throw new FileFrontException(String.format("配置节点不存在,%s,%s,%s", bizType, nodePath, attr), "", Status.FAIL);
		}
		
		return trimedStr(node.valueOf(attr));
	}
}
