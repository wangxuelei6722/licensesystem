package com.wangxl.licensesystem.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本的util方法
 * @author F-Ziz
 *
 */
public class BaseUtils {

	private static HashSet<String> noInterceptUrl = null;
	private Connection conn = null;	
	private static BaseUtils baseUtils = null;
	

	public static BaseUtils getInstance() {
		if (baseUtils == null)
			baseUtils = new BaseUtils();
		return baseUtils;
	}

	/**
	 * 获取系统当前时间
	 * @return
	 */
	public String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(new Date());
		return dateStr;
	}
	
	/**
	 * 字符串截取
	 * @param spiltStr 截取的字符串
	 * @param splitValue 截取的值
	 * @return List<String>
	 */
	public List<String> strSplit(String spiltStr, String splitValue) {
		String[] strArray = spiltStr.split(splitValue);
		List<String> strList = new ArrayList<String>();
		for (String str : strArray) {
			strList.add(str);
		}
		return strList;
	}
	
	/**
	 * 字符串截取--按起始位置
	 * @param str
	 * @param endIndex
	 * @return
	 */
	public String subString(String str, int endIndex) {
		if (str.length() >= endIndex) {
			return str.substring(0, endIndex) + "......";
		}
		return str;
	}
	
	/**
	 * 过滤掉首字母为标点符号的情况
	 * @param str
	 * @return
	 */
	public String matchSymbol(String str) {
		String s1 = str.substring(0, 1);
		String s2 = str.substring(1);
		String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regex);     
        Matcher m = p.matcher(s1);     
        return m.replaceAll("").trim()+s2;
	}
	
	/**
	 * 验证是否为空
	 * @return
	 */
	public String validateKeyword(String str) {
		if (str==null||StringUtils.isBlank(str))
			return "*";
		return str;
	}
	/**
	 * 验证经度
	 * @return
	 */
	public String validateLat(String str) {
		if (str==null||StringUtils.isBlank(str))
			return "39.897445";
		return str;
	}
	/**
	 * 验证纬度
	 * @return
	 */
	public String validateLng(String str) {
		if (str==null||StringUtils.isBlank(str))
			return "116.331398";
		return str;
	}

    public Properties getProperties(){
		Properties p = new Properties();
		InputStream in = BaseUtils.class.getClassLoader().getResourceAsStream("server_url.properties");  
		try {
			p.load(in);
			in.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
    
    public Properties getBaseConfig(){
		Properties p = new Properties();
		InputStream in = BaseUtils.class.getClassLoader().getResourceAsStream("base_config.properties");
		Reader reader = null;
		try {
			reader = new InputStreamReader(in, "utf-8");
			p.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (null != reader){
				try {
				 reader.close();
				} catch (IOException e) {
				e.printStackTrace();
		}
		}
		}
		return p;
	}
	public String getImgeUrl(String[] imgUrlArr) {
		StringBuilder stringBuilder=new StringBuilder();
		if(imgUrlArr!=null){
			for (String imgUrl : imgUrlArr) {
				stringBuilder.append(",");
				stringBuilder.append(imgUrl);
			}
			return stringBuilder.toString().substring(1);
		}
		return null;
	}
	/**
	 * 读入properties文件
	 * */
	public static String getProperties(String pathName,String propertyKey) throws IOException {
		InputStream insss =BaseUtils.class.getClassLoader().getResourceAsStream(pathName);
		Properties pss = new Properties();
		pss.load(insss);
		return pss.getProperty(propertyKey);
	}
	/**
	 * 清理IE缓存
	 * @param response
	 */
	public static void ClearCache(HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	}
	//获取授权码
	public static String getLicenseCode(){
		//授权码
		String license_code=BaseUtils.getInstance().getProperties().getProperty("license_code");
		return license_code;
	}

}
