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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jyald.util.StringHelper;

public class Log {
	
	private static StringBuffer _logBuffer = new StringBuffer(6 * 1024);
	private static PrintStream _stream;
	private static DebugLogLevel _logLevel = new DebugLogLevel();
	
	public static boolean printOnDefaultSysStreamLogReplica;
	
	private static String initLogBuffer(String format, Object...objects) {
		String finalLog;
		String currTime;
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		currTime = dateFormat.format(date);
        
		
		if (objects != null) 
			finalLog = String.format("%s : %s%s", currTime, String.format(format, objects), StringHelper.NEW_LINE);
		else
			finalLog = String.format("%s : %s%s", currTime, format, StringHelper.NEW_LINE);
		
		return finalLog;
	}
	
	private static void internalWrite(String format, boolean forceWriteToDisk, Object...objects) {
		String logLine;
		
		if (forceWriteToDisk || (_logBuffer.length() >= 5 * 1024)) {
			
			try {
				FileWriter fw = new FileWriter("jyald-deblog.txt",true);
				fw.write(_logBuffer.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			_logBuffer.delete(0, _logBuffer.length());
			
		}
		
		if (StringHelper.isNullOrEmpty(format))
			return;
		
		if (format.equals(StringHelper.NEW_LINE))
			return;
		
		logLine = initLogBuffer(format,objects);
		
		_logBuffer.append(logLine);
		
		if (Log.printOnDefaultSysStreamLogReplica)
			System.out.println(logLine);
	}
	
	public synchronized static void write(String format, Object...objects) {
		Log.internalWrite(format, false, objects);
	}
	
	public synchronized static void writeByLevel(int level, String format, Object...objects) {
		if (_logLevel.isLoggableLevel(level)) {
			Log.internalWrite(format, false, objects);
		}
	}
	
	public static void setLogLevel(int level) {
		_logLevel.setLevel(level);
	}
	
	public static PrintStream getPrintStreamInstance() {
		if (_stream == null) {
			_stream = new PrintStream(new DebugPrintStream());
		}
		
		return _stream;
	}
	
	public static void finish() {
		Log.write("Logging system finishing");
		Log.internalWrite(null, true);
		if (_stream != null)
			_stream.close();
	}
}
