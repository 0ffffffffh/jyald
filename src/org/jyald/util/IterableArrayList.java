package org.jyald.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class IterableArrayList<E> implements Iterable<E>, Serializable {
	ArrayList<E> items;
	
	public IterableArrayList() {
		
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
