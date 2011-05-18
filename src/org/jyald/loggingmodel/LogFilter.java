package org.jyald.loggingmodel;

import java.io.Serializable;

enum FilterOperator {
	Equal,
	NotEqual,
	Greater,
	Lower,
	GreaterAndEqual,
	LowerAndEqual,
	Contains,
	NotContains
}

enum FilterSection {
	LogTypeSection,
	TagSection,
	PidSection,
	MessageSection
}

public class LogFilter implements Serializable, IFilterMatchable {
	
	private FilterOperator oper;
	private FilterSection sect;
	private Object val;
	private boolean useRegExp;
	
	public LogFilter(FilterOperator op, Object value, FilterSection section) {
		oper = op;
		sect = section;
		val = value;
	}
	
	private boolean doMatchForDebugType(DebugType type) {
		switch (oper) {
			case Equal: 
				return ((DebugType)val) == type;
			case NotEqual:
				return ((DebugType)val) != type;
		}
		
		return false;
	}
	
	private boolean doMatchForString(String str) {
		String sval = (String)val;
		
		if (useRegExp) {
			return true;
		}
		
		switch (oper) {
			case Equal:
				return sval.compareTo(str) == 0;
			case NotEqual:
				return sval.compareTo(str) != 0;
			case Contains:
				return sval.contains(str) == true;
			case NotContains:
				return sval.contains(str) != true;
		}
		
		return false;
	}
	
	private boolean doMatchForInt(int value) {
		int ival = ((Integer)val).intValue();
		
		switch (oper) {
			case Equal: 
				return ival == value;
			case NotEqual:
				return ival != value;
			case Greater:
				return ival > value;
			case Lower:
				return ival < value;
			case GreaterAndEqual:
				return ival >= value;
			case LowerAndEqual:
				return ival <= value;
		}
		
		return false;
	}
	
	
	@Override
	public boolean match(LogEntry log) {
		
		switch (sect) {
			case LogTypeSection:
				return doMatchForDebugType(log.getDebugType());
			case PidSection:
				return doMatchForInt(log.getPid());
			case TagSection:
				return doMatchForString(log.getTag());
			case MessageSection:
				return doMatchForString(log.getMessage());
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("(%s) %s -> %s", 
				oper.toString(),
				sect.toString(),
				val.toString());
	}

}
