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


package org.jyald.debuglog;


public class DebugLogLevel {	
	private int logLevel;
	
	public DebugLogLevel() {
		logLevel = LogLevel.ALL;
	}
	
	private final boolean checkLevel(int level) {
		if (logLevel==LogLevel.ALL)
			return true;
		
		return (logLevel & level) != 0;
	}
	
	public int setLevel(int level) {
		int oldLevel;
		
		oldLevel = logLevel;
		logLevel |= level;
		return oldLevel;
	}
	
	public int unsetLevel(int level) {
		int oldLevel;
		
		oldLevel = logLevel;
		logLevel &= ~level;
		
		return oldLevel;
	}
	
	public final boolean isLoggableLevel(int level) {
		return checkLevel(level);
	}
}
