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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/*
 * pfff. swt widgets are very primitive.
 */

public class ListEx {
	private ArrayList<Object> itemObjects;
	private ArrayList<Object> selectedItems;
	private List listObj; //it doesn't support subclassing!
	
	public ListEx(Composite parent, int style) {
		listObj = new List(parent, style);
		itemObjects = new ArrayList<Object>();
		selectedItems = new ArrayList<Object>();
	}
	
	public void add(Object item) {
		itemObjects.add(item);
		listObj.add(item.toString());
	}
	
	public void add(Object item, int index) {
		itemObjects.add(index, item);
		listObj.add(item.toString(), index);
	}
	
	public final int getSelectedItemCount() {
		return listObj.getSelectionCount();
	}
	
	public final ArrayList<Object> getSelectedItems() {
		int [] selectedItemIndices = listObj.getSelectionIndices();
		
		if (selectedItemIndices == null || selectedItemIndices.length < 1) {
			return null;
		}
	
		selectedItems.clear();
		
		for (int i=0;i<selectedItemIndices.length; i++) {
			selectedItems.add(itemObjects.get(selectedItemIndices[i]));
		}
		
		return selectedItems;
	}
	
	public void remove(int index) {
		Object toRemove=null;
		
		if (index < 0 || index >= itemObjects.size()) {
			return;
		}
		
		toRemove = itemObjects.get(index);
		listObj.remove(index);
		itemObjects.remove(toRemove);
	}
	
	
	public void remove(Object item) {
		int index=0;
		Object toRemove = null;
		
		for (Object curr : itemObjects) {
			if (curr == item) {
				toRemove = curr;
				break;
			}
			index++;
		}
		
		if (toRemove != null) {
			itemObjects.remove(toRemove);
			listObj.remove(index);
		}
	}
	
	public void removeSelected() {
		int [] selectedItemIndices = listObj.getSelectionIndices();
		
		if (selectedItemIndices == null || selectedItemIndices.length < 1) {
			return;
		}
	
		selectedItems.clear();
		
		for (int i=0;i<selectedItemIndices.length; i++) {
			remove(selectedItemIndices[i]);
		}
	}
	
	public void selectAll() {
		listObj.selectAll();
	}
	
	public void deselectAll() {
		listObj.deselectAll();
	}
	
	public void setBounds(int x, int y, int w, int h) {
		listObj.setBounds(x, y, w, h);
	}
}
