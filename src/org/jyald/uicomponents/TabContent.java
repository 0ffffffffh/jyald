package org.jyald.uicomponents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.jyald.loggingmodel.LogEntry;

public class TabContent {
	private TabFolder container;
	private TabItem tabPage;
	private String tabText;
	private ListView logList;
	private Thread ownerThread;
	
	public TabContent(TabFolder containerObj, String text, boolean selected) {
		ownerThread = Thread.currentThread();
		container = containerObj;
		tabText = text;
		tabPage = new TabItem(container,SWT.NONE);
		tabPage.setText(tabText);
		
		logList = new ListView(container);
		logList.showIn(tabPage);
		
		logList.addColumn("Log Time");
		logList.addColumn("Level");
		logList.addColumn("Pid");
		logList.addColumn("Tag");
		logList.addColumn("Log Message");
		
		if (selected) {
			container.setSelection(tabPage);
		}
	}
	
	public TabContent(TabFolder containerObj, String text) {
		this(containerObj,text,false);
	}
	
	private final boolean asyncAccessRequired() {
		return Thread.currentThread().getId() != ownerThread.getId();
	}
	
	private void writeLogToList(LogEntry entry) {
		try {
			logList.addItem(getLogItemsForLogObject(entry));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String[] getLogItemsForLogObject(LogEntry log) {
		String[] itemText = new String[5];
		itemText[0] = "JUST NOW! YA NE OLACAGIDI?"; //TODO: heey
		itemText[1] = log.getDebugTypeString();
		itemText[2] = String.valueOf(log.getPid());
		itemText[3] = log.getTag();
		itemText[4] = log.getMessage();
		
		return itemText;
	}
	
	public void dispose() {
		logList.dispose();
		tabPage.dispose();
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
