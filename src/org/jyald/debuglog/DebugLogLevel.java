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
