package com.global.adk.biz.executor;

import com.yjf.common.lang.result.StandardResultInfo;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 16/8/29
 */
public class ServiceContextHolder {

    private static ThreadLocal<ServiceContext> threadLocal = new ThreadLocal<>();

    public static <PARAM,RESULT extends StandardResultInfo> ServiceContext<PARAM,RESULT> get(){
        return threadLocal.get();
    }

    public static void set(ServiceContext serviceContext){
        threadLocal.set(serviceContext);
    }

    public static void clear(){
        threadLocal.remove();
    }

}
