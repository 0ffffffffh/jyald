package org.jyald.util;

public class StringHelper {
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	public static boolean isNullOrEmpty(String s) {
		return (s == null || s == "");
	}
}
