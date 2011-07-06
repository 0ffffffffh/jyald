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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;

public class ListView {
	TabFolder container; 
	Table lvListView;
	java.util.List<ListViewColumn> listViewColumns;
	Menu menu;
	MenuItem newItem;
	
	public ListView(TabFolder parent) {
		container = parent;
		listViewColumns = new ArrayList<ListViewColumn>();	
	}
	
	
	private void clear() {
		lvListView.clearAll();
		lvListView.setItemCount(0);
	}
	
	private void initPopupMenu() {
		menu = new Menu(lvListView);
		
		newItem = new MenuItem(menu,SWT.NONE);
		
		newItem.setText("Clear logs");
		lvListView.setMenu(menu);
		
		newItem.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent se) {
			}

			@Override
			public void widgetSelected(SelectionEvent se) {
				clear();
			}
			
		});
	}
	
	private void showPopupMenu() {
		lvListView.setMenu(menu);
	}
	
	public void showIn(TabItem tab) {
		lvListView = new Table(container,SWT.BORDER | SWT.FULL_SELECTION);
		lvListView.setHeaderVisible(true);
		tab.setControl(lvListView);
		
		initPopupMenu();
		
		lvListView.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				if (arg0.button == 3)
					showPopupMenu();
			}
			
		});
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
