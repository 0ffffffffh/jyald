package org.jyald.uicomponents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.jyald.loggingmodel.LogEntry;

public class TabContent {
	private TabFolder container;
	private TabItem tabPage;
	private String tabText;
	private ListView logList;
	
	public TabContent(TabFolder containerObj, String text) {
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
	
	public void writeLog(LogEntry log) {
		
		try {
			logList.addItem(getLogItemsForLogObject(log));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
