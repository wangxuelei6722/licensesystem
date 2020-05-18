package com.wangxl.licensesystem.utils;

import java.io.File;

public class FileOperUtils {
	
	/**
	 * 创建临时文件夹
	 * @return
	 */
	public static File getNewTempFolder(){
		String tempPath = System.getProperty("java.io.tmpdir");
		File newFile = null;
		while(true){
			synchronized (FileOperUtils.class) {
				newFile = new File(tempPath, "f" + System.currentTimeMillis());
				if(!newFile.exists()){
					newFile.mkdirs();
					break;
				}
			}
		}
		return newFile;
	}
	
	public static void createFolder(String path){
		File file = new File(path);
		if(file.isDirectory() && (!file.exists())){
			file.mkdirs();
		}
	}
}
