package com.wangxl.licensesystem.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TimerTask;

public class DownloadImage extends TimerTask implements Runnable {

	private String sourceFilePath;
	private String destFilePath;
	
	public DownloadImage(String sourceFilePath, String destFilePath) {
		this.sourceFilePath = changeChineseToURIEncode(sourceFilePath);
		this.destFilePath = destFilePath;
	}
	
	public void run() {
		// 下载网络文件
		int fileLength = -1;
		int retryTime = 2;
		int tryTime = 0;
		while (tryTime < retryTime) {
	        try {
	            int bytesum = 0;
	            int byteread = 0;
		        URL url = new URL(sourceFilePath);
		
		        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		        conn.setConnectTimeout(60000); //连接主机超时为60s
		        conn.setReadTimeout(1200000); //下载超时为1200s
		        conn.connect();
		        fileLength = conn.getContentLength(); //获取需要下载的图片的大小
		        InputStream inStream = conn.getInputStream();
		        FileOutputStream fs = new FileOutputStream(destFilePath);
		
		        byte[] buffer = new byte[1024];
		        while ((byteread = inStream.read(buffer)) != -1) {
		            bytesum += byteread;
		            fs.write(buffer, 0, byteread);
			        fs.flush();
		        }
		        fs.flush();
		        fs.close();
		        inStream.close();
		        conn.disconnect();
		        break;
	        } catch (Exception e) {
				// TODO: handle exception
	        	System.err.println("Trytime: "+tryTime+"; download error! "+e);	        	
	        	tryTime++;
			}
		}
		File file = new File(destFilePath);
		if (file.exists() && file.length() < fileLength) { //下载后的图片不完整
			deleteFile(destFilePath);
		}
	}
	
	private boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }
	    return flag;  
	}
	
	// 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
 
    // 完整的判断中文汉字和符号
    private String changeChineseToURIEncode(String strName) {
    	String result = "";
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            String c = String.valueOf(ch[i]);
            if (isChinese(ch[i])) {
            	try {
					c = URLEncoder.encode(c, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
            }
            result += c;
        }
        return result;
    }
}
