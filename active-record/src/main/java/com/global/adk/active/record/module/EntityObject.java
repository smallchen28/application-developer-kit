package com.global.adk.active.record.module;

import com.global.adk.active.record.InternalSeqCreator;
import com.global.adk.common.exception.DomainException;

import java.util.Date;

public abstract class EntityObject extends DomainObjectValidator {
	
	private static final long serialVersionUID = 379082586467146862L;
	
	private long identity;
	
	private String bizNo;
	
	private InternalSeqCreator internalSeqCreator;
	
	private Date rawAddTime;
	
	private Date rawUpdateTime;
	
	public long getIdentity() {
		
		return identity;
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
	}
	
	public String getBizNo() {
		
		return bizNo;
	}
	
	public void setBizNo(String bizNo) {
		
		this.bizNo = bizNo;
	}
	
	public InternalSeqCreator internalSeqCreator() {
		return internalSeqCreator;
	}
	
	@Override
	public String generateBizNo(String sequenceName, boolean isOverrideIdentity, String bizPrefix) {
		
		if (internalSeqCreator == null) {
			throw new DomainException(String.format("序列号生存器InternalSeqCreator尚未初始化，不支持的操作..."));
		}
		
		long genSeq = internalSeqCreator.generateIdentity(sequenceName);
		
		bizNo = internalSeqCreator.generateBizNo(genSeq, bizPrefix);
		
		if (isOverrideIdentity) {
			this.identity = genSeq;
		}
		
		return bizNo;
	}
	
	@Override
	public void generateIdentity(String sequenceName) {
		
		if (internalSeqCreator == null) {
			throw new DomainException(String.format("序列号生存器InternalSeqCreator尚未初始化，不支持的操作..."));
		}
		
		this.identity = internalSeqCreator.generateIdentity(sequenceName);
	}
	
	public void setInternalSeqCreator(InternalSeqCreator internalSeqCreator) {
		this.internalSeqCreator = internalSeqCreator;
	}
	
	public Date getRawAddTime() {
		return rawAddTime;
	}

	public void setRawAddTime(Date rawAddTime) {
		this.rawAddTime = rawAddTime;
	}

	public Date getRawUpdateTime() {
		return rawUpdateTime;
	}

	public void setRawUpdateTime(Date rawUpdateTime) {
		this.rawUpdateTime = rawUpdateTime;
	}

	public abstract void reference(AggregateRoot ref);
}
