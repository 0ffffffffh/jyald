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
