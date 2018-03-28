/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-19 14:31 创建
 *
 */
package com.global.adk.api.annotation;

import java.lang.annotation.*;

/**
 * dubbo服务api.用于标注到对外服务接口
 * <p/>
 * <p/>
 * <p/>
 * 1.标识这个服务为dubbo服务<br/>
 * 2.标注了此注解并且使用adk-container的应用，将会自动生成dubbo服务， 应用只需要实现invoker即可(invoker serviceName为
 * DubboServiceAPI所标注服务的"简单名.方法名")
 *
 * @author karott (e-mail:chenlin@yiji.com)
 * @deprecated  karott 默认可以使用，但是不利于服务提供方未来做不同服务版本和分组的实现，会导致做法不统一
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface DubboServiceAPI {

    /**
     * 服务分组
     */
    String group() default "";

    /**
     * 服务版本
     */

    String version();

}
