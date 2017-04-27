package com.oneice.tree.pool;

import com.oneice.tree.utils.DESUtils;

/**
 * 默认的解密Properties属性的类
 * @author ice
 */
public class EncryptPropertiesDefault implements EncryptProperties{
	private String[] strs = {"name","password"};
	@Override
	public String convertStr(String key,String value) {
		// TODO Auto-generated method stub
		if(isEncryptStr(key)){
			return DESUtils.getDecryptString(value);
		}
		return value;
	}

	@Override
	public boolean isEncryptStr(String str) {
		// TODO Auto-generated method stub
		for(String s : strs){
			if(s.equals(str))
				return true;
		}
		return false;
	}

}
