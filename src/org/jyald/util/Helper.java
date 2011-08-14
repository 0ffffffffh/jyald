package org.jyald.util;

import java.io.File;

import org.jyald.debuglog.Log;


public class Helper {
	public static String getWorkingDir() {
		String path = Helper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.length()-1);
		path = path.replace("%20", " ");
		
		if (path.contains(".")) {
			int n = path.lastIndexOf("/");
			path = path.substring(0,n);
		}
		
		Log.write("Executing path detected: %s", path);
		
		return path;
	}
	
	public static boolean fileExist(String fileName) {
		File fileObj = new File(fileName);
		return fileObj.exists();
	}
}
