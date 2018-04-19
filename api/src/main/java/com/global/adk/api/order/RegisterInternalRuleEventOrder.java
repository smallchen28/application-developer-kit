package com.global.adk.api.order;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.global.common.service.OrderCheckException;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterInternalRuleEventOrder extends OrderBase {
	
	private static final long serialVersionUID = 6241863171568666912L;
	
	@NotNull
	@Size(min=1,max=256)
	private String eventName;
	
	@Valid
	private ArrayList<RelatedPolicyAttribute> attributes = Lists.newArrayList();
	
	@NotNull
	@Size(min=1,max=256)
	private String description;
	
	private Map<String, String> eventContext = Maps.newHashMap();
	
	private boolean enable = true;
	
	public String getEventName() {
		
		return eventName;
	}
	
	public void setEventName(String eventName) {
		
		this.eventName = eventName;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public Map<String, String> getEventContext() {
		
		return eventContext;
	}
	
	public void setEventContext(HashMap<String, String> eventContext) {
		
		this.eventContext = eventContext;
	}
	
	public ArrayList<RelatedPolicyAttribute> getAttributes() {
		
		return attributes;
	}

	@Override
	public void checkWithGroup(Class<?>... groups) {
		super.checkWithGroup(groups);

		if(enable && (attributes == null || attributes.size() == 0)){
			throw new OrderCheckException("attributes","当enable为true时attribute不可为空……");
		}
	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterInternalRuleEventOrder [eventName=");
		builder.append(eventName);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append(", description=");
		builder.append(description);
		builder.append(", eventContext=");
		builder.append(eventContext);
		builder.append("]");
		return builder.toString();
	}
	
	public boolean isEnable() {
		
		return enable;
	}
	
	public void setEnable(boolean enable) {
		
		this.enable = enable;
	}
	
}
