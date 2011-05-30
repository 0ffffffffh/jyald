package org.jyald.debuglog;


public class LogLevel {
	public static final int ALL = 0x0;
	public static final int CORE = 0x2;
	public static final int MODEL = 0x4;
	public static final int COMPONENT = 0x8;
	public static final int EXCEPTION = 0x10;
	public static final int UI = 0x20;
	
	public static int getLogLevelCombination(String levelStr) {
		
		if (levelStr.equals("all"))
			return ALL;
		else if (levelStr.equals("cor"))
			return CORE;
		else if (levelStr.equals("mod"))
			return MODEL;
		else if (levelStr.equals("com"))
			return COMPONENT;
		else if (levelStr.equals("exc"))
			return EXCEPTION;
		else if (levelStr.equals("ui"))
			return UI;
		
		return -1;
	}
}
