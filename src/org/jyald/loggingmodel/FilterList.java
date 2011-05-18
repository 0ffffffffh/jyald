package org.jyald.loggingmodel;

import java.io.Serializable;

import org.jyald.util.*;

public class FilterList implements Serializable, IFilterMatchable {
	
	private IterableArrayList<LogFilter> filters;
	private boolean linkAnd;
	
	
	public FilterList() {
		filters = new IterableArrayList<LogFilter>();
		linkAnd = false;
	}
	
	
	public void addFilter(LogFilter filter) {
		filters.add(filter);
	}
	
	public void clear() {
		if (filters != null) {
			filters.clear();
		}
	}
	
	public final int getCount() {
		if (filters != null) {
			return filters.getCount();
		}
		
		return -1;
	}
	
	public final boolean getLinkAndState() {
		return linkAnd;
	}
	
	@Override
	public boolean match(final LogEntry log) {
		final BoolContainer stat = new BoolContainer(false);
		final IntegerContainer matchCount = new IntegerContainer(0);
		
		filters.iterate(new ArrayListIterateHandler<LogFilter>() {

			@Override
			public boolean iterate(LogFilter item) {
				if (item.match(log)) {
					if (linkAnd) {
						matchCount.increment();
						return false;
					}
					
					stat.setBool(true);
					return true;
				}
				
				return false;
			}
			
		});
		
		if (linkAnd && filters.getCount() == matchCount.getInt()) {
			stat.setBool(true);
		}
		
		return stat.getBool();
	}
}
