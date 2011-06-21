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


package org.jyald.core;

import org.jyald.debuglog.Log;
import org.jyald.debuglog.LogLevel;
import org.jyald.loggingmodel.*;
import org.jyald.uicomponents.TabContent;
import org.jyald.util.*;


public class LogcatManager {
	private IterableArrayList<FilteredLogSlot> slots;
	private EntryList generalEntries;
	private TabContent generalLoggerUi;
	private ProcessObject logcatProcess;
	private DeviceActiveHandler deviceActivationHandler;
	
	public LogcatManager() {
		
		slots = new IterableArrayList<FilteredLogSlot>();
		generalEntries = new EntryList();
		
		logcatProcess = new ProcessObject("logcat");
		logcatProcess.setOutputLineReceiver(new ProcessStdoutHandler() {

			@Override
			public void onOutputLineReceived(String line) {
				onLineReceived(line);
			}
		});
	}
	
	
	private void onLineReceived(String line) {
		final BoolContainer isGeneralEntry = new BoolContainer(true);
		
		if (line.compareTo("DEVCON") == 0) {
			Log.write("Device online!");
			if (deviceActivationHandler != null)
				deviceActivationHandler.onDeviceActivated();
			return;
		}
		
		final LogEntry entry = LogEntry.parse(line);
		
		if (entry == null)
			return;
		
		slots.iterate(new ArrayListIterateHandler<FilteredLogSlot>() {

			@Override
			public boolean iterate(FilteredLogSlot item) {
				
				try {
					if (item.tryAdd(entry)) {
						isGeneralEntry.setBool(false);
						return true;
					}
					
				} catch (Exception e) {}
				
				isGeneralEntry.setBool(true);
				return false;
			}
		});
		
		if (isGeneralEntry.getBool()) {
			generalEntries.addEntry(entry);
			generalLoggerUi.writeLog(entry);
		}
		
		
	}
	
	public boolean start() throws Exception {
		if (StringHelper.isNullOrEmpty(logcatProcess.getExecutableFile()))
			throw new Exception("adb is not set!");
		
		Log.writeByLevel(LogLevel.CORE, "LogcatManager is now starting");
		
		logcatProcess.start();
		
		return logcatProcess.isRunning();
	}
	
	public void stop() {
		Log.writeByLevel(LogLevel.CORE,"LogcatManager stopping");
		
		logcatProcess.kill();
	}
	
	public final boolean isActive() {
		return logcatProcess.isRunning();
	}
	
	public final String getAdb() {
		return logcatProcess.getExecutableFile();
	}
	
	public void setAdb(String adbFile) {
		logcatProcess.setExecutableFile(adbFile);
	}
	
	public void registerActivationHandler(DeviceActiveHandler handler) {
		deviceActivationHandler = handler;
	}
	
	public FilteredLogSlot addSlot(String name, FilterList list, TabContent ui) {
		FilteredLogSlot slot = null;
		
		if (StringHelper.isNullOrEmpty(name)) 
			return null;
		
		slot = new FilteredLogSlot(name,list);
		
		try {
			slot.linkUi(ui);
		} catch (Exception e) {}
		
		slots.add(slot);
		
		if (slots.getCount() == 1) {
			generalLoggerUi = slot.getLoggerUi();
		}
		
		return slot;
	}
	
	public void removeSlot(FilteredLogSlot slot){
		slots.remove(slot);
		
		try {
			slot.dispose();
		} catch(Exception e) {
			e.printStackTrace(Log.getPrintStreamInstance());
		}
	}
	
	public void removeSlot(String name) {
		FilteredLogSlot toRemove = null;
		
		for (FilteredLogSlot slot : slots) {
			if (slot.getSlotName().compareTo(name) == 0) {
				toRemove = slot;
				break;
			}
		}
		
		if (toRemove != null) {
			removeSlot(toRemove);
		}
	}
	
	public void dispose() throws Exception {
		if (isActive()) 
			throw new Exception("You cannot dispose at this time. Because LogcatManager still active.");
		
		generalEntries.clear();
		
		for (FilteredLogSlot slot : slots) {
			slot.dispose();
		}
		
		slots.clear();
	}
	
	
	
}
