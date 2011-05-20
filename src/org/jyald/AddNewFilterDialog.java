package org.jyald;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.jyald.loggingmodel.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class AddNewFilterDialog extends Dialog {

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
			case MessageSection:
				val = txtValue.getText();
				break;
			case PidSection: {
				try {
					val = Integer.parseInt(txtValue.getText());
				}
				catch(Exception e) {
					MessageBox exDlg = new MessageBox(shlAddANew,SWT.ICON_ERROR);
					exDlg.setText("Error");
					exDlg.setMessage(e.getMessage());
					exDlg.open();
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
		filters.addFilter(filter);
		lstFilters.add(filter.toString());
	}
	
	private void onBtnCancelClick() {
		shlAddANew.close();
	}
	
	private void onBtnOkClick() {
		filterName = txtFilterName.getText();
		linkWithAndState = chkLinkRulesWith.getSelection();
		shlAddANew.close();
	}
	
	private void onCbFilterSectionSelectChanged() {
		cbValues.setVisible(cbFilterSection.getSelectionIndex() == 0);
	}

	
	private void createContents() {
		shlAddANew = new Shell(getParent(), getStyle());
		shlAddANew.setSize(615, 346);
		shlAddANew.setText("Add a New Filter");
		
		Group grpG = new Group(shlAddANew, SWT.NONE);
		grpG.setText("Filter Options");
		grpG.setBounds(10, 38, 284, 216);
		
		Label lblFilterSection = new Label(grpG, SWT.NONE);
		lblFilterSection.setBounds(25, 24, 78, 15);
		lblFilterSection.setText("Filter Section:");
		
		cbFilterSection = new Combo(grpG, SWT.NONE);
		cbFilterSection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCbFilterSectionSelectChanged();
			}
		});
		
		cbFilterSection.setItems(new String[] {"Debug Level", "Tag", "Process Id", "Message"});
		cbFilterSection.setBounds(104, 21, 170, 23);
		cbFilterSection.select(0);
		
		Label lblFilterOperation = new Label(grpG, SWT.NONE);
		lblFilterOperation.setBounds(10, 59, 93, 15);
		lblFilterOperation.setText("Filter Operation:");
		
		cbFilterOp = new Combo(grpG, SWT.NONE);
		cbFilterOp.setItems(new String[] {"Equal", "Not Equal", "Greater", "Lower", "Greater And Equal", "Lower And Equal", "Contain", "Not Contain"});
		cbFilterOp.setBounds(104, 56, 170, 23);
		cbFilterOp.select(0);
		
		Label lblValue = new Label(grpG, SWT.NONE);
		lblValue.setBounds(66, 96, 32, 15);
		lblValue.setText("Value:");
		
		chkUseRegularExpression = new Button(grpG, SWT.CHECK);
		chkUseRegularExpression.setBounds(133, 122, 141, 16);
		chkUseRegularExpression.setText("Use Regular Expression");
		
		Button btnAddToFilter = new Button(grpG, SWT.NONE);
		btnAddToFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onBtnAddToFilterClick();
			}
		});
		
		btnAddToFilter.setBounds(144, 168, 130, 31);
		btnAddToFilter.setText("Add to Filter List");
		
		cbValues = new Combo(grpG, SWT.NONE);
		cbValues.setItems(new String[] {"Information", "Debug", "Warning", "Error", "Verbose"});
		cbValues.setBounds(104, 91, 170, 23);
		cbValues.select(0);
		
		txtValue = new Text(grpG, SWT.BORDER);
		txtValue.setBounds(104, 93, 170, 21);
		
		Label lblFilterName = new Label(shlAddANew, SWT.NONE);
		lblFilterName.setBounds(23, 10, 70, 15);
		lblFilterName.setText("Filter Name:");
		
		txtFilterName = new Text(shlAddANew, SWT.BORDER);
		txtFilterName.setBounds(99, 10, 183, 21);
		
		lstFilters = new List(shlAddANew, SWT.BORDER);
		lstFilters.setBounds(300, 10, 299, 244);
		
		chkLinkRulesWith = new Button(shlAddANew, SWT.CHECK);
		chkLinkRulesWith.setBounds(300, 262, 138, 16);
		chkLinkRulesWith.setText("Link Rules with \"AND\"");
		
		Button btnOk = new Button(shlAddANew, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onBtnOkClick();
			}
		});
		btnOk.setBounds(441, 283, 75, 25);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shlAddANew, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				onBtnCancelClick();
			}
		});
		btnCancel.setBounds(522, 283, 75, 25);
		btnCancel.setText("Cancel");

	}
	
	public final String getFilterName() {
		return filterName;
	}
	
	public final boolean getLinkState() {
		return linkWithAndState;
	}
}
