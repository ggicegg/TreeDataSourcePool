package com.oneice.tree.pool;

public interface EncryptProperties {
	/**
	 * 对字符串进行解密转化
	 * @param str
	 * @return
	 * @author ice
	 */
	public String convertStr(String key,String value);
	
	/**
	 * 是否是加密过的字符串
	 * @param str
	 * @return
	 * @author ice
	 */
	public boolean isEncryptStr(String str);
}
