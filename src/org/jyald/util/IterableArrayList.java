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


package org.jyald.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class IterableArrayList<E> implements Iterable<E>, Serializable {
	ArrayList<E> items;
	
	public IterableArrayList() {
		items = new ArrayList<E>();
	}
	
	public void iterate(ArrayListIterateHandler<E> iterator) {
		for (E item : items) {
			if (iterator.iterate(item))
				break;
		}
	}
	
	public boolean add(E item) {
		return items.add(item);
	}
	
	public boolean remove(E item) {
		return items.remove(item);
	}
	
	public E removeAt(int index) {
		return items.remove(index);
	}
	
	public void clear() {
		items.clear();
	}
	
	public E get(int index) {
		return null;
	}
	
	public final int getCount() {
		return items.size();
	}


	@Override
	public Iterator<E> iterator() {
		return items.iterator();
	}
	
	
}
