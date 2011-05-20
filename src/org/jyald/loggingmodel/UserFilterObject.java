package org.jyald.loggingmodel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jyald.util.IterableArrayList;


public class UserFilterObject implements Serializable {
	private FilterList filters;
	private String filterName;
	private boolean rulesLinkState;
	
	public static boolean saveFilters(IterableArrayList<UserFilterObject> userFilters, String fileName) {
		
		try {
			FileOutputStream fso = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fso);
			oos.writeObject(userFilters);
			oos.close();
			fso.close();
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static IterableArrayList<UserFilterObject> loadFilters(String fileName) {
		IterableArrayList<UserFilterObject> filterObj = null;
		
		try {
			FileInputStream fsi = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fsi);
			filterObj = (IterableArrayList<UserFilterObject>)ois.readObject();
			ois.close();
			fsi.close();
		}
		catch (Exception e) {
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
