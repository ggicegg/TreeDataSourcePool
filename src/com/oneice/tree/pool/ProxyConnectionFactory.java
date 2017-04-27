package com.oneice.tree.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.lang.reflect.*;
import org.apache.log4j.Logger;
import com.oneice.tree.support.logging.LogFactory;

/**
 * 构造代理Connection的主要类
 * @author ice
 */
public class ProxyConnectionFactory {
	private final Logger logger = LogFactory.getLog(ProxyConnectionFactory.class);
	
	private String driver;
	private String url;
	private String name;
	private String password;
	private TreeDataSourcePool treeDataSourcePool;
	
	/**
	 * 从连接池配置属性中拿到建立连接的必要属性
	 * @param treeDataSourcePool
	 */
	public ProxyConnectionFactory(TreeDataSourcePool treeDataSourcePool){
		driver = treeDataSourcePool.getDriver();
		url = treeDataSourcePool.getUrl();
		name = treeDataSourcePool.getName();
		password = treeDataSourcePool.getPassword();
		this.treeDataSourcePool = treeDataSourcePool;
	}
	
	/**
	 * 创建代理Connection对象，改变其close的方法
	 * @return
	 * @author ice
	 */
	public Connection createProxyConnecion(){
		Object proxyConnection = null;
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, name, password);
			InvocationHandler handler = new ConnectionHandler(connection,treeDataSourcePool);
			proxyConnection = Proxy.newProxyInstance(ProxyConnectionFactory.class.getClassLoader(), new Class[]{Connection.class},handler);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("驱动错误",e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("获取连接错误",e);
		}
		return (Connection) proxyConnection;
	}
}

