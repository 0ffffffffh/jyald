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
		column.setWidth(15);
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
