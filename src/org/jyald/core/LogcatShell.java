package org.jyald.core;

import org.jyald.AdbShellDialog;

public class LogcatShell {
	private ProcessObject adbShellProcess;
	private AdbShellDialog shellDlgUi;
	
	public LogcatShell(String adb, AdbShellDialog shellDlg) {
		adbShellProcess = new ProcessObject("shell");
		adbShellProcess.setOutputLineReceiver(new ProcessStdoutHandler() {

			@Override
			public void onOutputLineReceived(String line) {
				if (line.equals("DEVCON"))
					return;
				
				shellDlgUi.writeOutput(line);
			}
			
		});
		
		shellDlgUi = shellDlg;
		
		adbShellProcess.setExecutableFile(adb);
	}
	
	public boolean startShell() {
		return adbShellProcess.start();
	}
	
	public void stopShell() {
		adbShellProcess.kill(true);
	}
	
	public boolean sendShellInput(String shellInput) {
		adbShellProcess.sendToOutputStream(shellInput);
		return false;
	}
	
	
}
