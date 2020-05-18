package com.wangxl.licensesystem.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;


public class FastDFSImage {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FastDFSImage.class);
	private String fastdfs_ip = CommonUtils.getInstance().getProperties().getProperty("fastdfs_ip");
	private String fastdfs_group_name = CommonUtils.getInstance().getProperties().getProperty("fastdfs_group_name");
	
	public String uploadImage(String imageFilePath) throws Exception {
	    return uploadImage(imageFilePath,290,320);
	}
	public String uploadImage(String imageFilePath, boolean mustCompress) throws Exception { //不压缩图片可以使用这个方法
		if (mustCompress == false) {
			/*try {*/
				BufferedImage bufferedImage = ImageIO.read(new File(imageFilePath));
				return uploadImage(imageFilePath, bufferedImage.getWidth(), bufferedImage.getHeight()); //分辨率不变				
			/*} catch (Exception e) {
				// TODO: handle exception
				System.err.println(imageFilePath+" is not an image!");
			}*/
		}
		//默认压缩为290*320
		return uploadImage(imageFilePath,290,320);
	}
	public String uploadImage(String imageFilePath,int width,int height) throws Exception {
		ArrayList imageFilePathList = new ArrayList<String>();
		imageFilePathList.add(imageFilePath);
		HashMap urlMapping = uploadImageBatch(imageFilePathList,width,height);
		return urlMapping.get(imageFilePath).toString();
	}
	
	public HashMap<String, String> uploadImageBatch(ArrayList imageFilePathList,int width,int height) throws Exception {
		logger.info("begin execute uploadImageBatch method");
		StorageClient storageClient = getStorageClient();
	    NameValuePair[] meta_list = new NameValuePair[3];
	    meta_list[0] = new NameValuePair("width", "120");
	    meta_list[1] = new NameValuePair("heigth", "120");
	    meta_list[2] = new NameValuePair("author", "gary");
	    
	    HashMap urlResultMapping = new HashMap<String, String>();
	    HashMap fileUploadTimes = new HashMap<String, Integer>();
	    
	    //把要上传的图片进行压缩，然后上传压缩后的图片
	    ImageCompress imageCompress = new ImageCompress();
	    String timeStamp = String.valueOf(System.currentTimeMillis());
		String fileBasePath = "image_compress_" + timeStamp + "_";
		logger.info("image_compress_" + timeStamp + "_"+"imageFilePathList:"+imageFilePathList);
	    for (int i=0; i<imageFilePathList.size(); i++) {
		    	File file = new File(imageFilePathList.get(i).toString());// 读入文件
		    	if (file.length() > 50000 && (imageFilePathList.get(i).toString().endsWith(".jpg") ||
                        imageFilePathList.get(i).toString().endsWith(".JPG") ||
                        imageFilePathList.get(i).toString().endsWith(".png") ||
                        imageFilePathList.get(i).toString().endsWith(".PNG"))) { //大于5kB进行压缩, 压缩为290*320格式	  
		    		String filePath = fileBasePath+i+".png";
				    logger.info("Parameter printing："+imageFilePathList.get(i).toString()+"filePath:"+filePath+"width:"+width+"height:"+height);
		    		imageCompress.resize(imageFilePathList.get(i).toString(), filePath, width, height);
		    		file.delete();
		    		File file2 = new File(filePath);
		    		file2.renameTo(file);
		    	}
	    }
	    
	    for (int i=0; i<imageFilePathList.size(); i++) {
			logger.info("imageFilePathList:"+imageFilePathList.get(i).toString());
	    	String imageFilePath = imageFilePathList.get(i).toString();
			logger.info((i+1)+"-th::");
			//System.out.println((i+1)+"-th::");
		    File file = new File(imageFilePath);
		    FileInputStream fis = new FileInputStream(file);
		    byte[] file_buff = null;
		    if(fis != null){
		    	int len = fis.available();
		    	file_buff = new byte[len];
		    	fis.read(file_buff);
		    }
		    
		    String group_name = null;
		    
		    long startTime = System.currentTimeMillis();
		    String[] results = storageClient.upload_file(file_buff, file.getName().substring(file.getName().lastIndexOf(".") + 1), meta_list);
		    System.out.println("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
	
		    if (results == null) {
		        System.err.println("upload file fail, error code: " + storageClient.getErrorCode());
		        
		        if (!fileUploadTimes.containsKey(imageFilePath)) {
		        	fileUploadTimes.put(imageFilePath, 0);
		        }
	        	int timesNow = Integer.parseInt(fileUploadTimes.get(imageFilePath).toString()) + 1;
	        	if (timesNow >= 10) {
	        		urlResultMapping.put(imageFilePath, "upload error");
	        	}
	        	else {
	        		fileUploadTimes.put(imageFilePath, timesNow);
	        		imageFilePathList.add(imageFilePath);
	        	}
		        continue;
		    }
		    
		    group_name = results[0];
		    String remote_filename = results[1];
		    
		    if (fis != null)
		    	fis.close();
		    
		    urlResultMapping.put(imageFilePath, fastdfs_ip + "/" + fastdfs_group_name + "/" + remote_filename);

		}
	    
	    return urlResultMapping;
	}

	private StorageClient getStorageClient() throws Exception {
		ClientGlobal.init(getClientConfFilePath());
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		return new StorageClient(trackerServer, storageServer);
	}

	public HashMap<String, String> uploadImageBatch(ArrayList imageFilePathList) throws Exception {
		//大于5kB进行压缩, 压缩为290*320格式
		return uploadImageBatch(imageFilePathList,290,320);
	}

	public  byte[] downloadFile(HttpServletResponse response, String fileId) throws Exception {
		StorageClient storageClient = getStorageClient();
        fileId=fileId.split(fastdfs_group_name)[1];
        byte[] bytes = storageClient.download_file(fastdfs_group_name, fileId.substring(1));
        return bytes;
    }
	
	public boolean deleteImage(String imageUrl) throws Exception {
		try{
			StorageClient storageClient = getStorageClient();
	    
	    String group_name = fastdfs_group_name;
	    if (imageUrl.split(fastdfs_group_name+"/").length < 2) { //没有该图片，可以认为删除成功
	    	return true;
	    }
	    String remote_filename = imageUrl.split(fastdfs_group_name+"/")[1];//"M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg";
	    int get = storageClient.delete_file(group_name, remote_filename);
	    if (get == 0) {
	    	return true;
	    }
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("删除失败！");
		}
	    return false;
	}
	
	public void getImageInfo(String imageUrl) throws Exception {
		StorageClient storageClient = getStorageClient();
	    
	    String group_name = fastdfs_group_name;
	    String remote_filename = imageUrl.split(fastdfs_group_name+"/")[1];//"M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg";
	    FileInfo fi = storageClient.get_file_info(group_name, remote_filename);
	    String sourceIpAddr = fi.getSourceIpAddr();
	    long size = fi.getFileSize();
	    System.out.println("ip:" + sourceIpAddr + ",size:" + size);
	}
	
	private String getClientConfFilePath() {
		String folder = this.getClass().getClassLoader().getResource("").getPath() + "fdfs_client.properties";
		logger.info("getClientConfFilePath-folder的值为:"+folder);
		System.err.println(folder);
		try {
			folder = URLDecoder.decode(folder, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("getClientConfFilePath-folder的值为:"+folder+"方法结束");
		return folder;
	}
	
	//根据外网image url先下载到本地文件，再上传到本地服务器
	public ArrayList<String> saveImageFromUrlList(ArrayList<String> urlList) {
		String errorUrl = "";
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String fileBasePath = "image_url_" + timeStamp + "_";
		ArrayList<String> localPathList = new ArrayList<String>();
		for (int i=0; i<urlList.size(); i++) {
			String[] type = urlList.get(i).toString().split("\\.");
			String localPath = "";
			if (type[type.length-1].equals("jpg") || type[type.length-1].equals("JPG") || type[type.length-1].equals("png") || type[type.length-1].equals("PNG")) {
				localPath = fileBasePath+i+"."+type[type.length-1];
			}
			else {
				localPath = fileBasePath+i+"."+"png";
			}
			localPathList.add(localPath);
		}
		ArrayList<String> urlInLocalServer = new ArrayList<String>();
		ExecutorService pool = Executors.newFixedThreadPool(500);
		for (int i=0; i<urlList.size(); i++) {
			DownloadImage downloadImage = new DownloadImage(urlList.get(i).toString(), localPathList.get(i).toString());
			Thread t = new Thread(downloadImage);
			pool.execute(t);
		}
		pool.shutdown();
		 try {
			pool.awaitTermination(10000, TimeUnit.MILLISECONDS.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> imageFilePathList = new ArrayList<String>();
		for (int i = 0; i < urlList.size(); i++) {
			if (checkFileExist(localPathList.get(i).toString())) {
				imageFilePathList.add(localPathList.get(i).toString());
			}
		}

		try {
			System.out.println("upload file number:: "+imageFilePathList.size());
			HashMap pathUrlMap = uploadImageBatch(imageFilePathList);
			// get url in local server
			for (int i = 0; i < urlList.size(); i++) {
				if (pathUrlMap.containsKey(localPathList.get(i).toString())) {
					urlInLocalServer.add(pathUrlMap.get(localPathList.get(i).toString()).toString());
				} else {
					urlInLocalServer.add(errorUrl);
				}
			}
		} catch (Exception e) {
			System.err.println("upload error!" + e);
		}
		// delete local file
		for (int i = 0; i < localPathList.size(); i++) {
			deleteFile(localPathList.get(i).toString());				
		}
		return urlInLocalServer;		
	}
	
	private boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }
	    return flag;  
	}
	
	private boolean checkFileExist(String sPath) {
		File file = new File(sPath);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}
