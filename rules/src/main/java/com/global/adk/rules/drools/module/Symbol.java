package com.global.adk.rules.drools.module;

import com.global.adk.common.exception.RuleException;

@SuppressWarnings("rawtypes")
public enum Symbol {
	GREATER_THEN(">") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			Class resultType = result.getClass();
			Class compareValueType = compareValue.getClass();
			
			if (resultType != compareValueType) {
				return false;
			}
			if (resultType == Byte.class) {
				byte rs = Byte.valueOf(result.toString());
				byte value = Byte.valueOf(compareValue.toString());
				return rs > value;
			} else if (resultType == Short.class) {
				short rs = Short.valueOf(result.toString());
				short value = Short.valueOf(compareValue.toString());
				return rs > value;
			} else if (resultType == Integer.class) {
				int rs = Integer.valueOf(result.toString());
				int value = Integer.valueOf(compareValue.toString());
				return rs > value;
			} else if (resultType == Long.class) {
				long rs = Long.valueOf(result.toString());
				long value = Long.valueOf(compareValue.toString());
				return rs > value;
			} else if (resultType == Float.class) {
				float rs = Float.valueOf(result.toString());
				float value = Float.valueOf(compareValue.toString());
				return rs > value;
			} else if (resultType == Double.class) {
				double rs = Double.valueOf(result.toString());
				double value = Double.valueOf(compareValue.toString());
				return rs > value;
			} else if (resultType == Character.class) {
				char rs = (char) result;
				char value = (char) compareValue;
				return rs > value;
			} else if (resultType == Boolean.class || resultType == String.class) {
				throw new RuleException(String.format("布尔值,String类型不允许进行>操作,result_type =%s", result));
			} else {
				throw new RuleException(String.format("不允许操作的数据类型,result_type =%s", result));
			}
		}
	},
	
	LESS_THEN("<") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			Class resultType = result.getClass();
			Class compareValueType = compareValue.getClass();
			
			if (resultType != compareValueType) {
				return false;
			}
			if (resultType == Byte.class) {
				byte rs = Byte.valueOf(result.toString());
				byte value = Byte.valueOf(compareValue.toString());
				return rs < value;
			} else if (resultType == Short.class) {
				short rs = Short.valueOf(result.toString());
				short value = Short.valueOf(compareValue.toString());
				return rs < value;
			} else if (resultType == Integer.class) {
				int rs = Integer.valueOf(result.toString());
				int value = Integer.valueOf(compareValue.toString());
				return rs < value;
			} else if (resultType == Long.class) {
				long rs = Long.valueOf(result.toString());
				long value = Long.valueOf(compareValue.toString());
				return rs < value;
			} else if (resultType == Float.class) {
				float rs = Float.valueOf(result.toString());
				float value = Float.valueOf(compareValue.toString());
				return rs < value;
			} else if (resultType == Double.class) {
				double rs = Double.valueOf(result.toString());
				double value = Double.valueOf(compareValue.toString());
				return rs < value;
			} else if (resultType == Character.class) {
				char rs = (char) result;
				char value = (char) compareValue;
				return rs < value;
			} else if (resultType == Boolean.class || resultType == String.class) {
				throw new RuleException(String.format("布尔值,String类型不允许进行 < 操作,result_type =%s", result));
			} else {
				throw new RuleException(String.format("不允许操作的数据类型,result_type =%s", result));
			}
		}
	},
	
	GREATER_EQUAL(">=") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			Class resultType = result.getClass();
			Class compareValueType = compareValue.getClass();
			
			if (resultType != compareValueType) {
				return false;
			}
			if (resultType == Byte.class) {
				byte rs = Byte.valueOf(result.toString());
				byte value = Byte.valueOf(compareValue.toString());
				return rs >= value;
			} else if (resultType == Short.class) {
				short rs = Short.valueOf(result.toString());
				short value = Short.valueOf(compareValue.toString());
				return rs >= value;
			} else if (resultType == Integer.class) {
				int rs = Integer.valueOf(result.toString());
				int value = Integer.valueOf(compareValue.toString());
				return rs >= value;
			} else if (resultType == Long.class) {
				long rs = Long.valueOf(result.toString());
				long value = Long.valueOf(compareValue.toString());
				return rs >= value;
			} else if (resultType == Float.class) {
				float rs = Float.valueOf(result.toString());
				float value = Float.valueOf(compareValue.toString());
				return rs >= value;
			} else if (resultType == Double.class) {
				double rs = Double.valueOf(result.toString());
				double value = Double.valueOf(compareValue.toString());
				return rs >= value;
			} else if (resultType == Character.class) {
				char rs = (char) result;
				char value = (char) compareValue;
				return rs >= value;
			} else if (resultType == Boolean.class || resultType == String.class) {
				throw new RuleException(String.format("布尔值,String类型不允许进行 >= 操作,result_type =%s", result));
			} else {
				throw new RuleException(String.format("不允许操作的数据类型,result_type =%s", result));
			}
		}
	},
	
	LESS_EQUAL("<=") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			Class resultType = result.getClass();
			Class compareValueType = compareValue.getClass();
			
			if (resultType != compareValueType) {
				return false;
			}
			if (resultType == Byte.class) {
				byte rs = Byte.valueOf(result.toString());
				byte value = Byte.valueOf(compareValue.toString());
				return rs <= value;
			} else if (resultType == Short.class) {
				short rs = Short.valueOf(result.toString());
				short value = Short.valueOf(compareValue.toString());
				return rs <= value;
			} else if (resultType == Integer.class) {
				int rs = Integer.valueOf(result.toString());
				int value = Integer.valueOf(compareValue.toString());
				return rs <= value;
			} else if (resultType == Long.class) {
				long rs = Long.valueOf(result.toString());
				long value = Long.valueOf(compareValue.toString());
				return rs <= value;
			} else if (resultType == Float.class) {
				float rs = Float.valueOf(result.toString());
				float value = Float.valueOf(compareValue.toString());
				return rs <= value;
			} else if (resultType == Double.class) {
				double rs = Double.valueOf(result.toString());
				double value = Double.valueOf(compareValue.toString());
				return rs <= value;
			} else if (resultType == Character.class) {
				char rs = (char) result;
				char value = (char) compareValue;
				return rs <= value;
			} else if (resultType == Boolean.class || resultType == String.class) {
				throw new RuleException(String.format("布尔值,String类型不允许进行 <= 操作,result_type =%s", result));
			} else {
				throw new RuleException(String.format("不允许操作的数据类型,result_type =%s", result));
			}
		}
	},
	
	EQUAL("==") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			Class resultType = result.getClass();
			Class compareValueType = compareValue.getClass();
			
			if (resultType != compareValueType) {
				return false;
			}
			if (resultType == Byte.class) {
				byte rs = Byte.valueOf(result.toString());
				byte value = Byte.valueOf(compareValue.toString());
				return rs == value;
			} else if (resultType == Short.class) {
				short rs = Short.valueOf(result.toString());
				short value = Short.valueOf(compareValue.toString());
				return rs == value;
			} else if (resultType == Integer.class) {
				int rs = Integer.valueOf(result.toString());
				int value = Integer.valueOf(compareValue.toString());
				return rs == value;
			} else if (resultType == Long.class) {
				long rs = Long.valueOf(result.toString());
				long value = Long.valueOf(compareValue.toString());
				return rs == value;
			} else if (resultType == Float.class) {
				float rs = Float.valueOf(result.toString());
				float value = Float.valueOf(compareValue.toString());
				return rs == value;
			} else if (resultType == Double.class) {
				double rs = Double.valueOf(result.toString());
				double value = Double.valueOf(compareValue.toString());
				return rs == value;
			} else if (resultType == String.class) {
				return result.toString().equals(compareValue.toString());
			} else if (resultType == Character.class) {
				char rs = (char) result;
				char value = (char) compareValue;
				return rs == value;
			} else if (resultType == Boolean.class) {
				boolean rs = Boolean.valueOf(result.toString());
				boolean value = Boolean.valueOf(compareValue.toString());
				return rs == value;
			} else {
				throw new RuleException(String.format("不允许操作的数据类型,result_type =%s", result));
			}
		}
	},
	
	NO_EQUAL("!=") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			Class resultType = result.getClass();
			Class compareValueType = compareValue.getClass();
			
			if (resultType != compareValueType) {
				return false;
			}
			if (resultType == Byte.class) {
				byte rs = Byte.valueOf(result.toString());
				byte value = Byte.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Short.class) {
				short rs = Short.valueOf(result.toString());
				short value = Short.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Integer.class) {
				int rs = Integer.valueOf(result.toString());
				int value = Integer.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Long.class) {
				long rs = Long.valueOf(result.toString());
				long value = Long.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Float.class) {
				float rs = Float.valueOf(result.toString());
				float value = Float.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Double.class) {
				double rs = Double.valueOf(result.toString());
				double value = Double.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Boolean.class) {
				boolean rs = Boolean.valueOf(result.toString());
				boolean value = Boolean.valueOf(compareValue.toString());
				return rs != value;
			} else if (resultType == Character.class) {
				char rs = (char) result;
				char value = (char) compareValue;
				return rs != value;
			} else if (resultType == String.class) {
				return !result.toString().equals(compareValue.toString());
			} else {
				throw new RuleException(String.format("不允许操作的数据类型,result_type =%s", result));
			}
		}
	},
	
	NOT_IN("!in") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return true;
			}
			
			if (result instanceof String) {
				if (compareValue instanceof String) {
					String[] values = ((String) compareValue).split(";");
					if (null != values) {
						for (String str : values) {
							if (str.equals((String) result)) {
								return false;
							}
						}
					}
				}
			}
			return true;
		}
	},
	
	IS_IN("in") {
		
		@Override
		public boolean caculate(Object result, Object compareValue) {
			if (null == result || null == compareValue) {
				return false;
			}
			
			if (result instanceof String) {
				if (compareValue instanceof String) {
					String[] values = ((String) compareValue).split(";");
					if (null != values) {
						for (String str : values) {
							if (str.equals((String) result)) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}
	};
	
	private String code;
	
	private Symbol(String code) {
		
		this.code = code;
	}
	
	public String getCode() {
		
		return code;
	}
	
	public static Symbol code(String code) {
		
		Symbol symbol = null;
		for (Symbol sb : values()) {
			if (sb.code.equals(code)) {
				symbol = sb;
				break;
			}
		}
		return symbol;
	}
	
	public abstract boolean caculate(Object result, Object compareValue);
	
}
