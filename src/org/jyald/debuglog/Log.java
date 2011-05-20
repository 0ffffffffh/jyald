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
	
	public synchronized static void write(String format, Object...objects) {
		
		if (_logBuffer.length() >= 5 * 1024) {
			
			try {
				FileWriter fw = new FileWriter("jyald-deblog.txt",true);
				fw.write(_logBuffer.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			_logBuffer.delete(0, _logBuffer.length());
			
		}
		
		_logBuffer.append(initLogBuffer(format,objects));
	}
	
	public static PrintStream getPrintStreamInstance() {
		if (_stream == null) {
			_stream = new PrintStream(new DebugPrintStream());
		}
		
		return _stream;
	}
	
	public void finish() {
		//TODO: doo
	}
}
