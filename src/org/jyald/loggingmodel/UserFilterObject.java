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


package org.jyald.loggingmodel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jyald.debuglog.Log;
import org.jyald.util.Helper;
import org.jyald.util.IterableArrayList;


public class UserFilterObject implements Serializable {
	private FilterList filters;
	private String filterName;
	private boolean rulesLinkState;
	
	public static boolean saveFilters(IterableArrayList<UserFilterObject> userFilters, String fileName) {
		
		fileName = Helper.getWorkingDir() + "/" + fileName;
		
		try {
			FileOutputStream fso = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fso);
			oos.writeObject(userFilters);
			oos.close();
			fso.close();
		}
		catch (Exception e) {
			e.printStackTrace(Log.getPrintStreamInstance());
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static IterableArrayList<UserFilterObject> loadFilters(String fileName) {
		IterableArrayList<UserFilterObject> filterObj = null;
		
		fileName = Helper.getWorkingDir() + "/" + fileName;
		
		try {
			FileInputStream fsi = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fsi);
			filterObj = (IterableArrayList<UserFilterObject>)ois.readObject();
			ois.close();
			fsi.close();
		}
		catch (Exception e) {
			e.printStackTrace(Log.getPrintStreamInstance());
			return null;
		}
		
		return filterObj;
	}
	
	public UserFilterObject(FilterList filterList,String name, boolean linkAndState) {
		filters = filterList;
		filterName = name;
		rulesLinkState = linkAndState;
		filters.setLinkAndState(linkAndState);
	}
	
	public final boolean getLinkState() {
		return rulesLinkState;
	}
	
	public final FilterList getFilterList() {
		return filters;
	}
	
	public final String getFilterName() {
		return filterName;
	}
	
	@Override
	public String toString() {
		return filterName;
	}
	
	
}
