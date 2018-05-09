package com.global.adk.active.record.module;

import com.global.adk.event.NotifierBus;
import com.yjf.common.lang.beans.Copier;

/**
 *
 * 领域模型骨架实现，实现基础转换能力
 */
public abstract class AbstractDomain implements Domain {
	
	private static final long serialVersionUID = 6947719096548733506L;
	
	private static final String[] EMPTY = new String[0];
	
	private NotifierBus notifierBus;
	
	@Override
	public <DTO> void convertFrom(DTO from) {
		convertFrom(from, EMPTY);
	}
	
	@Override
	public <DTO> void convertFrom(DTO dto, String... ignore) {
		Copier.copy(dto, this, ignore);
	}
	
	@Override
	public <DTO> void convertTo(DTO dto) {
		convertTo(dto, EMPTY);
	}
	
	@Override
	public <DTO> void convertTo(DTO dto, String... ignore) {
		Copier.copy(this, dto, ignore);
	}
	
	@Override
	public void publish(Object... events) {
		notifierBus.dispatcher(events);
	}
	
	public void setNotifierBus(NotifierBus notifierBus) {
		this.notifierBus = notifierBus;
	}

	public NotifierBus notifierBus(){
		return notifierBus;
	}
}
