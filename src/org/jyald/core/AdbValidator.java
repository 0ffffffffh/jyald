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

import org.jyald.exceptions.TimedOutException;
import org.jyald.util.Lock;

public class AdbValidator {
	private final String[] validResponses = {"unknown","offline","bootloader","device" };
	
	private ProcessObject adbValidator;
	private boolean valid = false;
	private Lock respLock;
	
	public AdbValidator(String executable) {
		
		respLock = new Lock();
		adbValidator = new ProcessObject("get-state");
		adbValidator.setExecutableFile(executable);
		
		adbValidator.setOutputLineReceiver(new ProcessStdoutHandler() {

			@Override
			public void onOutputLineReceived(String line) {
				
				for (int i=0;i<validResponses.length;i++) {
					if (line.equalsIgnoreCase(validResponses[i])) {
						valid = true;
						break;
					}
				}
				
				respLock.release();
			}
			
		});
	}
	
	private void killValidator() {
		adbValidator.kill(true);
	}
	
	public boolean validate() {
		respLock.lock();
		adbValidator.start();
		
		try {
			respLock.waitForLock(5000);
		} catch (TimedOutException e) {
		}
		
		killValidator();
		return valid;
		
	}
}
