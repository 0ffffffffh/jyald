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


package org.jyald.util;


public class LogTimeCalculator {
	private long beginTime;
	private final long MS_IN_SEC = 1000;
	private final long MS_IN_MIN = 60 * MS_IN_SEC;
	private final long MS_IN_HOUR = 60 * MS_IN_MIN;
	
	
	public LogTimeCalculator() {
		start();
	}
	
	public void reset() {
		beginTime = 0;
	}
	
	public void start() {
		beginTime = System.currentTimeMillis();
	}
	
	public String getCurrentTimes() {
		long diff = System.currentTimeMillis() - beginTime;

		long hours,mins,secs,msecs;
		
		hours = diff / MS_IN_HOUR;
		diff %= MS_IN_HOUR;
		
		mins = diff / MS_IN_MIN;
		diff %= MS_IN_MIN;
		
		secs = diff / MS_IN_SEC;
		diff %= MS_IN_SEC;
		
		msecs = diff;
		
		return String.format("%02d:%02d:%02d:%03d", hours,mins,secs,msecs);
	}
}
