package com.ligq.shoe.utils;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MD5Utils {
	private final static Logger logger = LoggerFactory.getLogger(MD5Utils.class); 

	/**
	 * MD5加码。32位
	 * @param inStr
	 * @return
	*/
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
			return"";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
	
		for (int i = 0; i < charArray.length; i++)
		byteArray[i] = (byte) charArray[i];
	
		byte[] md5Bytes = md5.digest(byteArray);
	
		StringBuffer hexValue = new StringBuffer();
	
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
			hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
	
		return hexValue.toString();
	}
}
