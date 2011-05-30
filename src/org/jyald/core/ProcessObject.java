package org.jyald.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.jyald.debuglog.Log;
import org.jyald.debuglog.LogLevel;
import org.jyald.exceptions.TimedOutException;
import org.jyald.util.Lock;
import org.jyald.util.StringHelper;

public class ProcessObject implements Runnable {
	
	private ProcessStdoutHandler stdoutRecv;
	private Lock workerLock,processStartWaitLock;
	private Thread consumeWorker;
	private boolean consume,running;
	private Process workingProcess;
	private String adbExecFile;
	private String unit;
	
	public ProcessObject(String adbUnit) {
		workerLock = new Lock();
		processStartWaitLock = new Lock();
		running = false;
		consume = true;
		unit = adbUnit;
	}
	
	private boolean internalStart() {
		try {
			workingProcess = Runtime.getRuntime().exec(String.format("\"%s\" %s", adbExecFile,unit));
		} catch (IOException e) {
			Log.write(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private void internalStop() {
		if (workingProcess != null) {
			Log.writeByLevel(LogLevel.CORE, "killing logcat process");
			workingProcess.destroy();
			running = false;
			workingProcess = null;
		}
	}
	
	private void raiseEvent(String line) {
		if (stdoutRecv != null) {
			stdoutRecv.onOutputLineReceived(line);
		}
	}
	
	public void setOutputLineReceiver(ProcessStdoutHandler handler) {
		stdoutRecv = handler;
	}
	
	public boolean start() {
		workerLock.lock();
		processStartWaitLock.lock();
		
		Log.writeByLevel(LogLevel.CORE, "Starting adb process worker thread");
		
		consumeWorker = new Thread(this);
		consumeWorker.start();
		
		Log.writeByLevel(LogLevel.CORE, "Waiting process to be ready");
		
		try {
			processStartWaitLock.waitForLock(10 * 1000);
		}
		catch (TimedOutException e) {
			Log.writeByLevel(LogLevel.CORE,"process start wait timed out!");
			return false;
		}
		
		Log.writeByLevel(LogLevel.CORE, "It seems ok");
		
		return isRunning();
	}
	
	public void kill() {
		int trycount=4;
		
		if (workingProcess != null && running) {
			
			Log.writeByLevel(LogLevel.CORE, "Trying to stop logcat process");
			
			consume = false;
			
			while (consumeWorker.getState() != Thread.State.TERMINATED) {
				
				Log.writeByLevel(LogLevel.CORE, "Waiting worker thread to finish #%d",trycount);
				
				try {
					workerLock.waitForLock(500);
				}
				catch (TimedOutException e) {
					if (trycount <= 0) {
						Log.writeByLevel(LogLevel.CORE, "Thread finish wait threshold limit exceeded.");
						/*
						i think it's gonna to infinite.
						force stop the process. 
						it may cause an io exception into worker code block.
						i'll be catch that excp and the thread will break safely
						 */
						internalStop(); 
						return;
					}
					
					trycount--;
					//thread.destroy() was deprecated!. why? go get the fuck ur self.
				}
			}
			
			internalStop();
		}
	}
	
	public final boolean isRunning() {
		return running;
	}
	
	public final String getExecutableFile() {
		return adbExecFile;
	}
	
	public void setExecutableFile(String file) {
		adbExecFile = file;
	}
	
	public void sendToOutputStream(String s) {
		if (!isRunning())
			return;
		
		
		OutputStream os = workingProcess.getOutputStream();  
		PrintStream bw= new PrintStream(new BufferedOutputStream(os), true);  
		
		
		bw.println(s);
		
	}

	@Override
	public void run() {
		BufferedReader streamReader;
		String bufferLine;
		boolean deviceNotConnected=true;
		
		if (!internalStart()) {
			processStartWaitLock.release();
			return;
		}
		
		running = true;
		processStartWaitLock.release();
		
		streamReader = new BufferedReader(new InputStreamReader(workingProcess.getInputStream()));
		
		while (consume) {
			
			try {
				bufferLine = streamReader.readLine();
				
				if (StringHelper.isNullOrEmpty(bufferLine)) {
					continue;
				}
				
				if (bufferLine.startsWith("-") || bufferLine.startsWith("*")) {
					continue;
				}
				
				if (deviceNotConnected) {
					deviceNotConnected = false;
					
					raiseEvent("DEVCON");
				}
			} catch (IOException e) {
				Log.writeByLevel(LogLevel.CORE, "AN IO EXCEPTION OCCURRED IN CONSUME LOOP.");
				consume = false;
				break;
			}
			
			if (bufferLine != null) {
				raiseEvent(bufferLine);
			}
		}
		
		Log.writeByLevel(LogLevel.CORE, "Exited consume loop. Releasing workerBlock");
		
		try {
			streamReader.close();
		} catch (IOException e) {
		}
		
		workerLock.release();
	}
	
	
}
