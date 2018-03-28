/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.flow.module;

/**
 * @author hasulee
 * @version 1.0.0
 * @email ligen@yiji.com
 * @history hasuelee创建于15-3-17 下午3:14<br>
 * @see
 * @since 1.0.0
 *
 * @author karott 枚举大小写适配[1.21.1-SNAPSHOT line 26]
 */
public enum NodeType {

    ACTIVE_NODE,
    AUTO_TASK,
    RETRY_TASK,;

    public static NodeType get(String name) {
        NodeType snt = null;
        for (NodeType nodeType : values()) {
			if (nodeType.name().equals(adapt(name))) {
				snt = nodeType;
                break;
			}
        }

        return snt;
    }

    //由于之前NodeType中的枚举为小写，现改为大写，而历史流程配置都是小写，这里需要转换适配
    private static String adapt(String name){
        //这里不会存在NPE
        return name.toUpperCase();
    }
}
