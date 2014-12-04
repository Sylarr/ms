package com.sinosoft.ms.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类。
 * 
 * @author HanYan
 * @date 2014-11-04
 */
public class MD5Util {

	/* 16进制字符 */
	private final static char[] hexDigits = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
 	* 转换为MD5密文的主方法。
 	* @param source
 	* @return
 	*/
	public static String md5(String source) {
		if (source == null) {
			return null;
		}
		String cipherText = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			cipherText = byte2hex(md.digest(source.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			cipherText = source;
		}
		return cipherText;
	}
	
	/**
	 * 转换为16进制。
	 * @param bytes
	 * @return
	 */
	private static String byte2hex(byte[] bytes) {
		
		int size = bytes.length;
		char[] result = new char[size << 1];
		int i,index = 0;
		for (i = 0; i < bytes.length; i++) {
			result[index++] = hexDigits[bytes[i] >>> 4 & 0xf];
			result[index++] = hexDigits[bytes[i] & 0xf];
		}
		return new String(result);
	}

	/**
	 * 测试方法。
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MD5Util.md5("0000"));
	}
}
