package com.wangxl.licensesystem.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author: wangxl
 * @description: 对称可逆ASC加密算法
 * @date :2018年5月9日下午6:13:09
 * @email:15110096722@163.com
 * @return: @
 */
//@Component("ascEncrypt")
public class ASCEncrypt
{

	private String key;

	public ASCEncrypt(String key)
	{

		this.key = key;
	}

	/* 加密算法 */
	public String encrypt(String souStr)
	{
		String base64Encode = "";
		try
		{
			base64Encode = base64Encode(aesEncryptToBytes(souStr, key));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HexUtil.bytes2Hex(base64Encode.getBytes());

	}

	/* 解密方法 */
	public String decrypt(String encodeStr)
	{

		byte[] bytes = HexUtil.hex2Bytes(encodeStr);
		String aesDecrypt = null;
		try
		{
			String string2 = new String(bytes, "utf-8"); // 转回来要注意编码的问题
			aesDecrypt = aesDecrypt(string2, key);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aesDecrypt;
	}

	/* 验证密码 */
	public boolean checkPwd(String souStr, String encodeStr)
	{
		String afterASCEncrypt = new ASCEncrypt(key).encrypt(souStr);
		if (afterASCEncrypt.equals(encodeStr))
		{
			return true;
		} 
		
		return false;

	}

	/**
	 * 将base 64 code AES解密
	 * 
	 * @param encryptStr
	 *            待解密的base 64 code
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception
	{
		return encryptStr.isEmpty() ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}

	/**
	 * AES解密
	 * 
	 * @param encryptBytes
	 *            待解密的byte[]
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception
	{

		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(decryptKey.getBytes());
		kgen.init(128, random);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		return new String(decryptBytes,"utf-8");
	}

	/**
	 * base 64 decode
	 * 
	 * @param base64Code
	 *            待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception
	{
		return base64Code.isEmpty() ? null : new BASE64Decoder().decodeBuffer(base64Code);
	}

	/**
	 * base 64 encode
	 * 
	 * @param bytes
	 *            待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes)
	{
		return new BASE64Encoder().encode(bytes);
	}

	/**
	 * AES加密
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception
	{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
		kgen.init(128,random);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

		return cipher.doFinal(content.getBytes("utf-8"));
	}

}
