package org.jyald.uicomponents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class ListViewColumn {
	TableColumn column;
	Table container;
	int percent;
	String text;
	
	public ListViewColumn(Table containerObj, String colText, int percentOfContainer) {
		container = containerObj;
		text = colText;
		percent = percentOfContainer;
		
		column = new TableColumn(container,SWT.NONE);
		column.setText(text);
		column.setWidth(100);
	}
	
	public ListViewColumn(Table containerObj, String colText) {
		this(containerObj, colText,0);
	}
	
	public ListViewColumn(Table containerObj) {
		this(containerObj,"");
	}
	
	public void onResize() {
		if (percent > 0) {
			column.setWidth((container.getSize().x * percent) / 100);
		}
	}
	
	public void dispose() {
		try {
			column.dispose();
		}
		catch(Exception e) {}
	}
	
	
	
}
