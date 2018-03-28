package com.yiji.adk.rules.drools.module;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Rule {
	
	private long identity;
	
	private String ruleName;
	
	private String description;
	
	private String script;
	
	private List<Condition> conditions = new ArrayList<>();
	
	private List<String> imports = new ArrayList<>();
	
	private List<String> globals = new ArrayList<>();

	private String reserved1;

	private String reserved2;

	private Timestamp rawAddTime;
	
	private Timestamp rawUpdateTime;
	
	public long getIdentity() {
		
		return identity;
	}
	
	public void setIdentity(long identity) {
		
		this.identity = identity;
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
	
	public List<Condition> getConditions() {
		
		return conditions;
	}
	
	public void setConditions(List<Condition> conditions) {
		
		this.conditions = conditions;
	}
	
	public List<String> getImports() {
		
		return imports;
	}
	
	public void setImports(List<String> imports) {
		
		this.imports = imports;
	}
	
	public List<String> getGlobals() {
		
		return globals;
	}
	
	public void setGlobals(List<String> globals) {
		
		this.globals = globals;
	}
	
	public Timestamp getRawAddTime() {
		
		return rawAddTime;
	}
	
	public void setRawAddTime(Timestamp rawAddTime) {
		
		this.rawAddTime = rawAddTime;
	}
	
	public Timestamp getRawUpdateTime() {
		
		return rawUpdateTime;
	}
	
	public void setRawUpdateTime(Timestamp rawUpdateTime) {
		
		this.rawUpdateTime = rawUpdateTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Rule rule = (Rule) o;

		if (identity != rule.identity) return false;
		if (conditions != null ? !conditions.equals(rule.conditions) : rule.conditions != null) return false;
		if (description != null ? !description.equals(rule.description) : rule.description != null) return false;
		if (globals != null ? !globals.equals(rule.globals) : rule.globals != null) return false;
		if (imports != null ? !imports.equals(rule.imports) : rule.imports != null) return false;
		if (rawAddTime != null ? !rawAddTime.equals(rule.rawAddTime) : rule.rawAddTime != null) return false;
		if (rawUpdateTime != null ? !rawUpdateTime.equals(rule.rawUpdateTime) : rule.rawUpdateTime != null)
			return false;
		if (reserved1 != null ? !reserved1.equals(rule.reserved1) : rule.reserved1 != null) return false;
		if (reserved2 != null ? !reserved2.equals(rule.reserved2) : rule.reserved2 != null) return false;
		if (ruleName != null ? !ruleName.equals(rule.ruleName) : rule.ruleName != null) return false;
		if (script != null ? !script.equals(rule.script) : rule.script != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (identity ^ (identity >>> 32));
		result = 31 * result + (ruleName != null ? ruleName.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (script != null ? script.hashCode() : 0);
		result = 31 * result + (conditions != null ? conditions.hashCode() : 0);
		result = 31 * result + (imports != null ? imports.hashCode() : 0);
		result = 31 * result + (globals != null ? globals.hashCode() : 0);
		result = 31 * result + (reserved1 != null ? reserved1.hashCode() : 0);
		result = 31 * result + (reserved2 != null ? reserved2.hashCode() : 0);
		result = 31 * result + (rawAddTime != null ? rawAddTime.hashCode() : 0);
		result = 31 * result + (rawUpdateTime != null ? rawUpdateTime.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Rule{");
		sb.append("identity=").append(identity);
		sb.append(", ruleName='").append(ruleName).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", script='").append(script).append('\'');
		sb.append(", conditions=").append(conditions);
		sb.append(", imports=").append(imports);
		sb.append(", globals=").append(globals);
		sb.append(", reserved1='").append(reserved1).append('\'');
		sb.append(", reserved2='").append(reserved2).append('\'');
		sb.append(", rawAddTime=").append(rawAddTime);
		sb.append(", rawUpdateTime=").append(rawUpdateTime);
		sb.append('}');
		return sb.toString();
	}

}
