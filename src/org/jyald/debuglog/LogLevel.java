package org.jyald.debuglog;

public class LogLevel {
	public static final int ALL = 0x0;
	public static final int CORE = 0x2;
	public static final int MODEL = 0x4;
	public static final int COMPONENT = 0x8;
	public static final int EXCEPTION = 0x10;
	public static final int UI = 0x20;
	
	public static int getLogLevelCombination(String logCmdStr) {
		int level=ALL;
		String []levels = logCmdStr.split(",");
		
		for (int i=0;i<levels.length;i++) {
			
			if (levels[i].equals("all"))
				return level;
			else if (levels[i].equals("cor"))
				level |= CORE;
			else if (levels[i].equals("mod"))
				level |= MODEL;
			else if (levels[i].equals("com"))
				level |= COMPONENT;
			else if (levels[i].equals("exc"))
				level |= EXCEPTION;
			else if (levels[i].equals("ui"))
				level |= UI;
		}
		
		return level;
	}
}
