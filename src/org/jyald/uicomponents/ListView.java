package org.jyald.uicomponents;


import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class ListView {
	TabFolder container; 
	Table lvListView;
	java.util.List<ListViewColumn> listViewColumns;
	
	public ListView(TabFolder parent) {
		container = parent;
		listViewColumns = new ArrayList<ListViewColumn>();
		
	}
	
	public void showIn(TabItem tab) {
		lvListView = new Table(container,SWT.BORDER | SWT.FULL_SELECTION);
		lvListView.setHeaderVisible(true);
		tab.setControl(lvListView);
	}
	
	public boolean addColumn(String columnName) {
		ListViewColumn column = new ListViewColumn(lvListView,columnName);
		return listViewColumns.add(column);
	}
	
	public boolean addColumn(String columnName, int percentWidth) {
		ListViewColumn column = new ListViewColumn(lvListView,columnName, percentWidth);
		return listViewColumns.add(column);
	}
	
	public void addItem(String ...items) throws Exception {
		if (items.length > listViewColumns.size())
			throw new Exception("Aww shit. ");
		
		
		ListViewItem lvi = new ListViewItem(lvListView);
		lvi.setItemTextColor(ListViewItem.GREEN);
		lvi.setItemText(items);
		
		lvListView.setRedraw(false);
		lvListView.setSelection(lvListView.getItemCount()-1);
		lvListView.setRedraw(true);
		
	}
	
	public void onResize() {
		for (ListViewColumn col : listViewColumns) {
			col.onResize();
		}
	}
	
	public void dispose() {
		
		lvListView.clearAll();
		
		for (ListViewColumn lvi : listViewColumns) {
			lvi.dispose();
		}
		
		try {
			lvListView.dispose();
		}
		catch (Exception e) {}
	}
	
	
}
