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
