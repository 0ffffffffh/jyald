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

import org.jyald.util.IterableArrayList;

public class EntryList {
	private IterableArrayList<LogEntry> entries;
	private FilterList filters;
	
	public EntryList() {
		entries = new IterableArrayList<LogEntry>();
	}
	
	public void addEntry(LogEntry log) {
		entries.add(log);
	}
	
	public void setFilter(FilterList filterList) {
		filters = filterList;
	}
	
	public void clear() {
		if (filters != null)
			filters.clear();
		
		filters = null;
		
		if (entries != null)
			entries.clear();			
	}
	
	public final int getCount() {
		return entries.getCount();
	}
}
