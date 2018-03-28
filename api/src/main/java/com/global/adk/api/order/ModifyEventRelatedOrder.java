package com.global.adk.api.order;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;

public class ModifyEventRelatedOrder extends OrderBase {
	
	private static final long serialVersionUID = 2430725889989903607L;
	
	@Min(value = 1)
	private long internalEventIdentity;
	
	private boolean internalEventEnable = true;
	
	private ArrayList<EventRelatedElement> elements = new ArrayList<>();

	@NotNull
	@Size(min=1 , max=256)
	private String description ;


	public enum Optype {
		valid,
		invalid,
		delete,
		add
	}
	
	public static class EventRelatedElement implements Serializable {
		
		private static final long serialVersionUID = 4320428984200861589L;
		
		private long policyIdentity;
		
		private Optype optype = Optype.invalid;
		
		public long getPolicyIdentity() {
			
			return policyIdentity;
		}
		
		public void setPolicyIdentity(long policyIdentity) {
			
			this.policyIdentity = policyIdentity;
		}
		
		public Optype getOptype() {
			
			return optype;
		}
		
		public void setOptype(Optype optype) {
			
			this.optype = optype;
		}
		
	}
	
	public long getInternalEventIdentity() {
		
		return internalEventIdentity;
	}
	
	public void setInternalEventIdentity(long internalEventIdentity) {
		
		this.internalEventIdentity = internalEventIdentity;
	}
	
	public ArrayList<EventRelatedElement> getElements() {
		
		return elements;
	}
	
	public void setElements(ArrayList<EventRelatedElement> elements) {
		
		this.elements = elements;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isInternalEventEnable() {
		
		return internalEventEnable;
	}
	
	public void setInternalEventEnable(boolean internalEventEnable) {
		
		this.internalEventEnable = internalEventEnable;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ModifyEventRelatedOrder{");
		sb.append("internalEventIdentity=").append(internalEventIdentity);
		sb.append(", internalEventEnable=").append(internalEventEnable);
		sb.append(", elements=").append(elements);
		sb.append(", description='").append(description).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
