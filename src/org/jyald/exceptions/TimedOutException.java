package org.jyald.exceptions;

public class TimedOutException extends Exception {
	private long ms;
	
	public TimedOutException(long timeout) {
		ms = timeout;
	}
	
	@Override
	public String getMessage() {
		return String.format("Wait has timedout. Timeout %d ms",ms);
	}
	
}