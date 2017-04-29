package com.oneice.tree.pool;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oneice.tree.support.logging.LogFactory;


/**
 * 构造TreeDataSourcePool连接池的主要类
 * @author ice
 */
public class TreeDataSourceFactory {
	private final Logger logger = LogFactory.getLog(TreeDataSourceFactory.class);
	private final String URL = "url";
	private final String NAME = "name";
	private final String PASSWORD = "password";
	private final String DRIVER = "driver";
	private final String MAX_SIZE = "maxsize";
	private final String IDLE_SIZE = "idlesize";
	private final String ADD_SIZE = "addsize";
	
	private TreeDataSourcePool treeDataSourcePool;
	private Properties conf;
	private CovertProperties covertProperties;
	/**
	 * 使用默认解密器或者无解密的工厂方法
	 */
	public TreeDataSourceFactory(){
		treeDataSourcePool = new TreeDataSourcePool();
		covertProperties = new CovertProperties();
		conf = covertProperties.getProperties();
	}
	/**
	 * 提供自定义解密器解密Properties的工厂方法
	 * @param encryptProperties
	 */
	public TreeDataSourceFactory(EncryptProperties encryptProperties){
		covertProperties = new CovertProperties(encryptProperties);
		conf = covertProperties.getProperties();
	}
	/**
	 * 将属性装配到treeDataSourcePool上，并进行初始化
	 * @return
	 * @author ice
	 */
	public TreeDataSourcePool createDataConnectionPool(){
		treeDataSourcePool = new TreeDataSourcePool();
		String value = "";
		int size = 0;
		value = conf.getProperty(URL);
		if(value != null){
			treeDataSourcePool.setUrl(value);
		}else{
			logger.info("未获取到URL值");
		}
		
		
		value = conf.getProperty(NAME);
		if(value != null){
			treeDataSourcePool.setName(value);
		}else{
			logger.error("未获取到数据库用户名");
		}
		
		
		value = conf.getProperty(PASSWORD);
		if(value != null){
			treeDataSourcePool.setPassword(value);
		}else{
			logger.error("未获取到数据库密码");
		}
		
		value = conf.getProperty(DRIVER);
		if(value != null){
			treeDataSourcePool.setDriver(value);
		}else{
			logger.error("未获取到数据库驱动名称");
		}
		
		try{
			size = Integer.valueOf(conf.getProperty(MAX_SIZE));
			treeDataSourcePool.setMaxsize(size);
		}catch(Exception e){
			logger.error("maxsize属性的值设置不正确，将以默认值设置,默认值为150");
			treeDataSourcePool.setMaxsize(150);
		}
		
		
		try{
			size = Integer.valueOf(conf.getProperty(IDLE_SIZE));
			treeDataSourcePool.setIdlesize(size);
		}catch(Exception e){
			logger.error("maxsize属性的值设置不正确，将以默认值设置,默认值为50");
			treeDataSourcePool.setIdlesize(50);
		}
	
		try{
			size = Integer.valueOf(conf.getProperty(IDLE_SIZE));
			treeDataSourcePool.setAddsize(size);
		}catch(Exception e){
			logger.error("addsize属性的值设置不正确，将以默认值设置,默认值为50");
			treeDataSourcePool.setIdlesize(50);
		}
		treeDataSourcePool.addConn();
		return treeDataSourcePool;
	}
}
