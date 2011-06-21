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


import org.jyald.uicomponents.TabContent;

public class FilteredLogSlot {
	private String slotName;
	private EntryList entries;
	private FilterList filters;
	private TabContent loggerUi;
	private boolean disposed;
	
	public FilteredLogSlot(String name, FilterList filterList) {
		entries = new EntryList();
		slotName = name;
		filters = filterList;
		disposed = false;
	}
	
	private void checkDisposed() throws Exception {
		if (disposed) 
			throw new Exception("Object was disposed! Please create a new instance");
		
	}
	
	public final String getSlotName() {
		return slotName;
	}
	
	public final int getFilterCount() throws Exception {
		checkDisposed();
		
		if (filters != null)
			return filters.getCount();
		
		return -1;
	}
	
	public void linkUi(TabContent ui) throws Exception {
		checkDisposed();
		
		loggerUi = ui;
	}
	
	public TabContent getLoggerUi() {
		return loggerUi;
	}
	
	
	public boolean tryAdd(LogEntry entry) throws Exception {
		checkDisposed();
		
		if (filters == null) 
			return false;
		
		if (filters.match(entry)) {
			loggerUi.writeLog(entry);
			entries.addEntry(entry);
			return true;
		}
		
		return false;
	}
	
	public void dispose() throws Exception {
		checkDisposed();
		
		filters.clear();
		filters = null;
		entries.clear();
		entries = null;
		
		loggerUi.dispose();
		loggerUi = null;
		
		disposed = true;
	}
	
}
