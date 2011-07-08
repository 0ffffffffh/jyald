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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.jyald.core.DeviceActiveHandler;
import org.jyald.core.LogcatManager;
import org.jyald.debuglog.Log;
import org.jyald.debuglog.LogLevel;
import org.jyald.loggingmodel.FilterList;
import org.jyald.loggingmodel.UserFilterObject;
import org.jyald.uicomponents.MsgBox;
import org.jyald.uicomponents.TabContent;
import org.jyald.util.IterableArrayList;
import org.jyald.util.StringHelper;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;


public class MainWindow {
	protected Shell shlMain;
	protected TabFolder tbTabContainer;
	private LogcatManager logcat;
	private IterableArrayList<UserFilterObject> userFilters;
	private Setting setting;
	private MenuItem mnStart;
	private final String jyaldTitle = "Yet Another Logcat Dumper";
	
	private static void parseCommandLine(String[] args) {
		int level = 0,levelComb=LogLevel.ALL;
		boolean pushToLevel=false;
		boolean done=false;
		
		List<String> levelOptions = new ArrayList<String>();
		
		
		for (int i=0;!done && i<args.length;i++) {
			
			if (args[i].equals("--level") || args[i].equals("-l")) {
				if (!pushToLevel) {
					pushToLevel=true;
					continue;
				}
			}
			else if (args[i].equals("--replica") || args[i].equals("-r")) {
				if (pushToLevel)
					done=true;
				
				if (i != args.length-1) {
					Log.printOnDefaultSysStreamLogReplica = args[i+1].equals("yes");
					i++;
				}
			}
			else if (pushToLevel) {
				levelOptions.add(args[i]);
			}
		}
		
		for (String levelStr : levelOptions) {
			level = LogLevel.getLogLevelCombination(levelStr);
			
			if (level == -1)
				continue;
			
			if (level == LogLevel.ALL) {
				levelComb = LogLevel.ALL;
				break;
			}
			
			levelComb |= level;
		}
		
		Log.setLogLevel(levelComb);
	}
	
	public static void main(String[] args) {
		
		Log.printOnDefaultSysStreamLogReplica = false;
		
		parseCommandLine(args);
		
		
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace(Log.getPrintStreamInstance());
		}
	}

	
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlMain.open();
		shlMain.layout();
		
		while (!shlMain.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void setWindowTitle(String s) {
		if (StringHelper.isNullOrEmpty(s)) {
			shlMain.setText(jyaldTitle);
			return;
		}
		
		shlMain.setText(String.format("%s - (%s)",jyaldTitle,s));
	}
	
	protected void onmnFilterManagerClick() {
		IterableArrayList<UserFilterObject> removedFilters;
		FilterManagerDialog dlg = new FilterManagerDialog(shlMain,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dlg.setUserFilterList(userFilters);
		dlg.open();
		
		removedFilters = dlg.getRemovedFilters();
		
		if (removedFilters.getCount() > 0) {
			tbTabContainer.setSelection(0);
			
			for (UserFilterObject currFilter : removedFilters) {
				
				logcat.removeSlot(currFilter.getFilterName());
			}
		}
		
		UserFilterObject.saveFilters(userFilters, "filters.flt");
	}
	
	protected void onmnNewFilterClick() {
		FilterList filterList;
		String name;
		boolean linkState;
		UserFilterObject userFilter;
		TabContent filterLoggerUi;
		
		AddNewFilterDialog dlg = new AddNewFilterDialog(shlMain,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		filterList = (FilterList)dlg.open();
		
		
		if (filterList == null) 
			return;
		
		name = dlg.getFilterName();
		linkState = dlg.getLinkState();
		
		userFilter = new UserFilterObject(filterList,name,linkState);
		userFilters.add(userFilter);
		
		filterLoggerUi = new TabContent(tbTabContainer, name, true);
		
		
		UserFilterObject.saveFilters(userFilters, "filters.flt");
		
		try {
			logcat.addSlot(name, filterList, filterLoggerUi);
		} catch (Exception e) {
			Log.write(e.getMessage());
			e.printStackTrace(Log.getPrintStreamInstance());
		}
	}
	
	protected void onmnSetAdbClick() {
		FileDialog fileDlg = new FileDialog(shlMain,SWT.OPEN);
		fileDlg.setText("Please select adb executable file");
		String selected = fileDlg.open();
		
		setting.adbExecutableFile = selected;
		
		if (StringHelper.isNullOrEmpty(selected)) {
			return;
		}
		
		AdbValidatorDialog validatorDlg = new AdbValidatorDialog(shlMain,SWT.DIALOG_TRIM,setting.adbExecutableFile);
		
		
		if (!((Boolean)validatorDlg.open())) {
			MsgBox.show(shlMain, "Validate error", "It's not an adb executable", SWT.ICON_ERROR);
			return;
		}
		
		if (Setting.saveSetting(setting))  {
			logcat.setAdb(setting.adbExecutableFile);
			MsgBox.show(shlMain,"setting","adb location has been set", SWT.ICON_INFORMATION);
		}
		else
			MsgBox.show(shlMain, "setting", "an error occurred while writing adb location", SWT.ICON_ERROR);
		
	}
	
	protected void onmnStartClick() {
		if (logcat.isActive()) {
			logcat.stop();
			mnStart.setText("Start");
			return;
		}
		
		setWindowTitle("Waiting for device...");
		
		
		try {
			if (!logcat.start()) {
				setWindowTitle(null);
				MsgBox.show(shlMain, "error", "logcat could not started", SWT.ICON_ERROR);
				return;
			}
		} catch (Exception e) {
			setWindowTitle("Connection error");
			MsgBox.show(shlMain, "error", e.getMessage(), SWT.ICON_ERROR);
			return;
		}
		
		mnStart.setText("Stop");
	}
	
	protected void onmnCreateShellClick() {
		AdbShellDialog shell = new AdbShellDialog(shlMain,SWT.SHELL_TRIM,setting.adbExecutableFile);
		shell.open();
	}
	
	protected void onTabContainerResized() {
		for (TabContent tab : TabContent.activeTabs) {
			tab.onResize();
		}
	}
	
	protected void onTabActivated() {
		onTabContainerResized();
	}
	
	protected void createContents() {
		shlMain = new Shell();
		shlMain.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				Log.writeByLevel(LogLevel.UI, "Exiting jyald. Stopping components");
				
				if (logcat.isActive()) {
					logcat.stop();
					try {
						logcat.dispose();
					} catch (Exception e1) {
						Log.write(e1.getMessage());
					}
				}
				
				Log.finish();
			}
		});
		
		shlMain.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if (tbTabContainer != null) {
					
					tbTabContainer.setSize(shlMain.getSize().x-40, 
							shlMain.getSize().y-80);
				}
			}
		});
		
		shlMain.setSize(464, 339);
		shlMain.setText(jyaldTitle);
		
		Menu menu = new Menu(shlMain, SWT.BAR);
		shlMain.setMenuBar(menu);
		
		MenuItem mnMainMenu = new MenuItem(menu, SWT.CASCADE);
		mnMainMenu.setText("Menu");
		
		Menu menu_1 = new Menu(mnMainMenu);
		mnMainMenu.setMenu(menu_1);
		
		MenuItem mnAddFilter = new MenuItem(menu_1, SWT.NONE);
		mnAddFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onmnNewFilterClick();
			}
		});
		
		mnAddFilter.setText("Add New Filter");
		
		MenuItem mnFilters = new MenuItem(menu_1, SWT.NONE);
		mnFilters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onmnFilterManagerClick();
			}
		});
		
		mnFilters.setText("Filters");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mnCreateShell = new MenuItem(menu_1, SWT.NONE);
		mnCreateShell.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onmnCreateShellClick();
			}
		});
		mnCreateShell.setText("Create shell");
		
		MenuItem mnSystemMenu = new MenuItem(menu, SWT.CASCADE);
		mnSystemMenu.setText("System");
		
		Menu menu_2 = new Menu(mnSystemMenu);
		mnSystemMenu.setMenu(menu_2);
		
		mnStart = new MenuItem(menu_2, SWT.NONE);
		mnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onmnStartClick();
			}
		});
		mnStart.setText("Start");
		
		MenuItem mnSetAdb = new MenuItem(menu_2, SWT.NONE);
		mnSetAdb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onmnSetAdbClick();
			}
		});
		mnSetAdb.setText("Set ADB");
		
		MenuItem mnAboutMenu = new MenuItem(menu, SWT.NONE);
		mnAboutMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AboutDialog aboutDlg = new AboutDialog(shlMain, SWT.DIALOG_TRIM);
				aboutDlg.open();
			}
		});
		
		mnAboutMenu.setText("About");
		
		tbTabContainer = new TabFolder(shlMain, SWT.NONE);
		tbTabContainer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onTabActivated();
			}
		});
		
		tbTabContainer.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				onTabContainerResized();
			}
		});
		
		tbTabContainer.setBounds(10, 10, 403, 222);
		
		setting = Setting.loadSetting();
		
		logcat = new LogcatManager();
		logcat.registerActivationHandler(new DeviceActiveHandler() {

			@Override
			public void onDeviceActivated() {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						setWindowTitle("Device activated");
					}
					
				});
				
			}
			
		});
		logcat.setAdb(setting.adbExecutableFile);
		
		userFilters = UserFilterObject.loadFilters("filters.flt");
		
		TabContent allLog = new TabContent(tbTabContainer,"All Logs");
		
		try {
			logcat.addSlot("All Logs", null,allLog);
			
		} catch (Exception e) {
			Log.write(e.getMessage());
			e.printStackTrace(Log.getPrintStreamInstance());
		}
		
		if (userFilters == null) {
			userFilters = new IterableArrayList<UserFilterObject>();
		}
		else {
			TabContent filterTabPage;
			
			for (UserFilterObject filter : userFilters) {
				filterTabPage = new TabContent(tbTabContainer,filter.getFilterName());
				
				try {
					logcat.addSlot(filter.getFilterName(), filter.getFilterList(),filterTabPage);
				} catch (Exception e1) {
					Log.write(e1.getMessage());
					e1.printStackTrace(Log.getPrintStreamInstance());
				}
			}
		}
		
	}
}
