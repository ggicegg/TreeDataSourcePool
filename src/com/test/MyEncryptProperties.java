package com.test;

import com.oneice.tree.pool.EncryptProperties;
import com.oneice.tree.utils.DESUtils;

public class MyEncryptProperties implements EncryptProperties {
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
		for(String s:strs){
			if(s.equals(str))
				return true;
		}
		return false;
	}

}
