package com.oneice.tree.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class DESUtils {
	private static Key key;
	private static String KEY_STR = "mykey";
	
	static{
		try{
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(KEY_STR.getBytes());
			generator.init(secureRandom);
			key = generator.generateKey();
			generator = null;
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 加密字符串得到basee64编码的
	 * Author: ice
	 * @param str
	 * @return
	 */
	public static String getEncryptString(String str){
		System.out.println(key);
		try{
			byte[] strBytes = str.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return Base64.getEncoder().encodeToString(encryptStrBytes);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static String getDecryptString(String str){
		try{
			byte[] strBytes = Base64.getDecoder().decode(str);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return new String(encryptStrBytes,"UTF-8");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		String name = "root";
		String password = "root";
		String ecryname = getEncryptString(name);
		String ecrypassword = getEncryptString(password);
		System.out.println("����ǰ:"+name+"\n"+"���ܺ�:"+ecryname);
		System.out.println("����ǰ:"+password+"\n"+"���ܺ�:"+ecrypassword);
		System.out.println("����:"+getDecryptString(ecryname)+"-"+getDecryptString(ecrypassword));
	}
}
