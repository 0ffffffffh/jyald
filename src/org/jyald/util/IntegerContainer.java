package org.jyald.util;


public class IntegerContainer {
	private int value;
	
	public IntegerContainer(int init) {
		value = init;
	}
	
	public final int getInt() {
		return value;
	}
	
	public void setInt(int val) {
		value = val;
	}
	
	public void increment() {
		value++;
	}
	
	public void decrement() {
		value--;
	}
}