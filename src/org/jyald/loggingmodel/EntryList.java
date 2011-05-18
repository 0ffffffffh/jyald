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
