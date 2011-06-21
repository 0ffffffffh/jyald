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
	
	
	public void addItem(int color, String ...items) throws Exception {
		if (items.length > listViewColumns.size())
			throw new Exception("Aww shit. ");
		
		
		ListViewItem lvi = new ListViewItem(lvListView);
		lvi.setItemTextColor(color);
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
