package com.ligq.shoe.utils;

import java.io.File;


public class FileUtils {

	
	public static File createTempDir() {
		
		  File baseDir = new File(System.getProperty("java.io.tmpdir"));
		  
		  return baseDir;
	}
}
