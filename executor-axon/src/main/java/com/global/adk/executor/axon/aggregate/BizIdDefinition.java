/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-19 14:31 创建
 *
 */
package com.global.adk.executor.axon.aggregate;

import com.yjf.common.util.ToString;

import java.io.Serializable;

/**
 * id系列定义值，不包括实体id
 *
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class BizIdDefinition implements Serializable{

    private String gid;
    private String reqId;
    private String merchantOrderNo;
    private String partnerId;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
