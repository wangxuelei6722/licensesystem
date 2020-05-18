package com.wangxl.licensesystem.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


/**
 * @author: Wangxl
 * @description: 不可逆加密类
 * @date :2019年3月20日 下午4:01:48
 * @modifier:
 * @modificationTime:
 * @description:
 */
@Component("encryptUtil")
public class EncryptUtil
{
	/*加密*/
	public String encodeStr(String souStr)
	{
		String split = "$";
		StringUtils regexUtil = new StringUtils();
		String encType =regexUtil.getRandomString(32);
		String randStr = regexUtil.getRandomString(32);
		String jiamaHead = encType + split + randStr + split ; 
		String jiamiStr = DigestUtils.sha256Hex(souStr + randStr); 
		return jiamaHead + jiamiStr;
	}
	/*验证密码*/
	public boolean checkStr(String souStr,String encodeStr)
	{
		String split = "$";
		String[] encodeStrs = encodeStr.split("\\"+split);
		if(encodeStrs.length == 3)
		{
			String encType = encodeStrs[0]; 
			String jiamaHead = encType + split + encodeStrs[1] + split ;
			String jiamiStr = DigestUtils.sha256Hex(souStr + encodeStrs[1]); 
			String finalStr = jiamaHead + jiamiStr ; 
			return finalStr.equalsIgnoreCase(encodeStr);
		}
		return false ; 
	}
	public static void main(String[] args)
	{
		EncryptUtil encryptUtil = new EncryptUtil() ; 
		String res = encryptUtil.encodeStr("123456");
		boolean aaa = encryptUtil.checkStr("123456",res);
		System.err.println(aaa);
		System.out.println(res);
	}
}
