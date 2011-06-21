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

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;


public class ListViewItem {
	private TableItem item;
	
	public static final int RED = 0xff0000;
	public static final int BLUE = 0x0000ff;
	public static final int GREEN = 0x008000;
	public static final int ORANGE = 0xffa500;
	public static final int BLACK = 0x000000;
	
	
	public ListViewItem(Table parent){
		item = new TableItem(parent,SWT.NONE);
		Font f = new Font(Display.getDefault(),"Times New Roman",14, SWT.NORMAL);
		item.setFont(f);
	}
	
	public void setItemTextColor(int rgb) {
		int r,g,b;
		r = (rgb >> 0x10) & 0xff;
		g = (rgb >> 0x8) & 0xff;
		b = rgb & 0xff;
		
		RGB colorVal = new RGB(r,g,b);
		Color color = new Color(Display.getDefault(),colorVal);
		
		item.setForeground(color);
	}
	
	public void setItemText(String ...items) {
		item.setText(items);
	}
	
	
	
}
