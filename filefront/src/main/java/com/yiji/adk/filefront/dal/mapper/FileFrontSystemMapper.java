/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * chenlin@yiji.com 2016-09-04 17:21 创建
 *
 */
package com.yiji.adk.filefront.dal.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author karott
 */
public interface FileFrontSystemMapper {
	
	@Select("SELECT SEQ_FILEFRONT.nextVal FROM DUAL")
	long getNextSequence();
}
