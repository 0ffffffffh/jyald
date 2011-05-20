package org.jyald;


import org.eclipse.swt.widgets.Display;
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
import org.jyald.uicomponents.TabContent;
import org.jyald.util.IterableArrayList;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class MainWindow {

	protected Shell shlYetAnotherLogcat;
	protected TabFolder tbTabContainer;
	private LogcatManager logcat;
	private IterableArrayList<UserFilterObject> userFilters;
	
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
		FilterManagerDialog dlg = new FilterManagerDialog(shlYetAnotherLogcat,SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dlg.open();
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
		
		try {
			logcat.addSlot(name, filterList).linkUi(filterLoggerUi);
		} catch (Exception e) {
			Log.write(e.getMessage());
			e.printStackTrace(Log.getPrintStreamInstance());
		}
		
	}
	
	protected void onTabContainerResized() {
		
		
	}
	
	protected void createContents() {
		shlYetAnotherLogcat = new Shell();
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
		
		MenuItem mnStart = new MenuItem(menu_2, SWT.NONE);
		mnStart.setText("Start");
		
		MenuItem mnSetAdb = new MenuItem(menu_2, SWT.NONE);
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
		logcat = new LogcatManager();
		
		TabContent allLog = new TabContent(tbTabContainer,"All Logs");
		
		try {
			logcat.addSlot("All Logs", null).linkUi(allLog);
			
		} catch (Exception e) {
			Log.write(e.getMessage());
		}
		
	}
}
