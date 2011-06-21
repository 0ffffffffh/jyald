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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.jyald.util.UrlLauncher;

public class AboutDialog extends DialogExtender {

	protected Object result;
	protected Shell shell;
	
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
		setText("About JYald");
	}
	
	public Object open() {
		createContents();
		Display display = getParent().getDisplay();

		locateCenter();
		
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(398, 260);
		shell.setText(getText());
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(72, 10, 297, 26);
		lblNewLabel.setText("JYald (Yet Another Logcat Dumper)");
		
		Link link = new Link(shell, SWT.NONE);
		link.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				UrlLauncher.go(event.text);
			}
			
		});
		
		link.setBounds(78, 101, 274, 28);
		link.setText("<a href=\"http://ahmetalpbalkan.com\">Ahmet Alp Balkan</a> for valuable feedbacks.");
		
		Label lblSpecialThanks = new Label(shell, SWT.NONE);
		lblSpecialThanks.setBounds(45, 80, 239, 26);
		lblSpecialThanks.setText("Special Thanks to;");
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnOk.setBounds(294, 198, 75, 25);
		btnOk.setText("Ok");
		
		Link link_1 = new Link(shell, SWT.NONE);
		link_1.setBounds(45, 46, 199, 28);
		link_1.setText("Author: <a href=\"http://oguzkartal.net\">Oguz Kartal</a>");
		link_1.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				UrlLauncher.go(event.text);
			}
			
		});
		
		Link link_2 = new Link(shell, SWT.NONE);
		link_2.setBounds(26, 135, 356, 57);
		link_2.setText("This program is distributed under GPL License. You can see source code on the project <a href=\"https://github.com/0ffffffffh/jyald\">GitHub</a> page.");
		
		link_2.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				UrlLauncher.go(event.text);
			}
			
		});
		
	}
	
	@Override
	public Shell getShell() {
		return shell;
	}
}
