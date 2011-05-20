package org.jyald;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jyald.debuglog.Log;

public class Setting implements Serializable {
	public String adbExecutableFile;
	
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
