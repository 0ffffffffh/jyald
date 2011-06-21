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
				if (line.equals("DEVCON")) {
					shellDlgUi.writeOutput("Connected");
					shellDlgUi.writeOutput("Welcome to JYALD Shell Interface.");
					return;
				}
				
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
