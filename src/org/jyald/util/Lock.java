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

import org.jyald.exceptions.TimedOutException;

public class Lock {
	private Object locker;
	private boolean state;
	
	public Lock() {
		locker = new Object();
		state = false;
	}
	
	public synchronized void lock() {
		state = false;
	}
	
	public synchronized void release() {
		
		state = true; 
		
		try {
			
			synchronized(locker) {
				locker.notify();
			}
		}
		catch (IllegalMonitorStateException e) {
			state = false;
		}
	}
	
	public boolean waitForLock(long ms) throws TimedOutException {
		
		if (!state) {
			try {
				synchronized(locker) {
					locker.wait(ms); 
				}
			}
			catch (InterruptedException e) {
				return true;
			}
			catch (IllegalMonitorStateException e1) {
				return false;
			}
			
			if (!state) 
				throw new TimedOutException(ms);
			
			return true;
		}
		
		return false;
	}
	
	public void waitForLock() {
		try {
			waitForLock(0); //infinite
		}catch (Exception e) {}
	}
}
