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
