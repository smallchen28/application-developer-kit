package com.global.adk.api.order;

import com.google.common.collect.Lists;
import com.global.common.service.OrderCheckException;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

public class RegisterRuleOrder extends OrderBase {
	
	private static final long serialVersionUID = -1965991595861447415L;
	
	@NotNull
	@Size(min=1,max=64)
	private String ruleName;
	
	@NotNull
	@Size(min=1,max=128)
	private String description;
	
	@NotNull
	@Size(min=1,max=4000)
	private String script;

	@Size(min = 1)
	private ArrayList<ConditionOrder> conditions = Lists.newArrayList();

	private ArrayList<String> imports = Lists.newArrayList();

	private ArrayList<String> globals = Lists.newArrayList();

	@Size(max = 256)
	private String reserved1;

	@Size(max = 256)
	private String reserved2;

	private boolean checkDroolsKonwledgeBase = false;

	@Override
	public void checkWithGroup(Class<?>... groups) {
		if(!ruleName.matches("^[\\D].*")){
			throw new OrderCheckException("rule","命名必须符合java类命名规范不能以数字开头……");
		}
		super.checkWithGroup(groups);
	}

	public static void main(String[] args) {
	}

	public String getRuleName() {
		
		return ruleName;
	}
	
	public void setRuleName(String ruleName) {
		
		this.ruleName = ruleName;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public String getScript() {
		
		return script;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public void setScript(String script) {
		
		this.script = script;
	}
	
	public ArrayList<ConditionOrder> getConditions() {
		
		return conditions;
	}
	
	public ArrayList<String> getImports() {
		
		return imports;
	}
	
	public void setImports(ArrayList<String> imports) {
		
		this.imports = imports;
	}
	
	public ArrayList<String> getGlobals() {
		
		return globals;
	}
	
	public void setGlobals(ArrayList<String> globals) {
		
		this.globals = globals;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RegisterRuleOrder{");
		sb.append("ruleName='").append(ruleName).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", script='").append(script).append('\'');
		sb.append(", conditions=").append(conditions);
		sb.append(", imports=").append(imports);
		sb.append(", globals=").append(globals);
		sb.append(", reserved1='").append(reserved1).append('\'');
		sb.append(", reserved2='").append(reserved2).append('\'');
		sb.append(", checkDroolsKonwledgeBase=").append(checkDroolsKonwledgeBase);
		sb.append('}');
		return sb.toString();
	}

	public boolean isCheckDroolsKonwledgeBase() {
		
		return checkDroolsKonwledgeBase;
	}
	
	public void setCheckDroolsKonwledgeBase(boolean checkDroolsKonwledgeBase) {
		
		this.checkDroolsKonwledgeBase = checkDroolsKonwledgeBase;
	}
	
}
