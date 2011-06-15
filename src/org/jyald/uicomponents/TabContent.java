package org.jyald.uicomponents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.jyald.loggingmodel.LogEntry;
import org.jyald.util.IterableArrayList;
import org.jyald.util.LogTimeCalculator;

public class TabContent {
	private TabFolder container;
	private TabItem tabPage;
	private String tabText;
	private ListView logList;
	private Thread ownerThread;
	private static LogTimeCalculator logTime = new LogTimeCalculator();
	public static IterableArrayList<TabContent> activeTabs = new IterableArrayList<TabContent>();
	
	public TabContent(TabFolder containerObj, String text, boolean selected) {
		ownerThread = Thread.currentThread();
		container = containerObj;
		tabText = text;
		tabPage = new TabItem(container,SWT.NONE);
		tabPage.setText(tabText);
		
		logList = new ListView(container);
		logList.showIn(tabPage);
		
		logList.addColumn("Log Time",20);
		logList.addColumn("Level",10);
		logList.addColumn("Pid", 10);
		logList.addColumn("Tag",15);
		logList.addColumn("Log Message",45);
		
		if (selected) {
			container.setSelection(tabPage);
		}
		
		activeTabs.add(this);
		
		onResize();
	}
	
	public TabContent(TabFolder containerObj, String text) {
		this(containerObj,text,false);
	}
	
	private final boolean asyncAccessRequired() {
		return Thread.currentThread().getId() != ownerThread.getId();
	}
	
	private void writeLogToList(LogEntry entry) {
		
		if (entry == null)
			return;
		
		try {
			logList.addItem(getColorForEntry(entry), getLogItemsForLogObject(entry));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getColorForEntry(LogEntry log) {
		
		switch (log.getDebugType()) {
			case Info:
				return ListViewItem.GREEN;
			case Debug:
				return ListViewItem.BLUE;
			case Warning:
				return ListViewItem.ORANGE;
			case Error:
				return ListViewItem.RED;
			case Verbose:
				return ListViewItem.BLACK;
		}
		
		return 0;
	}
	
	private String[] getLogItemsForLogObject(LogEntry log) {
		String[] itemText = new String[5];
		itemText[0] = logTime.getCurrentTimes();
		itemText[1] = log.getDebugTypeString();
		itemText[2] = String.valueOf(log.getPid());
		itemText[3] = log.getTag();
		itemText[4] = log.getMessage();
		
		return itemText;
	}
	
	
	
	public void dispose() {
		logList.dispose();
		tabPage.dispose();
		activeTabs.remove(this);
	}
	
	public void onResize() {
		logList.onResize();
	}
	
	public void writeLog(final LogEntry log) {
		
		if (asyncAccessRequired()) {
			Display disp = Display.getDefault();
			disp.asyncExec(new Runnable() {
				@Override
				public void run() {
					writeLogToList(log);
				}
			});
		}
		else {
			writeLogToList(log);
		}
	}
}
