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
			Log.writeByLevel(LogLevel.EXCEPTION,sb.toString());
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
		
		Log.writeByLevel(LogLevel.EXCEPTION,buf);
	}

	@Override
	public void write(byte[] b, int s, int len) {
		String lineBuf = new String(b,s,len,utf8);
		
		if (lineBuf.contains(StringHelper.NEW_LINE))
			return;
		
		Log.writeByLevel(LogLevel.EXCEPTION,lineBuf);
	}
	
}
