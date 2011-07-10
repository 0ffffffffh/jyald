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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.jyald.loggingmodel.*;
import org.jyald.uicomponents.MsgBox;
import org.jyald.util.StringHelper;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class AddNewFilterDialog extends DialogExtender {

	protected Shell shlAddANew;
	private Text txtFilterName;
	private Text txtValue;
	private List lstFilters;
	private FilterList filters;
	private Combo cbFilterOp;
	private Combo cbFilterSection;
	private Combo cbValues;
	private Button chkUseRegularExpression;
	private Button chkLinkRulesWith;
	
	private boolean linkWithAndState;
	private String filterName;
	
	
	public AddNewFilterDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		filters = new FilterList();
	}
	
	public Object open() {
		createContents();
		
		locateCenter();
		
		shlAddANew.open();
		shlAddANew.layout();
		Display display = getParent().getDisplay();
		while (!shlAddANew.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return filters.getCount() == 0 ? null : filters;
	}
	
	private LogFilter getFilterObjectFromUI() {
		LogFilter filter;
		Object val = null;
		FilterOperator op = LogFilter.getFilterOperatorFromInt(cbFilterOp.getSelectionIndex());
		FilterSection sect = LogFilter.getFilterSectionFromInt(cbFilterSection.getSelectionIndex());
		DebugType debugType = LogEntry.getDebugTypeFromInt(cbValues.getSelectionIndex()+1);
		
		switch (sect) {
			case LogTypeSection:
				val = debugType;
				break;
			case TagSection:
			case MessageSection: {
				val = txtValue.getText();
				if (StringHelper.isNullOrEmpty((String)val)) {
					MsgBox.show(shlAddANew, "Error", "Value textbox cant be empty", SWT.ICON_ERROR);
					return null;
				}
				break;
			}
			case PidSection: {
				try {
					val = Integer.parseInt(txtValue.getText());
				}
				catch(Exception e) {
					MsgBox.show(shlAddANew, "Error", e.getMessage(), SWT.ICON_ERROR);
					return null;
				}
				break;
			}
				
		}
		
		filter = new LogFilter(op,val,sect);
		filter.setUseRegex(chkUseRegularExpression.getSelection());
		
		return filter;
	}
	
	private void onBtnAddToFilterClick() {
		LogFilter filter = getFilterObjectFromUI();
		
		if (filter != null) {
			filters.addFilter(filter);
			lstFilters.add(filter.toString());
		}
	}
	
	private void onBtnCancelClick() {
		filters.clear();
		shlAddANew.close();
	}
	
	private void onBtnOkClick() {
		
		filterName = txtFilterName.getText();
		
		if (StringHelper.isNullOrEmpty(filterName)) {
			MsgBox.show(shlAddANew, "Warning", "Filter name can't be empty", SWT.ICON_WARNING);
			return;
		}
		
		
		linkWithAndState = chkLinkRulesWith.getSelection();
		shlAddANew.close();
	}
	
	private void onCbFilterSectionSelectChanged() {
		cbValues.setVisible(cbFilterSection.getSelectionIndex() == 0);
		txtValue.setVisible(!cbValues.getVisible());
	}

	
	private void createContents() {
		shlAddANew = new Shell(getParent(), getStyle());
		shlAddANew.setSize(659, 346);
		shlAddANew.setText("Add a New Filter");
		
		Group grpG = new Group(shlAddANew, SWT.NONE);
		grpG.setText("Filter Options");
		grpG.setBounds(10, 38, 317, 216);
		
		Label lblFilterSection = new Label(grpG, SWT.NONE);
		lblFilterSection.setBounds(17, 24, 114, 15);
		lblFilterSection.setText("Filter Section:");
		
		cbFilterSection = new Combo(grpG, SWT.NONE);
		cbFilterSection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCbFilterSectionSelectChanged();
			}
		});
		
		cbFilterSection.setItems(new String[] {"Debug Level", "Tag", "Process Id", "Message"});
		cbFilterSection.setBounds(137, 21, 170, 23);
		cbFilterSection.select(0);
		
		Label lblFilterOperation = new Label(grpG, SWT.NONE);
		lblFilterOperation.setBounds(10, 59, 121, 15);
		lblFilterOperation.setText("Filter Operation:");
		
		cbFilterOp = new Combo(grpG, SWT.NONE);
		cbFilterOp.setItems(new String[] {"Equal", "Not Equal", "Greater", "Lower", "Greater And Equal", "Lower And Equal", "Contain", "Not Contain"});
		cbFilterOp.setBounds(137, 56, 170, 23);
		cbFilterOp.select(0);
		
		Label lblValue = new Label(grpG, SWT.NONE);
		lblValue.setBounds(57, 94, 74, 15);
		lblValue.setText("Value:");
		
		chkUseRegularExpression = new Button(grpG, SWT.CHECK);
		chkUseRegularExpression.setBounds(117, 122, 190, 16);
		chkUseRegularExpression.setText("Use Regular Expression");
		
		Button btnAddToFilter = new Button(grpG, SWT.NONE);
		btnAddToFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onBtnAddToFilterClick();
			}
		});
		
		btnAddToFilter.setBounds(177, 164, 130, 31);
		btnAddToFilter.setText("Add to Filter List");
		
		cbValues = new Combo(grpG, SWT.NONE);
		cbValues.setItems(new String[] {"Information", "Debug", "Warning", "Error", "Verbose"});
		cbValues.setBounds(137, 91, 170, 23);
		cbValues.select(0);
		
		txtValue = new Text(grpG, SWT.BORDER);
		txtValue.setBounds(137, 91, 170, 21);
		
		Label lblFilterName = new Label(shlAddANew, SWT.NONE);
		lblFilterName.setBounds(29, 13, 84, 15);
		lblFilterName.setText("Filter Name:");
		
		txtFilterName = new Text(shlAddANew, SWT.BORDER);
		txtFilterName.setBounds(144, 10, 183, 21);
		
		lstFilters = new List(shlAddANew, SWT.BORDER);
		lstFilters.setBounds(344, 9, 299, 244);
		
		chkLinkRulesWith = new Button(shlAddANew, SWT.CHECK);
		chkLinkRulesWith.setBounds(344, 261, 218, 16);
		chkLinkRulesWith.setText("Link Rules with \"AND\"");
		
		Button btnOk = new Button(shlAddANew, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onBtnOkClick();
			}
		});
		btnOk.setBounds(487, 283, 75, 25);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shlAddANew, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onBtnCancelClick();
			}
		});
		btnCancel.setBounds(568, 283, 75, 25);
		btnCancel.setText("Cancel");
		
		onCbFilterSectionSelectChanged();
	}
	
	public final String getFilterName() {
		return filterName;
	}
	
	public final boolean getLinkState() {
		return linkWithAndState;
	}

	@Override
	public Shell getShell() {
		return shlAddANew;
	}
	
}
