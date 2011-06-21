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


package org.jyald;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jyald.core.LogcatShell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;

public class AdbShellDialog extends DialogExtender {

	private LogcatShell logcatShell;
	protected Object result;
	protected Shell shell;
	private StyledText txtShellOutput;
	private Text txtCommandInput;
	
	public AdbShellDialog(Shell parent, int style, String adb) {
		super(parent, style);
		setText("New Shell");
		logcatShell = new LogcatShell(adb,this);
	}

	
	public Object open() {
		createContents();
		
		locateCenter();
		
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		logcatShell.stopShell();
		
		return result;
	}
	
	protected void onTxtCommandInputKeyEnter() {
		String command = txtCommandInput.getText();
		
		txtCommandInput.setText("");
		
		logcatShell.sendShellInput(command);
	}
	
	protected void onResize() {
		if (txtShellOutput != null) {
			Rectangle rect = txtShellOutput.getBounds();
			rect.width = shell.getClientArea().width - 20;
			rect.height = shell.getClientArea().height - 80;
			
			txtShellOutput.setBounds(rect);
			
			rect.y = rect.height + 20;
			rect.height = (shell.getClientArea().height - rect.height) - 30;
			txtCommandInput.setBounds(rect);
		}
	}
	
	private void createContents() {
		shell = new Shell(getParent(), SWT.CLOSE | SWT.MIN | SWT.RESIZE | SWT.TITLE);
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				onResize();
			}
		});
		shell.setSize(430, 306);
		shell.setText(getText());
		
		txtShellOutput = new StyledText(shell, SWT.BORDER);
		txtShellOutput.setBounds(10, 10, 397, 208);
		
		txtCommandInput = new Text(shell, SWT.BORDER);
		txtCommandInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					onTxtCommandInputKeyEnter();
				}
			}
		});
		
		txtCommandInput.setBounds(10, 226, 397, 36);
		txtCommandInput.setFocus();
		
		txtShellOutput.append("Please wait. Trying connect to Adb Shell\n");
		
		logcatShell.startShell();
		
	}
	
	public void writeOutput(final String output) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() { 
				txtShellOutput.append(output + "\n");
				txtShellOutput.setSelection(txtShellOutput.getText().length());
			}
		});
	}


	@Override
	public Shell getShell() {
		return shell;
	}
	
}
