/*
 * JYald
 * 
 * Copyright (C) 2011 Oguz Kartal
 * 
 * This file is part of JYald
 * 
 * JYald is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JYald is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JYald.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.jyald.loggingmodel;

import java.io.Serializable;
import java.util.regex.Pattern;

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
	
	public static FilterOperator getFilterOperatorFromInt(int val) {
		FilterOperator[] ops = FilterOperator.class.getEnumConstants();
		return ops[val];
	}
	
	public static FilterSection getFilterSectionFromInt(int val) {
		FilterSection[] sects = FilterSection.class.getEnumConstants();
		return sects[val];
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
			return Pattern.matches(sval, str);
		}
		
		switch (oper) {
			case Equal:
				return str.compareTo(sval) == 0;
			case NotEqual:
				return str.compareTo(sval) != 0;
			case Contains:
				return str.contains(sval) == true;
			case NotContains:
				return str.contains(sval) != true;
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
	
	public void setUseRegex(boolean use) {
		useRegExp = use;
	}
	
	public final boolean getUseRegex() {
		return useRegExp;
	}
	
	@Override
	public String toString() {
		return String.format("(%s) %s -> %s", 
				oper.toString(),
				sect.toString(),
				val.toString());
	}

}
