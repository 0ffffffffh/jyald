package org.jyald;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.jyald.core.LogcatManager;
import org.jyald.debuglog.Log;
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
	protected Shell shlYetAnotherLogcat;
	protected TabFolder tbTabContainer;
	private LogcatManager logcat;
	private IterableArrayList<UserFilterObject> userFilters;
	private Setting setting;
	private MenuItem mnStart;
	
	public static void main(String[] args) {
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
		shlYetAnotherLogcat.open();
		shlYetAnotherLogcat.layout();
		
		while (!shlYetAnotherLogcat.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void onmnFilterManagerClick() {
		IterableArrayList<UserFilterObject> removedFilters;
		FilterManagerDialog dlg = new FilterManagerDialog(shlYetAnotherLogcat,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dlg.setUserFilterList(userFilters);
		dlg.open();
		
		removedFilters = dlg.getRemovedFilters();
		
		if (removedFilters.getCount() > 0) {
			for (UserFilterObject currFilter : removedFilters) {
				logcat.removeSlot(currFilter.getFilterName());
			}
		}
	}
	
	protected void onmnNewFilterClick() {
		FilterList filterList;
		String name;
		boolean linkState;
		UserFilterObject userFilter;
		TabContent filterLoggerUi;
		
		AddNewFilterDialog dlg = new AddNewFilterDialog(shlYetAnotherLogcat,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
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
			logcat.addSlot(name, filterList).linkUi(filterLoggerUi);
		} catch (Exception e) {
			Log.write(e.getMessage());
			e.printStackTrace(Log.getPrintStreamInstance());
		}
	}
	
	protected void onmnSetAdbClick() {
		FileDialog fileDlg = new FileDialog(shlYetAnotherLogcat,SWT.OPEN);
		fileDlg.setText("Please select adb executable file");
		String selected = fileDlg.open();
		
		setting.adbExecutableFile = selected;
		
		if (StringHelper.isNullOrEmpty(selected)) {
			return;
		}
		
		if (Setting.saveSetting(setting)) 
			MsgBox.show(shlYetAnotherLogcat,"setting","adb location has been set", SWT.ICON_INFORMATION);
		else
			MsgBox.show(shlYetAnotherLogcat, "setting", "an error occurred while writing adb location", SWT.ICON_ERROR);
		
	}
	
	protected void onmnStartClick() {
		if (!logcat.isActive()) {
			logcat.stop();
			mnStart.setText("Start");
			return;
		}
		
		try {
			if (!logcat.start()) {
				MsgBox.show(shlYetAnotherLogcat, "error", "logcat could not started", SWT.ICON_ERROR);
				return;
			}
		} catch (Exception e) {
			MsgBox.show(shlYetAnotherLogcat, "error", e.getMessage(), SWT.ICON_ERROR);
			return;
		}
		
		mnStart.setText("Stop");
	}
	
	protected void onTabContainerResized() {
		for (TabContent tab : TabContent.activeTabs) {
			tab.onResize();
		}
	}
	
	protected void createContents() {
		shlYetAnotherLogcat = new Shell();
		shlYetAnotherLogcat.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
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
		
		shlYetAnotherLogcat.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if (tbTabContainer != null) {
					
					tbTabContainer.setSize(shlYetAnotherLogcat.getSize().x-40, 
							shlYetAnotherLogcat.getSize().y-80);
				}
			}
		});
		
		shlYetAnotherLogcat.setSize(450, 300);
		shlYetAnotherLogcat.setText("Yet Another Logcat Dumper");
		
		Menu menu = new Menu(shlYetAnotherLogcat, SWT.BAR);
		shlYetAnotherLogcat.setMenuBar(menu);
		
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
				MessageBox msg = new MessageBox(shlYetAnotherLogcat);
				msg.setText("About");
				msg.setMessage("jyald\nauthor: oguz kartal \'11");
				msg.open();
			}
		});
		
		mnAboutMenu.setText("About");
		
		tbTabContainer = new TabFolder(shlYetAnotherLogcat, SWT.NONE);
		
		tbTabContainer.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				onTabContainerResized();
			}
		});
		
		tbTabContainer.setBounds(10, 10, 403, 222);
		
		userFilters = new IterableArrayList<UserFilterObject>();
		
		setting = Setting.loadSetting();
		
		logcat = new LogcatManager();
		logcat.setAdb(setting.adbExecutableFile);
		
		userFilters = UserFilterObject.loadFilters("filters.flt");
		
		TabContent allLog = new TabContent(tbTabContainer,"All Logs");
		
		try {
			logcat.addSlot("All Logs", null).linkUi(allLog);
			
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
					logcat.addSlot(filter.getFilterName(), filter.getFilterList()).
												linkUi(filterTabPage);
				} catch (Exception e1) {
					Log.write(e1.getMessage());
					e1.printStackTrace(Log.getPrintStreamInstance());
				}
			}
		}
		
	}
}
