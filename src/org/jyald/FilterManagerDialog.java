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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.jyald.loggingmodel.UserFilterObject;
import org.jyald.uicomponents.ListEx;
import org.jyald.uicomponents.MsgBox;
import org.jyald.util.IterableArrayList;


public class FilterManagerDialog extends DialogExtender {

	protected Object result;
	protected Shell shell;
	protected Button btnAll;
	protected ListEx lstUserFilters;
	private IterableArrayList<UserFilterObject> filters;
	private IterableArrayList<UserFilterObject> removedFilters;
	
	public FilterManagerDialog(Shell parent, int style) {
		super(parent, style);
		setText("Filter Manager");
		
		removedFilters = new IterableArrayList<UserFilterObject>();
	}
	
	public Object open() {
		createContents();
		
		locateCenter();
		
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return result;
	}

	private void closeDialog() {
		shell.close();
		shell.dispose();
	}
	
	private void onchkAllCheckStateChanged() {
		if (btnAll.getSelection())
			lstUserFilters.selectAll();
		else
			lstUserFilters.deselectAll();
	}
	
	private void onbtnRemoveSelectedFiltersClick() {
		ArrayList<Object> selectedItems;
		
		if (lstUserFilters.getSelectedItemCount() < 1) {
			MsgBox.show(shell, "warning", "You must select an item first", SWT.ICON_WARNING);
			return;
		}
		
		selectedItems = lstUserFilters.getSelectedItems();
		
		for (Object curr : selectedItems) {
			removedFilters.add((UserFilterObject)curr);
		}
		
		lstUserFilters.removeSelected();
		
		for (UserFilterObject obj : removedFilters) {
			filters.remove(obj);
		}
		
	}
	
	
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(376, 312);
		shell.setText(getText());
		
		lstUserFilters = new ListEx(shell, SWT.BORDER | SWT.MULTI);
		lstUserFilters.setBounds(20, 33, 328, 209);
		
		Button btnRemoveSelectedFilters = new Button(shell, SWT.NONE);
		btnRemoveSelectedFilters.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onbtnRemoveSelectedFiltersClick();
			}
		});
		
		btnRemoveSelectedFilters.setBounds(123, 248, 144, 25);
		btnRemoveSelectedFilters.setText("Remove Selected Filters");
		
		Button btnOk = new Button(shell, SWT.NONE);
		
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				closeDialog();
			}
		});
		
		btnOk.setBounds(273, 248, 75, 25);
		btnOk.setText("Ok");
		
		btnAll = new Button(shell, SWT.CHECK);
		
		btnAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onchkAllCheckStateChanged();
			}
		});
		
		btnAll.setBounds(20, 10, 93, 16);
		btnAll.setText("Select All");
		
		for (UserFilterObject filter : filters) {
			lstUserFilters.add(filter);
		}
		
	}
	
	public void setUserFilterList(IterableArrayList<UserFilterObject> list) {
		filters = list;
		
	}
	
	public final IterableArrayList<UserFilterObject> getRemovedFilters() {
		return removedFilters;
	}

	@Override
	public Shell getShell() {
		return shell;
	}
	
}
