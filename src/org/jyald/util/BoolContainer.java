package org.jyald.util;

public class BoolContainer {
	private boolean state;
	
	public BoolContainer(boolean initial) {
		state = initial;
	}
	
	public final boolean getBool() {
		return state;
	}
	
	public void setBool(boolean val) {
		state = val;
	}
	
}