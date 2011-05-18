package org.jyald.loggingmodel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



enum DebugType {
	Unknown,
	Info,
	Debug,
	Warning,
	Error,
	Verbose
}

public class LogEntry {
	
	private DebugType logType;
	private String logTag;
	private int pid;
	private String logMsg;
	
	private DebugType getDebugTypeFromIdentifier(char id) {
		
		switch (id) {
			case 'I': {
				return DebugType.Info;
			}
			case 'D': {
				return DebugType.Debug;
			}
			case 'W': {
				return DebugType.Warning;
			}
			case 'E': {
				return DebugType.Error;
			}
			case 'V': {
				return DebugType.Verbose;
			}
		}
		
		return DebugType.Unknown;
	}
	
	public static LogEntry parse(String buffer) {
		LogEntry log = null;
		char typeId;
		int pid;
		String tag,msg;
		
		Pattern regex = Pattern.compile("([DVIWE])/(.*?) ?\\((.*?)\\): (.*)");
		Matcher match = regex.matcher(buffer);
		
		if (match.find()) {
			if (match.groupCount() == 4) {
				typeId = match.group(1).charAt(0);
				tag = match.group(2).trim();
				
				try {
					pid = Integer.parseInt(match.group(3).trim());
				}
				catch (Exception e) 
				{ return null; }
				
				msg = match.group(4);
				
				log = new LogEntry(typeId,tag,pid,msg);
			}
		}
		
		return log;
	}
	
	public LogEntry(char typeId, String tag, int processId, String message) {
		logType = getDebugTypeFromIdentifier(typeId);
		logTag = tag;
		pid = processId;
		logMsg = message;
	}
	
	public final String getDebugTypeString() {
		return logType.toString();
	}
	
	public final DebugType getDebugType() {
		return logType;
	}
	
	public final String getTag() {
		return logTag;
	}
	
	public final int getPid() {
		return pid;
	}
	
	public final String getMessage() {
		return logMsg;
	}
	
	@Override
	public String toString() {
		return String.format("[%d] - (%s) %s - %s",
				pid,
				logType.toString(),
				logTag,
				logMsg);
	}
}
