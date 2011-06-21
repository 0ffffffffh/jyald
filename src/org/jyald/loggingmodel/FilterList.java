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
	
	public void setLinkAndState(boolean state) {
		linkAnd = state;
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
