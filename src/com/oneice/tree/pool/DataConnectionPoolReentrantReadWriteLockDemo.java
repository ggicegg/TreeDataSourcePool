package com.oneice.tree.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.*;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.oneice.tree.utils.GetPropertiesValue;






public class DataConnectionPoolReentrantReadWriteLockDemo implements DataSource{

	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private Lock readLock = readWriteLock.readLock();
	private Lock writeLock = readWriteLock.writeLock();
	//线程不安全
	private static final LinkedList<Connection> pool = new LinkedList<>(); 
	//线程安全吗？
//	private static LinkedList<Connection> pool = (LinkedList<Connection>) Collections.synchronizedList(pool2);
	
	private static String url;
	private static String name;
	private static String password;
	private static String driver;
	private static int size;
	private static int addsize;
	private static int maxsize;
	
	static{
//		pool = new LinkedList<>();
		try {
			url=GetPropertiesValue.getValue("url");
			name=GetPropertiesValue.getValue("name");
			password=GetPropertiesValue.getValue("password");
			driver=GetPropertiesValue.getValue("driver");
			size = Integer.valueOf(GetPropertiesValue.getValue("idlesize"));
			addsize=Integer.valueOf(GetPropertiesValue.getValue("addsize"));
			maxsize=Integer.valueOf(GetPropertiesValue.getValue("maxsize"));
			Class.forName(driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
	}
	public DataConnectionPoolReentrantReadWriteLockDemo() throws SQLException{
//		condition = lock.newCondition();
		addConntion2Pool(size);
	}
	/**
	 * 给pool中增加连接
	 * @param size要增加的连接数
	 * @throws SQLException 
	 */
	public void addConntion2Pool(int size) throws SQLException{
		for(int i = 0; i < size;i++){
			Connection connection = DriverManager.getConnection(url,name,password);
			InvocationHandler handler = new ConnectionHandler(connection);
			Object proxyConn =  Proxy.newProxyInstance(DataConnectionPoolReentrantReadWriteLockDemo.class.getClassLoader(), new Class[]{Connection.class}, handler);
			pool.add((Connection) proxyConn);
		}
	}
	/**
	 * 第二种实现，使用读写锁
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		readLock.lock();
		Object connection = null;
		try{
			connection = pool.removeFirst();
			return (Connection) connection;
		}finally{
			readLock.unlock();
		}
	}
	
	class ConnectionHandler implements InvocationHandler{

		public Object target;
		public ConnectionHandler(Object o){
			target = o;
		}
		
		/**
		 *第二种实现使用同步锁
		 */
		@Override
		public  Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// TODO Auto-generated method stub
			//如果时close方法，将代理对象放回连接池中
			if(method.getName().equals("close")){
				writeLock.lock();
				try{
					pool.add((Connection) proxy);
				}finally{
					writeLock.unlock();
				}		
			}else{//忘了其他方法要放行,难道我真是弱智
				return method.invoke(target, args);
			}
			return null;
		}
	}
	
	public void releaseConnection(Connection connection){
		pool.add(connection);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}	
}

