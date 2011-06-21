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

import java.util.ArrayList;

public class StringHelper {
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	public static boolean isEmpty(String s) {
		return s.equals("");
	}
	
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
