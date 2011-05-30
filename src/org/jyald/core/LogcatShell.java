package org.jyald.core;

public class LogcatShell {
	private ProcessObject adbShellProcess;
	
	public LogcatShell() {
		adbShellProcess = new ProcessObject("shell");
		adbShellProcess.setOutputLineReceiver(new ProcessStdoutHandler() {

			@Override
			public void onOutputLineReceived(String line) {
				//TODO:
			}
			
		});
	}
	
	public boolean startShell() {
		return false;
	}
	
	public void stopShell() {
		
	}
	
	public boolean sendShellInput(String shellInput) {
		return false;
	}
	
	
}
