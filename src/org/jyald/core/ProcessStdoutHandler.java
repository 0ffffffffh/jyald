package org.jyald.core;

public abstract class ProcessStdoutHandler {
	public abstract void onOutputLineReceived(String line);
}
