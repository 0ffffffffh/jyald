package org.jyald;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class NewFilterDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Button btnAll;
	protected List lstUserFilters;
	
	
	public NewFilterDialog(Shell parent, int style) {
		super(parent, style);
		setText("Filter Manager");
	}
	
	public Object open() {
		createContents();
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
		lstUserFilters.remove(lstUserFilters.getSelectionIndices());
	}
	
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(376, 312);
		shell.setText(getText());
		
		lstUserFilters = new List(shell, SWT.BORDER | SWT.MULTI);
		lstUserFilters.setBounds(20, 33, 328, 209);
		
		/*
		lstUserFilters.add("TEST ITEM #1");
		lstUserFilters.add("TEST ITEM #2");
		lstUserFilters.add("TEST ITEM #3");
		*/
		
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

	}
}
