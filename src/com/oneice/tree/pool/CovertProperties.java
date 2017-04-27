package com.oneice.tree.pool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oneice.tree.support.logging.LogFactory;
import com.oneice.tree.utils.GetPropertiesValue;

/**
 * 将文件中读取的配置属性进行转化的类
 * @author ice
 */
public class CovertProperties {
	Logger logger = LogFactory.getLog(CovertProperties.class);
	
	//供加密属性解密的接口
	private EncryptProperties encryProperties;
	
	
	public EncryptProperties getEncryProperties() {
		return encryProperties;
	}
	
	public void setEncryProperties(EncryptProperties encryProperties) {
		this.encryProperties = encryProperties;
	}
	
	public CovertProperties(){
		
	}
	
	/**
	 * 带自定义解密器的属性转化类
	 * @param encryProperties
	 */
	public CovertProperties(EncryptProperties encryProperties){
		this.encryProperties = encryProperties;
	}
	
	/**
	 * 对拿到的字符串属性进行转化，如果加密过，就解密得到
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author ice
	 */
	public Properties getProperties() {
		Properties conf = new Properties();
		
		String encrypt = "";
		try {
			encrypt = GetPropertiesValue.getValue("encrypt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置encrypt属性");
		}
		//如果有解密需求（即encrypt等于true）
		if("true".equals(encrypt)){
			//如果未实现自定义的解密器则使用默认的解密器
			if(encryProperties == null){
				encryProperties = new EncryptPropertiesDefault();
			}
		}else{//否则不提供解密业务
			encryProperties = new EncryptProperties() {
				
				@Override
				public boolean isEncryptStr(String str) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public String convertStr(String key,String value) {
					// TODO Auto-generated method stub
					return value;
				}
			};
		}
		
		
		String url = null;
		try {
			url = GetPropertiesValue.getValue("url");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置url属性");
		}
		String name = null;
		try {
			name = GetPropertiesValue.getValue("name");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.info("未设置name属性");
		}
		String password = null;
		try {
			password = GetPropertiesValue.getValue("password");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置password属性");
		}
		String driver = null;
		try {
			driver = GetPropertiesValue.getValue("driver");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置driver属性");
		}
		int idlesize = 0;
		try {
			idlesize = Integer.parseInt(GetPropertiesValue.getValue("idlesize"));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置idlesize属性");
		}
		int maxsize = 0;
		try {
			maxsize = Integer.parseInt(GetPropertiesValue.getValue("maxsize"));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置maxsize属性");
		}
		int addsize = 0;
		try {
			addsize = Integer.parseInt(GetPropertiesValue.getValue("addsize"));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			logger.info("未设置addsize属性");
		}
		
//		String encrypt = GetPropertiesValue.getValue("false");
//		
//		String url = GetPropertiesValue.getValue("url");
//		String name = GetPropertiesValue.getValue("name");
//		String password = GetPropertiesValue.getValue("password");
//		String driver = GetPropertiesValue.getValue("driver");
//		int idlesize = Integer.parseInt(GetPropertiesValue.getValue("idlesize"));
//		int maxsize = Integer.parseInt(GetPropertiesValue.getValue("maxsize"));
//		int addsize = Integer.parseInt(GetPropertiesValue.getValue("addsize"));
		
		conf.setProperty("url", convertEncrypt("url",url));
		conf.setProperty("name", convertEncrypt("name",name));
		conf.setProperty("password", convertEncrypt("password",password));
		conf.setProperty("driver", convertEncrypt("driver",driver));
		conf.setProperty("idlesize", convertEncrypt("idlesize",idlesize + ""));
		conf.setProperty("maxsize", convertEncrypt("maxsize",maxsize + ""));
		conf.setProperty("addsize", convertEncrypt("addsize",addsize + ""));
		
		return conf;
	}
	
	/**
	 * 将加密的属性转化出来
	 * @param str
	 * @return
	 * @author ice
	 */
	public String convertEncrypt(String key,String value){
		return encryProperties.convertStr(key,value);
	}
	
}
