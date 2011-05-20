package org.jyald.debuglog;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.jyald.util.StringHelper;

public class DebugPrintStream extends OutputStream {
	
	private final Charset utf8 = Charset.forName("UTF-8");
	private final StringBuilder sb = new StringBuilder();
	
	@Override
	public void write(int b) throws IOException {
		byte val = (byte)b;
		
		if (val == '\r')
			return;
		
		if (val == '\n' || val == 0) {
			Log.write(sb.toString());
			sb.delete(0, sb.length());
			return;
		}
		
		sb.append(String.format("%c",b));
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		String buf = new String(b,utf8);
		
		if (buf.contains(StringHelper.NEW_LINE))
			return;
		
		Log.write(buf);
	}

	@Override
	public void write(byte[] b, int s, int len) {
		String lineBuf = new String(b,s,len,utf8);
		
		if (lineBuf.contains(StringHelper.NEW_LINE))
			return;
		
		Log.write(lineBuf);
	}
	
}
