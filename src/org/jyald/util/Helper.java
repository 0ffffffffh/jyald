package org.jyald.util;

import java.io.File;

import org.jyald.debuglog.Log;


public class Helper {
	
	private static String workingDir = null;
	
	public static String getWorkingDir() {
		
		if (workingDir == null) {
			String path = Helper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.substring(1, path.length()-1);
			path = path.replace("%20", " ");
		
			if (path.contains(".")) {
				int n = path.lastIndexOf("/");
				
				if (n != -1) {
					path = path.substring(0,n);
				}
			}
		
			workingDir = path;
			
			Log.write("Executing path detected: %s", workingDir);
		}
		
		return workingDir;
	}
	
	public static boolean fileExist(String fileName) {
		File fileObj = new File(fileName);
		return fileObj.exists();
	}
}
