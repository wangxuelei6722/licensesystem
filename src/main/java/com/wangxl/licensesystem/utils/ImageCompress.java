package com.wangxl.licensesystem.utils;

import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.*;
/**
 * 图片压缩处理，支持jpg,png,bmp等
 * @author LJY
 * 调用example:
 * ImageCompress imgCom = new ImageCompress();
 * imgCom.resize("C:\\Users\\DELL\\Desktop\\1.bmp", "C:\\Users\\DELL\\Desktop\\2.png", 290, 320);//将图片压缩成290*320格式，如果之前比该格式小的图片，也会变成290*320
 */
public class ImageCompress {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ImageCompress.class);

	private Image img;
	private int width;
	private int height;
	
	/**
	 * 按照宽度还是高度进行压缩
	 * @param w int 最大宽度
	 * @param h int 最大高度
	 */
	public void resizeFix(String sourceFilePath, String destFilePath, int w, int h) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(sourceFilePath, destFilePath, w);
		} else {
			resizeByHeight(sourceFilePath, destFilePath, h);
		}
	}
	/**
	 * 以宽度为基准，等比例放缩图片
	 * @param w int 新宽度
	 */
	public void resizeByWidth(String sourceFilePath, String destFilePath, int w) throws IOException {
		int h = (int) (height * w / width);
		resize(sourceFilePath, destFilePath, w, h);
	}
	/**
	 * 以高度为基准，等比例缩放图片
	 * @param h int 新高度
	 */
	public void resizeByHeight(String sourceFilePath, String destFilePath, int h) throws IOException {
		int w = (int) (width * h / height);
		resize(sourceFilePath, destFilePath, w, h);
	}
	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param w int 新宽度
	 * @param h int 新高度
	 */
	public void resize(String sourceFilePath, String destFilePath, int w, int h) throws IOException {
		File file = new File(sourceFilePath);// 读入文件
		img = ImageIO.read(file);      // 构造Image对象
		width = img.getWidth(null
		);    // 得到源图宽
		height = img.getHeight(null);  // 得到源图长
		logger.info("drawImage width:"+width+"height:"+height);
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB ); 
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		logger.info("out print destFile:"+destFilePath);
		File destFile = new File(destFilePath);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		logger.info("begin execute createJPEGEncoder method。out:"+out);
		// 可以正常实现bmp、png、gif转jpg
		//此写法centos有的不兼容，会报异常
		/*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		logger.info("createJPEGEncoder 方法执行成功！encoder："+encoder);
		encoder.encode(image); // JPEG编码*/
		ImageIO.write(image, "jpeg", out);
		out.close();
	}
}