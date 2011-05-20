package org.jyald.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	
	public ProcessObject() {
		workerLock = new Lock();
		processStartWaitLock = new Lock();
		running = false;
		consume = true;
	}
	
	private boolean internalStart() {
		
		try {
			workingProcess = Runtime.getRuntime().exec(String.format("\"%s\" logcat", adbExecFile));
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	private void internalStop() {
		if (workingProcess != null) {
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
		
		consumeWorker = new Thread(this);
		consumeWorker.start();
		
		try {
			processStartWaitLock.waitForLock(10 * 1000);
		}
		catch (TimedOutException e) {
			return false;
		}
		
		return isRunning();
	}
	
	public void kill() {
		int trycount=10;
		
		if (workingProcess != null && running) {
			
			consume = false;
			
			while (consumeWorker.getState() != Thread.State.TERMINATED) {
				try {
					workerLock.waitForLock(1000);
				}
				catch (TimedOutException e) {
					if (trycount <= 0) {
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
				consume = false;
				break;
			}
			
			if (bufferLine != null) {
				raiseEvent(bufferLine);
			}
		}
		
		try {
			streamReader.close();
		} catch (IOException e) {
		}
		
		workerLock.release();
	}
	
	
}
