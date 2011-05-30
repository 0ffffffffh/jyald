package org.jyald.util;

import java.util.ArrayList;

public class StringHelper {
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	public static boolean isNullOrEmpty(String s) {
		return (s == null || s.equals(""));
	}
	
	public static String[] stringSplitWithDelimiter(String str, String delim) {
		ArrayList<String> blocks = new ArrayList<String>();
		String[] arrayBlock=null;
		
		int beg=0,end;
		final int delimLen = delim.length();
		
		for (;;) {
			end = str.indexOf(delim, beg);
			
			if (end == -1) {
				if (beg < str.length())
					blocks.add(str.substring(beg,str.length()));
				break;
			}
			
			blocks.add(str.substring(beg,end));
			
			beg = end+delimLen;
		}
		
		if (blocks.size() > 0) {
			arrayBlock = new String[blocks.size()];
			blocks.toArray(arrayBlock);
			blocks.clear();
		}
		
		return arrayBlock;
	}
}
