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


package org.jyald;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jyald.debuglog.Log;

public class Setting implements Serializable {
	public String adbExecutableFile;
	public int debugLevel;
	
	public static Setting loadSetting() {
		Setting setting;
		
		try {
			FileInputStream fsi = new FileInputStream("settings.stg");
			ObjectInputStream ois = new ObjectInputStream(fsi);
			setting = (Setting)ois.readObject();
			ois.close();
			fsi.close();
		}
		catch (Exception e) {
			e.printStackTrace(Log.getPrintStreamInstance());
			setting = new Setting();
		}
		
		return setting;
	}
	
	public static boolean saveSetting(Setting setting) {
		try {
			FileOutputStream fso = new FileOutputStream("settings.stg");
			ObjectOutputStream oos = new ObjectOutputStream(fso);
			oos.writeObject(setting);
			oos.close();
			fso.close();
		}
		catch (Exception e) {
			e.printStackTrace(Log.getPrintStreamInstance());
			return false;
		}
		
		return true;
	}
	
	
}
