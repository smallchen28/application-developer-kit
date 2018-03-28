/*
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017/2/8-17:31 创建
 *
 */

package com.yiji.adk.flow.state.retry;

import com.yiji.adk.flow.state.FlowTrace;

/**
 * 重试类型
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public enum RetryRetreatTypeEnum {

    BY_DOUBLE("byDouble", "成倍") {
        @Override
        public void retreatNextTime(FlowTrace flowTrace) {
            int timeUnit = flowTrace.getRetryMeta().getRetreatUnit();
            int skipUnit = timeUnit * (flowTrace.getRetryTimes() + 1);
            flowTrace.getRetryMeta().getRetreatTimeUnit().calUnitTime(flowTrace, skipUnit);
        }
    },

    BY_FIXED("byFixed", "固定") {
        @Override
        public void retreatNextTime(FlowTrace flowTrace) {
            //固定方式，直接追加固定时间单元
            flowTrace.getRetryMeta().getRetreatTimeUnit().calUnitTime(flowTrace,
                    flowTrace.getRetryMeta().getRetreatUnit());
        }
    },;

    /**
     * 枚举值
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;

    public abstract void retreatNextTime(FlowTrace flowTrace);

    /**
     * @param code    枚举值
     * @param message 枚举描述
     */
    private RetryRetreatTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return RetryType
     */
    public static RetryRetreatTypeEnum getByCode(String code) {
        for (RetryRetreatTypeEnum _enum : values()) {
            if (_enum.getCode().equals(code)) {
                return _enum;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举
     *
     * @return List<RetryType>
     */
    public static java.util.List<RetryRetreatTypeEnum> getAllEnum() {
        java.util.List<RetryRetreatTypeEnum> list = new java.util.ArrayList<RetryRetreatTypeEnum>(values().length);
        for (RetryRetreatTypeEnum _enum : values()) {
            list.add(_enum);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     *
     * @return List<String>
     */
    public static java.util.List<String> getAllEnumCode() {
        java.util.List<String> list = new java.util.ArrayList<String>(values().length);
        for (RetryRetreatTypeEnum _enum : values()) {
            list.add(_enum.getCode());
        }
        return list;
    }

    /**
     * 通过code获取msg
     *
     * @param code 枚举值
     * @return
     */
    public static String getMsgByCode(String code) {
        if (code == null) {
            return null;
        }
        RetryRetreatTypeEnum _enum = getByCode(code);
        if (_enum == null) {
            return null;
        }
        return _enum.getMessage();
    }

    /**
     * 获取枚举code
     *
     * @param _enum
     * @return
     */
    public static String getCode(RetryRetreatTypeEnum _enum) {
        if (_enum == null) {
            return null;
        }
        return _enum.getCode();
    }
}
